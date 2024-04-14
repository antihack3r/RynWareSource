// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.network.play.client;

import net.minecraft.network.INetHandler;
import java.io.IOException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.Packet;

public class CPacketEntityAction implements Packet<INetHandlerPlayServer>
{
    public int entityID;
    public Action action;
    public int auxData;
    
    public CPacketEntityAction() {
    }
    
    public CPacketEntityAction(final Entity entityIn, final Action actionIn) {
        this(entityIn, actionIn, 0);
    }
    
    public CPacketEntityAction(final Entity entityIn, final Action actionIn, final int auxDataIn) {
        this.entityID = entityIn.getEntityId();
        this.action = actionIn;
        this.auxData = auxDataIn;
    }
    
    @Override
    public void readPacketData(final PacketBuffer buf) throws IOException {
        this.entityID = buf.readVarIntFromBuffer();
        this.action = buf.readEnumValue(Action.class);
        this.auxData = buf.readVarIntFromBuffer();
    }
    
    @Override
    public void writePacketData(final PacketBuffer buf) throws IOException {
        buf.writeVarIntToBuffer(this.entityID);
        buf.writeEnumValue(this.action);
        buf.writeVarIntToBuffer(this.auxData);
    }
    
    @Override
    public void processPacket(final INetHandlerPlayServer handler) {
        handler.processEntityAction(this);
    }
    
    public Action getAction() {
        return this.action;
    }
    
    public int getAuxData() {
        return this.auxData;
    }
    
    public enum Action
    {
        START_SNEAKING, 
        STOP_SNEAKING, 
        STOP_SLEEPING, 
        START_SPRINTING, 
        STOP_SPRINTING, 
        START_RIDING_JUMP, 
        STOP_RIDING_JUMP, 
        OPEN_INVENTORY, 
        START_FALL_FLYING;
    }
}
