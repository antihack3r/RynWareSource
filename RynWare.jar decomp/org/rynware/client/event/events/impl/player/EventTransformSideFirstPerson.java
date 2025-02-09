// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.event.events.impl.player;

import net.minecraft.util.EnumHandSide;
import org.rynware.client.event.events.Event;

public class EventTransformSideFirstPerson implements Event
{
    private final EnumHandSide enumHandSide;
    
    public EventTransformSideFirstPerson(final EnumHandSide enumHandSide) {
        this.enumHandSide = enumHandSide;
    }
    
    public EnumHandSide getEnumHandSide() {
        return this.enumHandSide;
    }
}
