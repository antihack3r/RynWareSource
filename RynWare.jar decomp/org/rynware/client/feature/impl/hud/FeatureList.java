// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.hud;

import org.rynware.client.event.EventTarget;
import java.util.Iterator;
import java.util.List;
import org.rynware.client.utils.render.RenderUtils;
import org.rynware.client.utils.render.ColorUtils;
import org.lwjgl.opengl.GL11;
import org.rynware.client.utils.math.AnimationHelper;
import java.util.Comparator;
import org.rynware.client.utils.render.ClientHelper;
import org.rynware.client.Main;
import org.rynware.client.event.events.impl.render.EventRender2D;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.ColorSetting;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import java.awt.Color;
import org.rynware.client.feature.Feature;

public class FeatureList extends Feature
{
    public float scale;
    private Color glowColorValue;
    public static ListSetting colorList;
    public static ListSetting fontList;
    public static BooleanSetting background;
    public static ListSetting backGroundColorMode;
    public static NumberSetting backGroundAlpha;
    public NumberSetting offsetY;
    public NumberSetting listOffset;
    public ColorSetting backGroundColor;
    public BooleanSetting rightBorder;
    public BooleanSetting onlyBinds;
    public BooleanSetting noVisualModules;
    public BooleanSetting glow;
    public ListSetting glowBase;
    public ColorSetting glowColor;
    public NumberSetting glowRadius;
    public NumberSetting glowAlpha;
    public static ColorSetting oneColor;
    public static ColorSetting twoColor;
    public static BooleanSetting lowerCase;
    
    public FeatureList() {
        super("ArrayList", "\u0421\u043f\u0438\u0441\u043e\u043a \u043c\u043e\u0434\u0443\u043b\u0435\u0439", FeatureCategory.Hud);
        this.scale = 2.0f;
        this.glowColorValue = new Color(-1);
        this.offsetY = new NumberSetting("Offset Y", 9.0f, 3.0f, 10.0f, 0.05f, () -> true);
        this.listOffset = new NumberSetting("ListOffset", 15.0f, 1.0f, 120.0f, 1.0f, () -> true);
        this.backGroundColor = new ColorSetting("Color", new Color(17, 17, 17, 180).getRGB(), () -> FeatureList.background.getBoolValue() && FeatureList.backGroundColorMode.currentMode.equals("Custom"));
        this.rightBorder = new BooleanSetting("Right Border", true, () -> true);
        this.onlyBinds = new BooleanSetting("Only Binds", false, () -> true);
        this.noVisualModules = new BooleanSetting("No Visual Modules", false, () -> true);
        this.glow = new BooleanSetting("Glow", false, () -> true);
        this.glowBase = new ListSetting("Glow Color", "Rainbow", () -> this.glow.getBoolValue(), new String[] { "Rainbow", "Client", "Astolfo", "Custom" });
        this.glowColor = new ColorSetting("Glow Custom Color", new Color(16777215).getRGB(), () -> this.glowBase.currentMode.equalsIgnoreCase("Custom"));
        this.glowRadius = new NumberSetting("Glow Radius", 10.0f, 0.0f, 50.0f, 1.0f, () -> this.glow.getBoolValue());
        this.glowAlpha = new NumberSetting("Glow Alpha", 80.0f, 30.0f, 255.0f, 1.0f, () -> this.glow.getBoolValue());
        this.addSettings(FeatureList.colorList, FeatureList.oneColor, FeatureList.twoColor, FeatureList.fontList, this.onlyBinds, this.noVisualModules, FeatureList.background, FeatureList.backGroundColorMode, this.backGroundColor, FeatureList.backGroundAlpha, this.rightBorder, this.glow, this.glowRadius, this.glowColor, this.glowAlpha, this.offsetY, this.listOffset, this.glowBase, FeatureList.lowerCase);
    }
    
