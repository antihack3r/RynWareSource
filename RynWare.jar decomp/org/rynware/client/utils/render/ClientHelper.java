// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.utils.render;

import org.rynware.client.ui.font.MCFontRenderer;
import org.rynware.client.feature.impl.hud.TargetHUD;
import org.rynware.client.feature.impl.visual.EntityESP;
import net.minecraft.client.Minecraft;
import org.rynware.client.feature.impl.hud.FeatureList;
import java.awt.Color;
import net.minecraft.client.multiplayer.ServerData;
import org.rynware.client.utils.Helper;

public class ClientHelper implements Helper
{
    public static ServerData serverData;
    
    public static Color getClientColor() {
        Color color = Color.white;
        final Color onecolor = new Color(FeatureList.oneColor.getColorValue());
        final Color twoColor = new Color(FeatureList.twoColor.getColorValue());
        final double time = 10.0;
        final String mode = FeatureList.colorList.getOptions();
        final float yDist = 4.0f;
        int yTotal = 0;
        for (int i = 0; i < 30; ++i) {
            yTotal += Minecraft.getMinecraft().sfui18.getFontHeight() + 5;
        }
        if (mode.equalsIgnoreCase("Rainbow")) {
            color = ColorUtils.rainbow(20, 1.0f, 1.0f);
        }
        else if (mode.equalsIgnoreCase("Astolfo")) {
            color = ColorUtils.astolfo((float)(int)yDist, (float)yTotal);
        }
        else if (mode.equalsIgnoreCase("Custom")) {
            color = ColorUtils.TwoColoreffect(new Color(onecolor.getRGB()), new Color(twoColor.getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0 * (yDist * 2.55) / 60.0);
        }
        else if (mode.equalsIgnoreCase("Fade")) {
            color = ColorUtils.TwoColoreffect(new Color(onecolor.getRGB()), new Color(onecolor.darker().darker().getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0 * (yDist * 2.55) / 60.0);
        }
        return color;
    }
    
    public static Color getESPColor() {
        Color color = Color.white;
        final Color onecolor = new Color(EntityESP.colorEsp.getColorValue());
        final double time = 10.0;
        final String mode = EntityESP.colorMode.getOptions();
        final float yDist = 4.0f;
        int yTotal = 0;
        for (int i = 0; i < 30; ++i) {
            yTotal += Minecraft.getMinecraft().sfui18.getFontHeight() + 5;
        }
        if (mode.equalsIgnoreCase("Rainbow")) {
            color = ColorUtils.rainbow(20, 0.5f, 1.0f);
        }
        else if (mode.equalsIgnoreCase("Astolfo")) {
            color = ColorUtils.astolfoColors(5000.0f, 1);
        }
        else if (mode.equalsIgnoreCase("Custom")) {
            color = new Color(onecolor.getRGB());
        }
        else if (mode.equalsIgnoreCase("Client")) {
            color = getClientColor();
        }
        return color;
    }
    
    public static Color getHealthColor() {
        Color color = Color.white;
        final Color onecolor = new Color(EntityESP.healColor.getColorValue());
        final double time = 10.0;
        final String mode = EntityESP.healcolorMode.getOptions();
        final float yDist = 4.0f;
        int yTotal = 0;
        for (int i = 0; i < 30; ++i) {
            yTotal += Minecraft.getMinecraft().sfui18.getFontHeight() + 5;
        }
        if (mode.equalsIgnoreCase("Rainbow")) {
            color = ColorUtils.rainbow(20, 0.5f, 1.0f);
        }
        else if (mode.equalsIgnoreCase("Astolfo")) {
            color = ColorUtils.astolfoColors(5000.0f, 1);
        }
        else if (mode.equalsIgnoreCase("Custom")) {
            color = new Color(onecolor.getRGB());
        }
        else if (mode.equalsIgnoreCase("Client")) {
            color = getClientColor();
        }
        return color;
    }
    
    public static Color getTargetHudColor() {
        Color color = Color.white;
        final Color onecolor = new Color(TargetHUD.targetHudColor.getColorValue());
        final String mode = TargetHUD.thudColorMode.getOptions();
        final float yDist = 4.0f;
        int yTotal = 0;
        for (int i = 0; i < 30; ++i) {
            yTotal += Minecraft.getMinecraft().sfui18.getFontHeight() + 5;
        }
        if (mode.equalsIgnoreCase("Rainbow")) {
            color = ColorUtils.rainbow(20, 0.5f, 1.0f);
        }
        else if (mode.equalsIgnoreCase("Astolfo")) {
            color = ColorUtils.astolfoColors(5000.0f, 1);
        }
        else if (mode.equalsIgnoreCase("Custom")) {
            color = new Color(onecolor.getRGB());
        }
        else if (mode.equalsIgnoreCase("Client")) {
            color = getClientColor();
        }
        return color;
    }
    
    public static Color getClientColor(final float yStep, final float yStepFull, final int speed) {
        Color color = Color.white;
        final Color onecolor = new Color(FeatureList.oneColor.getColorValue());
        final Color twoColor = new Color(FeatureList.twoColor.getColorValue());
        final double time = 10.0;
        final String mode = FeatureList.colorList.getOptions();
        final float yDist = 4.0f;
        int yTotal = 0;
        for (int i = 0; i < 30; ++i) {
            yTotal += Minecraft.getMinecraft().sfui18.getFontHeight() + 5;
        }
        if (mode.equalsIgnoreCase("Rainbow")) {
            color = ColorUtils.rainbow((int)(yStep * time), 0.5f, 1.0f);
        }
        else if (mode.equalsIgnoreCase("Astolfo")) {
            color = ColorUtils.astolfo(yStep, yStepFull, 0.5f, (float)speed);
        }
        else if (mode.equalsIgnoreCase("Custom")) {
            color = ColorUtils.TwoColoreffect(new Color(onecolor.getRGB()), new Color(twoColor.getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0 * (yStep * 2.55) / 60.0);
        }
        else if (mode.equalsIgnoreCase("Fade")) {
            color = ColorUtils.TwoColoreffect(new Color(onecolor.getRGB()), new Color(onecolor.darker().darker().getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0 * (yStep * 2.55) / 60.0);
        }
        return color;
    }
    
    public static Color getClientColor(final float yStep, final float astolfoastep, final float yStepFull, final int speed) {
        Color color = Color.white;
        final Color onecolor = new Color(FeatureList.oneColor.getColorValue());
        final Color twoColor = new Color(FeatureList.twoColor.getColorValue());
        final double time = 11.0;
        final String mode = FeatureList.colorList.getOptions();
        int yTotal = 0;
        for (int i = 0; i < 30; ++i) {
            yTotal += Minecraft.getMinecraft().sfui18.getFontHeight() + 5;
        }
        if (mode.equalsIgnoreCase("Rainbow")) {
            color = ColorUtils.rainbowCol(yStep, yStepFull, 0.5f, (float)speed);
        }
        else if (mode.equalsIgnoreCase("Astolfo")) {
            color = ColorUtils.astolfo(astolfoastep, yStepFull, 0.5f, (float)speed);
        }
        else if (mode.equalsIgnoreCase("Custom")) {
            color = ColorUtils.TwoColoreffect(new Color(onecolor.getRGB()), new Color(twoColor.getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0 * (yStep * 2.55) / 60.0);
        }
        else if (mode.equalsIgnoreCase("Fade")) {
            color = ColorUtils.TwoColoreffect(new Color(onecolor.getRGB()), new Color(onecolor.darker().darker().getRGB()), Math.abs(System.currentTimeMillis() / time) / 100.0 + 3.0 * (yStep * 2.55) / 60.0);
        }
        return color;
    }
    
    public static MCFontRenderer getFontRender() {
        final Minecraft mc = Minecraft.getMinecraft();
        MCFontRenderer font = mc.sfui18;
        final String options;
        final String mode = options = FeatureList.fontList.getOptions();
        switch (options) {
            case "Myseo": {
                font = mc.neverlose500_16;
                break;
            }
            case "Tahoma": {
                font = mc.tahoma16;
                break;
            }
            case "SF-UI": {
                font = mc.sfui16;
                break;
            }
            case "Rubik": {
                font = mc.rubik_16;
                break;
            }
            case "Montserrat": {
                font = mc.mntsb_16;
                break;
            }
            case "Apple": {
                font = mc.apple_16;
                break;
            }
            case "Tenacity": {
                font = mc.tenacity_16;
                break;
            }
            case "Tenacity Bold": {
                font = mc.tenacitybold_16;
                break;
            }
        }
        return font;
    }
}
