// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.event.events.impl.player;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockLiquid;
import org.rynware.client.event.events.callables.EventCancellable;

public class EventLiquidSolid extends EventCancellable
{
    private final BlockLiquid blockLiquid;
    private final BlockPos pos;
    private AxisAlignedBB collision;
    
    public EventLiquidSolid(final BlockLiquid blockLiquid, final BlockPos pos) {
        this.blockLiquid = blockLiquid;
        this.pos = pos;
    }
    
    public BlockLiquid getBlock() {
        return this.blockLiquid;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public AxisAlignedBB setColision(final AxisAlignedBB expand) {
        return this.collision;
    }
}
