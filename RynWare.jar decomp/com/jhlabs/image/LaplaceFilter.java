// 
// Decompiled by Procyon v0.6.0
// 

package com.jhlabs.image;

import java.awt.image.ColorModel;
import java.awt.image.BufferedImage;

public class LaplaceFilter extends AbstractBufferedImageOp
{
    private void brightness(final int[] row) {
        for (int i = 0; i < row.length; ++i) {
            final int rgb = row[i];
            final int r = rgb >> 16 & 0xFF;
            final int g = rgb >> 8 & 0xFF;
            final int b = rgb & 0xFF;
            row[i] = (r + g + b) / 3;
        }
    }
    
    @Override
    public BufferedImage filter(final BufferedImage src, BufferedImage dst) {
        final int width = src.getWidth();
        final int height = src.getHeight();
        if (dst == null) {
            dst = this.createCompatibleDestImage(src, null);
        }
        int[] row1 = null;
        int[] row2 = null;
        int[] row3 = null;
        final int[] pixels = new int[width];
        row1 = this.getRGB(src, 0, 0, width, 1, row1);
        row2 = this.getRGB(src, 0, 0, width, 1, row2);
        this.brightness(row1);
        this.brightness(row2);
        for (int y = 0; y < height; ++y) {
            if (y < height - 1) {
                row3 = this.getRGB(src, 0, y + 1, width, 1, row3);
                this.brightness(row3);
            }
            pixels[0] = (pixels[width - 1] = -16777216);
            for (int x = 1; x < width - 1; ++x) {
                final int l1 = row2[x - 1];
                final int l2 = row1[x];
                final int l3 = row3[x];
                final int l4 = row2[x + 1];
                final int i = row2[x];
                final int max = Math.max(Math.max(l1, l2), Math.max(l3, l4));
                final int min = Math.min(Math.min(l1, l2), Math.min(l3, l4));
                final int gradient = (int)(0.5f * Math.max(max - i, i - min));
                final int r = (row1[x - 1] + row1[x] + row1[x + 1] + row2[x - 1] - 8 * row2[x] + row2[x + 1] + row3[x - 1] + row3[x] + row3[x + 1] > 0) ? gradient : (128 + gradient);
                pixels[x] = r;
            }
            this.setRGB(dst, 0, y, width, 1, pixels);
            final int[] t = row1;
            row1 = row2;
            row2 = row3;
            row3 = t;
        }
        row1 = this.getRGB(dst, 0, 0, width, 1, row1);
        row2 = this.getRGB(dst, 0, 0, width, 1, row2);
        for (int y = 0; y < height; ++y) {
            if (y < height - 1) {
                row3 = this.getRGB(dst, 0, y + 1, width, 1, row3);
            }
            pixels[0] = (pixels[width - 1] = -16777216);
            for (int x = 1; x < width - 1; ++x) {
                int r2 = row2[x];
                r2 = ((r2 <= 128 && (row1[x - 1] > 128 || row1[x] > 128 || row1[x + 1] > 128 || row2[x - 1] > 128 || row2[x + 1] > 128 || row3[x - 1] > 128 || row3[x] > 128 || row3[x + 1] > 128)) ? ((r2 >= 128) ? (r2 - 128) : r2) : 0);
                pixels[x] = (0xFF000000 | r2 << 16 | r2 << 8 | r2);
            }
            this.setRGB(dst, 0, y, width, 1, pixels);
            final int[] t = row1;
            row1 = row2;
            row2 = row3;
            row3 = t;
        }
        return dst;
    }
    
    @Override
    public String toString() {
        return "Edges/Laplace...";
    }
}
