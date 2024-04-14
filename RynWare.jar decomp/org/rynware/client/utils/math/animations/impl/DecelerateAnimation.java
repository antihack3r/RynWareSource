// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.utils.math.animations.impl;

import org.rynware.client.utils.math.animations.Direction;
import org.rynware.client.utils.math.animations.Animation;

public class DecelerateAnimation extends Animation
{
    public DecelerateAnimation(final int ms, final double endPoint) {
        super(ms, endPoint);
    }
    
    public DecelerateAnimation(final int ms, final double endPoint, final Direction direction) {
        super(ms, endPoint, direction);
    }
    
    @Override
    protected double getEquation(final double x) {
        final double x2 = x / this.duration;
        return 1.0 - (x2 - 1.0) * (x2 - 1.0);
    }
}
