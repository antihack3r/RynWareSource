// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.tileentity;

import net.minecraft.block.BlockJukebox;
import org.apache.logging.log4j.LogManager;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import org.apache.logging.log4j.Logger;

public abstract class TileEntity
{
    private static final Logger LOGGER;
    private static final RegistryNamespaced<ResourceLocation, Class<? extends TileEntity>> field_190562_f;
    protected World world;
    protected BlockPos pos;
    protected boolean tileEntityInvalid;
    private int blockMetadata;
    protected Block blockType;
    
    public TileEntity() {
        this.pos = BlockPos.ORIGIN;
        this.blockMetadata = -1;
    }
    
    private static void func_190560_a(final String p_190560_0_, final Class<? extends TileEntity> p_190560_1_) {
        TileEntity.field_190562_f.putObject(new ResourceLocation(p_190560_0_), p_190560_1_);
    }
    
    @Nullable
    public static ResourceLocation func_190559_a(final Class<? extends TileEntity> p_190559_0_) {
        return TileEntity.field_190562_f.getNameForObject(p_190559_0_);
    }
    
    public World getWorld() {
        return this.world;
    }
    
    public void setWorldObj(final World worldIn) {
        this.world = worldIn;
    }
    
    public boolean hasWorldObj() {
        return this.world != null;
    }
    
