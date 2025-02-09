// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.player;

import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.feature.Feature;

public class NoPush extends Feature
{
    public static BooleanSetting water;
    public static BooleanSetting players;
    public static BooleanSetting blocks;
    
    public NoPush() {
        super("NoPush", "\u0423\u0431\u0438\u0440\u0430\u0435\u0442 \u043e\u0442\u0442\u0430\u043b\u043a\u0438\u0432\u0430\u043d\u0438\u0435", FeatureCategory.Player);
        this.addSettings(NoPush.players, NoPush.water, NoPush.blocks);
    }
    
    static {
        NoPush.water = new BooleanSetting("Water", true, () -> true);
        NoPush.players = new BooleanSetting("Entity", true, () -> true);
        NoPush.blocks = new BooleanSetting("Blocks", true, () -> true);
    }
}
