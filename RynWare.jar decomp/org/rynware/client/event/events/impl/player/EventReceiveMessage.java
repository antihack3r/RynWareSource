// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.event.events.impl.player;

import org.rynware.client.event.events.callables.EventCancellable;

public class EventReceiveMessage extends EventCancellable
{
    public String message;
    public boolean cancelled;
    
    public EventReceiveMessage(final String chat) {
        this.message = chat;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    @Override
    public void setCancelled(final boolean b) {
        this.cancelled = b;
    }
}