    @EventTarget
    public void Event2D(final EventRender2D event) {
        if (!this.isEnabled()) {
            return;
        }
        final List<Feature> activeModules = Main.instance.featureManager.getAllFeatures();
        activeModules.sort(Comparator.comparingDouble(s -> -ClientHelper.getFontRender().getStringWidth(s.getSuffix())));
        final float displayWidth = event.getResolution().getScaledWidth() * (event.getResolution().getScaleFactor() / 2.0f) - 5.0f;
        int y = (int)(7.0f + this.listOffset.getNumberValue());
        int yTotal = 0;
        for (int i = 0; i < Main.instance.featureManager.getAllFeatures().size(); ++i) {
            yTotal += ClientHelper.getFontRender().getFontHeight() + 3;
        }
        for (final Feature module : activeModules) {
            module.animYto = AnimationHelper.Move(module.animYto, (float)(module.isEnabled() ? 1 : 0), (float)(6.5 * Main.deltaTime()), (float)(6.5 * Main.deltaTime()), (float)Main.deltaTime());
            if (module.animYto > 0.01f) {
                if (module.getSuffix().equals("ClickGui") || module.getSuffix().equalsIgnoreCase("Hud") || (this.noVisualModules.getBoolValue() && module.getCategory() == FeatureCategory.Visuals)) {
                    continue;
                }
                if (this.onlyBinds.getBoolValue() && module.getBind() == 0) {
                    continue;
                }
                GL11.glPushMatrix();
                GL11.glTranslated(1.0, (double)y, 1.0);
                GL11.glScaled(1.0, (double)module.animYto, 1.0);
                GL11.glTranslated(-1.0, (double)(-y), 1.0);
                if (this.glow.getBoolValue()) {
                    if (this.glowBase.currentMode.equalsIgnoreCase("Rainbow")) {
                        this.glowColorValue = ColorUtils.rainbow(20, 1.0f, 1.0f);
                    }
                    else if (this.glowBase.currentMode.equalsIgnoreCase("Astolfo")) {
                        this.glowColorValue = ColorUtils.astolfo(4.0f, (float)yTotal);
                    }
                    else if (this.glowBase.currentMode.equalsIgnoreCase("Client")) {
                        this.glowColorValue = ClientHelper.getClientColor();
                    }
                    else if (this.glowBase.currentMode.equalsIgnoreCase("Custom")) {
                        this.glowColorValue = new Color(this.glowColor.getColorValue());
                    }
                }
                if (this.glow.getBoolValue()) {
                    RenderUtils.drawBlurredShadow(displayWidth - ClientHelper.getFontRender().getStringWidth(FeatureList.lowerCase.getBoolValue() ? module.getSuffix().toLowerCase() : module.getSuffix()) - 3.5f, (float)y, (float)(3 + ClientHelper.getFontRender().getStringWidth(FeatureList.lowerCase.getBoolValue() ? module.getSuffix().toLowerCase() : module.getSuffix())), this.offsetY.getNumberValue(), (int)this.glowRadius.getNumberValue(), RenderUtils.injectAlpha(this.glowColorValue, (int)this.glowAlpha.getNumberValue()));
                }
                if (FeatureList.background.getBoolValue()) {
                    RenderUtils.drawRect(displayWidth - ClientHelper.getFontRender().getStringWidth(FeatureList.lowerCase.getBoolValue() ? module.getSuffix().toLowerCase() : module.getSuffix()) - 3.0f, y, displayWidth, y + this.offsetY.getNumberValue(), FeatureList.backGroundColorMode.currentMode.equals("Client") ? RenderUtils.injectAlpha(ClientHelper.getClientColor((float)y, (float)yTotal, 5), (int)FeatureList.backGroundAlpha.getNumberValue()).getRGB() : this.backGroundColor.getColorValue());
                }
                if (this.rightBorder.getBoolValue()) {
                    RenderUtils.drawRect(displayWidth - 1.0f, y, displayWidth, y + this.offsetY.getNumberValue(), ClientHelper.getClientColor((float)y, (float)yTotal, 5).getRGB());
                }
                final float f = FeatureList.fontList.currentMode.equalsIgnoreCase("Tahoma") ? 1.0f : 0.0f;
                ClientHelper.getFontRender().drawString(FeatureList.lowerCase.getBoolValue() ? module.getSuffix().toLowerCase() : module.getSuffix(), displayWidth - ClientHelper.getFontRender().getStringWidth(module.getSuffix()) - 2.0f, y + ClientHelper.getFontRender().getFontHeight() - 3 - f, ClientHelper.getClientColor((float)y, (float)yTotal, 5).getRGB());
                y += (int)(this.offsetY.getNumberValue() * module.animYto);
                GL11.glPopMatrix();
            }
        }
    }
    
    static {
        FeatureList.colorList = new ListSetting("ArrayList Color", "Astolfo", () -> true, new String[] { "Astolfo", "Rainbow", "Fade", "Custom" });
        FeatureList.fontList = new ListSetting("ArrayList Font", "Rubik", () -> true, new String[] { "Rubik", "SF-UI", "Myseo", "Tahoma", "Montserrat", "Apple", "Tenacity", "Tenacity Bold" });
        FeatureList.background = new BooleanSetting("Background", true, () -> true);
        FeatureList.backGroundColorMode = new ListSetting("Background Color", "Custom", () -> FeatureList.background.getBoolValue(), new String[] { "Custom", "Client" });
        FeatureList.backGroundAlpha = new NumberSetting("BackGround Alpha", 90.0f, 5.0f, 255.0f, 1.0f, () -> FeatureList.background.getBoolValue() && FeatureList.backGroundColorMode.currentMode.equals("Client"));
        FeatureList.oneColor = new ColorSetting("One Color", new Color(16777215).getRGB(), () -> FeatureList.colorList.currentMode.equals("Custom") || FeatureList.colorList.currentMode.equals("Fade"));
        FeatureList.twoColor = new ColorSetting("Two Color", new Color(16777215).getRGB(), () -> FeatureList.colorList.currentMode.equals("Custom"));
        FeatureList.lowerCase = new BooleanSetting("Font LowerCase", true, () -> true);
    }
}
