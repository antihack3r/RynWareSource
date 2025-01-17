// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.protocols.protocol1_13to1_12_2.storage;

import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_13to1_12_2.Protocol1_13To1_12_2;
import com.viaversion.viaversion.api.minecraft.Position;
import com.viaversion.viaversion.api.type.Type;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.protocols.protocol1_12_1to1_12.ServerboundPackets1_12_1;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.connection.StorableObject;

public class TabCompleteTracker implements StorableObject
{
    private int transactionId;
    private String input;
    private String lastTabComplete;
    private long timeToSend;
    
    public void sendPacketToServer(final UserConnection connection) {
        if (this.lastTabComplete == null || this.timeToSend > System.currentTimeMillis()) {
            return;
        }
        final PacketWrapper wrapper = PacketWrapper.create(ServerboundPackets1_12_1.TAB_COMPLETE, null, connection);
        wrapper.write(Type.STRING, this.lastTabComplete);
        wrapper.write(Type.BOOLEAN, false);
        wrapper.write(Type.OPTIONAL_POSITION, null);
        try {
            wrapper.scheduleSendToServer(Protocol1_13To1_12_2.class);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        this.lastTabComplete = null;
    }
    
    public int getTransactionId() {
        return this.transactionId;
    }
    
    public void setTransactionId(final int transactionId) {
        this.transactionId = transactionId;
    }
    
    public String getInput() {
        return this.input;
    }
    
    public void setInput(final String input) {
        this.input = input;
    }
    
    public String getLastTabComplete() {
        return this.lastTabComplete;
    }
    
    public void setLastTabComplete(final String lastTabComplete) {
        this.lastTabComplete = lastTabComplete;
    }
    
    public long getTimeToSend() {
        return this.timeToSend;
    }
    
    public void setTimeToSend(final long timeToSend) {
        this.timeToSend = timeToSend;
    }
}
