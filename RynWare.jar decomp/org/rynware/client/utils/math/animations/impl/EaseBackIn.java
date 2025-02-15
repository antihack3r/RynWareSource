// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.utils.math.animations.impl;

import org.rynware.client.utils.math.animations.Direction;
import org.rynware.client.utils.math.animations.Animation;

public class EaseBackIn extends Animation
{
    private final float easeAmount;
    
    public EaseBackIn(final int ms, final double endPoint, final float easeAmount) {
        super(ms, endPoint);
        this.easeAmount = easeAmount;
    }
    
    public EaseBackIn(final int ms, final double endPoint, final float easeAmount, final Direction direction) {
        super(ms, endPoint, direction);
        this.easeAmount = easeAmount;
    }
    
    @Override
    protected boolean correctOutput() {
        return true;
    }
    
    @Override
    protected double getEquation(final double x) {
        final double x2 = x / this.duration;
        final float shrink = this.easeAmount + 1.0f;
        return Math.max(0.0, 1.0 + shrink * Math.pow(x2 - 1.0, 3.0) + this.easeAmount * Math.pow(x2 - 1.0, 2.0));
    }
}
