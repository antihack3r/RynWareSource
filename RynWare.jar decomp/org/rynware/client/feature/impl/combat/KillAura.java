// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.combat;

import net.minecraft.network.play.client.CPacketHeldItemChange;
import org.rynware.client.utils.inventory.InvenotryUtil;
import org.rynware.client.ui.notification.NotificationRenderer;
import org.rynware.client.ui.notification.NotificationMode;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.network.play.server.SPacketEntityStatus;
import org.rynware.client.event.events.impl.packet.EventReceivePacket;
import org.lwjgl.input.Mouse;
import net.minecraft.item.ItemAxe;
import org.rynware.client.event.events.impl.player.EventUpdate;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumHand;
import net.minecraft.item.ItemShield;
import org.rynware.client.event.events.impl.packet.EventAttackSilent;
import org.rynware.client.utils.math.GCDFix;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.Block;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.block.BlockLiquid;
import org.rynware.client.utils.movement.MovementUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.Entity;
import org.rynware.client.utils.math.RotationHelper;
import org.rynware.client.utils.math.KillauraUtils;
import org.rynware.client.event.events.impl.player.EventPreMotion;
import org.rynware.client.event.EventTarget;
import net.minecraft.network.play.client.CPacketUseEntity;
import org.rynware.client.event.events.impl.packet.EventSendPacket;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.MultipleBoolSetting;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import net.minecraft.entity.EntityLivingBase;
import org.rynware.client.utils.math.TimerHelper;
import org.rynware.client.feature.Feature;

public class KillAura extends Feature
{
    public static TimerHelper timerHelper;
    public static float yaw;
    public float pitch;
    public float pitch2;
    private int notiTicks;
    public static boolean isAttacking;
    TimerHelper shieldFixerTimer;
    public float yaw2;
    public static boolean isBreaked;
    public static EntityLivingBase target;
    public static ListSetting rotationMode;
    public static ListSetting typeMode;
    public static ListSetting sortMode;
    public static NumberSetting fov;
    public static NumberSetting attackCoolDown;
    public static NumberSetting range;
    public static NumberSetting yawrandom;
    public static NumberSetting pitchRandom;
    public static BooleanSetting staticPitch;
    public static NumberSetting pitchHead;
    public BooleanSetting rayCast;
    public static BooleanSetting tpsSync;
    public static BooleanSetting walls;
    public static BooleanSetting onlyCritical;
    public BooleanSetting spaceOnly;
    public NumberSetting criticalFallDistance;
    public BooleanSetting shieldFixer;
    public NumberSetting fixerDelay;
    public BooleanSetting shieldDesync;
    public static BooleanSetting shieldBreaker;
    public static BooleanSetting breakNotifications;
    public static MultipleBoolSetting targetsSetting;
    
    public KillAura() {
        super("KillAura", "\u0410\u0442\u0442\u0430\u043a\u0443\u0435\u0442 \u0435\u043d\u0442\u0438\u0442\u0438", FeatureCategory.Combat);
        this.pitch2 = 0.0f;
        this.shieldFixerTimer = new TimerHelper();
        this.yaw2 = 0.0f;
        this.rayCast = new BooleanSetting("RayCast", false, () -> true);
        this.spaceOnly = new BooleanSetting("Space Only", false, () -> KillAura.onlyCritical.getBoolValue());
        this.criticalFallDistance = new NumberSetting("Critical Fall Distance", 0.2f, 0.08f, 1.0f, 0.01f, () -> KillAura.onlyCritical.getBoolValue());
        this.shieldFixer = new BooleanSetting("ShieldFixer", false, () -> true);
        this.fixerDelay = new NumberSetting("Fixer Delay", 150.0f, 0.0f, 600.0f, 10.0f, () -> this.shieldFixer.getBoolValue());
        this.shieldDesync = new BooleanSetting("Shield Desync", false, () -> true);
        this.addSettings(KillAura.rotationMode, KillAura.typeMode, KillAura.sortMode, KillAura.targetsSetting, KillAura.fov, KillAura.range, KillAura.attackCoolDown, this.rayCast, KillAura.tpsSync, KillAura.yawrandom, KillAura.pitchRandom, KillAura.pitchHead, KillAura.staticPitch, KillAura.walls, KillAura.onlyCritical, this.spaceOnly, this.criticalFallDistance, KillAura.shieldBreaker, KillAura.breakNotifications, this.shieldFixer, this.fixerDelay, this.shieldDesync);
    }
    
