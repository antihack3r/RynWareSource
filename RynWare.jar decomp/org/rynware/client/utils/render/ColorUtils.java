// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.utils.render;

import net.minecraft.util.math.MathHelper;
import java.text.NumberFormat;
import net.minecraft.entity.EntityLivingBase;
import org.rynware.client.feature.impl.hud.ClickGUI;
import java.awt.Color;
import org.rynware.client.utils.Helper;

public class ColorUtils implements Helper
{
    public static Color astolfo(final boolean clickgui, final int yOffset) {
        final float speed = clickgui ? (ClickGUI.speed.getNumberValue() * 100.0f) : 1000.0f;
        float hue = (float)(System.currentTimeMillis() % (int)speed + yOffset);
        if (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5f) {
            hue = 0.5f - (hue - 0.5f);
        }
        hue += 0.5f;
        return Color.getHSBColor(hue, 0.4f, 1.0f);
    }
    
    public static Color astolfoColors(final float speed, final int yOffset) {
        float hue;
        for (hue = (float)(System.currentTimeMillis() % (int)speed + yOffset); hue > speed; hue -= speed) {}
        if ((hue /= speed) > 0.5) {
            hue = 0.5f - (hue - 0.5f);
        }
        return Color.getHSBColor(hue += 0.5f, 0.4f, 1.0f);
    }
    
    public static int getHealthColor(final float health, final float maxHealth) {
        return Color.HSBtoRGB(Math.max(0.0f, Math.min(health, maxHealth) / maxHealth) / 3.0f, 1.0f, 0.8f) | 0xFF000000;
    }
    
    public static Color interpolateColorC(final Color color1, final Color color2, float amount) {
        amount = Math.min(1.0f, Math.max(0.0f, amount));
        return new Color(interpolateInt(color1.getRed(), color2.getRed(), amount), interpolateInt(color1.getGreen(), color2.getGreen(), amount), interpolateInt(color1.getBlue(), color2.getBlue(), amount), interpolateInt(color1.getAlpha(), color2.getAlpha(), amount));
    }
    
    public static int interpolateInt(final int oldValue, final int newValue, final double interpolationValue) {
        return interpolate(oldValue, newValue, (float)interpolationValue).intValue();
    }
    
    public static Double interpolate(final double oldValue, final double newValue, final double interpolationValue) {
        return oldValue + (newValue - oldValue) * interpolationValue;
    }
    
    public static Color getHealthColor(final EntityLivingBase entityLivingBase) {
        final float health = entityLivingBase.getHealth();
        final float[] fractions = { 0.0f, 0.15f, 0.55f, 0.7f, 0.9f };
        final Color[] colors = { new Color(133, 0, 0), Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN };
        final float progress = health / entityLivingBase.getMaxHealth();
        return (health >= 0.0f) ? blendColors(fractions, colors, progress).brighter() : colors[0];
    }
    
    public static Color blendColors(final float[] fractions, final Color[] colors, final float progress) {
        if (fractions == null) {
            throw new IllegalArgumentException("Fractions can't be null");
        }
        if (colors == null) {
            throw new IllegalArgumentException("Colours can't be null");
        }
        if (fractions.length != colors.length) {
            throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
        }
        final int[] indicies = getFractionIndicies(fractions, progress);
        final float[] range = { fractions[indicies[0]], fractions[indicies[1]] };
        final Color[] colorRange = { colors[indicies[0]], colors[indicies[1]] };
        final float max = range[1] - range[0];
        final float value = progress - range[0];
        final float weight = value / max;
        return blend(colorRange[0], colorRange[1], 1.0f - weight);
    }
    
