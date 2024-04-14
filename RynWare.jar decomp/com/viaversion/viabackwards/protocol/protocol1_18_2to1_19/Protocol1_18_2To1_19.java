// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_18_2to1_19;

import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.data.MappingData;
import com.google.common.base.Preconditions;
import com.viaversion.viaversion.libs.kyori.adventure.text.Component;
import com.viaversion.viaversion.libs.kyori.adventure.text.ComponentLike;
import com.viaversion.viaversion.libs.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import com.viaversion.viaversion.libs.kyori.adventure.text.TextReplacementConfig;
import com.viaversion.viaversion.api.rewriter.ItemRewriter;
import com.viaversion.viaversion.api.rewriter.EntityRewriter;
import com.viaversion.viaversion.api.data.entity.EntityTracker;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;
import com.viaversion.viaversion.data.entity.EntityTrackerBase;
import com.viaversion.viaversion.api.minecraft.entities.Entity1_19Types;
import com.viaversion.viaversion.api.connection.StorableObject;
import com.viaversion.viabackwards.protocol.protocol1_18_2to1_19.storage.DimensionRegistryStorage;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.protocols.base.ServerboundLoginPackets;
import com.viaversion.viaversion.protocols.base.ClientboundLoginPackets;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import java.time.Instant;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viabackwards.ViaBackwards;
import com.viaversion.viaversion.rewriter.CommandRewriter;
import com.viaversion.viabackwards.protocol.protocol1_18_2to1_19.data.CommandRewriter1_19;
import com.viaversion.viaversion.rewriter.StatisticsRewriter;
import com.viaversion.viaversion.api.minecraft.RegistryType;
import com.viaversion.viaversion.rewriter.TagRewriter;
import com.viaversion.viaversion.api.type.Type;
import com.viaversion.viaversion.api.protocol.remapper.PacketRemapper;
import com.viaversion.viabackwards.api.rewriters.SoundRewriter;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.Protocol1_19To1_18_2;
import com.viaversion.viabackwards.api.rewriters.TranslatableRewriter;
import com.viaversion.viabackwards.protocol.protocol1_18_2to1_19.packets.BlockItemPackets1_19;
import com.viaversion.viabackwards.protocol.protocol1_18_2to1_19.packets.EntityPackets1_19;
import java.util.UUID;
import com.viaversion.viabackwards.protocol.protocol1_18_2to1_19.data.BackwardsMappings;
import com.viaversion.viaversion.protocols.protocol1_17to1_16_4.ServerboundPackets1_17;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.ServerboundPackets1_19;
import com.viaversion.viaversion.protocols.protocol1_18to1_17_1.ClientboundPackets1_18;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.ClientboundPackets1_19;
import com.viaversion.viabackwards.api.BackwardsProtocol;

public final class Protocol1_18_2To1_19 extends BackwardsProtocol<ClientboundPackets1_19, ClientboundPackets1_18, ServerboundPackets1_19, ServerboundPackets1_17>
{
    public static final BackwardsMappings MAPPINGS;
    private static final String[] CHAT_KEYS;
    private static final UUID ZERO_UUID;
    private static final byte[] EMPTY_BYTES;
    private final EntityPackets1_19 entityRewriter;
    private final BlockItemPackets1_19 blockItemPackets;
    private final TranslatableRewriter translatableRewriter;
    
    public Protocol1_18_2To1_19() {
        super(ClientboundPackets1_19.class, ClientboundPackets1_18.class, ServerboundPackets1_19.class, ServerboundPackets1_17.class);
        this.entityRewriter = new EntityPackets1_19(this);
        this.blockItemPackets = new BlockItemPackets1_19(this);
        this.translatableRewriter = new TranslatableRewriter(this);
    }
    
