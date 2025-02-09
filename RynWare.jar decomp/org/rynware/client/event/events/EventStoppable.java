// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.event.events;

public abstract class EventStoppable implements Event
{
    private boolean stopped;
    
    public void stop() {
        this.stopped = true;
    }
    
    public boolean isStopped() {
        return this.stopped;
    }
}
