// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.movement;

import net.minecraft.block.BlockLiquid;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.Minecraft;
import org.rynware.client.event.events.impl.player.EventPreMotion;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import org.rynware.client.event.events.impl.player.EventLiquidSolid;
import org.rynware.client.event.EventTarget;
import org.rynware.client.utils.movement.MovementUtils;
import net.minecraft.util.math.BlockPos;
import org.rynware.client.utils.math.MathematicHelper;
import org.rynware.client.event.events.impl.player.EventUpdate;
import net.minecraft.client.settings.KeyBinding;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.feature.Feature;

public class Jesus extends Feature
{
    public static ListSetting mode;
    public static NumberSetting speed;
    public static NumberSetting NCPSpeed;
    public static BooleanSetting useTimer;
    private final NumberSetting timerSpeed;
    private int waterTicks;
    
    public Jesus() {
        super("Jesus", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0445\u043e\u0434\u0438\u0442\u044c \u043f\u043e \u0432\u043e\u0434\u0435", FeatureCategory.Movement);
        this.timerSpeed = new NumberSetting("Timer Speed", 1.05f, 1.01f, 1.5f, 0.01f, () -> Jesus.useTimer.getBoolValue());
        this.waterTicks = 0;
        this.addSettings(Jesus.mode, Jesus.speed, Jesus.NCPSpeed, Jesus.useTimer, this.timerSpeed);
    }
    
    @Override
    public void onDisable() {
        KeyBinding.setKeyBindState(Jesus.mc.gameSettings.keyBindJump.getKeyCode(), false);
        Jesus.mc.timer.timerSpeed = 1.0f;
        this.waterTicks = 0;
        super.onDisable();
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate e) {
        final float offsetY = MathematicHelper.randomizeFloat(0.1f, 0.3f);
        if (Jesus.mode.currentMode.equalsIgnoreCase("Matrix Last")) {
            if (this.isFluid(new BlockPos(Jesus.mc.player.posX, Jesus.mc.player.posY + offsetY, Jesus.mc.player.posZ))) {
                Jesus.mc.player.setVelocity(Jesus.mc.player.motionX, 0.0, Jesus.mc.player.motionZ);
            }
            if (Jesus.mc.player.isInWater()) {
                Jesus.mc.player.setSprinting(false);
                MovementUtils.strafe(MovementUtils.getSpeed() + 0.32f);
                KeyBinding.setKeyBindState(Jesus.mc.gameSettings.keyBindJump.getKeyCode(), true);
            }
        }
    }
    
    @EventTarget
    public void onLiquidBB(final EventLiquidSolid event) {
        if (Jesus.mode.currentMode.equals("NCP")) {
            event.setCancelled(true);
        }
        else if (Jesus.mode.currentMode.equalsIgnoreCase("Matrix new")) {
            if (Jesus.mc.player.isInWater()) {
                return;
            }
            if (Jesus.mc.player.posY > event.getPos().getY() + 0.999991) {
                event.setColision(Block.FULL_BLOCK_AABB.expand(0.0, -9.0E-6, 0.0));
            }
        }
    }
    
    private boolean isWater() {
        final BlockPos bp1 = new BlockPos(Jesus.mc.player.posX - 0.5, Jesus.mc.player.posY - 0.5, Jesus.mc.player.posZ - 0.5);
        final BlockPos bp2 = new BlockPos(Jesus.mc.player.posX - 0.5, Jesus.mc.player.posY - 0.5, Jesus.mc.player.posZ + 0.5);
        final BlockPos bp3 = new BlockPos(Jesus.mc.player.posX + 0.5, Jesus.mc.player.posY - 0.5, Jesus.mc.player.posZ + 0.5);
        final BlockPos bp4 = new BlockPos(Jesus.mc.player.posX + 0.5, Jesus.mc.player.posY - 0.5, Jesus.mc.player.posZ - 0.5);
        return (Jesus.mc.player.world.getBlockState(bp1).getBlock() == Blocks.WATER && Jesus.mc.player.world.getBlockState(bp2).getBlock() == Blocks.WATER && Jesus.mc.player.world.getBlockState(bp3).getBlock() == Blocks.WATER && Jesus.mc.player.world.getBlockState(bp4).getBlock() == Blocks.WATER) || (Jesus.mc.player.world.getBlockState(bp1).getBlock() == Blocks.LAVA && Jesus.mc.player.world.getBlockState(bp2).getBlock() == Blocks.LAVA && Jesus.mc.player.world.getBlockState(bp3).getBlock() == Blocks.LAVA && Jesus.mc.player.world.getBlockState(bp4).getBlock() == Blocks.LAVA);
    }
    
    private boolean isFluid(final BlockPos bp) {
        final BlockPos bp2 = new BlockPos(bp.getX() - 0.5, bp.getY() - 0.5, bp.getZ() - 0.5);
        final BlockPos bp3 = new BlockPos(bp.getX() - 0.5, bp.getY() - 0.5, bp.getZ() + 0.5);
        final BlockPos bp4 = new BlockPos(bp.getX() + 0.5, bp.getY() - 0.5, bp.getZ() + 0.5);
        final BlockPos bp5 = new BlockPos(bp.getX() + 0.5, bp.getY() - 0.5, bp.getZ() - 0.5);
        return (Jesus.mc.player.world.getBlockState(bp2).getBlock() == Blocks.WATER && Jesus.mc.player.world.getBlockState(bp3).getBlock() == Blocks.WATER && Jesus.mc.player.world.getBlockState(bp4).getBlock() == Blocks.WATER && Jesus.mc.player.world.getBlockState(bp5).getBlock() == Blocks.WATER) || (Jesus.mc.player.world.getBlockState(bp2).getBlock() == Blocks.LAVA && Jesus.mc.player.world.getBlockState(bp3).getBlock() == Blocks.LAVA && Jesus.mc.player.world.getBlockState(bp4).getBlock() == Blocks.LAVA && Jesus.mc.player.world.getBlockState(bp5).getBlock() == Blocks.LAVA);
    }
    
    @EventTarget
    public void onPreMotion(final EventPreMotion event) {
        this.setSuffix(Jesus.mode.getCurrentMode());
        final BlockPos blockPos = new BlockPos(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY - 0.1, Minecraft.getMinecraft().player.posZ);
        final Block block = Minecraft.getMinecraft().world.getBlockState(blockPos).getBlock();
        if (Jesus.useTimer.getBoolValue()) {
            Jesus.mc.timer.timerSpeed = this.timerSpeed.getNumberValue();
        }
        if (Jesus.mode.currentMode.equalsIgnoreCase("Matrix new")) {
            if (Jesus.mc.world.getBlockState(new BlockPos(Jesus.mc.player.posX, Jesus.mc.player.posY - 0.2, Jesus.mc.player.posZ)).getBlock() == Blocks.WATER) {
                Jesus.mc.player.jumpMovementFactor = 0.0f;
            }
            if (Jesus.mc.world.getBlockState(new BlockPos(Jesus.mc.player.posX, Jesus.mc.player.posY + 0.008, Jesus.mc.player.posZ)).getBlock() == Blocks.WATER && !Jesus.mc.player.onGround) {
                final boolean isUp = Jesus.mc.world.getBlockState(new BlockPos(Jesus.mc.player.posX, Jesus.mc.player.posY + 0.03, Jesus.mc.player.posZ)).getBlock() == Blocks.WATER;
                Jesus.mc.player.jumpMovementFactor = 0.0f;
                final float yport = (MovementUtils.getSpeed() > 0.1) ? 0.02f : 0.032f;
                Jesus.mc.player.setVelocity(Jesus.mc.player.motionX, (Jesus.mc.player.fallDistance < 3.5) ? ((Jesus.mc.player.isCollidedHorizontally && Keyboard.isKeyDown(57)) ? 0.21050000190734863 : (isUp ? yport : (-yport))) : -0.5, Jesus.mc.player.motionZ);
                if ((Jesus.mc.player.posY > (int)Jesus.mc.player.posY + 0.89 && Jesus.mc.player.posY <= (int)Jesus.mc.player.posY + 1) || Jesus.mc.player.fallDistance > 3.5) {
                    Jesus.mc.player.posY = (int)Jesus.mc.player.posY + 1 + 1.0E-45;
                    if (!Jesus.mc.player.isInWater()) {
                        MovementUtils.setMotion(1.0);
                    }
                }
            }
            if (Jesus.mc.player.isInWater() || Jesus.mc.world.getBlockState(new BlockPos(Jesus.mc.player.posX, Jesus.mc.player.posY + 0.15, Jesus.mc.player.posZ)).getBlock() == Blocks.WATER) {
                Jesus.mc.player.motionY = 0.16;
                if (Jesus.mc.world.getBlockState(new BlockPos(Jesus.mc.player.posX, Jesus.mc.player.posY + 2.0, Jesus.mc.player.posZ)).getBlock() == Blocks.AIR) {
                    Jesus.mc.player.motionY = 0.12;
                }
                if (Jesus.mc.world.getBlockState(new BlockPos(Jesus.mc.player.posX, Jesus.mc.player.posY + 0.18, Jesus.mc.player.posZ)).getBlock() == Blocks.AIR) {
                    Jesus.mc.player.motionY = 0.18;
                }
            }
        }
        else if (Jesus.mode.currentMode.equalsIgnoreCase("NCP")) {
            if (block instanceof BlockLiquid && !Minecraft.getMinecraft().player.onGround && Minecraft.getMinecraft().world.getBlockState(new BlockPos(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY, Minecraft.getMinecraft().player.posZ)).getBlock() == Blocks.WATER) {
                Minecraft.getMinecraft().player.motionX = 0.0;
                Minecraft.getMinecraft().player.motionY = 0.035999998450279236;
                Minecraft.getMinecraft().player.motionZ = 0.0;
            }
            if (this.isWater() && block instanceof BlockLiquid) {
                if (Jesus.timerHelper.hasReached(400.0)) {
                    Jesus.mc.player.motionY = 0.12;
                    Jesus.timerHelper.reset();
                }
                Jesus.mc.player.onGround = false;
                Jesus.mc.player.isAirBorne = true;
                MovementUtils.setSpeed(Jesus.NCPSpeed.getNumberValue());
                event.setPosY((Jesus.mc.player.ticksExisted % 2 == 0) ? (event.getPosY() + 0.042) : (event.getPosY() - 0.053));
                event.setOnGround(false);
            }
        }
        else if (Jesus.mode.currentMode.equalsIgnoreCase("Matrix old")) {
            final BlockPos blockPos2 = new BlockPos(Jesus.mc.player.posX, Jesus.mc.player.posY - 0.02, Jesus.mc.player.posZ);
            final Block block2 = Jesus.mc.world.getBlockState(blockPos2).getBlock();
            if (Jesus.mc.world.getBlockState(new BlockPos(Jesus.mc.player.posX, Jesus.mc.player.posY - 0.5, Jesus.mc.player.posZ)).getBlock() == Blocks.WATER && Jesus.mc.player.onGround) {
                Jesus.mc.player.motionY = 0.2;
            }
            if (block2 instanceof BlockLiquid && !Jesus.mc.player.onGround) {
                if (Jesus.mc.world.getBlockState(new BlockPos(Jesus.mc.player.posX, Jesus.mc.player.posY + 0.12, Jesus.mc.player.posZ)).getBlock() == Blocks.WATER) {
                    Jesus.mc.player.motionX = 0.0;
                    Jesus.mc.player.motionY = 0.10000000149011612;
                    Jesus.mc.player.motionZ = 0.0;
                }
                else {
                    MovementUtils.setSpeed(1.100000023841858);
                    Jesus.mc.player.motionY = -0.10000000149011612;
                }
                if (Jesus.mc.player.isCollidedHorizontally) {
                    Jesus.mc.player.motionY = 0.0;
                }
            }
            if (Jesus.mc.world.getBlockState(new BlockPos(Jesus.mc.player.posX, Jesus.mc.player.posY + 0.2, Jesus.mc.player.posZ)).getBlock() instanceof BlockLiquid) {
                Jesus.mc.player.motionY = 0.18;
            }
        }
        else if (Jesus.mode.currentMode.equalsIgnoreCase("Matrix Zoom")) {
            if (Jesus.mc.gameSettings.keyBindForward.pressed && Jesus.mc.world.getBlockState(new BlockPos(Jesus.mc.player.posX, Jesus.mc.player.posY + 0.9999999747378752, Jesus.mc.player.posZ)).getBlock() instanceof BlockLiquid) {
                Jesus.mc.player.motionY = 0.9;
            }
            if (block instanceof BlockLiquid && !Minecraft.getMinecraft().player.onGround) {
                if (Minecraft.getMinecraft().world.getBlockState(new BlockPos(Minecraft.getMinecraft().player.posX, Minecraft.getMinecraft().player.posY, Minecraft.getMinecraft().player.posZ)).getBlock() == Blocks.WATER) {
                    Minecraft.getMinecraft().player.motionY = 0.035999998450279236;
                    Minecraft.getMinecraft().player.motionX = 0.0;
                    Minecraft.getMinecraft().player.motionZ = 0.0;
                }
                else {
                    MovementUtils.setSpeed(Jesus.speed.getNumberValue());
                }
                if (Minecraft.getMinecraft().player.isCollidedHorizontally) {
                    Minecraft.getMinecraft().player.motionY = 0.2;
                }
            }
        }
    }
    
    static {
        Jesus.mode = new ListSetting("Jesus Mode", "Matrix new", () -> true, new String[] { "Matrix new", "Matrix old", "Matrix Zoom", "NCP", "Matrix Last" });
        Jesus.speed = new NumberSetting("Speed", 0.65f, 0.1f, 10.0f, 0.01f, () -> !Jesus.mode.currentMode.equals("NCP") && !Jesus.mode.currentMode.equals("Matrix new") && !Jesus.mode.currentMode.equals("Matrix old"));
        Jesus.NCPSpeed = new NumberSetting("NCP Speed", 0.25f, 0.01f, 0.5f, 0.01f, () -> Jesus.mode.currentMode.equals("NCP"));
        Jesus.useTimer = new BooleanSetting("Use Timer", false, () -> true);
    }
}
