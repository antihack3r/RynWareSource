// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.gen.feature;

import net.minecraft.util.EnumFacing;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import java.util.Random;
import net.minecraft.world.World;

public class WorldGenGlowStone2 extends WorldGenerator
{
    @Override
    public boolean generate(final World worldIn, final Random rand, final BlockPos position) {
        if (!worldIn.isAirBlock(position)) {
            return false;
        }
        if (worldIn.getBlockState(position.up()).getBlock() != Blocks.NETHERRACK) {
            return false;
        }
        worldIn.setBlockState(position, Blocks.GLOWSTONE.getDefaultState(), 2);
        for (int i = 0; i < 1500; ++i) {
            final BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), -rand.nextInt(12), rand.nextInt(8) - rand.nextInt(8));
            if (worldIn.getBlockState(blockpos).getMaterial() == Material.AIR) {
                int j = 0;
                for (final EnumFacing enumfacing : EnumFacing.values()) {
                    if (worldIn.getBlockState(blockpos.offset(enumfacing)).getBlock() == Blocks.GLOWSTONE) {
                        ++j;
                    }
                    if (j > 1) {
                        break;
                    }
                }
                if (j == 1) {
                    worldIn.setBlockState(blockpos, Blocks.GLOWSTONE.getDefaultState(), 2);
                }
            }
        }
        return true;
    }
}
