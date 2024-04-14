// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.combat;

import net.minecraft.entity.item.EntityEnderCrystal;
import java.util.Iterator;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemAir;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.client.gui.inventory.GuiInventory;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.event.EventTarget;
import net.minecraft.client.renderer.GlStateManager;
import org.rynware.client.event.events.impl.render.EventRender2D;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import org.rynware.client.ui.settings.Setting;
import java.util.ArrayList;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.utils.math.TimerHelper;
import java.util.List;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.feature.Feature;

public class AutoTotem extends Feature
{
    private final NumberSetting health;
    private final BooleanSetting countTotem;
    private final BooleanSetting checkCrystal;
    private final BooleanSetting checkTnt;
    private final NumberSetting radiusTnt;
    private final BooleanSetting inventoryOnly;
    private final NumberSetting radiusCrystal;
    private final NumberSetting swapBackDelay;
    private final NumberSetting fallDistance;
    private final BooleanSetting switchBack;
    private final BooleanSetting checkFall;
    private final List<Integer> lastItem;
    private final TimerHelper timerHelper;
    private boolean swap;
    
    public AutoTotem() {
        super("AutoTotem", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0431\u0435\u0440\u0451\u0442 \u0442\u043e\u0442\u0435\u043c \u043f\u0440\u0438 \u043d\u0443\u0436\u043d\u043e\u043c \u0445\u043f", FeatureCategory.Combat);
        this.checkTnt = new BooleanSetting("Check Tnt", true, () -> true);
        this.radiusTnt = new NumberSetting("Distance to Tnt", 6.0f, 1.0f, 8.0f, 1.0f, () -> true);
        this.switchBack = new BooleanSetting("Swap Back", true, () -> true);
        this.lastItem = new ArrayList<Integer>();
        this.timerHelper = new TimerHelper();
        this.swap = false;
        this.health = new NumberSetting("Health Amount", 3.5f, 1.0f, 20.0f, 0.5f, () -> true);
        this.inventoryOnly = new BooleanSetting("Only Inventory", false, () -> true);
        this.swapBackDelay = new NumberSetting("Swap back delay", 100.0f, 10.0f, 500.0f, 5.0f, this.switchBack::getBoolValue);
        this.countTotem = new BooleanSetting("Count Totem", true, () -> true);
        this.checkFall = new BooleanSetting("Check Fall", true, () -> true);
        this.fallDistance = new NumberSetting("Fall Distance", 15.0f, 5.0f, 125.0f, 5.0f, this.checkFall::getBoolValue);
        this.checkCrystal = new BooleanSetting("Check Crystal", true, () -> true);
        this.radiusCrystal = new NumberSetting("Distance to Crystal", 6.0f, 1.0f, 8.0f, 1.0f, this.checkCrystal::getBoolValue);
        this.addSettings(this.switchBack, this.swapBackDelay, this.health, this.inventoryOnly, this.countTotem, this.checkFall, this.fallDistance, this.checkCrystal, this.radiusCrystal, this.checkTnt, this.radiusTnt);
    }
    
    private int fountTotemCount() {
        int count = 0;
        for (int i = 0; i < AutoTotem.mc.player.inventory.getSizeInventory(); ++i) {
            final ItemStack stack = AutoTotem.mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() == Items.Totem) {
                ++count;
            }
        }
        return count;
    }
    
