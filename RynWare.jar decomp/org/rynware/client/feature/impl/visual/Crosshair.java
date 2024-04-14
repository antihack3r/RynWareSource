// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.visual;

import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.feature.Feature;

public class Crosshair extends Feature
{
    public static BooleanSetting smart;
    
    public Crosshair() {
        super("Crosshair", "", FeatureCategory.Visuals);
        this.addSettings(Crosshair.smart);
    }
    
    static {
        Crosshair.smart = new BooleanSetting("Smart", false, () -> true);
    }
}
