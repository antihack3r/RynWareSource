// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.clickgui;

import net.minecraft.util.math.MathHelper;

public final class ScreenHelper
{
    private double x;
    private double y;
    
    public ScreenHelper(final float x, final float y) {
        this.x = x;
        this.y = y;
    }
    
    public final void interpolate(final double targetX, final double targetY, final double smoothing) {
        this.x = animate(targetX, this.x, smoothing);
        this.y = animate(targetY, this.y, smoothing);
    }
    
    public void animate(final double newX, final double newY) {
        this.x = animate(this.x, newX, 1.0);
        this.y = animate(this.y, newY, 1.0);
    }
    
    public static double animate(final double target, final double current, final double speed) {
        return MathHelper.lerp(current, target, speed);
    }
    
    public double getX() {
        return this.x;
    }
    
    public void setX(final double x) {
        this.x = x;
    }
    
    public double getY() {
        return this.y;
    }
    
    public void setY(final double y) {
        this.y = y;
    }
}
