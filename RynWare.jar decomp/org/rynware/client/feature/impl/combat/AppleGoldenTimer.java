// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.combat;

import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;
import org.rynware.client.event.EventTarget;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.lwjgl.input.Mouse;
import net.minecraft.init.Items;
import org.rynware.client.event.events.impl.player.EventPreMotion;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.feature.Feature;

public class AppleGoldenTimer extends Feature
{
    public static boolean cooldown;
    private boolean isEated;
    public static BooleanSetting smart;
    
    public AppleGoldenTimer() {
        super("AppleGoldenTimer", FeatureCategory.Combat);
        this.addSettings(AppleGoldenTimer.smart);
    }
    
    @EventTarget
    public void onUpdate(final EventPreMotion eventUpdate) {
        if (AppleGoldenTimer.mc.player.getHeldItemOffhand().isOnFinish() || (AppleGoldenTimer.mc.player.getHeldItemMainhand().isOnFinish() && AppleGoldenTimer.mc.player.getActiveItemStack().getItem() == Items.GOLDEN_APPLE)) {
            this.isEated = true;
        }
        if (this.isEated) {
            AppleGoldenTimer.mc.player.getCooldownTracker().setCooldown(Items.GOLDEN_APPLE, 55);
            this.isEated = false;
        }
        if (AppleGoldenTimer.mc.player.getCooldownTracker().hasCooldown(Items.GOLDEN_APPLE) && AppleGoldenTimer.mc.player.getActiveItemStack().getItem() == Items.GOLDEN_APPLE) {
            AppleGoldenTimer.mc.gameSettings.keyBindUseItem.setPressed(false);
        }
        else if (Mouse.isButtonDown(1) && !(AppleGoldenTimer.mc.currentScreen instanceof GuiContainer)) {
            AppleGoldenTimer.mc.gameSettings.keyBindUseItem.setPressed(true);
        }
    }
    
    private boolean isGoldenApple(final ItemStack itemStack) {
        return itemStack != null && !itemStack.func_190926_b() && itemStack.getItem() instanceof ItemAppleGold;
    }
    
    static {
        AppleGoldenTimer.smart = new BooleanSetting("Smart", false, () -> true);
    }
}
