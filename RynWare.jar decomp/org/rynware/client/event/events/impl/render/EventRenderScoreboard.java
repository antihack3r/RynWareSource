// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.event.events.impl.render;

import org.rynware.client.event.types.EventType;
import org.rynware.client.event.events.Event;

public class EventRenderScoreboard implements Event
{
    private EventType state;
    
    public EventRenderScoreboard(final EventType state) {
        this.state = state;
    }
    
    public EventType getState() {
        return this.state;
    }
    
    public void setState(final EventType state) {
        this.state = state;
    }
    
    public boolean isPre() {
        return this.state == EventType.PRE;
    }
}
