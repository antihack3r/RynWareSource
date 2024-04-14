// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.utils.math;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.MathHelper;
import java.util.List;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.Comparator;
import java.util.ArrayList;
import net.minecraft.util.EnumHand;
import java.util.Iterator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import org.rynware.client.feature.Feature;
import org.rynware.client.feature.impl.combat.AntiBot;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityMagmaCube;
import org.rynware.client.feature.impl.combat.KillAura;
import net.minecraft.entity.monster.EntitySlime;
import org.rynware.client.friend.Friend;
import org.rynware.client.Main;
import net.minecraft.entity.EntityLivingBase;
import org.rynware.client.utils.Helper;

public class KillauraUtils implements Helper
{
    public static TimerHelper timerHelper;
    
    public static boolean canAttack(final EntityLivingBase player) {
        for (final Friend friend : Main.instance.friendManager.getFriends()) {
            if (!player.getName().equals(friend.getName())) {
                continue;
            }
            return false;
        }
        if (player instanceof EntitySlime && !KillAura.targetsSetting.getSetting("Mobs").getBoolValue()) {
            return false;
        }
        if (player instanceof EntityMagmaCube && !KillAura.targetsSetting.getSetting("Mobs").getBoolValue()) {
            return false;
        }
        if (player instanceof EntityDragon && !KillAura.targetsSetting.getSetting("Mobs").getBoolValue()) {
            return false;
        }
        if (player instanceof EntityArmorStand) {
            return false;
        }
        if (Main.instance.featureManager.getFeature(AntiBot.class).isEnabled() && AntiBot.isBotPlayer.contains(player)) {
            return false;
        }
        if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager || player instanceof EntitySlime || player instanceof EntitySquid) {
            if (player instanceof EntityPlayer && !KillAura.targetsSetting.getSetting("Players").getBoolValue()) {
                return false;
            }
            if (player instanceof EntityAnimal && !KillAura.targetsSetting.getSetting("Animals").getBoolValue()) {
                return false;
            }
            if (player instanceof EntityMob && !KillAura.targetsSetting.getSetting("Mobs").getBoolValue()) {
                return false;
            }
            if (player instanceof EntityVillager && !KillAura.targetsSetting.getSetting("Villagers").getBoolValue()) {
                return false;
            }
            if (player instanceof EntityOcelot && !KillAura.targetsSetting.getSetting("Animals").getBoolValue()) {
                return false;
            }
            if (player instanceof EntityWolf && !KillAura.targetsSetting.getSetting("Animals").getBoolValue()) {
                return false;
            }
            if (player instanceof EntityEnderman && !KillAura.targetsSetting.getSetting("Mobs").getBoolValue()) {
                return false;
            }
            if (player instanceof EntitySlime && !KillAura.targetsSetting.getSetting("Mobs").getBoolValue()) {
                return false;
            }
            if (player instanceof EntitySquid && !KillAura.targetsSetting.getSetting("Mobs").getBoolValue()) {
                return false;
            }
            if (player.isInvisible() && !KillAura.targetsSetting.getSetting("Invisibles").getBoolValue()) {
                return false;
            }
        }
        if (!canSeeEntityAtFov(player, KillAura.fov.getNumberValue() * 2.0f)) {
            return false;
        }
        if (!range(player, KillAura.range.getNumberValue())) {
            return false;
        }
        if (!player.canEntityBeSeen(KillauraUtils.mc.player)) {
            return KillAura.walls.getBoolValue();
        }
        return player != KillauraUtils.mc.player;
    }
    
    public static boolean canSeeEntityAtFov(final Entity entityLiving, final float scope) {
        final double diffX = entityLiving.posX - KillauraUtils.mc.player.posX;
        final double diffZ = entityLiving.posZ - KillauraUtils.mc.player.posZ;
        final float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        final double difference = angleDifference(yaw, KillauraUtils.mc.player.rotationYaw);
        return difference <= scope;
    }
    
    public static double angleDifference(final float oldYaw, final float newYaw) {
        float yaw = Math.abs(oldYaw - newYaw) % 360.0f;
        if (yaw > 180.0f) {
            yaw = 360.0f - yaw;
        }
        return yaw;
    }
    
    private static boolean range(final EntityLivingBase entity, final float range) {
        return KillauraUtils.mc.player.getDistanceToEntity(entity) <= range;
    }
    
    public static void attackEntity(final EntityLivingBase target) {
        if (target == null || KillauraUtils.mc.player.getHealth() < 0.0f) {
            return;
        }
        if (KillauraUtils.mc.player.getDistanceToEntity(target) > KillAura.range.getNumberValue()) {
            return;
        }
        if (!target.isDead) {
            final float attackDelay = KillAura.attackCoolDown.getNumberValue() * (KillAura.tpsSync.getBoolValue() ? (TPSUtils.getTickRate() / 20.0f) : 1.0f);
            if (KillauraUtils.mc.player.getCooledAttackStrength(attackDelay) == 1.0f) {
                KillauraUtils.mc.playerController.attackEntity(KillauraUtils.mc.player, rayCast(target, KillAura.range.getNumberValue()));
                KillauraUtils.mc.player.swingArm(EnumHand.MAIN_HAND);
                KillAura.BreakShield(target);
            }
        }
    }
    
    public static EntityLivingBase getSortEntities() {
        final List<EntityLivingBase> entity = new ArrayList<EntityLivingBase>();
        for (final Entity e : KillauraUtils.mc.world.loadedEntityList) {
            if (e instanceof EntityLivingBase) {
                final EntityLivingBase player = (EntityLivingBase)e;
                if (KillauraUtils.mc.player.getDistanceToEntity(player) >= KillAura.range.getNumberValue() || !canAttack(player)) {
                    continue;
                }
                if (player.getHealth() > 0.0f && !player.isDead) {
                    entity.add(player);
                }
                else {
                    entity.remove(player);
                }
            }
        }
        final String mode = KillAura.sortMode.getOptions();
        if (mode.equalsIgnoreCase("Distance")) {
            entity.sort(Comparator.comparingDouble((ToDoubleFunction<? super EntityLivingBase>)KillauraUtils.mc.player::getDistanceToEntity));
        }
        else if (mode.equalsIgnoreCase("Crosshair")) {
            entity.sort(Comparator.comparingDouble((ToDoubleFunction<? super EntityLivingBase>)KillauraUtils::Angle));
        }
        else if (mode.equalsIgnoreCase("Health")) {
            entity.sort((o1, o2) -> (int)(o1.getHealth() - o2.getHealth()));
        }
        else if (mode.equalsIgnoreCase("Higher Armor")) {
            entity.sort(Comparator.comparing((Function<? super Object, ? extends Comparable>)EntityLivingBase::getTotalArmorValue).reversed());
        }
        else if (mode.equalsIgnoreCase("Lowest Armor")) {
            entity.sort(Comparator.comparing((Function<? super EntityLivingBase, ? extends Comparable>)EntityLivingBase::getTotalArmorValue));
        }
        if (KillAura.typeMode.currentMode.equalsIgnoreCase("Single") && KillAura.target != null && entity.contains(KillAura.target)) {
            entity.removeIf(x -> x != KillAura.target);
        }
        if (entity.isEmpty()) {
            return null;
        }
        return entity.get(0);
    }
    
    public static float Angle(final EntityLivingBase entity) {
        final double diffX = entity.posX - KillauraUtils.mc.player.posX;
        final double diffZ = entity.posZ - KillauraUtils.mc.player.posZ;
        return (float)Math.abs(MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0 - KillauraUtils.mc.player.rotationYaw));
    }
    
    public static Entity rayCast(final Entity entityIn, double range) {
        final Vec3d vec = entityIn.getPositionVector().add(new Vec3d(0.0, entityIn.getEyeHeight(), 0.0));
        final Vec3d vecPositionVector = KillauraUtils.mc.player.getPositionVector().add(new Vec3d(0.0, KillauraUtils.mc.player.getEyeHeight(), 0.0));
        final AxisAlignedBB axis = KillauraUtils.mc.player.getEntityBoundingBox().addCoord(vec.xCoord - vecPositionVector.xCoord, vec.yCoord - vecPositionVector.yCoord, vec.zCoord - vecPositionVector.zCoord).expand(1.0, 1.0, 1.0);
        Entity entityRayCast = null;
        for (final Entity entity : KillauraUtils.mc.world.getEntitiesWithinAABBExcludingEntity(KillauraUtils.mc.player, axis)) {
            if (entity.canBeCollidedWith() && entity instanceof EntityLivingBase) {
                final float size = entity.getCollisionBorderSize();
                final AxisAlignedBB axis2 = entity.getEntityBoundingBox().expand(size, size, size);
                final RayTraceResult rayTrace = axis2.calculateIntercept(vecPositionVector, vec);
                if (axis2.isVecInside(vecPositionVector)) {
                    if (range < 0.0) {
                        continue;
                    }
                    entityRayCast = entity;
                    range = 0.0;
                }
                else {
                    if (rayTrace == null) {
                        continue;
                    }
                    final double dist = vecPositionVector.distanceTo(rayTrace.hitVec);
                    if (range != 0.0 && dist >= range) {
                        continue;
                    }
                    entityRayCast = entity;
                    range = dist;
                }
            }
        }
        return entityRayCast;
    }
    
    static {
        KillauraUtils.timerHelper = new TimerHelper();
    }
}
