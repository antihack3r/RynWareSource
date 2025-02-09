// 
// Decompiled by Procyon v0.6.0
// 

package com.github.lunatrius.schematica.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;

public interface ISchematic
{
    IBlockState getBlockState(final BlockPos p0);
    
    int getWidth();
    
    int getHeight();
    
    int getLength();
}
