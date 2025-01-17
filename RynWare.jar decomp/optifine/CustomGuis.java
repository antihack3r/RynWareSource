// 
// Decompiled by Procyon v0.6.0
// 

package optifine;

import java.util.Calendar;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Arrays;
import net.minecraft.client.resources.IResourcePack;
import java.util.List;
import java.util.ArrayList;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiMerchant;
import net.minecraft.client.gui.inventory.GuiScreenHorseInventory;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.client.gui.GuiHopper;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.client.gui.GuiEnchantment;
import net.minecraft.client.gui.inventory.GuiDispenser;
import net.minecraft.client.gui.inventory.GuiCrafting;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiBrewingStand;
import net.minecraft.client.gui.inventory.GuiBeacon;
import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.Minecraft;

public class CustomGuis
{
    private static Minecraft mc;
    private static PlayerControllerOF playerControllerOF;
    private static CustomGuiProperties[][] guiProperties;
    public static boolean isChristmas;
    
    public static ResourceLocation getTextureLocation(final ResourceLocation p_getTextureLocation_0_) {
        if (CustomGuis.guiProperties == null) {
            return p_getTextureLocation_0_;
        }
        final GuiScreen guiscreen = CustomGuis.mc.currentScreen;
        if (!(guiscreen instanceof GuiContainer)) {
            return p_getTextureLocation_0_;
        }
        if (!p_getTextureLocation_0_.getResourceDomain().equals("minecraft") || !p_getTextureLocation_0_.getResourcePath().startsWith("textures/gui/")) {
            return p_getTextureLocation_0_;
        }
        if (CustomGuis.playerControllerOF == null) {
            return p_getTextureLocation_0_;
        }
        final IBlockAccess iblockaccess = CustomGuis.mc.world;
        if (iblockaccess == null) {
            return p_getTextureLocation_0_;
        }
        if (guiscreen instanceof GuiContainerCreative) {
            return getTexturePos(CustomGuiProperties.EnumContainer.CREATIVE, CustomGuis.mc.player.getPosition(), iblockaccess, p_getTextureLocation_0_);
        }
        if (guiscreen instanceof GuiInventory) {
            return getTexturePos(CustomGuiProperties.EnumContainer.INVENTORY, CustomGuis.mc.player.getPosition(), iblockaccess, p_getTextureLocation_0_);
        }
        final BlockPos blockpos = CustomGuis.playerControllerOF.getLastClickBlockPos();
        if (blockpos != null) {
            if (guiscreen instanceof GuiRepair) {
                return getTexturePos(CustomGuiProperties.EnumContainer.ANVIL, blockpos, iblockaccess, p_getTextureLocation_0_);
            }
            if (guiscreen instanceof GuiBeacon) {
                return getTexturePos(CustomGuiProperties.EnumContainer.BEACON, blockpos, iblockaccess, p_getTextureLocation_0_);
            }
            if (guiscreen instanceof GuiBrewingStand) {
                return getTexturePos(CustomGuiProperties.EnumContainer.BREWING_STAND, blockpos, iblockaccess, p_getTextureLocation_0_);
            }
            if (guiscreen instanceof GuiChest) {
                return getTexturePos(CustomGuiProperties.EnumContainer.CHEST, blockpos, iblockaccess, p_getTextureLocation_0_);
            }
            if (guiscreen instanceof GuiCrafting) {
                return getTexturePos(CustomGuiProperties.EnumContainer.CRAFTING, blockpos, iblockaccess, p_getTextureLocation_0_);
            }
            if (guiscreen instanceof GuiDispenser) {
                return getTexturePos(CustomGuiProperties.EnumContainer.DISPENSER, blockpos, iblockaccess, p_getTextureLocation_0_);
            }
            if (guiscreen instanceof GuiEnchantment) {
                return getTexturePos(CustomGuiProperties.EnumContainer.ENCHANTMENT, blockpos, iblockaccess, p_getTextureLocation_0_);
            }
            if (guiscreen instanceof GuiFurnace) {
                return getTexturePos(CustomGuiProperties.EnumContainer.FURNACE, blockpos, iblockaccess, p_getTextureLocation_0_);
            }
            if (guiscreen instanceof GuiHopper) {
                return getTexturePos(CustomGuiProperties.EnumContainer.HOPPER, blockpos, iblockaccess, p_getTextureLocation_0_);
            }
            if (guiscreen instanceof GuiShulkerBox) {
                return getTexturePos(CustomGuiProperties.EnumContainer.SHULKER_BOX, blockpos, iblockaccess, p_getTextureLocation_0_);
            }
        }
        final Entity entity = CustomGuis.playerControllerOF.getLastClickEntity();
        if (entity != null) {
            if (guiscreen instanceof GuiScreenHorseInventory) {
                return getTextureEntity(CustomGuiProperties.EnumContainer.HORSE, entity, iblockaccess, p_getTextureLocation_0_);
            }
            if (guiscreen instanceof GuiMerchant) {
                return getTextureEntity(CustomGuiProperties.EnumContainer.VILLAGER, entity, iblockaccess, p_getTextureLocation_0_);
            }
        }
        return p_getTextureLocation_0_;
    }
    
