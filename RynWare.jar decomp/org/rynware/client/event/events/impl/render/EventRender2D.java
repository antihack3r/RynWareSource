// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.event.events.impl.render;

import net.minecraft.client.gui.ScaledResolution;
import org.rynware.client.event.events.Event;

public class EventRender2D implements Event
{
    private final ScaledResolution resolution;
    
    public EventRender2D(final ScaledResolution resolution) {
        this.resolution = resolution;
    }
    
    public ScaledResolution getResolution() {
        return this.resolution;
    }
}
