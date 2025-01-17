// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.platform;

import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.gson.JsonArray;
import com.viaversion.viaversion.libs.gson.JsonObject;
import io.netty.channel.ChannelPipeline;
import com.viaversion.viaversion.api.Via;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import com.viaversion.viaversion.util.ReflectionUtil;
import io.netty.channel.ChannelInitializer;
import java.util.Iterator;
import com.viaversion.viaversion.util.SynchronizedListWrapper;
import java.util.ArrayList;
import java.lang.reflect.Field;
import com.viaversion.viaversion.util.Pair;
import io.netty.channel.ChannelFuture;
import java.util.List;
import com.viaversion.viaversion.api.platform.ViaInjector;

public abstract class LegacyViaInjector implements ViaInjector
{
    protected final List<ChannelFuture> injectedFutures;
    protected final List<Pair<Field, Object>> injectedLists;
    
    public LegacyViaInjector() {
        this.injectedFutures = new ArrayList<ChannelFuture>();
        this.injectedLists = new ArrayList<Pair<Field, Object>>();
    }
    
    @Override
    public void inject() throws ReflectiveOperationException {
        final Object connection = this.getServerConnection();
        if (connection == null) {
            throw new RuntimeException("Failed to find the core component 'ServerConnection'");
        }
        for (final Field field : connection.getClass().getDeclaredFields()) {
            if (List.class.isAssignableFrom(field.getType())) {
                if (field.getGenericType().getTypeName().contains(ChannelFuture.class.getName())) {
                    field.setAccessible(true);
                    final List<ChannelFuture> list = (List<ChannelFuture>)field.get(connection);
                    final List<ChannelFuture> wrappedList = new SynchronizedListWrapper<ChannelFuture>(list, o -> {
                        try {
                            this.injectChannelFuture((ChannelFuture)o);
                        }
                        catch (final ReflectiveOperationException e) {
                            throw new RuntimeException(e);
                        }
                        return;
                    });
                    synchronized (list) {
                        for (final ChannelFuture future : list) {
                            this.injectChannelFuture(future);
                        }
                        field.set(connection, wrappedList);
                    }
                    this.injectedLists.add(new Pair<Field, Object>(field, connection));
                }
            }
        }
    }
    
    private void injectChannelFuture(final ChannelFuture future) throws ReflectiveOperationException {
        final List<String> names = future.channel().pipeline().names();
        ChannelHandler bootstrapAcceptor = null;
        for (final String name : names) {
            final ChannelHandler handler = future.channel().pipeline().get(name);
            try {
                ReflectionUtil.get(handler, "childHandler", ChannelInitializer.class);
                bootstrapAcceptor = handler;
            }
            catch (final ReflectiveOperationException ex) {
                continue;
            }
            break;
        }
        if (bootstrapAcceptor == null) {
            bootstrapAcceptor = future.channel().pipeline().first();
        }
        try {
            final ChannelInitializer<Channel> oldInitializer = (ChannelInitializer<Channel>)ReflectionUtil.get(bootstrapAcceptor, "childHandler", ChannelInitializer.class);
            ReflectionUtil.set(bootstrapAcceptor, "childHandler", this.createChannelInitializer(oldInitializer));
            this.injectedFutures.add(future);
        }
        catch (final NoSuchFieldException ignored) {
            this.blame(bootstrapAcceptor);
        }
    }
    
