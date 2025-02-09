// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.misc;

import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.feature.Feature;

public class NameProtect extends Feature
{
    public static BooleanSetting myName;
    public static BooleanSetting friends;
    public static BooleanSetting otherName;
    public static BooleanSetting tabSpoof;
    public static BooleanSetting scoreboardSpoof;
    
    public NameProtect() {
        super("NameProtect", "\u0421\u043a\u0440\u044b\u0432\u0430\u0435\u0442 \u0432\u0430\u0448\u0435 \u0438\u043c\u044f \u043d\u0430 \u0441\u0435\u0440\u0432\u0435\u0440\u0430\u0445", FeatureCategory.Misc);
        this.addSettings(NameProtect.myName, NameProtect.otherName, NameProtect.friends, NameProtect.tabSpoof, NameProtect.scoreboardSpoof);
    }
    
    static {
        NameProtect.myName = new BooleanSetting("My Name", true, () -> true);
        NameProtect.friends = new BooleanSetting("Friends Spoof", true, () -> true);
        NameProtect.otherName = new BooleanSetting("Other Names", false, () -> true);
        NameProtect.tabSpoof = new BooleanSetting("Tab Spoof", false, () -> true);
        NameProtect.scoreboardSpoof = new BooleanSetting("Scoreboard Spoof", true, () -> true);
    }
}
