// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.utils.math.animations.impl;

import org.rynware.client.utils.math.animations.Direction;
import org.rynware.client.utils.math.animations.Animation;

public class EaseInOutQuad extends Animation
{
    public EaseInOutQuad(final int ms, final double endPoint) {
        super(ms, endPoint);
    }
    
    public EaseInOutQuad(final int ms, final double endPoint, final Direction direction) {
        super(ms, endPoint, direction);
    }
    
    @Override
    protected double getEquation(final double x1) {
        final double x2 = x1 / this.duration;
        return (x2 < 0.5) ? (2.0 * Math.pow(x2, 2.0)) : (1.0 - Math.pow(-2.0 * x2 + 2.0, 2.0) / 2.0);
    }
}