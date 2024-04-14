// 
// Decompiled by Procyon v0.6.0
// 

package com.jhlabs.image;

import java.awt.image.BufferedImage;

public class TritoneFilter extends PointFilter
{
    private int shadowColor;
    private int midColor;
    private int highColor;
    private int[] lut;
    
    public TritoneFilter() {
        this.shadowColor = -16777216;
        this.midColor = -7829368;
        this.highColor = -1;
    }
    
    @Override
    public BufferedImage filter(final BufferedImage src, BufferedImage dst) {
        this.lut = new int[256];
        for (int i = 0; i < 128; ++i) {
            final float t = i / 127.0f;
            this.lut[i] = ImageMath.mixColors(t, this.shadowColor, this.midColor);
        }
        for (int i = 128; i < 256; ++i) {
            final float t = (i - 127) / 128.0f;
            this.lut[i] = ImageMath.mixColors(t, this.midColor, this.highColor);
        }
        dst = super.filter(src, dst);
        this.lut = null;
        return dst;
    }
    
    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        return this.lut[PixelUtils.brightness(rgb)];
    }
    
    public void setShadowColor(final int shadowColor) {
        this.shadowColor = shadowColor;
    }
    
    public int getShadowColor() {
        return this.shadowColor;
    }
    
    public void setMidColor(final int midColor) {
        this.midColor = midColor;
    }
    
    public int getMidColor() {
        return this.midColor;
    }
    
    public void setHighColor(final int highColor) {
        this.highColor = highColor;
    }
    
    public int getHighColor() {
        return this.highColor;
    }
    
    @Override
    public String toString() {
        return "Colors/Tritone...";
    }
}