    public void readFromNBT(final NBTTagCompound compound) {
        this.pos = new BlockPos(compound.getInteger("x"), compound.getInteger("y"), compound.getInteger("z"));
    }
    
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
        return this.writeInternal(compound);
    }
    
    private NBTTagCompound writeInternal(final NBTTagCompound compound) {
        final ResourceLocation resourcelocation = TileEntity.field_190562_f.getNameForObject(this.getClass());
        if (resourcelocation == null) {
            throw new RuntimeException(this.getClass() + " is missing a mapping! This is a bug!");
        }
        compound.setString("id", resourcelocation.toString());
        compound.setInteger("x", this.pos.getX());
        compound.setInteger("y", this.pos.getY());
        compound.setInteger("z", this.pos.getZ());
        return compound;
    }
    
    @Nullable
    public static TileEntity create(final World worldIn, final NBTTagCompound compound) {
        TileEntity tileentity = null;
        final String s = compound.getString("id");
        try {
            final Class<? extends TileEntity> oclass = TileEntity.field_190562_f.getObject(new ResourceLocation(s));
            if (oclass != null) {
                tileentity = (TileEntity)oclass.newInstance();
            }
        }
        catch (final Throwable throwable1) {
            TileEntity.LOGGER.error("Failed to create block entity {}", (Object)s, (Object)throwable1);
        }
        if (tileentity != null) {
            try {
                tileentity.setWorldCreate(worldIn);
                tileentity.readFromNBT(compound);
            }
            catch (final Throwable throwable2) {
                TileEntity.LOGGER.error("Failed to load data for block entity {}", (Object)s, (Object)throwable2);
                tileentity = null;
            }
        }
        else {
            TileEntity.LOGGER.warn("Skipping BlockEntity with id {}", (Object)s);
        }
        return tileentity;
    }
    
    protected void setWorldCreate(final World worldIn) {
    }
    
    public int getBlockMetadata() {
        if (this.blockMetadata == -1) {
            final IBlockState iblockstate = this.world.getBlockState(this.pos);
            this.blockMetadata = iblockstate.getBlock().getMetaFromState(iblockstate);
        }
        return this.blockMetadata;
    }
    
    public void markDirty() {
        if (this.world != null) {
            final IBlockState iblockstate = this.world.getBlockState(this.pos);
            this.blockMetadata = iblockstate.getBlock().getMetaFromState(iblockstate);
            this.world.markChunkDirty(this.pos, this);
            if (this.getBlockType() != Blocks.AIR) {
                this.world.updateComparatorOutputLevel(this.pos, this.getBlockType());
            }
        }
    }
    
    public double getDistanceSq(final double x, final double y, final double z) {
        final double d0 = this.pos.getX() + 0.5 - x;
        final double d2 = this.pos.getY() + 0.5 - y;
        final double d3 = this.pos.getZ() + 0.5 - z;
        return d0 * d0 + d2 * d2 + d3 * d3;
    }
    
    public double getMaxRenderDistanceSquared() {
        return 4096.0;
    }
    
    public BlockPos getPos() {
        return this.pos;
    }
    
    public Block getBlockType() {
        if (this.blockType == null && this.world != null) {
            this.blockType = this.world.getBlockState(this.pos).getBlock();
        }
        return this.blockType;
    }
    
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return null;
    }
    
    public NBTTagCompound getUpdateTag() {
        return this.writeInternal(new NBTTagCompound());
    }
    
    public boolean isInvalid() {
        return this.tileEntityInvalid;
    }
    
    public void invalidate() {
        this.tileEntityInvalid = true;
    }
    
    public void validate() {
        this.tileEntityInvalid = false;
    }
    
    public boolean receiveClientEvent(final int id, final int type) {
        return false;
    }
    
    public void updateContainingBlockInfo() {
        this.blockType = null;
        this.blockMetadata = -1;
    }
    
    public void addInfoToCrashReport(final CrashReportCategory reportCategory) {
        reportCategory.setDetail("Name", new ICrashReportDetail<String>() {
            @Override
            public String call() throws Exception {
                return TileEntity.field_190562_f.getNameForObject(TileEntity.this.getClass()) + " // " + TileEntity.this.getClass().getCanonicalName();
            }
        });
        if (this.world != null) {
            CrashReportCategory.addBlockInfo(reportCategory, this.pos, this.getBlockType(), this.getBlockMetadata());
            reportCategory.setDetail("Actual block type", new ICrashReportDetail<String>() {
                @Override
                public String call() throws Exception {
                    final int i = Block.getIdFromBlock(TileEntity.this.world.getBlockState(TileEntity.this.pos).getBlock());
                    try {
                        return String.format("ID #%d (%s // %s)", i, Block.getBlockById(i).getUnlocalizedName(), Block.getBlockById(i).getClass().getCanonicalName());
                    }
                    catch (final Throwable var3) {
                        return "ID #" + i;
                    }
                }
            });
            reportCategory.setDetail("Actual block data value", new ICrashReportDetail<String>() {
                @Override
                public String call() throws Exception {
                    final IBlockState iblockstate = TileEntity.this.world.getBlockState(TileEntity.this.pos);
                    final int i = iblockstate.getBlock().getMetaFromState(iblockstate);
                    if (i < 0) {
                        return "Unknown? (Got " + i + ")";
                    }
                    final String s = String.format("%4s", Integer.toBinaryString(i)).replace(" ", "0");
                    return String.format("%1$d / 0x%1$X / 0b%2$s", i, s);
                }
            });
        }
    }
    
    public void setPos(final BlockPos posIn) {
        this.pos = posIn.toImmutable();
    }
    
    public boolean onlyOpsCanSetNbt() {
        return false;
    }
    
    @Nullable
    public ITextComponent getDisplayName() {
        return null;
    }
    
    public void rotate(final Rotation p_189667_1_) {
    }
    
    public void mirror(final Mirror p_189668_1_) {
    }
    
    static {
        LOGGER = LogManager.getLogger();
        field_190562_f = new RegistryNamespaced<ResourceLocation, Class<? extends TileEntity>>();
        func_190560_a("furnace", TileEntityFurnace.class);
        func_190560_a("chest", TileEntityChest.class);
        func_190560_a("ender_chest", TileEntityEnderChest.class);
        func_190560_a("jukebox", BlockJukebox.TileEntityJukebox.class);
        func_190560_a("dispenser", TileEntityDispenser.class);
        func_190560_a("dropper", TileEntityDropper.class);
        func_190560_a("sign", TileEntitySign.class);
        func_190560_a("mob_spawner", TileEntityMobSpawner.class);
        func_190560_a("noteblock", TileEntityNote.class);
        func_190560_a("piston", TileEntityPiston.class);
        func_190560_a("brewing_stand", TileEntityBrewingStand.class);
        func_190560_a("enchanting_table", TileEntityEnchantmentTable.class);
        func_190560_a("end_portal", TileEntityEndPortal.class);
        func_190560_a("beacon", TileEntityBeacon.class);
        func_190560_a("skull", TileEntitySkull.class);
        func_190560_a("daylight_detector", TileEntityDaylightDetector.class);
        func_190560_a("hopper", TileEntityHopper.class);
        func_190560_a("comparator", TileEntityComparator.class);
        func_190560_a("flower_pot", TileEntityFlowerPot.class);
        func_190560_a("banner", TileEntityBanner.class);
        func_190560_a("structure_block", TileEntityStructure.class);
        func_190560_a("end_gateway", TileEntityEndGateway.class);
        func_190560_a("command_block", TileEntityCommandBlock.class);
        func_190560_a("shulker_box", TileEntityShulkerBox.class);
        func_190560_a("bed", TileEntityBed.class);
    }
}
