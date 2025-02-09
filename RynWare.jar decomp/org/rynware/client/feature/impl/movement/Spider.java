// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.movement;

import net.minecraft.block.state.IBlockState;
import org.rynware.client.utils.math.RotationHelper;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.init.Blocks;
import org.rynware.client.event.events.impl.player.BlockHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos;
import net.minecraft.item.ItemBlock;
import org.rynware.client.utils.movement.MovementUtils;
import org.rynware.client.event.events.impl.player.EventPreMotion;
import net.minecraft.network.play.client.CPacketPlayer;
import org.rynware.client.event.events.impl.packet.EventSendPacket;
import org.rynware.client.event.EventTarget;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.utils.math.TimerHelper;
import org.rynware.client.feature.Feature;

public class Spider extends Feature
{
    TimerHelper spiderTimer;
    public static final ListSetting spiderMode;
    public NumberSetting climbSpeed;
    private int ticks;
    private int fticks;
    private int prevSlot;
    
    public Spider() {
        super("Spider", FeatureCategory.Movement);
        this.spiderTimer = new TimerHelper();
        this.climbSpeed = new NumberSetting("Climb Speed", 1.0f, 0.0f, 5.0f, 0.1f, () -> true);
        this.ticks = 0;
        this.addSettings(this.climbSpeed, Spider.spiderMode);
    }
    
    @Override
    public void onDisable() {
        this.ticks = 0;
        this.fticks = 0;
        super.onDisable();
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        this.setSuffix("" + this.climbSpeed.getNumberValue());
    }
    
    @EventTarget
    public void onP(final EventSendPacket event) {
        if (Spider.spiderMode.currentMode.equalsIgnoreCase("Matrix") && Spider.mc.player.isCollidedHorizontally && event.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packet = (CPacketPlayer)event.getPacket();
            if (this.spiderTimer.hasReached(this.climbSpeed.getNumberValue() * 100.0f)) {
                this.spiderTimer.reset();
                Spider.mc.player.jump();
                Spider.mc.player.motionY = 0.41999998688697815;
                packet.onGround = true;
            }
        }
    }
    
    @EventTarget
    public void onPreMotion(final EventPreMotion event) {
        if (Spider.spiderMode.currentMode.equalsIgnoreCase("Sunrise")) {
            if (!MovementUtils.isMoving() || !Spider.mc.player.isCollidedHorizontally) {
                return;
            }
            this.prevSlot = Spider.mc.player.inventory.currentItem;
            if (Spider.mc.player.ticksExisted % 2 == 0) {
                int find = -2;
                for (int i = 0; i <= 8; ++i) {
                    if (Spider.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock) {
                        find = i;
                    }
                }
                if (find == -2) {
                    return;
                }
                final BlockPos pos = new BlockPos(Spider.mc.player.posX, Spider.mc.player.posY + 2.0, Spider.mc.player.posZ);
                final EnumFacing side = getPlaceableSide(pos);
                if (side != null) {
                    Spider.mc.player.inventory.currentItem = find;
                    final BlockPos neighbour = new BlockPos(Spider.mc.player.posX, Spider.mc.player.posY + 2.0, Spider.mc.player.posZ).offset(side);
                    final EnumFacing opposite = side.getOpposite();
                    final Vec3d hitVec = new Vec3d(neighbour).addVector(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
                    final Vec2f rotation = this.getRotationTo(hitVec);
                    event.setYaw(rotation.x);
                    event.setPitch(rotation.y);
                    final float x = (float)(hitVec.xCoord - neighbour.getX());
                    final float y = (float)(hitVec.yCoord - neighbour.getY());
                    final float z = (float)(hitVec.zCoord - neighbour.getZ());
                    Spider.mc.rightClickDelayTimer = 0;
                    Spider.mc.playerController.processRightClickBlock(Spider.mc.player, Spider.mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
                    if (BlockHelper.getBlock(new BlockPos(Spider.mc.player).add(0, 2, 0)) == Blocks.AIR) {
                        Spider.mc.playerController.processRightClickBlock(Spider.mc.player, Spider.mc.world, neighbour, opposite, new Vec3d(x, y, z), EnumHand.MAIN_HAND);
                    }
                    else {
                        Spider.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, neighbour, opposite));
                        Spider.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, neighbour, opposite));
                    }
                }
                if (this.spiderTimer.hasReached(this.climbSpeed.getNumberValue() * 100.0f)) {
                    Spider.mc.player.onGround = true;
                    Spider.mc.player.isCollidedVertically = true;
                    Spider.mc.player.isAirBorne = true;
                    Spider.mc.player.jump();
                    Spider.mc.player.motionY = 0.41999998688697815;
                    this.spiderTimer.reset();
                }
            }
            ++this.ticks;
            Spider.mc.player.inventory.currentItem = this.prevSlot;
        }
    }
    
    private Vec2f getRotationTo(final Vec3d posTo) {
        final EntityPlayerSP player = Spider.mc.player;
        return (player != null) ? this.getRotationTo(player.getPositionEyes(1.0f), posTo) : Vec2f.ZERO;
    }
    
    private Vec2f getRotationTo(final Vec3d posFrom, final Vec3d posTo) {
        return this.getRotationFromVec(posTo.subtract(posFrom));
    }
    
    private Vec2f getRotationFromVec(final Vec3d vec) {
        final double lengthXZ = Math.hypot(vec.xCoord, vec.zCoord);
        final double yaw = RotationHelper.getFixedRotation((float)(Math.toDegrees(Math.atan2(vec.zCoord, vec.xCoord)) - 90.0));
        final double pitch = RotationHelper.getFixedRotation((float)Math.toDegrees(-Math.atan2(vec.yCoord, lengthXZ)));
        return new Vec2f((float)yaw, (float)pitch);
    }
    
    public static EnumFacing getPlaceableSide(final BlockPos pos) {
        for (final EnumFacing side : EnumFacing.values()) {
            final BlockPos neighbour = pos.offset(side);
            if (!Spider.mc.world.isAirBlock(neighbour)) {
                final IBlockState blockState = BlockHelper.getState(neighbour);
                if (!blockState.getMaterial().isReplaceable()) {
                    return side;
                }
            }
        }
        return null;
    }
    
    static {
        spiderMode = new ListSetting("Spider Mode", "Matrix", () -> true, new String[] { "Matrix", "Sunrise" });
    }
}
