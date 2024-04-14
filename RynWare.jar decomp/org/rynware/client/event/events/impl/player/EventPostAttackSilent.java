// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.event.events.impl.player;

import net.minecraft.entity.Entity;
import org.rynware.client.event.events.callables.EventCancellable;

public class EventPostAttackSilent extends EventCancellable
{
    private final Entity targetEntity;
    
    public EventPostAttackSilent(final Entity targetEntity) {
        this.targetEntity = targetEntity;
    }
    
    public Entity getTargetEntity() {
        return this.targetEntity;
    }
}
