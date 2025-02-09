// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.utils.math;

import net.minecraft.util.math.MathHelper;
import net.minecraft.client.Minecraft;
import org.rynware.client.utils.Helper;

public class AnimationHelper implements Helper
{
    public static double delta;
    
    public static float animation(final float animation, final float target, final float speedTarget) {
        float dif = (target - animation) / Math.max((float)Minecraft.getDebugFPS(), 5.0f) * 15.0f;
        if (dif > 0.0f) {
            dif = Math.max(speedTarget, dif);
            dif = Math.min(target - animation, dif);
        }
        else if (dif < 0.0f) {
            dif = Math.min(-speedTarget, dif);
            dif = Math.max(target - animation, dif);
        }
        return animation + dif;
    }
    
    public static double animation(final double animation, final double target, final double speedTarget) {
        double dif = (target - animation) / Math.max(Minecraft.getDebugFPS(), 5) * speedTarget;
        if (dif > 0.0) {
            dif = Math.max(speedTarget, dif);
            dif = Math.min(target - animation, dif);
        }
        else if (dif < 0.0) {
            dif = Math.min(-speedTarget, dif);
            dif = Math.max(target - animation, dif);
        }
        return animation + dif;
    }
    
    public static float calculateCompensation(final float target, float current, float delta, final double speed) {
        final float diff = current - target;
        if (delta < 1.0f) {
            delta = 1.0f;
        }
        if (delta > 1000.0f) {
            delta = 16.0f;
        }
        final double dif = Math.max(speed * delta / 16.66666603088379, 0.5);
        if (diff > speed) {
            if ((current -= (float)dif) < target) {
                current = target;
            }
        }
        else if (diff < -speed) {
            if ((current += (float)dif) > target) {
                current = target;
            }
        }
        else {
            current = target;
        }
        return current;
    }
    
    public static float Move(final float from, final float to, final float minstep, final float maxstep, final float factor) {
        float f = (to - from) * MathHelper.clamp(factor, 0.0f, 1.0f);
        if (f < 0.0f) {
            f = MathHelper.clamp(f, -maxstep, -minstep);
        }
        else {
            f = MathHelper.clamp(f, minstep, maxstep);
        }
        if (Math.abs(f) > Math.abs(to - from)) {
            return to;
        }
        return from + f;
    }
    
    public static double Interpolate(final double current, final double old, final double scale) {
        return old + (current - old) * scale;
    }
    
    public static float getAnimationState(float animation, final float finalState, final float speed) {
        final float add = (float)(AnimationHelper.delta * (speed / 1000.0f));
        if (animation < finalState) {
            if (animation + add < finalState) {
                animation += add;
            }
            else {
                animation = finalState;
            }
        }
        else if (animation - add > finalState) {
            animation -= add;
        }
        else {
            animation = finalState;
        }
        return animation;
    }
}