    private static ResourceLocation getTexturePos(final CustomGuiProperties.EnumContainer p_getTexturePos_0_, final BlockPos p_getTexturePos_1_, final IBlockAccess p_getTexturePos_2_, final ResourceLocation p_getTexturePos_3_) {
        final CustomGuiProperties[] acustomguiproperties = CustomGuis.guiProperties[p_getTexturePos_0_.ordinal()];
        if (acustomguiproperties == null) {
            return p_getTexturePos_3_;
        }
        for (int i = 0; i < acustomguiproperties.length; ++i) {
            final CustomGuiProperties customguiproperties = acustomguiproperties[i];
            if (customguiproperties.matchesPos(p_getTexturePos_0_, p_getTexturePos_1_, p_getTexturePos_2_)) {
                return customguiproperties.getTextureLocation(p_getTexturePos_3_);
            }
        }
        return p_getTexturePos_3_;
    }
    
    private static ResourceLocation getTextureEntity(final CustomGuiProperties.EnumContainer p_getTextureEntity_0_, final Entity p_getTextureEntity_1_, final IBlockAccess p_getTextureEntity_2_, final ResourceLocation p_getTextureEntity_3_) {
        final CustomGuiProperties[] acustomguiproperties = CustomGuis.guiProperties[p_getTextureEntity_0_.ordinal()];
        if (acustomguiproperties == null) {
            return p_getTextureEntity_3_;
        }
        for (int i = 0; i < acustomguiproperties.length; ++i) {
            final CustomGuiProperties customguiproperties = acustomguiproperties[i];
            if (customguiproperties.matchesEntity(p_getTextureEntity_0_, p_getTextureEntity_1_, p_getTextureEntity_2_)) {
                return customguiproperties.getTextureLocation(p_getTextureEntity_3_);
            }
        }
        return p_getTextureEntity_3_;
    }
    
    public static void update() {
        CustomGuis.guiProperties = null;
        if (Config.isCustomGuis()) {
            final List<List<CustomGuiProperties>> list = new ArrayList<List<CustomGuiProperties>>();
            final IResourcePack[] airesourcepack = Config.getResourcePacks();
            for (int i = airesourcepack.length - 1; i >= 0; --i) {
                final IResourcePack iresourcepack = airesourcepack[i];
                update(iresourcepack, list);
            }
            CustomGuis.guiProperties = propertyListToArray(list);
        }
    }
    
    private static CustomGuiProperties[][] propertyListToArray(final List<List<CustomGuiProperties>> p_propertyListToArray_0_) {
        if (p_propertyListToArray_0_.isEmpty()) {
            return null;
        }
        final CustomGuiProperties[][] acustomguiproperties = new CustomGuiProperties[CustomGuiProperties.EnumContainer.values().length][];
        for (int i = 0; i < acustomguiproperties.length; ++i) {
            if (p_propertyListToArray_0_.size() > i) {
                final List<CustomGuiProperties> list = p_propertyListToArray_0_.get(i);
                if (list != null) {
                    final CustomGuiProperties[] acustomguiproperties2 = list.toArray(new CustomGuiProperties[list.size()]);
                    acustomguiproperties[i] = acustomguiproperties2;
                }
            }
        }
        return acustomguiproperties;
    }
    
    private static void update(final IResourcePack p_update_0_, final List<List<CustomGuiProperties>> p_update_1_) {
        final String[] astring = ResUtils.collectFiles(p_update_0_, "optifine/gui/container/", ".properties", null);
        Arrays.sort(astring);
        for (int i = 0; i < astring.length; ++i) {
            final String s = astring[i];
            Config.dbg("CustomGuis: " + s);
            try {
                final ResourceLocation resourcelocation = new ResourceLocation(s);
                final InputStream inputstream = p_update_0_.getInputStream(resourcelocation);
                if (inputstream == null) {
                    Config.warn("CustomGuis file not found: " + s);
                }
                else {
                    final Properties properties = new Properties();
                    properties.load(inputstream);
                    inputstream.close();
                    final CustomGuiProperties customguiproperties = new CustomGuiProperties(properties, s);
                    if (customguiproperties.isValid(s)) {
                        addToList(customguiproperties, p_update_1_);
                    }
                }
            }
            catch (final FileNotFoundException var9) {
                Config.warn("CustomGuis file not found: " + s);
            }
            catch (final Exception exception) {
                exception.printStackTrace();
            }
        }
    }
    
    private static void addToList(final CustomGuiProperties p_addToList_0_, final List<List<CustomGuiProperties>> p_addToList_1_) {
        if (p_addToList_0_.getContainer() == null) {
            warn("Invalid container: " + p_addToList_0_.getContainer());
        }
        else {
            final int i = p_addToList_0_.getContainer().ordinal();
            while (p_addToList_1_.size() <= i) {
                p_addToList_1_.add(null);
            }
            List<CustomGuiProperties> list = p_addToList_1_.get(i);
            if (list == null) {
                list = new ArrayList<CustomGuiProperties>();
                p_addToList_1_.set(i, list);
            }
            list.add(p_addToList_0_);
        }
    }
    
    public static PlayerControllerOF getPlayerControllerOF() {
        return CustomGuis.playerControllerOF;
    }
    
    public static void setPlayerControllerOF(final PlayerControllerOF p_setPlayerControllerOF_0_) {
        CustomGuis.playerControllerOF = p_setPlayerControllerOF_0_;
    }
    
    private static boolean isChristmas() {
        final Calendar calendar = Calendar.getInstance();
        return calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26;
    }
    
    private static void warn(final String p_warn_0_) {
        Config.warn("[CustomGuis] " + p_warn_0_);
    }
    
    static {
        CustomGuis.mc = Config.getMinecraft();
        CustomGuis.playerControllerOF = null;
        CustomGuis.guiProperties = null;
        CustomGuis.isChristmas = isChristmas();
    }
}
