// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.utils.math;

import org.rynware.client.feature.impl.combat.KillAura;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.Entity;
import org.rynware.client.utils.Helper;

public class RotationHelper implements Helper
{
    public static boolean isLookingAtEntity(final float yaw, final float pitch, final float xExp, final float yExp, final float zExp, final Entity entity, final double range) {
        final Vec3d src = RotationHelper.mc.player.getPositionEyes(1.0f);
        final Vec3d vectorForRotation = Entity.getVectorForRotation(pitch, yaw);
        final Vec3d dest = src.addVector(vectorForRotation.xCoord * range, vectorForRotation.yCoord * range, vectorForRotation.zCoord * range);
        final RayTraceResult rayTraceResult = RotationHelper.mc.world.rayTraceBlocks(src, dest, false, false, true);
        return rayTraceResult != null && entity.getEntityBoundingBox().expand(xExp, yExp, zExp).calculateIntercept(src, dest) != null;
    }
    
    public static double getDistanceOfEntityToBlock(final Entity entity, final BlockPos pos) {
        return getDistance(entity.posX, entity.posY, entity.posZ, pos.getX(), pos.getY(), pos.getZ());
    }
    
    public static double getDistance(final double x, final double y, final double z, final double x1, final double y1, final double z1) {
        final double posX = x - x1;
        final double posY = y - y1;
        final double posZ = z - z1;
        return MathHelper.sqrt(posX * posX + posY * posY + posZ * posZ);
    }
    
    public static Vec3d getEyesPos() {
        return new Vec3d(RotationHelper.mc.player.posX, RotationHelper.mc.player.getEntityBoundingBox().minY + RotationHelper.mc.player.getEyeHeight(), RotationHelper.mc.player.posZ);
    }
    
