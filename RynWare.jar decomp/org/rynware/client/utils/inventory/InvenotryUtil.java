// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.utils.inventory;

import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.Item;
import net.minecraft.item.EnumAction;
import net.minecraft.entity.EntityLivingBase;
import java.util.Iterator;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemAxe;
import org.rynware.client.utils.Helper;

public class InvenotryUtil implements Helper
{
    public static boolean doesHotbarHaveAxe() {
        for (int i = 0; i < 9; ++i) {
            InvenotryUtil.mc.player.inventory.getStackInSlot(i);
            if (InvenotryUtil.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemAxe) {
                return true;
            }
        }
        return false;
    }
    
    public static void disabler(final int elytra) {
        InvenotryUtil.mc.playerController.windowClick(0, elytra, 0, ClickType.PICKUP, InvenotryUtil.mc.player);
        InvenotryUtil.mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, InvenotryUtil.mc.player);
        InvenotryUtil.mc.player.connection.sendPacket(new CPacketEntityAction(InvenotryUtil.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        InvenotryUtil.mc.player.connection.sendPacket(new CPacketEntityAction(InvenotryUtil.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
        InvenotryUtil.mc.playerController.windowClick(0, 6, 0, ClickType.PICKUP, InvenotryUtil.mc.player);
        InvenotryUtil.mc.playerController.windowClick(0, elytra, 0, ClickType.PICKUP, InvenotryUtil.mc.player);
    }
    
    public static int getSlotWithElytra() {
        for (int i = 0; i < 45; ++i) {
            final ItemStack itemStack = InvenotryUtil.mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() == Items.ELYTRA) {
                return (i < 9) ? (i + 36) : i;
            }
        }
        return -1;
    }
    
    public static int getSlowWithArmor() {
        for (int i = 0; i < 45; ++i) {
            final ItemStack itemStack = InvenotryUtil.mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() == Items.DIAMOND_CHESTPLATE || itemStack.getItem() == Items.GOLDEN_CHESTPLATE || itemStack.getItem() == Items.LEATHER_CHESTPLATE || itemStack.getItem() == Items.CHAINMAIL_CHESTPLATE || itemStack.getItem() == Items.IRON_LEGGINGS) {
                return (i < 9) ? (i + 36) : i;
            }
        }
        return -1;
    }
    
    public static void swapElytraToChestplate() {
        for (final ItemStack stack : InvenotryUtil.mc.player.inventory.armorInventory) {
            if (stack.getItem() == Items.ELYTRA) {
                final int slot = (getSlowWithArmor() < 9) ? (getSlowWithArmor() + 36) : getSlowWithArmor();
                if (getSlowWithArmor() == -1) {
                    continue;
                }
                InvenotryUtil.mc.playerController.windowClick(0, slot, 0, ClickType.PICKUP, InvenotryUtil.mc.player);
                InvenotryUtil.mc.playerController.windowClick(6, slot, 0, ClickType.PICKUP, InvenotryUtil.mc.player);
            }
        }
    }
    
    public static boolean isActiveItemStackBlocking(final EntityLivingBase base, final int ticks) {
        if (base.isHandActive() && !base.getActiveItemStack().func_190926_b()) {
            final Item item = base.getActiveItemStack().getItem();
            return item.getItemUseAction(base.getActiveItemStack()) == EnumAction.BLOCK && item.getMaxItemUseDuration(base.getActiveItemStack()) - base.activeItemStackUseCount >= ticks;
        }
        return false;
    }
    
    public static int getAxe() {
        for (int i = 0; i < 9; ++i) {
            final ItemStack itemStack = InvenotryUtil.mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() instanceof ItemAxe) {
                return i;
            }
        }
        return 1;
    }
    
    public static boolean doesHotbarHaveBlock() {
        for (int i = 0; i < 9; ++i) {
            if (InvenotryUtil.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) {
                return true;
            }
        }
        return false;
    }
    
    public static int getTotemAtHotbar() {
        for (int i = 0; i < 45; ++i) {
            final ItemStack itemStack = InvenotryUtil.mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() == Items.Totem) {
                return (i < 9) ? (i + 36) : i;
            }
        }
        return -1;
    }
    
    public static int getSwordAtHotbar() {
        for (int i = 0; i < 9; ++i) {
            final ItemStack itemStack = InvenotryUtil.mc.player.inventory.getStackInSlot(i);
            if (itemStack.getItem() instanceof ItemSword) {
                return i;
            }
        }
        return 1;
    }
}
