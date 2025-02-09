// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.movement;

import org.rynware.client.event.events.impl.player.EventMove;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import org.rynware.client.event.events.impl.player.EventPreMotion;
import org.rynware.client.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import org.rynware.client.ui.notification.NotificationRenderer;
import org.rynware.client.ui.notification.NotificationMode;
import org.rynware.client.utils.movement.MovementUtils;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.init.Items;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.utils.math.TimerHelper;
import org.rynware.client.feature.Feature;

public class Speed extends Feature
{
    public static float ticks;
    private static float spartanTicks;
    public double boost;
    public double moveSpeed;
    public static int stage;
    public double less;
    public double stair;
    public boolean slowDownHop;
    public TimerHelper timerHelper;
    public static final ListSetting speedMode;
    
    public Speed() {
        super("Speed", "\u0414\u0435\u043b\u0430\u0435\u0442 \u0432\u0430\u0441 \u0431\u044b\u0441\u0442\u0440\u0435\u0435", FeatureCategory.Movement);
        this.timerHelper = new TimerHelper();
        this.addSettings(Speed.speedMode);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        final String modezxc = Speed.speedMode.getOptions();
        if (modezxc.equalsIgnoreCase("Matrix Elytra")) {
            if (Speed.mc.player.isInWeb || Speed.mc.player.isOnLadder() || Speed.mc.player.isInLiquid()) {
                return;
            }
            int eIndex = -1;
            int elytraCount = 0;
            for (int i = 0; i < 45; ++i) {
                if (Speed.mc.player.inventory.getStackInSlot(i).getItem() == Items.ELYTRA && eIndex == -1) {
                    eIndex = i;
                }
                if (Speed.mc.player.inventory.getStackInSlot(i).getItem() == Items.ELYTRA) {
                    ++elytraCount;
                }
            }
            if (Speed.mc.gameSettings.keyBindJump.isKeyDown()) {
                return;
            }
            if (Speed.mc.player.ticksExisted % 20 == 0) {
                Speed.mc.playerController.windowClick(0, eIndex, 0, ClickType.PICKUP, Speed.mc.player);
                Speed.mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, Speed.mc.player);
                Speed.mc.player.connection.sendPacket(new CPacketEntityAction(Speed.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                Speed.mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, Speed.mc.player);
                Speed.mc.playerController.windowClick(0, eIndex, 0, ClickType.PICKUP, Speed.mc.player);
            }
            if (Speed.mc.gameSettings.keyBindJump.isKeyDown()) {
                return;
            }
            final EntityPlayerSP player = Speed.mc.player;
            player.motionZ *= 1.8;
            final EntityPlayerSP player2 = Speed.mc.player;
            player2.motionX *= 1.8;
            MovementUtils.strafe();
            if (elytraCount == 0 && eIndex == -1 && Speed.mc.player.getHeldItemOffhand().getItem() != Items.ELYTRA) {
                NotificationRenderer.queue("\u0412§6Matrix Elytra", "\u0420\u2019\u0420\u0455\u0420·\u0421\u040a\u0420\u0458\u0420\u0451\u0421\u201a\u0420µ \u0421\u040c\u0420»\u0420\u0451\u0421\u201a\u0421\u0402\u0421\u2039 \u0420\u0406 \u0420\u0451\u0420\u0405\u0420\u0406\u0420µ\u0420\u0405\u0421\u201a\u0420°\u0421\u0402\u0421\u040a!", 6, NotificationMode.WARNING);
                this.toggle();
            }
        }
    }
    
