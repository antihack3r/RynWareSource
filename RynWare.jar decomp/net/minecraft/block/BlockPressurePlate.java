// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.block;

import net.minecraft.block.state.BlockStateContainer;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.init.SoundEvents;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;

public class BlockPressurePlate extends BlockBasePressurePlate
{
    public static final PropertyBool POWERED;
    private final Sensitivity sensitivity;
    
    protected BlockPressurePlate(final Material materialIn, final Sensitivity sensitivityIn) {
        super(materialIn);
        this.setDefaultState(this.blockState.getBaseState().withProperty((IProperty<Comparable>)BlockPressurePlate.POWERED, false));
        this.sensitivity = sensitivityIn;
    }
    
    @Override
    protected int getRedstoneStrength(final IBlockState state) {
        return state.getValue((IProperty<Boolean>)BlockPressurePlate.POWERED) ? 15 : 0;
    }
    
    @Override
    protected IBlockState setRedstoneStrength(final IBlockState state, final int strength) {
        return state.withProperty((IProperty<Comparable>)BlockPressurePlate.POWERED, strength > 0);
    }
    
    @Override
    protected void playClickOnSound(final World worldIn, final BlockPos color) {
        if (this.blockMaterial == Material.WOOD) {
            worldIn.playSound(null, color, SoundEvents.BLOCK_WOOD_PRESSPLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3f, 0.8f);
        }
        else {
            worldIn.playSound(null, color, SoundEvents.BLOCK_STONE_PRESSPLATE_CLICK_ON, SoundCategory.BLOCKS, 0.3f, 0.6f);
        }
    }
    
    @Override
    protected void playClickOffSound(final World worldIn, final BlockPos pos) {
        if (this.blockMaterial == Material.WOOD) {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_WOOD_PRESSPLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3f, 0.7f);
        }
        else {
            worldIn.playSound(null, pos, SoundEvents.BLOCK_STONE_PRESSPLATE_CLICK_OFF, SoundCategory.BLOCKS, 0.3f, 0.5f);
        }
    }
    
    @Override
    protected int computeRedstoneStrength(final World worldIn, final BlockPos pos) {
        final AxisAlignedBB axisalignedbb = BlockPressurePlate.PRESSURE_AABB.offset(pos);
        List<? extends Entity> list = null;
        switch (this.sensitivity) {
            case EVERYTHING: {
                list = worldIn.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb);
                break;
            }
            case MOBS: {
                list = worldIn.getEntitiesWithinAABB((Class<? extends Entity>)EntityLivingBase.class, axisalignedbb);
                break;
            }
            default: {
                return 0;
            }
        }
        if (!list.isEmpty()) {
            for (final Entity entity : list) {
                if (!entity.doesEntityNotTriggerPressurePlate()) {
                    return 15;
                }
            }
        }
        return 0;
    }
    
    @Override
    public IBlockState getStateFromMeta(final int meta) {
        return this.getDefaultState().withProperty((IProperty<Comparable>)BlockPressurePlate.POWERED, meta == 1);
    }
    
    @Override
    public int getMetaFromState(final IBlockState state) {
        return ((boolean)state.getValue((IProperty<Boolean>)BlockPressurePlate.POWERED)) ? 1 : 0;
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, (IProperty<?>[])new IProperty[] { BlockPressurePlate.POWERED });
    }
    
    static {
        POWERED = PropertyBool.create("powered");
    }
    
    public enum Sensitivity
    {
        EVERYTHING, 
        MOBS;
    }
}
