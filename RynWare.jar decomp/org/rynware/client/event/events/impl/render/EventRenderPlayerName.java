// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.event.events.impl.render;

import net.minecraft.entity.EntityLivingBase;
import org.rynware.client.event.events.callables.EventCancellable;

public class EventRenderPlayerName extends EventCancellable
{
    private final EntityLivingBase entity;
    private String renderedName;
    
    public EventRenderPlayerName(final EntityLivingBase entity, final String renderedName) {
        this.entity = entity;
        this.renderedName = renderedName;
    }
    
    public EntityLivingBase getEntity() {
        return this.entity;
    }
    
    public String getRenderedName() {
        return this.renderedName;
    }
    
    public void setRenderedName(final String renderedName) {
        this.renderedName = renderedName;
    }
}