    @EventTarget
    public void onRender2D(final EventRender2D event) {
        if (this.fountTotemCount() > 0 && this.countTotem.getBoolValue()) {
            AutoTotem.mc.rubik_18.drawStringWithShadow(this.fountTotemCount() + "", event.getResolution().getScaledWidth() / 2.0f + 19.0f, event.getResolution().getScaledHeight() / 2.0f, -1);
            for (int i = 0; i < AutoTotem.mc.player.inventory.getSizeInventory(); ++i) {
                final ItemStack stack = AutoTotem.mc.player.inventory.getStackInSlot(i);
                if (stack.getItem() == Items.Totem) {
                    GlStateManager.pushMatrix();
                    GlStateManager.disableBlend();
                    AutoTotem.mc.getRenderItem().renderItemAndEffectIntoGUI(stack, event.getResolution().getScaledWidth() / 2 + 4, event.getResolution().getScaledHeight() / 2 - 7);
                    GlStateManager.popMatrix();
                }
            }
        }
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (this.inventoryOnly.getBoolValue() && !(AutoTotem.mc.currentScreen instanceof GuiInventory)) {
            return;
        }
        int tIndex = -1;
        int totemCount = 0;
        for (int i = 0; i < 45; ++i) {
            if (AutoTotem.mc.player.inventory.getStackInSlot(i).getItem() == Items.Totem && tIndex == -1) {
                tIndex = i;
            }
            if (AutoTotem.mc.player.inventory.getStackInSlot(i).getItem() == Items.Totem) {
                ++totemCount;
            }
        }
        if ((AutoTotem.mc.player.getHealth() < this.health.getNumberValue() || this.checkCrystal() || this.checkTNT() || this.checkFall(this.fallDistance.getNumberValue())) && totemCount != 0 && tIndex != -1) {
            if (AutoTotem.mc.player.getHeldItemOffhand().getItem() != Items.Totem) {
                AutoTotem.mc.playerController.windowClick(0, (tIndex < 9) ? (tIndex + 36) : tIndex, 1, ClickType.PICKUP, AutoTotem.mc.player);
                AutoTotem.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, AutoTotem.mc.player);
                AutoTotem.mc.playerController.windowClick(0, (tIndex < 9) ? (tIndex + 36) : tIndex, 0, ClickType.PICKUP, AutoTotem.mc.player);
                this.swap = true;
                this.lastItem.add(tIndex);
            }
        }
        else if (this.switchBack.getBoolValue() && (this.swap || totemCount == 0) && this.lastItem.size() > 0) {
            if (!(AutoTotem.mc.player.inventory.getStackInSlot(this.lastItem.get(0)).getItem() instanceof ItemAir) && this.timerHelper.hasReached(this.swapBackDelay.getNumberValue())) {
                AutoTotem.mc.playerController.windowClick(0, (this.lastItem.get(0) < 9) ? (this.lastItem.get(0) + 36) : ((int)this.lastItem.get(0)), 0, ClickType.PICKUP, AutoTotem.mc.player);
                AutoTotem.mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, AutoTotem.mc.player);
                AutoTotem.mc.playerController.windowClick(0, (this.lastItem.get(0) < 9) ? (this.lastItem.get(0) + 36) : ((int)this.lastItem.get(0)), 0, ClickType.PICKUP, AutoTotem.mc.player);
                this.timerHelper.reset();
            }
            this.swap = false;
            this.lastItem.clear();
        }
    }
    
    private boolean checkFall(final float fallDist) {
        return this.checkFall.getBoolValue() && AutoTotem.mc.player.fallDistance > fallDist;
    }
    
    private boolean checkTNT() {
        if (!this.checkTnt.getBoolValue()) {
            return false;
        }
        for (final Entity entity : AutoTotem.mc.world.loadedEntityList) {
            if (entity instanceof EntityTNTPrimed && AutoTotem.mc.player.getDistanceToEntity(entity) <= this.radiusTnt.getNumberValue()) {
                return true;
            }
            if (!(entity instanceof EntityMinecartTNT)) {
                continue;
            }
            if (AutoTotem.mc.player.getDistanceToEntity(entity) > this.radiusTnt.getNumberValue()) {
                continue;
            }
            return true;
        }
        return false;
    }
    
    private boolean checkCrystal() {
        if (!this.checkCrystal.getBoolValue()) {
            return false;
        }
        for (final Entity entity : AutoTotem.mc.world.loadedEntityList) {
            if (entity instanceof EntityEnderCrystal) {
                if (AutoTotem.mc.player.getDistanceToEntity(entity) > this.radiusCrystal.getNumberValue()) {
                    continue;
                }
                return true;
            }
        }
        return false;
    }
}
