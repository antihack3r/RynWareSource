// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.event.events.impl.player;

import org.rynware.client.event.events.callables.EventCancellable;

public class EventPostMotion extends EventCancellable
{
    public float pitch;
    private float yaw;
    
    public EventPostMotion(final float yaw, final float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }
    
    public float getYaw() {
        return this.yaw;
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
    }
    
    public float getPitch() {
        return this.pitch;
    }
    
    public void setPitch(final float pitch) {
        this.pitch = pitch;
    }
}
