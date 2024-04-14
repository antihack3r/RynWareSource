// 
// Decompiled by Procyon v0.6.0
// 

package com.jhlabs.image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.util.Hashtable;
import java.awt.image.BufferedImage;

public class ScaleFilter extends AbstractBufferedImageOp
{
    private int width;
    private int height;
    
    public ScaleFilter() {
        this(32, 32);
    }
    
    public ScaleFilter(final int width, final int height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public BufferedImage filter(final BufferedImage src, BufferedImage dst) {
        if (dst == null) {
            final ColorModel dstCM = src.getColorModel();
            dst = new BufferedImage(dstCM, dstCM.createCompatibleWritableRaster(this.width, this.height), dstCM.isAlphaPremultiplied(), null);
        }
        final Image scaleImage = src.getScaledInstance(this.width, this.height, 16);
        final Graphics2D g = dst.createGraphics();
        g.drawImage(scaleImage, 0, 0, this.width, this.height, null);
        g.dispose();
        return dst;
    }
    
    @Override
    public String toString() {
        return "Distort/Scale";
    }
}
