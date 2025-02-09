// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.combat;

import org.rynware.client.event.EventTarget;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.feature.Feature;

public class MultiActions extends Feature
{
    private final NumberSetting clickCoolDown;
    
    public MultiActions() {
        super("MultiActions", "\u041c\u0443\u043b\u044c\u0442\u0438\u043a \u0430\u043a\u0442\u0435\u043e\u043d\u0441", FeatureCategory.Combat);
        this.clickCoolDown = new NumberSetting("Click CoolDown", 1.0f, 0.5f, 1.0f, 0.1f, () -> true);
        this.addSettings(this.clickCoolDown);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (MultiActions.mc.player.getCooledAttackStrength(0.0f) == this.clickCoolDown.getNumberValue() && MultiActions.mc.gameSettings.keyBindAttack.pressed) {
            MultiActions.mc.clickMouse();
        }
    }
}
