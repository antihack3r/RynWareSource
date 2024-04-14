// 
// Decompiled by Procyon v0.6.0
// 

package com.jhlabs.image;

import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.awt.Image;
import java.awt.Composite;
import com.jhlabs.composite.SubtractComposite;
import java.awt.image.BufferedImage;

public class DoGFilter extends AbstractBufferedImageOp
{
    private float radius1;
    private float radius2;
    private boolean normalize;
    private boolean invert;
    
    public DoGFilter() {
        this.radius1 = 1.0f;
        this.radius2 = 2.0f;
        this.normalize = true;
    }
    
    public void setRadius1(final float radius1) {
        this.radius1 = radius1;
    }
    
    public float getRadius1() {
        return this.radius1;
    }
    
    public void setRadius2(final float radius2) {
        this.radius2 = radius2;
    }
    
    public float getRadius2() {
        return this.radius2;
    }
    
    public void setNormalize(final boolean normalize) {
        this.normalize = normalize;
    }
    
    public boolean getNormalize() {
        return this.normalize;
    }
    
    public void setInvert(final boolean invert) {
        this.invert = invert;
    }
    
    public boolean getInvert() {
        return this.invert;
    }
    
    @Override
    public BufferedImage filter(final BufferedImage src, final BufferedImage dst) {
        final int width = src.getWidth();
        final int height = src.getHeight();
        final BufferedImage image1 = new BoxBlurFilter(this.radius1, this.radius1, 3).filter(src, null);
        BufferedImage image2 = new BoxBlurFilter(this.radius2, this.radius2, 3).filter(src, null);
        final Graphics2D g2d = image2.createGraphics();
        g2d.setComposite(new SubtractComposite(1.0f));
        g2d.drawImage(image1, 0, 0, null);
        g2d.dispose();
        if (this.normalize && this.radius1 != this.radius2) {
            int[] pixels = null;
            int max = 0;
            for (int y = 0; y < height; ++y) {
                pixels = this.getRGB(image2, 0, y, width, 1, pixels);
                for (final int rgb : pixels) {
                    final int r = rgb >> 16 & 0xFF;
                    final int g = rgb >> 8 & 0xFF;
                    final int b = rgb & 0xFF;
                    if (r > max) {
                        max = r;
                    }
                    if (g > max) {
                        max = g;
                    }
                    if (b > max) {
                        max = b;
                    }
                }
            }
            for (int y = 0; y < height; ++y) {
                pixels = this.getRGB(image2, 0, y, width, 1, pixels);
                for (int x = 0; x < width; ++x) {
                    final int rgb = pixels[x];
                    int r = rgb >> 16 & 0xFF;
                    int g = rgb >> 8 & 0xFF;
                    int b = rgb & 0xFF;
                    r = r * 255 / max;
                    g = g * 255 / max;
                    b = b * 255 / max;
                    pixels[x] = ((rgb & 0xFF000000) | r << 16 | g << 8 | b);
                }
                this.setRGB(image2, 0, y, width, 1, pixels);
            }
        }
        if (this.invert) {
            image2 = new InvertFilter().filter(image2, image2);
        }
        return image2;
    }
    
    @Override
    public String toString() {
        return "Edges/Difference of Gaussians...";
    }
}
