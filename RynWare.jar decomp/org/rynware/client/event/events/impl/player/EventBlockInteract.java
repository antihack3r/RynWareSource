// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.event.events.impl.player;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.rynware.client.event.events.callables.EventCancellable;

public class EventBlockInteract extends EventCancellable
{
    private BlockPos pos;
    private EnumFacing face;
    
    public EventBlockInteract(final BlockPos pos, final EnumFacing face) {
        this.pos = pos;
        this.face = face;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public void setPos(final BlockPos pos) {
        this.pos = pos;
    }
    
    public EnumFacing getFace() {
        return this.face;
    }
    
    public void setFace(final EnumFacing face) {
        this.face = face;
    }
}
