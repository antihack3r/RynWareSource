// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.event.events;

public interface Cancellable
{
    boolean isCancelled();
    
    void setCancelled(final boolean p0);
}