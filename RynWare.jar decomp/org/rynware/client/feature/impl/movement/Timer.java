// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.movement;

import org.rynware.client.event.EventTarget;
import org.rynware.client.utils.movement.MovementUtils;
import org.rynware.client.event.events.impl.player.EventPreMotion;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.feature.Feature;

public class Timer extends Feature
{
    public static int ticks;
    public boolean active;
    private final NumberSetting timerSpeed;
    public static BooleanSetting smart;
    
    public Timer() {
        super("Timer", FeatureCategory.Player);
        this.timerSpeed = new NumberSetting("Timer Amount", 2.0f, 0.1f, 10.0f, 0.1f, () -> true);
        this.addSettings(this.timerSpeed, Timer.smart);
    }
    
    @EventTarget
    public void onPreUpdate(final EventPreMotion preMotion) {
        if (!Timer.smart.getBoolValue()) {
            Timer.mc.timer.timerSpeed = this.timerSpeed.getNumberValue();
        }
        if (Timer.smart.getBoolValue()) {
            if (Timer.ticks <= 50 && !this.active && MovementUtils.isMoving()) {
                ++Timer.ticks;
                Timer.mc.timer.timerSpeed = this.timerSpeed.getNumberValue();
            }
            if (Timer.ticks == 50) {
                this.active = true;
            }
            if (this.active) {
                Timer.mc.timer.timerSpeed = 1.0f;
                if (!MovementUtils.isMoving()) {
                    --Timer.ticks;
                }
            }
            if (Timer.ticks == 0) {
                this.active = false;
            }
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        Timer.mc.timer.timerSpeed = 1.0f;
    }
    
    static {
        Timer.ticks = 0;
        Timer.smart = new BooleanSetting("Smart", false, () -> true);
    }
}
