// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import javax.annotation.Nullable;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.item.ItemBanner;
import net.minecraft.item.ItemStack;
import java.util.List;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.world.IWorldNameable;

public class TileEntityBanner extends TileEntity implements IWorldNameable
{
    private String field_190617_a;
    private EnumDyeColor baseColor;
    private NBTTagList patterns;
    private boolean patternDataSet;
    private List<BannerPattern> patternList;
    private List<EnumDyeColor> colorList;
    private String patternResourceLocation;
    
    public TileEntityBanner() {
        this.baseColor = EnumDyeColor.BLACK;
    }
    
    public void setItemValues(final ItemStack stack, final boolean p_175112_2_) {
        this.patterns = null;
        final NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag");
        if (nbttagcompound != null && nbttagcompound.hasKey("Patterns", 9)) {
            this.patterns = nbttagcompound.getTagList("Patterns", 10).copy();
        }
        this.baseColor = (p_175112_2_ ? func_190616_d(stack) : ItemBanner.getBaseColor(stack));
        this.patternList = null;
        this.colorList = null;
        this.patternResourceLocation = "";
        this.patternDataSet = true;
        this.field_190617_a = (stack.hasDisplayName() ? stack.getDisplayName() : null);
    }
    
    @Override
    public String getName() {
        return this.hasCustomName() ? this.field_190617_a : "banner";
    }
    
    @Override
    public boolean hasCustomName() {
        return this.field_190617_a != null && !this.field_190617_a.isEmpty();
    }
    
    @Override
    public ITextComponent getDisplayName() {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName(), new Object[0]);
    }
    
    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("Base", this.baseColor.getDyeDamage());
        if (this.patterns != null) {
            compound.setTag("Patterns", this.patterns);
        }
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.field_190617_a);
        }
        return compound;
    }
    
    @Override
    public void readFromNBT(final NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("CustomName", 8)) {
            this.field_190617_a = compound.getString("CustomName");
        }
        this.baseColor = EnumDyeColor.byDyeDamage(compound.getInteger("Base"));
        this.patterns = compound.getTagList("Patterns", 10);
        this.patternList = null;
        this.colorList = null;
        this.patternResourceLocation = null;
        this.patternDataSet = true;
    }
    
    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 6, this.getUpdateTag());
    }
    
    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }
    
    public static int getPatterns(final ItemStack stack) {
        final NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag");
        return (nbttagcompound != null && nbttagcompound.hasKey("Patterns")) ? nbttagcompound.getTagList("Patterns", 10).tagCount() : 0;
    }
    
    public List<BannerPattern> getPatternList() {
        this.initializeBannerData();
        return this.patternList;
    }
    
    public List<EnumDyeColor> getColorList() {
        this.initializeBannerData();
        return this.colorList;
    }
    
    public String getPatternResourceLocation() {
        this.initializeBannerData();
        return this.patternResourceLocation;
    }
    
    private void initializeBannerData() {
        if (this.patternList == null || this.colorList == null || this.patternResourceLocation == null) {
            if (!this.patternDataSet) {
                this.patternResourceLocation = "";
            }
            else {
                this.patternList = Lists.newArrayList();
                this.colorList = Lists.newArrayList();
                this.patternList.add(BannerPattern.BASE);
                this.colorList.add(this.baseColor);
                this.patternResourceLocation = "b" + this.baseColor.getDyeDamage();
                if (this.patterns != null) {
                    for (int i = 0; i < this.patterns.tagCount(); ++i) {
                        final NBTTagCompound nbttagcompound = this.patterns.getCompoundTagAt(i);
                        final BannerPattern bannerpattern = BannerPattern.func_190994_a(nbttagcompound.getString("Pattern"));
                        if (bannerpattern != null) {
                            this.patternList.add(bannerpattern);
                            final int j = nbttagcompound.getInteger("Color");
                            this.colorList.add(EnumDyeColor.byDyeDamage(j));
                            this.patternResourceLocation = this.patternResourceLocation + bannerpattern.func_190993_b() + j;
                        }
                    }
                }
            }
        }
    }
    
    public static void removeBannerData(final ItemStack stack) {
        final NBTTagCompound nbttagcompound = stack.getSubCompound("BlockEntityTag");
        if (nbttagcompound != null && nbttagcompound.hasKey("Patterns", 9)) {
            final NBTTagList nbttaglist = nbttagcompound.getTagList("Patterns", 10);
            if (!nbttaglist.hasNoTags()) {
                nbttaglist.removeTag(nbttaglist.tagCount() - 1);
                if (nbttaglist.hasNoTags()) {
                    stack.getTagCompound().removeTag("BlockEntityTag");
                    if (stack.getTagCompound().hasNoTags()) {
                        stack.setTagCompound(null);
                    }
                }
            }
        }
    }
    
    public ItemStack func_190615_l() {
        final ItemStack itemstack = ItemBanner.func_190910_a(this.baseColor, this.patterns);
        if (this.hasCustomName()) {
            itemstack.setStackDisplayName(this.getName());
        }
        return itemstack;
    }
    
    public static EnumDyeColor func_190616_d(final ItemStack p_190616_0_) {
        final NBTTagCompound nbttagcompound = p_190616_0_.getSubCompound("BlockEntityTag");
        return (nbttagcompound != null && nbttagcompound.hasKey("Base")) ? EnumDyeColor.byDyeDamage(nbttagcompound.getInteger("Base")) : EnumDyeColor.BLACK;
    }
}
