// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.event.events.impl.packet;

import org.rynware.client.event.events.Event;
import org.rynware.client.event.events.callables.EventCancellable;

public class EventMessage extends EventCancellable implements Event
{
    public String message;
    
    public EventMessage(final String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return this.message;
    }
}