    @Override
    protected void registerPackets() {
        this.executeAsyncAfterLoaded(Protocol1_19To1_18_2.class, () -> {
            Protocol1_18_2To1_19.MAPPINGS.load();
            this.entityRewriter.onMappingDataLoaded();
            return;
        });
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_19.ACTIONBAR);
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_19.TITLE_TEXT);
        this.translatableRewriter.registerComponentPacket(ClientboundPackets1_19.TITLE_SUBTITLE);
        this.translatableRewriter.registerBossBar(ClientboundPackets1_19.BOSSBAR);
        this.translatableRewriter.registerDisconnect(ClientboundPackets1_19.DISCONNECT);
        this.translatableRewriter.registerTabList(ClientboundPackets1_19.TAB_LIST);
        this.translatableRewriter.registerOpenWindow(ClientboundPackets1_19.OPEN_WINDOW);
        this.translatableRewriter.registerCombatKill(ClientboundPackets1_19.COMBAT_KILL);
        this.translatableRewriter.registerPing();
        this.blockItemPackets.register();
        this.entityRewriter.register();
        final SoundRewriter soundRewriter = new SoundRewriter(this);
        soundRewriter.registerStopSound(ClientboundPackets1_19.STOP_SOUND);
        ((AbstractProtocol<ClientboundPackets1_19, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_19.SOUND, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.read(Type.LONG);
                this.handler(soundRewriter.getSoundHandler());
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_19.ENTITY_SOUND, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(Type.VAR_INT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.read(Type.LONG);
                this.handler(soundRewriter.getSoundHandler());
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_19.NAMED_SOUND, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.map(Type.VAR_INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.INT);
                this.map(Type.FLOAT);
                this.map(Type.FLOAT);
                this.read(Type.LONG);
                this.handler(soundRewriter.getNamedSoundHandler());
            }
        });
        final TagRewriter tagRewriter = new TagRewriter(this);
        tagRewriter.removeTags("minecraft:banner_pattern");
        tagRewriter.removeTags("minecraft:instrument");
        tagRewriter.removeTags("minecraft:cat_variant");
        tagRewriter.removeTags("minecraft:painting_variant");
        tagRewriter.renameTag(RegistryType.BLOCK, "minecraft:wool_carpets", "minecraft:carpets");
        tagRewriter.renameTag(RegistryType.ITEM, "minecraft:wool_carpets", "minecraft:carpets");
        tagRewriter.addEmptyTag(RegistryType.ITEM, "minecraft:occludes_vibration_signals");
        tagRewriter.registerGeneric(ClientboundPackets1_19.TAGS);
        new StatisticsRewriter(this).register(ClientboundPackets1_19.STATISTICS);
        final CommandRewriter commandRewriter = new CommandRewriter1_19(this);
        ((AbstractProtocol<ClientboundPackets1_19, C2, S1, S2>)this).registerClientbound(ClientboundPackets1_19.DECLARE_COMMANDS, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final Object val$commandRewriter = commandRewriter;
                    for (int size = wrapper.passthrough((Type<Integer>)Type.VAR_INT), i = 0; i < size; ++i) {
                        final byte flags = wrapper.passthrough((Type<Byte>)Type.BYTE);
                        wrapper.passthrough(Type.VAR_INT_ARRAY_PRIMITIVE);
                        if ((flags & 0x8) != 0x0) {
                            wrapper.passthrough((Type<Object>)Type.VAR_INT);
                        }
                        final int nodeType = flags & 0x3;
                        if (nodeType == 1 || nodeType == 2) {
                            wrapper.passthrough(Type.STRING);
                        }
                        if (nodeType == 2) {
                            final int argumentTypeId = wrapper.read((Type<Integer>)Type.VAR_INT);
                            String argumentType = Protocol1_18_2To1_19.MAPPINGS.argumentType(argumentTypeId);
                            if (argumentType == null) {
                                ViaBackwards.getPlatform().getLogger().warning("Unknown command argument type id: " + argumentTypeId);
                                argumentType = "minecraft:no";
                            }
                            wrapper.write(Type.STRING, commandRewriter.handleArgumentType(argumentType));
                            commandRewriter.handleArgument(wrapper, argumentType);
                            if ((flags & 0x10) != 0x0) {
                                wrapper.passthrough(Type.STRING);
                            }
                        }
                    }
                    wrapper.passthrough((Type<Object>)Type.VAR_INT);
                });
            }
        });
        ((AbstractProtocol<ClientboundPackets1_19, C2, S1, S2>)this).cancelClientbound(ClientboundPackets1_19.SERVER_DATA);
        ((AbstractProtocol<ClientboundPackets1_19, C2, S1, S2>)this).cancelClientbound(ClientboundPackets1_19.CHAT_PREVIEW);
        ((AbstractProtocol<ClientboundPackets1_19, C2, S1, S2>)this).cancelClientbound(ClientboundPackets1_19.SET_DISPLAY_CHAT_PREVIEW);
        ((Protocol<ClientboundPackets1_19, ClientboundPackets1_18, S1, S2>)this).registerClientbound(ClientboundPackets1_19.PLAYER_CHAT, ClientboundPackets1_18.CHAT_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.handler(wrapper -> {
                    final JsonElement message = wrapper.read(Type.COMPONENT);
                    final JsonElement unsignedMessage = wrapper.read(Type.OPTIONAL_COMPONENT);
                    wrapper.write(Type.COMPONENT, (unsignedMessage != null) ? unsignedMessage : message);
                    return;
                });
                this.map(Type.VAR_INT, Type.BYTE);
                this.map(Type.UUID);
                this.handler(wrapper -> {
                    final JsonElement senderName = wrapper.read(Type.COMPONENT);
                    final JsonElement teamName = wrapper.read(Type.OPTIONAL_COMPONENT);
                    final JsonElement element = wrapper.get(Type.COMPONENT, 0);
                    Protocol1_18_2To1_19.this.handleChatType(wrapper, senderName, teamName, element);
                    return;
                });
                this.read(Type.LONG);
                this.read(Type.LONG);
                this.read(Type.BYTE_ARRAY_PRIMITIVE);
            }
        });
        ((Protocol<ClientboundPackets1_19, ClientboundPackets1_18, S1, S2>)this).registerClientbound(ClientboundPackets1_19.SYSTEM_CHAT, ClientboundPackets1_18.CHAT_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.COMPONENT);
                this.map(Type.VAR_INT, Type.BYTE);
                this.create(Type.UUID, Protocol1_18_2To1_19.ZERO_UUID);
                this.handler(wrapper -> Protocol1_18_2To1_19.this.handleChatType(wrapper, null, null, wrapper.get(Type.COMPONENT, 0)));
            }
        });
        ((AbstractProtocol<C1, C2, S1, ServerboundPackets1_17>)this).registerServerbound(ServerboundPackets1_17.CHAT_MESSAGE, new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.create(Type.LONG, Instant.now().toEpochMilli());
                this.create(Type.LONG, 0L);
                this.handler(wrapper -> {
                    final String message = wrapper.get(Type.STRING, 0);
                    if (!message.isEmpty() && message.charAt(0) == '/') {
                        wrapper.setPacketType(ServerboundPackets1_19.CHAT_COMMAND);
                        wrapper.set(Type.STRING, 0, message.substring(1));
                        wrapper.write(Type.VAR_INT, 0);
                        wrapper.write(Type.BOOLEAN, false);
                    }
                    else {
                        wrapper.write(Type.BYTE_ARRAY_PRIMITIVE, Protocol1_18_2To1_19.EMPTY_BYTES);
                        wrapper.write(Type.BOOLEAN, false);
                    }
                });
            }
        });
        this.registerClientbound(State.LOGIN, ClientboundLoginPackets.GAME_PROFILE.getId(), ClientboundLoginPackets.GAME_PROFILE.getId(), new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.UUID);
                this.map(Type.STRING);
                this.handler(wrapper -> {
                    for (int properties = wrapper.read((Type<Integer>)Type.VAR_INT), i = 0; i < properties; ++i) {
                        wrapper.read(Type.STRING);
                        wrapper.read(Type.STRING);
                        if (wrapper.read((Type<Boolean>)Type.BOOLEAN)) {
                            wrapper.read(Type.STRING);
                        }
                    }
                });
            }
        });
        this.registerServerbound(State.LOGIN, ServerboundLoginPackets.HELLO.getId(), ServerboundLoginPackets.HELLO.getId(), new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.STRING);
                this.create(Type.BOOLEAN, false);
            }
        });
        this.registerServerbound(State.LOGIN, ServerboundLoginPackets.ENCRYPTION_KEY.getId(), ServerboundLoginPackets.ENCRYPTION_KEY.getId(), new PacketRemapper() {
            @Override
            public void registerMap() {
                this.map(Type.BYTE_ARRAY_PRIMITIVE);
                this.create(Type.BOOLEAN, true);
            }
        });
    }
    
    @Override
    public void init(final UserConnection user) {
        user.put(new DimensionRegistryStorage());
        this.addEntityTracker(user, new EntityTrackerBase(user, Entity1_19Types.PLAYER, true));
    }
    
    @Override
    public BackwardsMappings getMappingData() {
        return Protocol1_18_2To1_19.MAPPINGS;
    }
    
    @Override
    public TranslatableRewriter getTranslatableRewriter() {
        return this.translatableRewriter;
    }
    
    @Override
    public EntityRewriter getEntityRewriter() {
        return this.entityRewriter;
    }
    
    @Override
    public ItemRewriter getItemRewriter() {
        return this.blockItemPackets;
    }
    
    private TextReplacementConfig replace(final JsonElement replacement) {
        return TextReplacementConfig.builder().matchLiteral("%s").replacement(GsonComponentSerializer.gson().deserializeFromTree(replacement)).once().build();
    }
    
    private void handleChatType(final PacketWrapper wrapper, final JsonElement senderName, final JsonElement teamName, final JsonElement text) throws Exception {
        this.translatableRewriter.processText(text);
        final byte type = wrapper.get((Type<Byte>)Type.BYTE, 0);
        if (type > 2) {
            wrapper.set(Type.BYTE, 0, (Byte)0);
        }
        final String key = Protocol1_18_2To1_19.CHAT_KEYS[type];
        if (key != null) {
            Component component = Component.text(ViaBackwards.getConfig().chatTypeFormat(key));
            if (key.equals("chat.type.team.text")) {
                Preconditions.checkNotNull((Object)teamName, (Object)"Team name is null");
                component = component.replaceText(this.replace(teamName));
            }
            if (senderName != null) {
                component = component.replaceText(this.replace(senderName));
            }
            component = component.replaceText(this.replace(text));
            wrapper.set(Type.COMPONENT, 0, GsonComponentSerializer.gson().serializeToTree(component));
        }
    }
    
    static {
        MAPPINGS = new BackwardsMappings();
        CHAT_KEYS = new String[] { "chat.type.text", null, null, "chat.type.announcement", "commands.message.display.incoming", "chat.type.team.text", "chat.type.emote", null };
        ZERO_UUID = new UUID(0L, 0L);
        EMPTY_BYTES = new byte[0];
    }
}
