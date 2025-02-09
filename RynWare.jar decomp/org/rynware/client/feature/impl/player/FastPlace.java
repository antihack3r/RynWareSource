// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.player;

import org.rynware.client.event.EventTarget;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.feature.Feature;

public class FastPlace extends Feature
{
    public FastPlace() {
        super("FastPlace", FeatureCategory.Player);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        FastPlace.mc.rightClickDelayTimer = 0;
    }
    
    @Override
    public void onDisable() {
        FastPlace.mc.rightClickDelayTimer = 6;
        super.onDisable();
    }
}