    public static float[] getRotationVector(final Vec3d vec) {
        final Vec3d eyesPos = getEyesPos();
        final double diffX = vec.xCoord - eyesPos.xCoord;
        final double diffY = vec.yCoord - (RotationHelper.mc.player.posY + RotationHelper.mc.player.getEyeHeight() + 0.5);
        final double diffZ = vec.zCoord - eyesPos.zCoord;
        final double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0) + MathematicHelper.randomizeFloat(-2.0f, 2.0f);
        float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ))) + MathematicHelper.randomizeFloat(-2.0f, 2.0f);
        yaw = RotationHelper.mc.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - RotationHelper.mc.player.rotationYaw));
        pitch = RotationHelper.mc.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - RotationHelper.mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90.0f, 90.0f);
        return new float[] { yaw, pitch };
    }
    
    public static boolean isLookingAtEntity(final boolean blockCheck, final float yaw, final float pitch, final float xExp, final float yExp, final float zExp, final Entity entity, final double range) {
        final Vec3d src = RotationHelper.mc.player.getPositionEyes(1.0f);
        final Vec3d vectorForRotation = Entity.getVectorForRotation(pitch, yaw);
        final Vec3d dest = src.addVector(vectorForRotation.xCoord * range, vectorForRotation.yCoord * range, vectorForRotation.zCoord * range);
        final RayTraceResult rayTraceResult = RotationHelper.mc.world.rayTraceBlocks(src, dest, false, false, true);
        return rayTraceResult != null && (!blockCheck || rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK) && entity.getEntityBoundingBox().expand(xExp, yExp, zExp).calculateIntercept(src, dest) != null;
    }
    
    public static double getDistance(final double x1, final double z1, final double x2, final double z2) {
        final double deltaX = x1 - x2;
        final double deltaZ = z1 - z2;
        return Math.hypot(deltaX, deltaZ);
    }
    
    public static float getAngle(final Entity entity) {
        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * Minecraft.getMinecraft().getRenderPartialTicks() - Minecraft.getMinecraft().getRenderManager().renderPosX;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * Minecraft.getMinecraft().getRenderPartialTicks() - Minecraft.getMinecraft().getRenderManager().renderPosZ;
        final float yaw = (float)(-Math.toDegrees(Math.atan2(x, z)));
        return (float)(yaw - AnimationHelper.Interpolate(Minecraft.getMinecraft().player.rotationYaw, Minecraft.getMinecraft().player.prevRotationYaw, 1.0));
    }
    
    public static float getAngle(final Vec3d vec3d) {
        final double x = vec3d.xCoord + (vec3d.xCoord - vec3d.xCoord) * Minecraft.getMinecraft().getRenderPartialTicks() - Minecraft.getMinecraft().getRenderManager().renderPosX;
        final double z = vec3d.zCoord + (vec3d.zCoord - vec3d.zCoord) * Minecraft.getMinecraft().getRenderPartialTicks() - Minecraft.getMinecraft().getRenderManager().renderPosZ;
        final float yaw = (float)(-Math.toDegrees(Math.atan2(x, z)));
        return (float)(yaw - AnimationHelper.Interpolate(Minecraft.getMinecraft().player.rotationYaw, Minecraft.getMinecraft().player.prevRotationYaw, 1.0));
    }
    
    public static float[] getTargetRotations(final Entity ent) {
        final double x = ent.posX;
        final double z = ent.posZ;
        final double y = ent.posY + ent.getEyeHeight() / 2.0f;
        return getRotationFromPosition(x, z, y);
    }
    
    public static float[] getRotationFromPosition(final double x, final double z, final double y) {
        final double xDiff = x - Minecraft.getMinecraft().player.posX;
        final double zDiff = z - Minecraft.getMinecraft().player.posZ;
        final double yDiff = y - Minecraft.getMinecraft().player.posY - 1.7;
        final double dist = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff);
        final float yaw = (float)(Math.atan2(zDiff, xDiff) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(yDiff, dist) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }
    
    public static boolean isAimAtMe(final Entity entity, final float breakRadius) {
        final float entityYaw = MathHelper.wrapDegrees(entity.rotationYaw);
        return Math.abs(MathHelper.wrapDegrees(getYawToEntity(entity, RotationHelper.mc.player) - entityYaw)) <= breakRadius;
    }
    
    public static boolean posCheck(final Entity entity) {
        return checkPosition(RotationHelper.mc.player.posY, entity.posY - 1.5, entity.posY + 1.5);
    }
    
    public static boolean checkPosition(final double pos1, final double pos2, final double pos3) {
        return pos1 <= pos3 && pos1 >= pos2;
    }
    
    public static float getYawToEntity(final Entity mainEntity, final Entity targetEntity) {
        final double pX = mainEntity.posX;
        final double pZ = mainEntity.posZ;
        final double eX = targetEntity.posX;
        final double eZ = targetEntity.posZ;
        final double dX = pX - eX;
        final double dZ = pZ - eZ;
        final double yaw = Math.toDegrees(Math.atan2(dZ, dX)) + 90.0;
        return (float)yaw;
    }
    
    public static float[] getRotations(final double x, final double y, final double z) {
        final double n = x + 0.5;
        final double diffX = n - Minecraft.getMinecraft().player.posX;
        final double n2 = (y + 0.5) / 2.0;
        final double posY = Minecraft.getMinecraft().player.posY;
        final double diffY = n2 - (posY + Minecraft.getMinecraft().player.getEyeHeight());
        final double n3 = z + 0.5;
        final double diffZ = n3 - Minecraft.getMinecraft().player.posZ;
        final double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        final float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793) - 90.0f;
        final float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / 3.141592653589793));
        return new float[] { yaw, pitch };
    }
    
    public static float[] getRotations(final Entity entityIn) {
        final double diffX = entityIn.posX - RotationHelper.mc.player.posX;
        final double diffZ = entityIn.posZ - RotationHelper.mc.player.posZ;
        double diffY;
        if (entityIn instanceof EntityLivingBase) {
            diffY = entityIn.posY + entityIn.getEyeHeight() - (RotationHelper.mc.player.posY + RotationHelper.mc.player.getEyeHeight()) - 0.4000000059604645;
        }
        else {
            diffY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0 - (RotationHelper.mc.player.posY + RotationHelper.mc.player.getEyeHeight());
        }
        if (!RotationHelper.mc.player.canEntityBeSeen(entityIn)) {
            diffY = entityIn.posY + entityIn.height - (RotationHelper.mc.player.posY + RotationHelper.mc.player.getEyeHeight());
        }
        final double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0 + GCDFix.getFixedRotation(MathematicHelper.randomizeFloat(-1.75f, 1.75f)));
        float pitch = (float)(Math.toDegrees(-Math.atan2(diffY, diffXZ)) + GCDFix.getFixedRotation(MathematicHelper.randomizeFloat(-1.8f, 1.75f)));
        yaw = RotationHelper.mc.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - RotationHelper.mc.player.rotationYaw));
        pitch = RotationHelper.mc.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - RotationHelper.mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90.0f, 90.0f);
        return new float[] { yaw, pitch };
    }
    
    public static float[] getMatrix2Rotations(final Entity entityIn) {
        final double diffX = entityIn.posX - RotationHelper.mc.player.posX;
        final double diffZ = entityIn.posZ - RotationHelper.mc.player.posZ;
        double diffY;
        if (entityIn instanceof EntityLivingBase) {
            diffY = entityIn.posY + entityIn.getEyeHeight() - (RotationHelper.mc.player.posY + RotationHelper.mc.player.getEyeHeight()) - (entityIn.getEntityBoundingBox().maxY - entityIn.getEntityBoundingBox().minY) / 2.0 + 0.2800000011920929;
        }
        else {
            diffY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0 - (RotationHelper.mc.player.posY + RotationHelper.mc.player.getEyeHeight());
        }
        if (!RotationHelper.mc.player.canEntityBeSeen(entityIn)) {
            diffY = entityIn.posY + entityIn.height - (RotationHelper.mc.player.posY + RotationHelper.mc.player.getEyeHeight());
        }
        final double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0 + getFixedRotation(1.1f + random(-1.75f, 2.2307692f)));
        float pitch = (float)(Math.toDegrees(-Math.atan2(diffY, diffXZ)) + getFixedRotation(1.1f + random(-2.2307692f, 1.35f)));
        yaw = RotationHelper.mc.player.rotationYaw + getFixedRotation(MathHelper.wrapDegrees(yaw - RotationHelper.mc.player.rotationYaw));
        pitch = RotationHelper.mc.player.rotationPitch + getFixedRotation(MathHelper.wrapDegrees(pitch - RotationHelper.mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -87.0f, 87.0f);
        return new float[] { yaw, pitch };
    }
    
    public static float random(final float min, final float max) {
        return (float)(Math.random() * (max - min) + min);
    }
    
    public static float getFixedRotation(final float value) {
        float f1 = (f1 = (float)(RotationHelper.mc.gameSettings.mouseSensitivity * 0.6 + 0.2)) * f1 * f1 * 8.0f;
        return Math.round(value / (float)(f1 * 0.15)) * (float)(f1 * 0.15);
    }
    
    public static float[] getFakeRotations(final Entity entityIn) {
        final double diffX = entityIn.posX - RotationHelper.mc.player.posX;
        final double diffZ = entityIn.posZ - RotationHelper.mc.player.posZ;
        double diffY;
        if (entityIn instanceof EntityLivingBase) {
            diffY = entityIn.posY + entityIn.getEyeHeight() - (RotationHelper.mc.player.posY + RotationHelper.mc.player.getEyeHeight()) - 0.20000000298023224;
        }
        else {
            diffY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0 - (RotationHelper.mc.player.posY + RotationHelper.mc.player.getEyeHeight());
        }
        final double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / 3.141592653589793 - 90.0);
        float pitch = (float)(-(Math.atan2(diffY, diffXZ) * 180.0 / 3.141592653589793));
        yaw = RotationHelper.mc.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - RotationHelper.mc.player.rotationYaw));
        pitch = RotationHelper.mc.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - RotationHelper.mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90.0f, 90.0f);
        return new float[] { yaw, pitch };
    }
    
    public static float[] getCustomRotations(final Entity entityIn) {
        final double diffX = entityIn.posX - RotationHelper.mc.player.posX;
        final double diffZ = entityIn.posZ - RotationHelper.mc.player.posZ;
        double diffY;
        if (entityIn instanceof EntityLivingBase) {
            if (!KillAura.staticPitch.getBoolValue()) {
                diffY = entityIn.posY + entityIn.getEyeHeight() - (RotationHelper.mc.player.posY + RotationHelper.mc.player.getEyeHeight()) - KillAura.pitchHead.getNumberValue();
            }
            else {
                diffY = entityIn.getEyeHeight() - RotationHelper.mc.player.getEyeHeight() - KillAura.pitchHead.getNumberValue();
            }
        }
        else {
            diffY = (entityIn.getEntityBoundingBox().minY + entityIn.getEntityBoundingBox().maxY) / 2.0 - (RotationHelper.mc.player.posY + RotationHelper.mc.player.getEyeHeight());
        }
        if (!RotationHelper.mc.player.canEntityBeSeen(entityIn)) {
            diffY = entityIn.posY + entityIn.height - (RotationHelper.mc.player.posY + RotationHelper.mc.player.getEyeHeight());
        }
        final double diffXZ = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0) + GCDFix.getFixedRotation(MathematicHelper.randomizeFloat(KillAura.yawrandom.getNumberValue(), -KillAura.yawrandom.getNumberValue()));
        float pitch = (float)Math.toDegrees(-Math.atan2(diffY, diffXZ)) + GCDFix.getFixedRotation(MathematicHelper.randomizeFloat(KillAura.pitchRandom.getNumberValue(), -KillAura.pitchRandom.getNumberValue()));
        yaw = RotationHelper.mc.player.rotationYaw + GCDFix.getFixedRotation(MathHelper.wrapDegrees(yaw - RotationHelper.mc.player.rotationYaw));
        pitch = RotationHelper.mc.player.rotationPitch + GCDFix.getFixedRotation(MathHelper.wrapDegrees(pitch - RotationHelper.mc.player.rotationPitch));
        pitch = MathHelper.clamp(pitch, -90.0f, 90.0f);
        return new float[] { yaw, pitch };
    }
}