    @EventTarget
    public void onPreMotion(final EventPreMotion event) {
        final String mode = Speed.speedMode.getOptions();
        if (mode.equalsIgnoreCase("Matrix")) {
            if (Speed.mc.player.isInWeb || Speed.mc.player.isOnLadder() || Speed.mc.player.isInLiquid()) {
                return;
            }
            final double x = Speed.mc.player.posX;
            final double y = Speed.mc.player.posY;
            final double z = Speed.mc.player.posZ;
            final double yaw = Speed.mc.player.rotationYaw * 0.017453292;
            if (Speed.mc.player.onGround && !Speed.mc.gameSettings.keyBindJump.pressed) {
                Speed.mc.player.jump();
                Speed.mc.timer.timerSpeed = 1.3f;
                Speed.ticks = 11.0f;
            }
            else if (Speed.ticks < 11.0f) {
                ++Speed.ticks;
            }
            if (Speed.mc.player.motionY == -0.4448259643949201 && Speed.mc.world.getBlockState(new BlockPos(Speed.mc.player.posX, Speed.mc.player.posY - 0.9, Speed.mc.player.posZ)).getBlock() != Blocks.AIR) {
                final EntityPlayerSP player = Speed.mc.player;
                player.motionX *= 2.05;
                final EntityPlayerSP player2 = Speed.mc.player;
                player2.motionZ *= 2.05;
                Speed.mc.player.setPosition(x - Math.sin(yaw) * 0.003, y, z + Math.cos(yaw) * 0.003);
            }
            Speed.ticks = 0.0f;
        }
        else if (mode.equalsIgnoreCase("Matrix New")) {
            if (Speed.mc.player.isInWeb || Speed.mc.player.isOnLadder() || Speed.mc.player.isInLiquid()) {
                return;
            }
            if (Speed.mc.player.onGround && !Speed.mc.gameSettings.keyBindJump.pressed) {
                Speed.mc.player.jump();
            }
            if (Speed.mc.player.ticksExisted % 3 == 0) {
                Speed.mc.timer.timerSpeed = 1.3f;
            }
            else {
                Speed.mc.timer.timerSpeed = 1.0f;
            }
            if (Speed.mc.player.motionY == -0.4448259643949201 && Speed.mc.world.getBlockState(new BlockPos(Speed.mc.player.posX, Speed.mc.player.posY - 0.9, Speed.mc.player.posZ)).getBlock() != Blocks.AIR) {
                Speed.mc.player.jumpMovementFactor = 0.05f;
                if (Speed.mc.player.ticksExisted % 2 == 0) {
                    final EntityPlayerSP player3 = Speed.mc.player;
                    player3.motionX *= 2.0;
                    final EntityPlayerSP player4 = Speed.mc.player;
                    player4.motionZ *= 2.0;
                }
                else {
                    MovementUtils.setMotion(MovementUtils.getSpeed() * 1.0f + 0.22f);
                }
            }
        }
        else if (mode.equalsIgnoreCase("Damage")) {
            if (MovementUtils.isMoving()) {
                if (Speed.mc.player.onGround) {
                    Speed.mc.player.addVelocity(-Math.sin(MovementUtils.getAllDirection()) * 9.5 / 24.5, 0.0, Math.cos(MovementUtils.getAllDirection()) * 9.5 / 24.5);
                    MovementUtils.strafe();
                }
                else if (Speed.mc.player.isInWater()) {
                    Speed.mc.player.addVelocity(-Math.sin(MovementUtils.getAllDirection()) * 9.5 / 24.5, 0.0, Math.cos(MovementUtils.getAllDirection()) * 9.5 / 24.5);
                    MovementUtils.strafe();
                }
                else if (!Speed.mc.player.onGround) {
                    Speed.mc.player.addVelocity(-Math.sin(MovementUtils.getAllDirection()) * 0.11 / 24.5, 0.0, Math.cos(MovementUtils.getAllDirection()) * 0.11 / 24.5);
                    MovementUtils.strafe();
                }
                else {
                    Speed.mc.player.addVelocity(-Math.sin(MovementUtils.getAllDirection()) * 0.005 * MovementUtils.getSpeed(), 0.0, Math.cos(MovementUtils.getAllDirection()) * 0.005 * MovementUtils.getSpeed());
                    MovementUtils.strafe();
                }
            }
        }
        else if (mode.equalsIgnoreCase("AAC 5.1.0") && MovementUtils.isMoving()) {
            if (Speed.mc.player.ticksExisted % 3 == 0) {
                final EntityPlayerSP player5 = Speed.mc.player;
                player5.motionX *= 1.07;
                final EntityPlayerSP player6 = Speed.mc.player;
                player6.motionZ *= 1.07;
                Speed.mc.player.jumpMovementFactor = 0.256f;
                Speed.mc.player.isAirBorne = true;
            }
            MovementUtils.setMotion(1.07);
            Speed.mc.player.jumpMovementFactor = 0.014f;
            Speed.mc.player.addVelocity(-Math.sin(MovementUtils.getAllDirection()) * 0.07 / 24.5, 0.0, Math.cos(MovementUtils.getAllDirection()) * 0.07 / 24.5);
        }
    }
    
