// 
// Decompiled by Procyon v0.6.0
// 

package com.jhlabs.composite;

import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.AlphaComposite;
import java.awt.Composite;

public final class MiscComposite implements Composite
{
    public static final int BLEND = 0;
    public static final int ADD = 1;
    public static final int SUBTRACT = 2;
    public static final int DIFFERENCE = 3;
    public static final int MULTIPLY = 4;
    public static final int DARKEN = 5;
    public static final int BURN = 6;
    public static final int COLOR_BURN = 7;
    public static final int SCREEN = 8;
    public static final int LIGHTEN = 9;
    public static final int DODGE = 10;
    public static final int COLOR_DODGE = 11;
    public static final int HUE = 12;
    public static final int SATURATION = 13;
    public static final int VALUE = 14;
    public static final int COLOR = 15;
    public static final int OVERLAY = 16;
    public static final int SOFT_LIGHT = 17;
    public static final int HARD_LIGHT = 18;
    public static final int PIN_LIGHT = 19;
    public static final int EXCLUSION = 20;
    public static final int NEGATION = 21;
    public static final int AVERAGE = 22;
    public static final int STENCIL = 23;
    public static final int SILHOUETTE = 24;
    private static final int MIN_RULE = 0;
    private static final int MAX_RULE = 24;
    public static String[] RULE_NAMES;
    protected float extraAlpha;
    protected int rule;
    
    private MiscComposite(final int rule) {
        this(rule, 1.0f);
    }
    
    private MiscComposite(final int rule, final float alpha) {
        if (alpha < 0.0f || alpha > 1.0f) {
            throw new IllegalArgumentException("alpha value out of range");
        }
        if (rule < 0 || rule > 24) {
            throw new IllegalArgumentException("unknown composite rule");
        }
        this.rule = rule;
        this.extraAlpha = alpha;
    }
    
    public static Composite getInstance(final int rule, final float alpha) {
        switch (rule) {
            case 0: {
                return AlphaComposite.getInstance(3, alpha);
            }
            case 1: {
                return new AddComposite(alpha);
            }
            case 2: {
                return new SubtractComposite(alpha);
            }
            case 3: {
                return new DifferenceComposite(alpha);
            }
            case 4: {
                return new MultiplyComposite(alpha);
            }
            case 5: {
                return new DarkenComposite(alpha);
            }
            case 6: {
                return new BurnComposite(alpha);
            }
            case 7: {
                return new ColorBurnComposite(alpha);
            }
            case 8: {
                return new ScreenComposite(alpha);
            }
            case 9: {
                return new LightenComposite(alpha);
            }
            case 10: {
                return new DodgeComposite(alpha);
            }
            case 11: {
                return new ColorDodgeComposite(alpha);
            }
            case 12: {
                return new HueComposite(alpha);
            }
            case 13: {
                return new SaturationComposite(alpha);
            }
            case 14: {
                return new ValueComposite(alpha);
            }
            case 15: {
                return new ColorComposite(alpha);
            }
            case 16: {
                return new OverlayComposite(alpha);
            }
            case 17: {
                return new SoftLightComposite(alpha);
            }
            case 18: {
                return new HardLightComposite(alpha);
            }
            case 19: {
                return new PinLightComposite(alpha);
            }
            case 20: {
                return new ExclusionComposite(alpha);
            }
            case 21: {
                return new NegationComposite(alpha);
            }
            case 22: {
                return new AverageComposite(alpha);
            }
            case 23: {
                return AlphaComposite.getInstance(6, alpha);
            }
            case 24: {
                return AlphaComposite.getInstance(8, alpha);
            }
            default: {
                return new MiscComposite(rule, alpha);
            }
        }
    }
    
    @Override
    public CompositeContext createContext(final ColorModel srcColorModel, final ColorModel dstColorModel, final RenderingHints hints) {
        return new MiscCompositeContext(this.rule, this.extraAlpha, srcColorModel, dstColorModel);
    }
    
    public float getAlpha() {
        return this.extraAlpha;
    }
    
    public int getRule() {
        return this.rule;
    }
    
    @Override
    public int hashCode() {
        return Float.floatToIntBits(this.extraAlpha) * 31 + this.rule;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof MiscComposite)) {
            return false;
        }
        final MiscComposite c = (MiscComposite)o;
        return this.rule == c.rule && this.extraAlpha == c.extraAlpha;
    }
    
    static {
        MiscComposite.RULE_NAMES = new String[] { "Normal", "Add", "Subtract", "Difference", "Multiply", "Darken", "Burn", "Color Burn", "Screen", "Lighten", "Dodge", "Color Dodge", "Hue", "Saturation", "Brightness", "Color", "Overlay", "Soft Light", "Hard Light", "Pin Light", "Exclusion", "Negation", "Average", "Stencil", "Silhouette" };
    }
}
