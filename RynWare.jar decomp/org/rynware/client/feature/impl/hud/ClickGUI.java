// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.hud;

import net.minecraft.client.gui.GuiScreen;
import org.rynware.client.Main;
import org.rynware.client.ui.settings.Setting;
import java.awt.Color;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.ColorSetting;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.feature.Feature;

public class ClickGUI extends Feature
{
    public static BooleanSetting girl;
    public static BooleanSetting potato_mode;
    public static ListSetting backGroundColor;
    public static ListSetting girlmode;
    public static ListSetting panelMode;
    public static BooleanSetting glow;
    public static BooleanSetting particles;
    public static BooleanSetting blur;
    public static NumberSetting blurInt;
    public static ColorSetting color;
    public static ColorSetting bgcolor;
    public static ListSetting guiColor;
    public static NumberSetting speed;
    public static NumberSetting glowRadius2;
    
    public ClickGUI() {
        super("ClickGUI", "\u041c\u0435\u043d\u044e \u0447\u0438\u0442\u0430", FeatureCategory.Hud);
        this.setBind(54);
        ClickGUI.color = new ColorSetting("Gui Color", new Color(34, 179, 255, 255).getRGB(), () -> ClickGUI.guiColor.currentMode.equals("Custom"));
        ClickGUI.bgcolor = new ColorSetting("Color One", new Color(34, 179, 255, 255).getRGB(), () -> ClickGUI.backGroundColor.currentMode.equals("Static"));
        this.addSettings(ClickGUI.panelMode, ClickGUI.potato_mode, ClickGUI.guiColor, ClickGUI.color, ClickGUI.glow, ClickGUI.glowRadius2, ClickGUI.speed, ClickGUI.backGroundColor, ClickGUI.bgcolor, ClickGUI.blur, ClickGUI.blurInt, ClickGUI.particles, ClickGUI.girl, ClickGUI.girlmode);
    }
    
    @Override
    public void onEnable() {
        ClickGUI.mc.displayGuiScreen(Main.instance.clickGui);
        Main.instance.featureManager.getFeature(ClickGUI.class).setEnabled(false);
        super.onEnable();
    }
    
    static {
        ClickGUI.girl = new BooleanSetting("Anime", true, () -> true);
        ClickGUI.potato_mode = new BooleanSetting("Potato Mode", false, () -> true);
        ClickGUI.backGroundColor = new ListSetting("Background Color", "Static", () -> !ClickGUI.potato_mode.getBoolValue(), new String[] { "Astolfo", "Rainbow", "Static" });
        ClickGUI.girlmode = new ListSetting("Anime Mode", "Girl", () -> ClickGUI.girl.getBoolValue(), new String[] { "Girl", "Rem", "Kaneki", "Violet", "Kirshtein", "002" });
        ClickGUI.panelMode = new ListSetting("Panel Mode", "Rect", () -> ClickGUI.girl.getBoolValue(), new String[] { "Rect", "Blur" });
        ClickGUI.glow = new BooleanSetting("Glow", true, () -> !ClickGUI.potato_mode.getBoolValue());
        ClickGUI.particles = new BooleanSetting("Particles", true, () -> !ClickGUI.potato_mode.getBoolValue());
        ClickGUI.blur = new BooleanSetting("Blur", true, () -> !ClickGUI.potato_mode.getBoolValue());
        ClickGUI.blurInt = new NumberSetting("Blur Amount", 5.0f, 5.0f, 10.0f, 1.0f, () -> ClickGUI.blur.getBoolValue() && !ClickGUI.potato_mode.getBoolValue());
        ClickGUI.guiColor = new ListSetting("Color", "Custom", () -> true, new String[] { "Astolfo", "Client", "Rainbow", "Custom" });
        ClickGUI.speed = new NumberSetting("Speed", 35.0f, 10.0f, 100.0f, 1.0f, () -> true);
        ClickGUI.glowRadius2 = new NumberSetting("Glow Radius", 10.0f, 5.0f, 55.0f, 1.0f, () -> !ClickGUI.potato_mode.getBoolValue());
    }
}
