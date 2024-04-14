// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.movement;

import org.rynware.client.event.EventTarget;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.feature.Feature;

public class Sprint extends Feature
{
    public Sprint() {
        super("AutoSprint", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0441\u043f\u0440\u0438\u043d\u0442\u0438\u0442", FeatureCategory.Movement);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        if (Sprint.mc.player.getFoodStats().getFoodLevel() / 2 > 3) {
            Sprint.mc.player.setSprinting(Sprint.mc.player.moveForward > 0.0f);
        }
    }
    
    @Override
    public void onDisable() {
        Sprint.mc.player.setSprinting(false);
        super.onDisable();
    }
}
