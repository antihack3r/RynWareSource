// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.utils.other;

import org.rynware.client.utils.render.RenderUtils;
import java.awt.Color;

public class Particles
{
    public double x;
    public double y;
    public double deltaX;
    public double deltaY;
    public float size;
    public double opacity;
    public String text;
    public Color color;
    
    public void render2D() {
        RenderUtils.drawBlurredShadow((float)this.x, (float)this.y, 5.0f, 5.0f, 6, new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), (int)this.opacity));
    }
    
    public void updatePosition() {
        this.x += this.deltaX * 2.0;
        this.y += this.deltaY * 2.0;
        this.deltaY *= 0.95;
        this.deltaX *= 0.95;
        this.opacity -= 2.0;
        if (this.opacity < 1.0) {
            this.opacity = 1.0;
        }
    }
    
    public void init(final double x, final double y, final double deltaX, final double deltaY, final float size, final Color color) {
        this.x = x;
        this.y = y;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.size = size;
        this.opacity = 254.0;
        this.color = color;
    }
}
