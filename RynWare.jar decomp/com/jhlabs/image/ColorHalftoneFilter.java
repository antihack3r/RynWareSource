// 
// Decompiled by Procyon v0.6.0
// 

package com.jhlabs.image;

import java.awt.image.WritableRaster;
import java.awt.image.ColorModel;
import java.awt.image.BufferedImage;

public class ColorHalftoneFilter extends AbstractBufferedImageOp
{
    private float dotRadius;
    private float cyanScreenAngle;
    private float magentaScreenAngle;
    private float yellowScreenAngle;
    
    public ColorHalftoneFilter() {
        this.dotRadius = 2.0f;
        this.cyanScreenAngle = (float)Math.toRadians(108.0);
        this.magentaScreenAngle = (float)Math.toRadians(162.0);
        this.yellowScreenAngle = (float)Math.toRadians(90.0);
    }
    
    public void setdotRadius(final float dotRadius) {
        this.dotRadius = dotRadius;
    }
    
    public float getdotRadius() {
        return this.dotRadius;
    }
    
    public float getCyanScreenAngle() {
        return this.cyanScreenAngle;
    }
    
    public void setCyanScreenAngle(final float cyanScreenAngle) {
        this.cyanScreenAngle = cyanScreenAngle;
    }
    
    public float getMagentaScreenAngle() {
        return this.magentaScreenAngle;
    }
    
    public void setMagentaScreenAngle(final float magentaScreenAngle) {
        this.magentaScreenAngle = magentaScreenAngle;
    }
    
    public float getYellowScreenAngle() {
        return this.yellowScreenAngle;
    }
    
    public void setYellowScreenAngle(final float yellowScreenAngle) {
        this.yellowScreenAngle = yellowScreenAngle;
    }
    
    @Override
    public BufferedImage filter(final BufferedImage src, BufferedImage dst) {
        final int width = src.getWidth();
        final int height = src.getHeight();
        final int type = src.getType();
        final WritableRaster srcRaster = src.getRaster();
        if (dst == null) {
            dst = this.createCompatibleDestImage(src, null);
        }
        final float gridSize = 2.0f * this.dotRadius * 1.414f;
        final float[] angles = { this.cyanScreenAngle, this.magentaScreenAngle, this.yellowScreenAngle };
        final float[] mx = { 0.0f, -1.0f, 1.0f, 0.0f, 0.0f };
        final float[] my = { 0.0f, 0.0f, 0.0f, -1.0f, 1.0f };
        final float halfGridSize = gridSize / 2.0f;
        final int[] outPixels = new int[width];
        final int[] inPixels = this.getRGB(src, 0, 0, width, height, null);
        for (int y = 0; y < height; ++y) {
            for (int x = 0, ix = y * width; x < width; ++x, ++ix) {
                outPixels[x] = ((inPixels[ix] & 0xFF000000) | 0xFFFFFF);
            }
            for (int channel = 0; channel < 3; ++channel) {
                final int shift = 16 - 8 * channel;
                final int mask = 255 << shift;
                final float angle = angles[channel];
                final float sin = (float)Math.sin(angle);
                final float cos = (float)Math.cos(angle);
                for (int x2 = 0; x2 < width; ++x2) {
                    float tx = x2 * cos + y * sin;
                    float ty = -x2 * sin + y * cos;
                    tx = tx - ImageMath.mod(tx - halfGridSize, gridSize) + halfGridSize;
                    ty = ty - ImageMath.mod(ty - halfGridSize, gridSize) + halfGridSize;
                    float f = 1.0f;
                    for (int i = 0; i < 5; ++i) {
                        final float ttx = tx + mx[i] * gridSize;
                        final float tty = ty + my[i] * gridSize;
                        final float ntx = ttx * cos - tty * sin;
                        final float nty = ttx * sin + tty * cos;
                        final int nx = ImageMath.clamp((int)ntx, 0, width - 1);
                        final int ny = ImageMath.clamp((int)nty, 0, height - 1);
                        final int argb = inPixels[ny * width + nx];
                        final int nr = argb >> shift & 0xFF;
                        float l = nr / 255.0f;
                        l = 1.0f - l * l;
                        l *= (float)(halfGridSize * 1.414);
                        final float dx = x2 - ntx;
                        final float dy = y - nty;
                        final float dx2 = dx * dx;
                        final float dy2 = dy * dy;
                        final float R = (float)Math.sqrt(dx2 + dy2);
                        final float f2 = 1.0f - ImageMath.smoothStep(R, R + 1.0f, l);
                        f = Math.min(f, f2);
                    }
                    int v = (int)(255.0f * f);
                    v <<= shift;
                    v ^= ~mask;
                    v |= 0xFF000000;
                    final int[] array = outPixels;
                    final int n = x2;
                    array[n] &= v;
                }
            }
            this.setRGB(dst, 0, y, width, 1, outPixels);
        }
        return dst;
    }
    
    @Override
    public String toString() {
        return "Pixellate/Color Halftone...";
    }
}
