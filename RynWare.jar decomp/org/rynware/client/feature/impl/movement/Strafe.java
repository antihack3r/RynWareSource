// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.movement;

import org.rynware.client.event.EventTarget;
import org.rynware.client.utils.movement.MovementUtils;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.feature.Feature;

public class Strafe extends Feature
{
    public static BooleanSetting smart;
    
    public Strafe() {
        super("Strafe", FeatureCategory.Movement);
        this.addSettings(Strafe.smart);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        if (!this.isEnabled()) {
            return;
        }
        if (MovementUtils.isMoving() && MovementUtils.getSpeed() < 0.2177f) {
            MovementUtils.strafe();
            if (Math.abs(Strafe.mc.player.movementInput.moveStrafe) < 0.1 && Strafe.mc.gameSettings.keyBindForward.pressed) {
                MovementUtils.strafe();
            }
            if (Strafe.mc.player.onGround) {
                MovementUtils.strafe();
            }
        }
    }
    
    static {
        Strafe.smart = new BooleanSetting("Smart", false, () -> true);
    }
}
