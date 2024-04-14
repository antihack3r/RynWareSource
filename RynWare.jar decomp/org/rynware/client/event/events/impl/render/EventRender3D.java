// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.event.events.impl.render;

import org.rynware.client.event.events.Event;

public class EventRender3D implements Event
{
    private final float partialTicks;
    
    public EventRender3D(final float partialTicks) {
        this.partialTicks = partialTicks;
    }
    
    public float getPartialTicks() {
        return this.partialTicks;
    }
}
