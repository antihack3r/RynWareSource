// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.utils.movement;

import org.rynware.client.event.events.impl.player.EventMove;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.potion.Potion;
import java.util.Objects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import org.rynware.client.utils.Helper;

public class MovementUtils implements Helper
{
    public static final double WALK_SPEED = 0.221;
    
    public static int getSpeedEffect() {
        if (MovementUtils.mc.player.isPotionActive(MobEffects.SPEED)) {
            return Objects.requireNonNull(MovementUtils.mc.player.getActivePotionEffect(MobEffects.SPEED)).getAmplifier() + 1;
        }
        return 0;
    }
    
    public static double getBaseSpeed() {
        double baseSpeed = 0.2873;
        if (MovementUtils.mc.player.isPotionActive(Potion.getPotionById(1))) {
            final int amplifier = MovementUtils.mc.player.getActivePotionEffect(Potion.getPotionById(1)).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    
    public static void setMotion(final double speed) {
        double forward = MovementUtils.mc.player.movementInput.moveForward;
        double strafe = MovementUtils.mc.player.movementInput.moveStrafe;
        float yaw = MovementUtils.mc.player.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            MovementUtils.mc.player.motionX = 0.0;
            MovementUtils.mc.player.motionZ = 0.0;
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            final double sin = MathHelper.sin((float)Math.toRadians(yaw + 90.0f));
            final double cos = MathHelper.cos((float)Math.toRadians(yaw + 90.0f));
            MovementUtils.mc.player.motionX = forward * speed * cos + strafe * speed * sin;
            MovementUtils.mc.player.motionZ = forward * speed * sin - strafe * speed * cos;
        }
    }
    
    public static void setSpeed(final double speed) {
        float f = MovementUtils.mc.player.movementInput.moveForward;
        float f2 = MovementUtils.mc.player.movementInput.moveStrafe;
        float f3 = MovementUtils.mc.player.rotationYaw;
        if (f == 0.0f && f2 == 0.0f) {
            MovementUtils.mc.player.motionX = 0.0;
            MovementUtils.mc.player.motionZ = 0.0;
        }
        else if (f != 0.0f) {
            if (f2 >= 1.0f) {
                f3 += ((f > 0.0f) ? -35 : 35);
                f2 = 0.0f;
            }
            else if (f2 <= -1.0f) {
                f3 += ((f > 0.0f) ? 35 : -35);
                f2 = 0.0f;
            }
            if (f > 0.0f) {
                f = 1.0f;
            }
            else if (f < 0.0f) {
                f = -1.0f;
            }
        }
        final double d0 = Math.cos(Math.toRadians(f3 + 90.0f));
        final double d2 = Math.sin(Math.toRadians(f3 + 90.0f));
        MovementUtils.mc.player.motionX = f * speed * d0 + f2 * speed * d2;
        MovementUtils.mc.player.motionZ = f * speed * d2 - f2 * speed * d0;
    }
    
    public static float getMoveDirection() {
        final double motionX = MovementUtils.mc.player.motionX;
        final double motionZ = MovementUtils.mc.player.motionZ;
        final float direction = (float)(Math.atan2(motionX, motionZ) / 3.141592653589793 * 180.0);
        return -direction;
    }
    
    public static boolean airBlockAboveHead() {
        final AxisAlignedBB bb = new AxisAlignedBB(MovementUtils.mc.player.posX - 0.3, MovementUtils.mc.player.posY + MovementUtils.mc.player.getEyeHeight(), MovementUtils.mc.player.posZ + 0.3, MovementUtils.mc.player.posX + 0.3, MovementUtils.mc.player.posY + (MovementUtils.mc.player.onGround ? 2.5 : 1.5), MovementUtils.mc.player.posZ - 0.3);
        return MovementUtils.mc.world.getCollisionBoxes(MovementUtils.mc.player, bb).isEmpty();
    }
    
    public static void setMotion(final EventMove e, final double speed, final float pseudoYaw, final double aa, final double po4) {
        double forward = po4;
        double strafe = aa;
        float yaw = pseudoYaw;
        if (po4 != 0.0) {
            if (aa > 0.0) {
                yaw = pseudoYaw + ((po4 > 0.0) ? -45 : 45);
            }
            else if (aa < 0.0) {
                yaw = pseudoYaw + ((po4 > 0.0) ? 45 : -45);
            }
            strafe = 0.0;
            if (po4 > 0.0) {
                forward = 1.0;
            }
            else if (po4 < 0.0) {
                forward = -1.0;
            }
        }
        if (strafe > 0.0) {
            strafe = 1.0;
        }
        else if (strafe < 0.0) {
            strafe = -1.0;
        }
        final double kak = Math.cos(Math.toRadians(yaw + 90.0f));
        final double nety = Math.sin(Math.toRadians(yaw + 90.0f));
        e.setX(forward * speed * kak + strafe * speed * nety);
        e.setZ(forward * speed * nety - strafe * speed * kak);
    }
    
    public static void setEventSpeed(final EventMove event, final double speed) {
        double forward = MovementUtils.mc.player.movementInput.moveForward;
        double strafe = MovementUtils.mc.player.movementInput.moveStrafe;
        float yaw = MovementUtils.mc.player.rotationYaw;
        if (forward == 0.0 && strafe == 0.0) {
            event.setX(0.0);
            event.setZ(0.0);
        }
        else {
            if (forward != 0.0) {
                if (strafe > 0.0) {
                    yaw += ((forward > 0.0) ? -45 : 45);
                }
                else if (strafe < 0.0) {
                    yaw += ((forward > 0.0) ? 45 : -45);
                }
                strafe = 0.0;
                if (forward > 0.0) {
                    forward = 1.0;
                }
                else if (forward < 0.0) {
                    forward = -1.0;
                }
            }
            event.setX(forward * speed * Math.cos(Math.toRadians(yaw + 90.0f)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0f)));
            event.setZ(forward * speed * Math.sin(Math.toRadians(yaw + 90.0f)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0f)));
        }
    }
    
    public static boolean isBlockAboveHead() {
        final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(MovementUtils.mc.player.posX - 0.3, MovementUtils.mc.player.posY + MovementUtils.mc.player.getEyeHeight(), MovementUtils.mc.player.posZ + 0.3, MovementUtils.mc.player.posX + 0.3, MovementUtils.mc.player.posY + (MovementUtils.mc.player.onGround ? 2.5 : 1.5), MovementUtils.mc.player.posZ - 0.3);
        return MovementUtils.mc.world.getCollisionBoxes(MovementUtils.mc.player, axisAlignedBB).isEmpty();
    }
    
    public static void strafe() {
        if (MovementUtils.mc.gameSettings.keyBindBack.isKeyDown()) {
            return;
        }
        strafe(getSpeed());
    }
    
    public static float getSpeed() {
        return (float)Math.sqrt(MovementUtils.mc.player.motionX * MovementUtils.mc.player.motionX + MovementUtils.mc.player.motionZ * MovementUtils.mc.player.motionZ);
    }
    
    public static float getAllDirection() {
        float rotationYaw = MovementUtils.mc.player.rotationYaw;
        float factor = 0.0f;
        if (MovementUtils.mc.player.movementInput.moveForward > 0.0f) {
            factor = 1.0f;
        }
        if (MovementUtils.mc.player.movementInput.moveForward < 0.0f) {
            factor = -1.0f;
        }
        if (factor == 0.0f) {
            if (MovementUtils.mc.player.movementInput.moveStrafe > 0.0f) {
                rotationYaw -= 90.0f;
            }
            if (MovementUtils.mc.player.movementInput.moveStrafe < 0.0f) {
                rotationYaw += 90.0f;
            }
        }
        else {
            if (MovementUtils.mc.player.movementInput.moveStrafe > 0.0f) {
                rotationYaw -= 45.0f * factor;
            }
            if (MovementUtils.mc.player.movementInput.moveStrafe < 0.0f) {
                rotationYaw += 45.0f * factor;
            }
        }
        if (factor < 0.0f) {
            rotationYaw -= 180.0f;
        }
        return (float)Math.toRadians(rotationYaw);
    }
    
    public static void strafe(final float speed) {
        if (!isMoving()) {
            return;
        }
        final double yaw = getAllDirection();
        MovementUtils.mc.player.motionX = -Math.sin(yaw) * speed;
        MovementUtils.mc.player.motionZ = Math.cos(yaw) * speed;
    }
    
    public static boolean isMoving() {
        return MovementUtils.mc.player.movementInput.moveStrafe != 0.0 || MovementUtils.mc.player.movementInput.moveForward != 0.0;
    }
}
