// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.utils.math;

import net.minecraft.util.math.MathHelper;
import java.security.SecureRandom;
import java.math.RoundingMode;
import java.math.BigDecimal;
import org.rynware.client.utils.Helper;

public class MathematicHelper implements Helper
{
    public static BigDecimal round(final float f, final int times) {
        BigDecimal bd = new BigDecimal(Float.toString(f));
        bd = bd.setScale(times, RoundingMode.HALF_UP);
        return bd;
    }
    
    public static int getRandomInRange(final int min, final int max) {
        return (int)(Math.random() * (max - min) + min);
    }
    
    public static float getRandomInRange(final float min, final float max) {
        final SecureRandom random = new SecureRandom();
        return random.nextFloat() * (max - min) + min;
    }
    
    public static double getRandomInRange(final double min, final double max) {
        final SecureRandom random = new SecureRandom();
        return random.nextDouble() * (max - min) + min;
    }
    
    public static double lerp(final double old, final double newVal, final double amount) {
        return (1.0 - amount) * old + amount * newVal;
    }
    
    public static Double interpolate(final double oldValue, final double newValue, final double interpolationValue) {
        return oldValue + (newValue - oldValue) * interpolationValue;
    }
    
    public static float interpolateFloat(final float oldValue, final float newValue, final double interpolationValue) {
        return interpolate(oldValue, newValue, (float)interpolationValue).floatValue();
    }
    
    public static int interpolateInt(final int oldValue, final int newValue, final double interpolationValue) {
        return interpolate(oldValue, newValue, (float)interpolationValue).intValue();
    }
    
    public static float calculateGaussianValue(final float x, final float sigma) {
        final double PI = 3.141592653;
        final double output = 1.0 / Math.sqrt(2.0 * PI * (sigma * sigma));
        return (float)(output * Math.exp(-(x * x) / (2.0 * (sigma * sigma))));
    }
    
    public static double roundToHalf(final double d) {
        return Math.round(d * 2.0) / 2.0;
    }
    
    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static float getRandomFloat(final float max, final float min) {
        final SecureRandom random = new SecureRandom();
        return random.nextFloat() * (max - min) + min;
    }
    
    public static int getMiddle(final int old, final int newValue) {
        return (old + newValue) / 2;
    }
    
    public static int getCenter(final int width, final int rectWidth) {
        return width / 2 - rectWidth / 2;
    }
    
    public static double round(final double num, final double increment) {
        final double v = Math.round(num / increment) * increment;
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static float checkAngle(final float one, final float two, final float three) {
        float f = MathHelper.wrapDegrees(one - two);
        if (f < -three) {
            f = -three;
        }
        if (f >= three) {
            f = three;
        }
        return one - f;
    }
    
    public static float clamp(float val, final float min, final float max) {
        if (val <= min) {
            val = min;
        }
        if (val >= max) {
            val = max;
        }
        return val;
    }
    
    public static float randomizeFloat(final float min, final float max) {
        return (float)(min + (max - min) * Math.random());
    }
}
