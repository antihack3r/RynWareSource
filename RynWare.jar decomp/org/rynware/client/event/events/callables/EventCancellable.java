// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.event.events.callables;

import org.rynware.client.event.events.Cancellable;
import org.rynware.client.event.events.Event;

public abstract class EventCancellable implements Event, Cancellable
{
    private boolean cancelled;
    
    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    @Override
    public void setCancelled(final boolean state) {
        this.cancelled = state;
    }
}
