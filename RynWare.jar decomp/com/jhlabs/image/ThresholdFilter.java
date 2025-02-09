// 
// Decompiled by Procyon v0.6.0
// 

package com.jhlabs.image;

public class ThresholdFilter extends PointFilter
{
    private int lowerThreshold;
    private int upperThreshold;
    private int white;
    private int black;
    
    public ThresholdFilter() {
        this(127);
    }
    
    public ThresholdFilter(final int t) {
        this.white = 16777215;
        this.black = 0;
        this.setLowerThreshold(t);
        this.setUpperThreshold(t);
    }
    
    public void setLowerThreshold(final int lowerThreshold) {
        this.lowerThreshold = lowerThreshold;
    }
    
    public int getLowerThreshold() {
        return this.lowerThreshold;
    }
    
    public void setUpperThreshold(final int upperThreshold) {
        this.upperThreshold = upperThreshold;
    }
    
    public int getUpperThreshold() {
        return this.upperThreshold;
    }
    
    public void setWhite(final int white) {
        this.white = white;
    }
    
    public int getWhite() {
        return this.white;
    }
    
    public void setBlack(final int black) {
        this.black = black;
    }
    
    public int getBlack() {
        return this.black;
    }
    
    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        final int v = PixelUtils.brightness(rgb);
        final float f = ImageMath.smoothStep((float)this.lowerThreshold, (float)this.upperThreshold, (float)v);
        return (rgb & 0xFF000000) | (ImageMath.mixColors(f, this.black, this.white) & 0xFFFFFF);
    }
    
    @Override
    public String toString() {
        return "Stylize/Threshold...";
    }
}
