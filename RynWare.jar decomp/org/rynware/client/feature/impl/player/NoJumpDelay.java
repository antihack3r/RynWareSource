// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.player;

import org.rynware.client.event.EventTarget;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.feature.Feature;

public class NoJumpDelay extends Feature
{
    public NoJumpDelay() {
        super("NoJumpDelay", "\u0423\u0431\u0438\u0440\u0430\u0435\u0442 \u0437\u0430\u0434\u0435\u0440\u0436\u043a\u0443 \u043f\u0440\u044b\u0436\u043a\u043e\u0432", FeatureCategory.Player);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        if (!this.isEnabled()) {
            return;
        }
        NoJumpDelay.mc.player.jumpTicks = 0;
    }
}
