// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.block;

import java.util.EnumSet;
import java.util.Set;
import java.util.Iterator;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import java.util.Random;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.material.Material;

public class BlockDynamicLiquid extends BlockLiquid
{
    int adjacentSourceBlocks;
    
    protected BlockDynamicLiquid(final Material materialIn) {
        super(materialIn);
    }
    
    private void placeStaticBlock(final World worldIn, final BlockPos pos, final IBlockState currentState) {
        worldIn.setBlockState(pos, BlockLiquid.getStaticBlock(this.blockMaterial).getDefaultState().withProperty((IProperty<Comparable>)BlockDynamicLiquid.LEVEL, (Comparable)currentState.getValue((IProperty<V>)BlockDynamicLiquid.LEVEL)), 2);
    }
    
    @Override
    public void updateTick(final World worldIn, final BlockPos pos, IBlockState state, final Random rand) {
        int i = state.getValue((IProperty<Integer>)BlockDynamicLiquid.LEVEL);
        int j = 1;
        if (this.blockMaterial == Material.LAVA && !worldIn.provider.doesWaterVaporize()) {
            j = 2;
        }
        int k = this.tickRate(worldIn);
        if (i > 0) {
            int l = -100;
            this.adjacentSourceBlocks = 0;
            for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                l = this.checkAdjacentBlock(worldIn, pos.offset(enumfacing), l);
            }
            int i2 = l + j;
            if (i2 >= 8 || l < 0) {
                i2 = -1;
            }
            final int j2 = this.getDepth(worldIn.getBlockState(pos.up()));
            if (j2 >= 0) {
                if (j2 >= 8) {
                    i2 = j2;
                }
                else {
                    i2 = j2 + 8;
                }
            }
            if (this.adjacentSourceBlocks >= 2 && this.blockMaterial == Material.WATER) {
                final IBlockState iblockstate = worldIn.getBlockState(pos.down());
                if (iblockstate.getMaterial().isSolid()) {
                    i2 = 0;
                }
                else if (iblockstate.getMaterial() == this.blockMaterial && iblockstate.getValue((IProperty<Integer>)BlockDynamicLiquid.LEVEL) == 0) {
                    i2 = 0;
                }
            }
            if (this.blockMaterial == Material.LAVA && i < 8 && i2 < 8 && i2 > i && rand.nextInt(4) != 0) {
                k *= 4;
            }
            if (i2 == i) {
                this.placeStaticBlock(worldIn, pos, state);
            }
            else if ((i = i2) < 0) {
                worldIn.setBlockToAir(pos);
            }
            else {
                state = state.withProperty((IProperty<Comparable>)BlockDynamicLiquid.LEVEL, i2);
                worldIn.setBlockState(pos, state, 2);
                worldIn.scheduleUpdate(pos, this, k);
                worldIn.notifyNeighborsOfStateChange(pos, this, false);
            }
        }
        else {
            this.placeStaticBlock(worldIn, pos, state);
        }
        final IBlockState iblockstate2 = worldIn.getBlockState(pos.down());
        if (this.canFlowInto(worldIn, pos.down(), iblockstate2)) {
            if (this.blockMaterial == Material.LAVA && worldIn.getBlockState(pos.down()).getMaterial() == Material.WATER) {
                worldIn.setBlockState(pos.down(), Blocks.STONE.getDefaultState());
                this.triggerMixEffects(worldIn, pos.down());
                return;
            }
            if (i >= 8) {
                this.tryFlowInto(worldIn, pos.down(), iblockstate2, i);
            }
            else {
                this.tryFlowInto(worldIn, pos.down(), iblockstate2, i + 8);
            }
        }
        else if (i >= 0 && (i == 0 || this.isBlocked(worldIn, pos.down(), iblockstate2))) {
            final Set<EnumFacing> set = this.getPossibleFlowDirections(worldIn, pos);
            int k2 = i + j;
            if (i >= 8) {
                k2 = 1;
            }
            if (k2 >= 8) {
                return;
            }
            for (final EnumFacing enumfacing2 : set) {
                this.tryFlowInto(worldIn, pos.offset(enumfacing2), worldIn.getBlockState(pos.offset(enumfacing2)), k2);
            }
        }
    }
    
    private void tryFlowInto(final World worldIn, final BlockPos pos, final IBlockState state, final int level) {
        if (this.canFlowInto(worldIn, pos, state)) {
            if (state.getMaterial() != Material.AIR) {
                if (this.blockMaterial == Material.LAVA) {
                    this.triggerMixEffects(worldIn, pos);
                }
                else {
                    state.getBlock().dropBlockAsItem(worldIn, pos, state, 0);
                }
            }
            worldIn.setBlockState(pos, this.getDefaultState().withProperty((IProperty<Comparable>)BlockDynamicLiquid.LEVEL, level), 3);
        }
    }
    
    private int getSlopeDistance(final World worldIn, final BlockPos pos, final int distance, final EnumFacing calculateFlowCost) {
        int i = 1000;
        for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            if (enumfacing != calculateFlowCost) {
                final BlockPos blockpos = pos.offset(enumfacing);
                final IBlockState iblockstate = worldIn.getBlockState(blockpos);
                if (this.isBlocked(worldIn, blockpos, iblockstate) || (iblockstate.getMaterial() == this.blockMaterial && iblockstate.getValue((IProperty<Integer>)BlockDynamicLiquid.LEVEL) <= 0)) {
                    continue;
                }
                if (!this.isBlocked(worldIn, blockpos.down(), iblockstate)) {
                    return distance;
                }
                if (distance >= this.getSlopeFindDistance(worldIn)) {
                    continue;
                }
                final int j = this.getSlopeDistance(worldIn, blockpos, distance + 1, enumfacing.getOpposite());
                if (j >= i) {
                    continue;
                }
                i = j;
            }
        }
        return i;
    }
    
    private int getSlopeFindDistance(final World worldIn) {
        return (this.blockMaterial == Material.LAVA && !worldIn.provider.doesWaterVaporize()) ? 2 : 4;
    }
    
    private Set<EnumFacing> getPossibleFlowDirections(final World worldIn, final BlockPos pos) {
        int i = 1000;
        final Set<EnumFacing> set = EnumSet.noneOf(EnumFacing.class);
        for (final EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
            final BlockPos blockpos = pos.offset(enumfacing);
            final IBlockState iblockstate = worldIn.getBlockState(blockpos);
            if (!this.isBlocked(worldIn, blockpos, iblockstate) && (iblockstate.getMaterial() != this.blockMaterial || iblockstate.getValue((IProperty<Integer>)BlockDynamicLiquid.LEVEL) > 0)) {
                int j;
                if (this.isBlocked(worldIn, blockpos.down(), worldIn.getBlockState(blockpos.down()))) {
                    j = this.getSlopeDistance(worldIn, blockpos, 1, enumfacing.getOpposite());
                }
                else {
                    j = 0;
                }
                if (j < i) {
                    set.clear();
                }
                if (j > i) {
                    continue;
                }
                set.add(enumfacing);
                i = j;
            }
        }
        return set;
    }
    
    private boolean isBlocked(final World worldIn, final BlockPos pos, final IBlockState state) {
        final Block block = worldIn.getBlockState(pos).getBlock();
        return block instanceof BlockDoor || block == Blocks.STANDING_SIGN || block == Blocks.LADDER || block == Blocks.REEDS || block.blockMaterial == Material.PORTAL || block.blockMaterial == Material.STRUCTURE_VOID || block.blockMaterial.blocksMovement();
    }
    
    protected int checkAdjacentBlock(final World worldIn, final BlockPos pos, final int currentMinLevel) {
        int i = this.getDepth(worldIn.getBlockState(pos));
        if (i < 0) {
            return currentMinLevel;
        }
        if (i == 0) {
            ++this.adjacentSourceBlocks;
        }
        if (i >= 8) {
            i = 0;
        }
        return (currentMinLevel >= 0 && i >= currentMinLevel) ? currentMinLevel : i;
    }
    
    private boolean canFlowInto(final World worldIn, final BlockPos pos, final IBlockState state) {
        final Material material = state.getMaterial();
        return material != this.blockMaterial && material != Material.LAVA && !this.isBlocked(worldIn, pos, state);
    }
    
    @Override
    public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state) {
        if (!this.checkForMixing(worldIn, pos, state)) {
            worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
        }
    }
}
