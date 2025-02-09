// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.event.events.impl.input;

import org.rynware.client.event.events.callables.EventCancellable;

public class EventMouse extends EventCancellable
{
    private int key;
    
    public EventMouse(final int key) {
        this.key = key;
    }
    
    public int getKey() {
        return this.key;
    }
    
    public void setKey(final int key) {
        this.key = key;
    }
}
