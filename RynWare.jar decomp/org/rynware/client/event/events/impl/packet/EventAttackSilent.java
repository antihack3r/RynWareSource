// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.event.events.impl.packet;

import net.minecraft.entity.Entity;
import org.rynware.client.event.events.callables.EventCancellable;

public class EventAttackSilent extends EventCancellable
{
    private final Entity targetEntity;
    
    public EventAttackSilent(final Entity targetEntity) {
        this.targetEntity = targetEntity;
    }
    
    public Entity getTargetEntity() {
        return this.targetEntity;
    }
}
