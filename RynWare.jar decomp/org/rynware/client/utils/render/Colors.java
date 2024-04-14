// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.utils.render;

import java.awt.Color;

public class Colors
{
    private Color outputColor;
    
    public Colors(final int rgb) {
        this.outputColor = new Color(rgb);
    }
    
    public Colors(final Color color) {
        this.outputColor = new Color(color.getRGB());
    }
    
    public Colors(final String hex) {
        this.outputColor = Color.decode(hex);
    }
    
    public Colors setAlpha(final int alpha) {
        final int r = this.outputColor.getRed();
        final int g = this.outputColor.getGreen();
        final int b = this.outputColor.getBlue();
        this.outputColor = new Color(r, g, b, alpha);
        return this;
    }
    
    public Colors setRed(final int red) {
        final int g = this.outputColor.getGreen();
        final int b = this.outputColor.getBlue();
        final int a = this.outputColor.getAlpha();
        this.outputColor = new Color(red, g, b, a);
        return this;
    }
    
    public Colors setGreen(final int green) {
        final int r = this.outputColor.getRed();
        final int b = this.outputColor.getBlue();
        final int a = this.outputColor.getAlpha();
        this.outputColor = new Color(r, green, b, a);
        return this;
    }
    
    public Colors setBlue(final int blue) {
        final int r = this.outputColor.getRed();
        final int g = this.outputColor.getGreen();
        final int a = this.outputColor.getAlpha();
        this.outputColor = new Color(r, g, blue, a);
        return this;
    }
    
    public Color getColor() {
        return this.outputColor;
    }
}
