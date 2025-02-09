// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.combat;

import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;
import org.rynware.client.event.EventTarget;
import org.rynware.client.event.events.impl.player.EventPreMotion;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.feature.Feature;

public class AutoGapple extends Feature
{
    private boolean isActive;
    public static NumberSetting health;
    
    public AutoGapple() {
        super("AutoGapple", FeatureCategory.Combat);
        AutoGapple.health = new NumberSetting("Health Amount", 15.0f, 1.0f, 20.0f, 1.0f, () -> true);
        this.addSettings(AutoGapple.health);
    }
    
    @EventTarget
    public void onUpdate(final EventPreMotion eventUpdate) {
        this.setSuffix("" + (int)AutoGapple.health.getNumberValue());
        if (AutoGapple.mc.player == null || AutoGapple.mc.world == null) {
            return;
        }
        if (this.isGoldenApple(AutoGapple.mc.player.getHeldItemOffhand()) && AutoGapple.mc.player.getHealth() <= AutoGapple.health.getNumberValue()) {
            this.isActive = true;
            AutoGapple.mc.gameSettings.keyBindUseItem.pressed = true;
        }
        else if (this.isActive) {
            AutoGapple.mc.gameSettings.keyBindUseItem.pressed = false;
            this.isActive = false;
        }
    }
    
    private boolean isGoldenApple(final ItemStack itemStack) {
        return itemStack != null && !itemStack.func_190926_b() && itemStack.getItem() instanceof ItemAppleGold;
    }
    
    @Override
    public void onDisable() {
        AutoGapple.mc.gameSettings.keyBindUseItem.pressed = false;
        this.isActive = false;
        super.onDisable();
    }
}
