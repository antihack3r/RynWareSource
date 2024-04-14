// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.utils.render;

import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;
import org.rynware.client.utils.Helper;

public class PlayerUtil implements Helper
{
    public static Block getBlock(final double offsetX, final double offsetY, final double offsetZ) {
        return PlayerUtil.mc.world.getBlockState(new BlockPos(offsetX, offsetY, offsetZ)).getBlock();
    }
}
