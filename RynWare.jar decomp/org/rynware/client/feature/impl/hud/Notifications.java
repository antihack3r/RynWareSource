// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.hud;

import org.rynware.client.ui.settings.Setting;
import java.awt.Color;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.ColorSetting;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.feature.Feature;

public class Notifications extends Feature
{
    public static ListSetting notifMode;
    public static BooleanSetting state;
    public static ListSetting notifColorModes;
    public static ColorSetting notifColor;
    public static BooleanSetting notifBlur;
    public static NumberSetting blurAlpha;
    
    public Notifications() {
        super("Notifications", "\u0423\u0432\u0435\u0434\u043e\u043c\u043b\u0435\u043d\u0438\u044f \u043a\u043b\u0438\u0435\u043d\u0442\u0430", FeatureCategory.Hud);
        Notifications.state = new BooleanSetting("Module State", true, () -> true);
        Notifications.notifColorModes = new ListSetting("Color Mode", "Custom", () -> true, new String[] { "Astolfo", "Rainbow", "Custom", "Client" });
        Notifications.notifMode = new ListSetting("Notification Mode", "Rect", () -> true, new String[] { "Rect", "Chat" });
        Notifications.notifColor = new ColorSetting("Notification Color", Color.DARK_GRAY.getRGB(), () -> Notifications.notifMode.currentMode.equalsIgnoreCase("Rect") && Notifications.notifColorModes.currentMode.equalsIgnoreCase("Custom"));
        Notifications.notifBlur = new BooleanSetting("Notification Blur", true, () -> Notifications.notifMode.currentMode.equalsIgnoreCase("Rect"));
        Notifications.blurAlpha = new NumberSetting("Blur Alpha", 125.0f, 1.0f, 255.0f, 1.0f, () -> Notifications.notifBlur.getBoolValue());
        this.addSettings(Notifications.notifMode, Notifications.state, Notifications.notifColor, Notifications.notifBlur, Notifications.notifColorModes, Notifications.blurAlpha);
    }
}
