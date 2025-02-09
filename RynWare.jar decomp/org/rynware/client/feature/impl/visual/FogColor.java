// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.visual;

import org.rynware.client.event.EventTarget;
import org.rynware.client.utils.render.ClientHelper;
import org.rynware.client.utils.render.ColorUtils;
import org.rynware.client.event.events.impl.render.EventFogColor;
import org.rynware.client.ui.settings.Setting;
import java.awt.Color;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.ColorSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.feature.Feature;

public class FogColor extends Feature
{
    public static NumberSetting distance;
    public ListSetting colorMode;
    public ColorSetting customColor;
    
    public FogColor() {
        super("FogColor", FeatureCategory.Visuals);
        this.colorMode = new ListSetting("Fog Color", "Rainbow", () -> true, new String[] { "Rainbow", "Client", "Custom" });
        FogColor.distance = new NumberSetting("Distance", 0.1f, 0.01f, 2.0f, 0.01f, () -> true);
        this.customColor = new ColorSetting("Custom Fog", new Color(11219403).getRGB(), () -> this.colorMode.currentMode.equals("Custom"));
        this.addSettings(this.colorMode, FogColor.distance, this.customColor);
    }
    
    @EventTarget
    public void onFogColor(final EventFogColor event) {
        final String colorModeValue = this.colorMode.getOptions();
        if (colorModeValue.equalsIgnoreCase("Rainbow")) {
            final Color color = ColorUtils.rainbow(1, 1.0f, 1.0f);
            event.setRed((float)color.getRed());
            event.setGreen((float)color.getGreen());
            event.setBlue((float)color.getBlue());
        }
        else if (colorModeValue.equalsIgnoreCase("Client")) {
            final Color clientColor = ClientHelper.getClientColor();
            event.setRed((float)clientColor.getRed());
            event.setGreen((float)clientColor.getGreen());
            event.setBlue((float)clientColor.getBlue());
        }
        else if (colorModeValue.equalsIgnoreCase("Custom")) {
            final Color customColorValue = new Color(this.customColor.getColorValue());
            event.setRed((float)customColorValue.getRed());
            event.setGreen((float)customColorValue.getGreen());
            event.setBlue((float)customColorValue.getBlue());
        }
    }
}