    public static int[] getFractionIndicies(final float[] fractions, final float progress) {
        final int[] range = new int[2];
        int startPoint;
        for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {}
        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }
        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }
    
    public static Color blend(final Color color1, final Color color2, final double ratio) {
        final float r = (float)ratio;
        final float ir = 1.0f - r;
        final float[] rgb1 = new float[3];
        final float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        float red = rgb1[0] * r + rgb2[0] * ir;
        float green = rgb1[1] * r + rgb2[1] * ir;
        float blue = rgb1[2] * r + rgb2[2] * ir;
        if (red < 0.0f) {
            red = 0.0f;
        }
        else if (red > 255.0f) {
            red = 255.0f;
        }
        if (green < 0.0f) {
            green = 0.0f;
        }
        else if (green > 255.0f) {
            green = 255.0f;
        }
        if (blue < 0.0f) {
            blue = 0.0f;
        }
        else if (blue > 255.0f) {
            blue = 255.0f;
        }
        Color color3 = null;
        try {
            color3 = new Color(red, green, blue);
        }
        catch (final IllegalArgumentException exp) {
            NumberFormat.getNumberInstance();
        }
        return color3;
    }
    
    public static Color TwoColoreffect(final Color cl1, final Color cl2, final double speed) {
        final double thing = speed / 4.0 % 1.0;
        final float val = MathHelper.clamp((float)Math.sin(18.84955592153876 * thing) / 2.0f + 0.5f, 0.0f, 1.0f);
        return new Color(lerp(cl1.getRed() / 255.0f, cl2.getRed() / 255.0f, val), lerp(cl1.getGreen() / 255.0f, cl2.getGreen() / 255.0f, val), lerp(cl1.getBlue() / 255.0f, cl2.getBlue() / 255.0f, val));
    }
    
    public static float lerp(final float a, final float b, final float f) {
        return a + f * (b - a);
    }
    
    public static int fadeColor(final int startColor, final int endColor, float progress) {
        if (progress > 1.0f) {
            progress = 1.0f - progress % 1.0f;
        }
        return fade(startColor, endColor, progress);
    }
    
    public static Color rainbowCol(final float yDist, final float yTotal, final float saturation, final float speedt) {
        float speed;
        float hue;
        for (speed = 1800.0f, hue = System.currentTimeMillis() % (int)speed + (yTotal - yDist) * speedt; hue > speed; hue -= speed) {}
        hue /= speed;
        if (hue > 5.0f) {
            hue = 5.0f - (hue - 5.0f);
        }
        hue += 5.0f;
        return Color.getHSBColor(hue, saturation, 1.0f);
    }
    
    public static int fade(final int startColor, final int endColor, final float progress) {
        final float invert = 1.0f - progress;
        final int r = (int)((startColor >> 16 & 0xFF) * invert + (endColor >> 16 & 0xFF) * progress);
        final int g = (int)((startColor >> 8 & 0xFF) * invert + (endColor >> 8 & 0xFF) * progress);
        final int b = (int)((startColor & 0xFF) * invert + (endColor & 0xFF) * progress);
        final int a = (int)((startColor >> 24 & 0xFF) * invert + (endColor >> 24 & 0xFF) * progress);
        return (a & 0xFF) << 24 | (r & 0xFF) << 16 | (g & 0xFF) << 8 | (b & 0xFF);
    }
    
    public static Color astolfo(final float yDist, final float yTotal) {
        float speed;
        float hue;
        for (speed = 3500.0f, hue = System.currentTimeMillis() % (int)speed + (yTotal - yDist) * 12.0f; hue > speed; hue -= speed) {}
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5f - (hue - 0.5f);
        }
        hue += 0.5f;
        return new Color(hue, 0.4f, 1.0f);
    }
    
    public static Color astolfo(final float yDist, final float yTotal, final float saturation, final float speedt) {
        float speed;
        float hue;
        for (speed = 1800.0f, hue = System.currentTimeMillis() % (int)speed + (yTotal - yDist) * speedt; hue > speed; hue -= speed) {}
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5f - (hue - 0.5f);
        }
        hue += 0.5f;
        return Color.getHSBColor(hue, saturation, 1.0f);
    }
    
    public static int getColor(final int red, final int green, final int blue) {
        return getColor(red, green, blue, 255);
    }
    
    public static int getColor(final int red, final int green, final int blue, final int alpha) {
        int color = 0;
        color |= alpha << 24;
        color |= red << 16;
        color |= green << 8;
        return color |= blue;
    }
    
    public static int getColor(final Color color) {
        return getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
    
    public static int getColor(final int bright) {
        return getColor(bright, bright, bright, 255);
    }
    
    public static int getColor(final int brightness, final int alpha) {
        return getColor(brightness, brightness, brightness, alpha);
    }
    
    public static Color rainbow(final int delay, final float saturation, final float brightness) {
        double rainbow = Math.ceil((double)((System.currentTimeMillis() + delay) / 16L));
        rainbow %= 360.0;
        return Color.getHSBColor((float)(rainbow / 360.0), saturation, brightness);
    }
}