    @EventTarget
    public void onEventMove(final EventMove e) {
        if (Speed.speedMode.currentMode.equalsIgnoreCase("Spartan") && MovementUtils.isMoving()) {
            Speed.mc.timer.timerSpeed = 0.825f;
            if (MovementUtils.getSpeed() < 0.265f) {
                MovementUtils.strafe(MovementUtils.getSpeed() * 1.07f);
                if (Math.abs(Speed.mc.player.movementInput.moveStrafe) < 0.1 && !Speed.mc.gameSettings.keyBindBack.pressed) {
                    MovementUtils.strafe(MovementUtils.getSpeed() + Math.abs(Speed.mc.player.moveStrafing));
                }
                if (Speed.mc.player.onGround) {
                    MovementUtils.strafe();
                }
                Speed.mc.player.jumpMovementFactor = 0.0182f;
            }
            if (Speed.mc.player.fallDistance < 0.45f) {
                Speed.mc.timer.timerSpeed = 1.45f;
            }
        }
    }
    
    @EventTarget
    public void onMove(final EventMove e) {
        if (Speed.speedMode.currentMode.equalsIgnoreCase("NCP")) {
            if (!Speed.mc.gameSettings.keyBindJump.isKeyDown()) {
                return;
            }
            Speed.mc.player.jumpMovementFactor *= (float)1.04;
            boolean collided = Speed.mc.player.isCollidedHorizontally;
            if (collided) {
                Speed.stage = -1;
            }
            if (this.stair > 0.0) {
                this.stair -= 0.3;
            }
            this.less -= ((this.less > 1.0) ? 0.24 : 0.17);
            if (this.less < 0.0) {
                this.less = 0.0;
            }
            if (!Speed.mc.player.isInWater() && Speed.mc.player.onGround) {
                collided = Speed.mc.player.isCollidedHorizontally;
                if (Speed.stage >= 0 || collided) {
                    Speed.stage = 0;
                    final float motY = 0.42f;
                    Speed.mc.player.motionY = motY;
                    if (this.stair == 0.0) {
                        e.setY(motY);
                    }
                    ++this.less;
                    this.slowDownHop = (this.less > 1.0 && !this.slowDownHop);
                    if (this.less > 1.15) {
                        this.less = 1.15;
                    }
                }
            }
            this.moveSpeed = this.getCurrentSpeed(Speed.stage) + 0.0335;
            this.moveSpeed *= 0.85;
            if (this.stair > 0.0) {
                this.moveSpeed *= 1.0;
            }
            if (this.slowDownHop) {
                this.moveSpeed *= 0.8575;
            }
            if (Speed.mc.player.isInWater()) {
                this.moveSpeed = 0.351;
            }
            if (MovementUtils.isMoving()) {
                MovementUtils.setEventSpeed(e, (float)this.moveSpeed);
            }
            ++Speed.stage;
        }
    }
    
    public double getCurrentSpeed(final int stage) {
        double speed = MovementUtils.getBaseSpeed() + 0.028 * MovementUtils.getSpeedEffect() + MovementUtils.getSpeedEffect() / 15.0;
        final double initSpeed = 0.4145 + MovementUtils.getSpeedEffect() / 12.5;
        final double decrease = stage / 500.0 * 1.87;
        if (stage == 0) {
            speed = 0.64 + (MovementUtils.getSpeedEffect() + 0.028 * MovementUtils.getSpeedEffect()) * 0.134;
        }
        else if (stage == 1) {
            speed = initSpeed;
        }
        else if (stage >= 2) {
            speed = initSpeed - decrease;
        }
        return Math.max(speed, this.slowDownHop ? speed : (MovementUtils.getBaseSpeed() + 0.028 * MovementUtils.getSpeedEffect()));
    }
    
    private int findarmor() {
        for (int i = 0; i < 45; ++i) {
            if (Speed.mc.player.inventoryContainer.getSlot(i).getStack() != null && Speed.mc.player.inventoryContainer.getSlot(i).getStack().getUnlocalizedName().contains("chestplate")) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public void onDisable() {
        Speed.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
    
    static {
        Speed.ticks = 0.0f;
        speedMode = new ListSetting("Speed Mode", "Matrix", () -> true, new String[] { "Matrix", "Matrix New", "Damage", "Matrix Elytra", "AAC 5.1.0", "NCP", "Spartan" });
    }
}