    @Override
    public void uninject() throws ReflectiveOperationException {
        for (final ChannelFuture future : this.injectedFutures) {
            final ChannelPipeline pipeline = future.channel().pipeline();
            ChannelHandler bootstrapAcceptor = pipeline.first();
            if (bootstrapAcceptor == null) {
                Via.getPlatform().getLogger().info("Empty pipeline, nothing to uninject");
            }
            else {
                for (final String name : pipeline.names()) {
                    final ChannelHandler handler = pipeline.get(name);
                    if (handler == null) {
                        Via.getPlatform().getLogger().warning("Could not get handler " + name);
                    }
                    else {
                        try {
                            if (ReflectionUtil.get(handler, "childHandler", ChannelInitializer.class) instanceof WrappedChannelInitializer) {
                                bootstrapAcceptor = handler;
                                break;
                            }
                            continue;
                        }
                        catch (final ReflectiveOperationException ex) {}
                    }
                }
                try {
                    final ChannelInitializer<Channel> initializer = (ChannelInitializer<Channel>)ReflectionUtil.get(bootstrapAcceptor, "childHandler", ChannelInitializer.class);
                    if (!(initializer instanceof WrappedChannelInitializer)) {
                        continue;
                    }
                    ReflectionUtil.set(bootstrapAcceptor, "childHandler", ((WrappedChannelInitializer)initializer).original());
                }
                catch (final Exception e) {
                    Via.getPlatform().getLogger().severe("Failed to remove injection handler, reload won't work with connections, please reboot!");
                    e.printStackTrace();
                }
            }
        }
        this.injectedFutures.clear();
        for (final Pair<Field, Object> pair : this.injectedLists) {
            try {
                final Field field = pair.key();
                final Object o = field.get(pair.value());
                if (!(o instanceof SynchronizedListWrapper)) {
                    continue;
                }
                final List<ChannelFuture> originalList = ((SynchronizedListWrapper)o).originalList();
                synchronized (originalList) {
                    field.set(pair.value(), originalList);
                }
            }
            catch (final ReflectiveOperationException e2) {
                Via.getPlatform().getLogger().severe("Failed to remove injection, reload won't work with connections, please reboot!");
            }
        }
        this.injectedLists.clear();
    }
    
    @Override
    public boolean lateProtocolVersionSetting() {
        return true;
    }
    
    @Override
    public JsonObject getDump() {
        final JsonObject data = new JsonObject();
        final JsonArray injectedChannelInitializers = new JsonArray();
        data.add("injectedChannelInitializers", injectedChannelInitializers);
        for (final ChannelFuture future : this.injectedFutures) {
            final JsonObject futureInfo = new JsonObject();
            injectedChannelInitializers.add(futureInfo);
            futureInfo.addProperty("futureClass", future.getClass().getName());
            futureInfo.addProperty("channelClass", future.channel().getClass().getName());
            final JsonArray pipeline = new JsonArray();
            futureInfo.add("pipeline", pipeline);
            for (final String pipeName : future.channel().pipeline().names()) {
                final JsonObject handlerInfo = new JsonObject();
                pipeline.add(handlerInfo);
                handlerInfo.addProperty("name", pipeName);
                final ChannelHandler channelHandler = future.channel().pipeline().get(pipeName);
                if (channelHandler == null) {
                    handlerInfo.addProperty("status", "INVALID");
                }
                else {
                    handlerInfo.addProperty("class", channelHandler.getClass().getName());
                    try {
                        final Object child = ReflectionUtil.get(channelHandler, "childHandler", ChannelInitializer.class);
                        handlerInfo.addProperty("childClass", child.getClass().getName());
                        if (!(child instanceof WrappedChannelInitializer)) {
                            continue;
                        }
                        handlerInfo.addProperty("oldInit", ((WrappedChannelInitializer)child).original().getClass().getName());
                    }
                    catch (final ReflectiveOperationException ex) {}
                }
            }
        }
        final JsonObject wrappedLists = new JsonObject();
        final JsonObject currentLists = new JsonObject();
        try {
            for (final Pair<Field, Object> pair : this.injectedLists) {
                final Field field = pair.key();
                final Object list = field.get(pair.value());
                currentLists.addProperty(field.getName(), list.getClass().getName());
                if (list instanceof SynchronizedListWrapper) {
                    wrappedLists.addProperty(field.getName(), ((SynchronizedListWrapper)list).originalList().getClass().getName());
                }
            }
            data.add("wrappedLists", wrappedLists);
            data.add("currentLists", currentLists);
        }
        catch (final ReflectiveOperationException ex2) {}
        return data;
    }
    
    @Override
    public String getEncoderName() {
        return "encoder";
    }
    
    @Override
    public String getDecoderName() {
        return "decoder";
    }
    
    protected abstract Object getServerConnection() throws ReflectiveOperationException;
    
    protected abstract WrappedChannelInitializer createChannelInitializer(final ChannelInitializer<Channel> p0);
    
    protected abstract void blame(final ChannelHandler p0) throws ReflectiveOperationException;
}
