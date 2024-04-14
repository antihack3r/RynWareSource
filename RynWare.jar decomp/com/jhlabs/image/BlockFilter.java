// 
// Decompiled by Procyon v0.6.0
// 

package com.jhlabs.image;

import java.awt.image.WritableRaster;
import java.awt.image.ColorModel;
import java.awt.image.BufferedImage;

public class BlockFilter extends AbstractBufferedImageOp
{
    private int blockSize;
    
    public BlockFilter() {
        this.blockSize = 2;
    }
    
    public BlockFilter(final int blockSize) {
        this.blockSize = 2;
        this.blockSize = blockSize;
    }
    
    public void setBlockSize(final int blockSize) {
        this.blockSize = blockSize;
    }
    
    public int getBlockSize() {
        return this.blockSize;
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
        final int[] pixels = new int[this.blockSize * this.blockSize];
        for (int y = 0; y < height; y += this.blockSize) {
            for (int x = 0; x < width; x += this.blockSize) {
                final int w = Math.min(this.blockSize, width - x);
                final int h = Math.min(this.blockSize, height - y);
                final int t = w * h;
                this.getRGB(src, x, y, w, h, pixels);
                int r = 0;
                int g = 0;
                int b = 0;
                int i = 0;
                for (int by = 0; by < h; ++by) {
                    for (int bx = 0; bx < w; ++bx) {
                        final int argb = pixels[i];
                        r += (argb >> 16 & 0xFF);
                        g += (argb >> 8 & 0xFF);
                        b += (argb & 0xFF);
                        ++i;
                    }
                }
                final int argb = r / t << 16 | g / t << 8 | b / t;
                i = 0;
                for (int by = 0; by < h; ++by) {
                    for (int bx = 0; bx < w; ++bx) {
                        pixels[i] = ((pixels[i] & 0xFF000000) | argb);
                        ++i;
                    }
                }
                this.setRGB(dst, x, y, w, h, pixels);
            }
        }
        return dst;
    }
    
    @Override
    public String toString() {
        return "Pixellate/Mosaic...";
    }
}
