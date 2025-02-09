// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.movement;

import org.rynware.client.event.EventTarget;
import org.rynware.client.utils.movement.MovementUtils;
import net.minecraft.init.MobEffects;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.feature.Feature;

public class WaterSpeed extends Feature
{
    private final NumberSetting speed;
    private final BooleanSetting speedCheck;
    
    public WaterSpeed() {
        super("WaterSpeed", FeatureCategory.Movement);
        this.speed = new NumberSetting("Speed Amount", 0.4f, 0.1f, 4.0f, 0.01f, () -> true);
        this.speedCheck = new BooleanSetting("Speed Potion Check", false, () -> true);
        this.addSettings(this.speedCheck, this.speed);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (!WaterSpeed.mc.player.isPotionActive(MobEffects.SPEED) && this.speedCheck.getBoolValue()) {
            return;
        }
        if (WaterSpeed.mc.player.isInWater() || WaterSpeed.mc.player.isInLava()) {
            MovementUtils.setSpeed(this.speed.getNumberValue());
        }
    }
}