    @EventTarget
    public void onSendPacket(final EventSendPacket event) {
        if (event.getPacket() instanceof CPacketUseEntity) {
            final CPacketUseEntity cPacketUseEntity = (CPacketUseEntity)event.getPacket();
            if (cPacketUseEntity.getAction() == CPacketUseEntity.Action.INTERACT) {
                event.setCancelled(true);
            }
            if (cPacketUseEntity.getAction() == CPacketUseEntity.Action.INTERACT_AT) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventTarget
    public void onPreAttack(final EventPreMotion event) {
        final String mode = KillAura.rotationMode.getOptions();
        this.setSuffix("" + mode);
        KillAura.target = KillauraUtils.getSortEntities();
        if (KillAura.target == null) {
            return;
        }
        if (!KillAura.rotationMode.currentMode.equals("Snap") && !RotationHelper.isLookingAtEntity(false, KillAura.yaw, this.pitch, 0.12f, 0.12f, 0.12f, KillAura.target, KillAura.range.getNumberValue()) && this.rayCast.getBoolValue()) {
            return;
        }
        KillAura.mc.player.jumpTicks = 0;
        final BlockPos blockPos = new BlockPos(KillAura.mc.player.posX, KillAura.mc.player.posY - 0.1, KillAura.mc.player.posZ);
        final Block block = KillAura.mc.world.getBlockState(blockPos).getBlock();
        final float f2 = KillAura.mc.player.getCooledAttackStrength(0.5f);
        final boolean flag = f2 > 0.9f;
        if (!flag && KillAura.onlyCritical.getBoolValue()) {
            return;
        }
        if (KillAura.mc.gameSettings.keyBindJump.isKeyDown() || !this.spaceOnly.getBoolValue()) {
            if (MovementUtils.airBlockAboveHead()) {
                if (KillAura.mc.player.fallDistance < this.criticalFallDistance.getNumberValue() && !(block instanceof BlockLiquid) && KillAura.onlyCritical.getBoolValue() && !KillAura.mc.player.isRiding() && !KillAura.mc.player.isOnLadder() && !KillAura.mc.player.isInLiquid() && !KillAura.mc.player.isInWeb) {
                    KillAura.mc.player.connection.sendPacket(new CPacketEntityAction(KillAura.mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
                    return;
                }
            }
            else if (KillAura.mc.player.fallDistance > 0.0f && !KillAura.mc.player.onGround && KillAura.onlyCritical.getBoolValue() && !KillAura.mc.player.isRiding() && !KillAura.mc.player.isOnLadder() && !KillAura.mc.player.isInLiquid() && !KillAura.mc.player.isInWeb) {
                KillAura.mc.player.connection.sendPacket(new CPacketEntityAction(KillAura.mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
                return;
            }
        }
        if (KillAura.rotationMode.currentMode.equals("Snap") && KillAura.mc.player.getCooledAttackStrength(0.0f) >= 1.0f) {
            final float[] rots1 = RotationHelper.getRotations(KillAura.target);
            KillAura.mc.player.renderYawOffset = rots1[0];
            KillAura.mc.player.rotationYawHead = rots1[0];
            KillAura.mc.player.rotationPitchHead = rots1[1];
            KillAura.mc.player.rotationYaw = rots1[0];
            KillAura.mc.player.rotationPitch = rots1[1];
        }
        KillauraUtils.attackEntity(KillAura.target);
    }
    
    @EventTarget
    public void onRotations(final EventPreMotion event) {
        final String mode = KillAura.rotationMode.getOptions();
        if (KillAura.target == null) {
            return;
        }
        if (!KillAura.target.isDead) {
            final float[] matrix = RotationHelper.getRotations(KillAura.target);
            final float[] matrix2 = RotationHelper.getMatrix2Rotations(KillAura.target);
            final float[] fake = RotationHelper.getFakeRotations(KillAura.target);
            final float[] custom = RotationHelper.getCustomRotations(KillAura.target);
            if (mode.equalsIgnoreCase("Matrix")) {
                event.setYaw(matrix[0]);
                event.setPitch(matrix[1]);
                KillAura.yaw = matrix[0];
                this.pitch = matrix[1];
                KillAura.mc.player.renderYawOffset = matrix[0];
                KillAura.mc.player.rotationYawHead = matrix[0];
                KillAura.mc.player.rotationPitchHead = matrix[1];
            }
            else if (mode.equalsIgnoreCase("Sunrise")) {
                this.yaw2 = GCDFix.getFixedRotation(MathHelper.Rotate(this.yaw2, matrix[0], 40.0f, 50.0f));
                this.pitch2 = GCDFix.getFixedRotation(MathHelper.Rotate(this.pitch2, matrix[1], 0.35f, 2.1f));
                event.setYaw(this.yaw2);
                event.setPitch(this.pitch2);
                KillAura.yaw = this.yaw2;
                this.pitch = this.pitch2;
                KillAura.mc.player.renderYawOffset = fake[0];
                KillAura.mc.player.rotationYawHead = fake[0];
                KillAura.mc.player.rotationPitchHead = fake[1];
            }
            else if (mode.equalsIgnoreCase("Custom")) {
                event.setYaw(custom[0]);
                event.setPitch(custom[1]);
                KillAura.yaw = custom[0];
                this.pitch = custom[1];
                KillAura.mc.player.renderYawOffset = custom[0];
                KillAura.mc.player.rotationYawHead = custom[0];
                KillAura.mc.player.rotationPitchHead = custom[1];
            }
            else if (mode.equalsIgnoreCase("Matrix2")) {
                event.setYaw(matrix2[0]);
                event.setPitch(matrix2[1]);
                KillAura.yaw = matrix2[0];
                this.pitch = matrix2[1];
                KillAura.mc.player.renderYawOffset = matrix2[0];
                KillAura.mc.player.rotationYawHead = matrix2[0];
                KillAura.mc.player.rotationPitchHead = matrix2[1];
            }
        }
    }
    
    @EventTarget
    public void onAttackSilent(final EventAttackSilent eventAttackSilent) {
        KillAura.isAttacking = true;
        if (KillAura.mc.player.isBlocking() && this.shieldFixerTimer.hasReached(this.fixerDelay.getNumberValue()) && KillAura.mc.player.getHeldItem(EnumHand.OFF_HAND).getItem() instanceof ItemShield && this.shieldFixer.getBoolValue()) {
            KillAura.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, KillAura.mc.player.getPosition(), EnumFacing.UP));
            KillAura.mc.playerController.processRightClick(KillAura.mc.player, KillAura.mc.world, EnumHand.OFF_HAND);
            this.shieldFixerTimer.reset();
        }
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (this.shieldDesync.getBoolValue() && KillAura.mc.player.isBlocking() && KillAura.target != null && KillAura.mc.player.ticksExisted % 8 == 0) {
            KillAura.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(900, 900, 900), EnumFacing.DOWN));
            KillAura.mc.playerController.processRightClick(KillAura.mc.player, KillAura.mc.world, EnumHand.OFF_HAND);
        }
        if (this.shieldFixer.getBoolValue()) {
            if (KillAura.target.getHeldItemMainhand().getItem() instanceof ItemAxe) {
                if (KillAura.mc.gameSettings.keyBindUseItem.isKeyDown()) {
                    KillAura.mc.gameSettings.keyBindUseItem.pressed = false;
                }
            }
            else {
                KillAura.mc.gameSettings.keyBindUseItem.pressed = Mouse.isButtonDown(1);
            }
        }
    }
    
    @EventTarget
    public void onSound(final EventReceivePacket sound) {
        if (KillAura.breakNotifications.getBoolValue() && sound.getPacket() instanceof SPacketEntityStatus) {
            final SPacketEntityStatus sPacketEntityStatus = (SPacketEntityStatus)sound.getPacket();
            if (sPacketEntityStatus.getOpCode() == 30 && sPacketEntityStatus.getEntity(KillAura.mc.world) == KillAura.target) {
                if (this.notiTicks < 2) {
                    NotificationRenderer.queue(TextFormatting.GREEN + "Shield-Breaker", "Successfully destroyed " + KillAura.target.getName() + " shield", 2, NotificationMode.SUCCESS);
                }
                else {
                    this.notiTicks = 0;
                }
            }
        }
    }
    
    public static void BreakShield(final EntityLivingBase tg) {
        if (InvenotryUtil.doesHotbarHaveAxe() && KillAura.shieldBreaker.getBoolValue()) {
            final int item = InvenotryUtil.getAxe();
            if (InvenotryUtil.getAxe() >= 0 && tg instanceof EntityPlayer && tg.isHandActive() && tg.getActiveItemStack().getItem() instanceof ItemShield) {
                KillAura.mc.player.connection.sendPacket(new CPacketHeldItemChange(item));
                KillAura.mc.playerController.attackEntity(KillAura.mc.player, KillAura.target);
                KillAura.mc.player.swingArm(EnumHand.MAIN_HAND);
                KillAura.mc.player.connection.sendPacket(new CPacketHeldItemChange(KillAura.mc.player.inventory.currentItem));
            }
        }
    }
    
    @Override
    public void onDisable() {
        KillAura.target = null;
        super.onDisable();
    }
    
    static {
        KillAura.timerHelper = new TimerHelper();
        KillAura.rotationMode = new ListSetting("Rotation Mode", "Matrix", () -> true, new String[] { "Vanilla", "Matrix", "Matrix2", "Sunrise", "Snap", "Custom", "AAC" });
        KillAura.typeMode = new ListSetting("Type Mode", "Single", () -> true, new String[] { "Single", "Switch" });
        KillAura.sortMode = new ListSetting("Priority Mode", "Distance", () -> KillAura.typeMode.currentMode.equalsIgnoreCase("Switch"), new String[] { "Distance", "Health", "Crosshair", "Higher Armor", "Lowest Armor" });
        KillAura.fov = new NumberSetting("FOV", 180.0f, 0.0f, 180.0f, 1.0f, () -> true);
        KillAura.attackCoolDown = new NumberSetting("Attack CoolDown", 0.85f, 0.1f, 1.0f, 0.01f, () -> !KillAura.rotationMode.currentMode.equals("Snap"));
        KillAura.range = new NumberSetting("AttackRange", 3.6f, 3.0f, 6.0f, 0.01f, () -> true);
        KillAura.yawrandom = new NumberSetting("Yaw Random", 1.6f, 0.1f, 20.0f, 0.01f, () -> KillAura.rotationMode.currentMode.equals("Custom"));
        KillAura.pitchRandom = new NumberSetting("Pitch Random", 1.6f, 0.1f, 20.0f, 0.01f, () -> KillAura.rotationMode.currentMode.equals("Custom"));
        KillAura.staticPitch = new BooleanSetting("Static Pitch", false, () -> KillAura.rotationMode.currentMode.equals("Custom"));
        KillAura.pitchHead = new NumberSetting("Pitch Head", 0.35f, 0.1f, 1.2f, 0.01f, () -> KillAura.rotationMode.currentMode.equals("Custom"));
        KillAura.tpsSync = new BooleanSetting("TpsSync", false, () -> true);
        KillAura.walls = new BooleanSetting("Walls", true, () -> true);
        KillAura.onlyCritical = new BooleanSetting("Only Critical", false, () -> true);
        KillAura.shieldBreaker = new BooleanSetting("ShieldBreaker", false, () -> true);
        KillAura.breakNotifications = new BooleanSetting("Break Notifications", true, () -> KillAura.shieldBreaker.getBoolValue());
        KillAura.targetsSetting = new MultipleBoolSetting("Targets", new BooleanSetting[] { new BooleanSetting("Players", true), new BooleanSetting("Mobs"), new BooleanSetting("Animals"), new BooleanSetting("Villagers"), new BooleanSetting("Invisibles", true) });
    }
}
