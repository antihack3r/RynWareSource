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

public class HitBox extends Feature
{
    public static NumberSetting hitboxSize;
    
    public HitBox() {
        super("HitBox", FeatureCategory.Combat);
        this.addSettings(HitBox.hitboxSize);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        this.setSuffix("" + HitBox.hitboxSize.getNumberValue());
    }
    
    static {
        HitBox.hitboxSize = new NumberSetting("HitBox Size", 0.25f, 0.1f, 2.0f, 0.1f, () -> true);
    }
}
