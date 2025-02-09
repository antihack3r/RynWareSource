// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.event.events.impl.render;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.state.IBlockState;
import org.rynware.client.event.events.Event;

public class EventRenderBlock implements Event
{
    private final IBlockState state;
    private final BlockPos pos;
    private final IBlockAccess access;
    private final BufferBuilder bufferBuilder;
    
    public EventRenderBlock(final IBlockState state, final BlockPos pos, final IBlockAccess access, final BufferBuilder bufferBuilder) {
        this.state = state;
        this.pos = pos;
        this.access = access;
        this.bufferBuilder = bufferBuilder;
    }
    
    public IBlockState getState() {
        return this.state;
    }
    
    public BufferBuilder getBufferBuilder() {
        return this.bufferBuilder;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public IBlockAccess getAccess() {
        return this.access;
    }
}
