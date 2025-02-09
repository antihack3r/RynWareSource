// 
// Decompiled by Procyon v0.6.0
// 

package com.jhlabs.image;

import java.awt.image.ColorModel;
import java.awt.image.BufferedImage;

public class HalftoneFilter extends AbstractBufferedImageOp
{
    private float softness;
    private boolean invert;
    private boolean monochrome;
    private BufferedImage mask;
    
    public HalftoneFilter() {
        this.softness = 0.1f;
    }
    
    public void setSoftness(final float softness) {
        this.softness = softness;
    }
    
    public float getSoftness() {
        return this.softness;
    }
    
    public void setMask(final BufferedImage mask) {
        this.mask = mask;
    }
    
    public BufferedImage getMask() {
        return this.mask;
    }
    
    public void setInvert(final boolean invert) {
        this.invert = invert;
    }
    
    public boolean getInvert() {
        return this.invert;
    }
    
    public void setMonochrome(final boolean monochrome) {
        this.monochrome = monochrome;
    }
    
    public boolean getMonochrome() {
        return this.monochrome;
    }
    
    @Override
    public BufferedImage filter(final BufferedImage src, BufferedImage dst) {
        final int width = src.getWidth();
        final int height = src.getHeight();
        if (dst == null) {
            dst = this.createCompatibleDestImage(src, null);
        }
        if (this.mask == null) {
            return dst;
        }
        final int maskWidth = this.mask.getWidth();
        final int maskHeight = this.mask.getHeight();
        final float s = 255.0f * this.softness;
        final int[] inPixels = new int[width];
        final int[] maskPixels = new int[maskWidth];
        for (int y = 0; y < height; ++y) {
            this.getRGB(src, 0, y, width, 1, inPixels);
            this.getRGB(this.mask, 0, y % maskHeight, maskWidth, 1, maskPixels);
            for (int x = 0; x < width; ++x) {
                int maskRGB = maskPixels[x % maskWidth];
                final int inRGB = inPixels[x];
                if (this.invert) {
                    maskRGB ^= 0xFFFFFF;
                }
                if (this.monochrome) {
                    final int v = PixelUtils.brightness(maskRGB);
                    final int iv = PixelUtils.brightness(inRGB);
                    final float f = 1.0f - ImageMath.smoothStep(iv - s, iv + s, (float)v);
                    final int a = (int)(255.0f * f);
                    inPixels[x] = ((inRGB & 0xFF000000) | a << 16 | a << 8 | a);
                }
                else {
                    final int ir = inRGB >> 16 & 0xFF;
                    final int ig = inRGB >> 8 & 0xFF;
                    final int ib = inRGB & 0xFF;
                    final int mr = maskRGB >> 16 & 0xFF;
                    final int mg = maskRGB >> 8 & 0xFF;
                    final int mb = maskRGB & 0xFF;
                    final int r = (int)(255.0f * (1.0f - ImageMath.smoothStep(ir - s, ir + s, (float)mr)));
                    final int g = (int)(255.0f * (1.0f - ImageMath.smoothStep(ig - s, ig + s, (float)mg)));
                    final int b = (int)(255.0f * (1.0f - ImageMath.smoothStep(ib - s, ib + s, (float)mb)));
                    inPixels[x] = ((inRGB & 0xFF000000) | r << 16 | g << 8 | b);
                }
            }
            this.setRGB(dst, 0, y, width, 1, inPixels);
        }
        return dst;
    }
    
    @Override
    public String toString() {
        return "Stylize/Halftone...";
    }
}
