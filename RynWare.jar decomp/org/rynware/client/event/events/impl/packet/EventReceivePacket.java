// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.event.events.impl.packet;

import net.minecraft.network.Packet;
import org.rynware.client.event.events.callables.EventCancellable;

public class EventReceivePacket extends EventCancellable
{
    private Packet<?> packet;
    
    public EventReceivePacket(final Packet<?> packet) {
        this.packet = packet;
    }
    
    public Packet<?> getPacket() {
        return this.packet;
    }
    
    public void setPacket(final Packet<?> packet) {
        this.packet = packet;
    }
}
