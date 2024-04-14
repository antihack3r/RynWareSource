// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.movement;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.init.Items;
import org.rynware.client.event.events.impl.player.EventPreMotion;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.utils.movement.MovementUtils;
import org.rynware.client.event.events.impl.packet.EventSendPacket;
import org.rynware.client.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.rynware.client.event.events.impl.packet.EventReceivePacket;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.utils.math.TimerHelper;
import org.rynware.client.feature.Feature;

public class Flight extends Feature
{
    public boolean damage;
    public boolean flaging;
    public TimerHelper timerHelper;
    public static ListSetting flyMode;
    public final NumberSetting speed;
    public final NumberSetting motionY;
    public final NumberSetting ftspeed;
    public final NumberSetting ftmotion;
    private static final ListSetting clipMode;
    private static final NumberSetting yclip;
    private static final NumberSetting customClip;
    private static final NumberSetting customBlink;
    private final TimerHelper timer;
    private final TimerHelper timer3;
    private final TimerHelper timer4;
    private final TimerHelper timer5;
    private final TimerHelper timer2;
    private float blinkTime;
    private float clipTime;
    private float clipTimes;
    private boolean disableLogger;
    private boolean shouldClip;
    private boolean hasWarned;
    public float ticks;
    public int jumpTicks;
    
    public Flight() {
        super("Flight", "\u0424\u043b\u0430\u0439 \u0431\u0435\u0437 \u043a\u0440\u0435\u0430\u0442\u0438\u0432\u0430??", FeatureCategory.Movement);
        this.damage = false;
        this.flaging = false;
        this.timerHelper = new TimerHelper();
        this.speed = new NumberSetting("Flight Speed", 5.0f, 0.1f, 15.0f, 0.1f, () -> Flight.flyMode.currentMode.equals("Vanilla") || Flight.flyMode.currentMode.equals("Matrix Rtp"));
        this.motionY = new NumberSetting("Motion Y", 0.05f, 0.01f, 0.1f, 0.01f, () -> Flight.flyMode.currentMode.equals("Matrix Elytra"));
        this.ftspeed = new NumberSetting("Funtime Speed", 2.0f, 0.1f, 2.0f, 0.2f, () -> Flight.flyMode.currentMode.equalsIgnoreCase("Funtime"));
        this.ftmotion = new NumberSetting("Funtime Motion", 2.0f, 0.1f, 30.0f, 0.07f, () -> Flight.flyMode.currentMode.equalsIgnoreCase("Funtime"));
        this.timer = new TimerHelper();
        this.timer3 = new TimerHelper();
        this.timer4 = new TimerHelper();
        this.timer5 = new TimerHelper();
        this.timer2 = new TimerHelper();
        this.blinkTime = 0.0f;
        this.clipTime = 0.0f;
        this.clipTimes = 0.0f;
        this.disableLogger = false;
        this.shouldClip = true;
        this.hasWarned = false;
        this.ticks = 0.0f;
        this.jumpTicks = 0;
        this.addSettings(Flight.flyMode, this.motionY, this.speed, this.ftspeed, this.ftmotion, Flight.clipMode, Flight.yclip, Flight.customClip, Flight.customBlink);
    }
    
    @Override
    public void onEnable() {
        this.timer.reset();
        this.timer2.reset();
        this.hasWarned = false;
        this.clipTimes = 0.0f;
        this.shouldClip = true;
        super.onEnable();
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket event) {
        this.flaging = false;
        if (this.isEnabled() && event.getPacket() instanceof SPacketPlayerPosLook) {
            this.flaging = true;
        }
        if (Flight.flyMode.currentMode.equalsIgnoreCase("Matrix")) {
            final Packet<?> packet = event.getPacket();
            if (Flight.mc.player == null || this.disableLogger) {
                return;
            }
            if (packet instanceof CPacketPlayer) {
                event.setCancelled(true);
            }
            if (packet instanceof CPacketPlayer.Position || packet instanceof CPacketPlayer.PositionRotation || packet instanceof CPacketPlayerTryUseItemOnBlock || packet instanceof CPacketAnimation || packet instanceof CPacketEntityAction || packet instanceof CPacketUseEntity) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventTarget
    public void onSendPax(final EventSendPacket e) {
        if (Flight.flyMode.currentMode.equalsIgnoreCase("Spartan")) {
            if (e.getPacket() instanceof CPacketPlayer) {
                final CPacketPlayer cp = (CPacketPlayer)e.getPacket();
                if (Flight.mc.gameSettings.keyBindJump.isKeyDown() && Flight.mc.player.ticksExisted % 8 == 0) {
                    cp.onGround = true;
                    Flight.mc.player.connection.sendPacket(new CPacketPlayer.Position(Flight.mc.player.posX, cp.y - 1.0, Flight.mc.player.posZ, false));
                    Flight.mc.player.setPositionAndUpdate(Flight.mc.player.posX, Flight.mc.player.prevPosY + 1.0, Flight.mc.player.posZ);
                    Flight.mc.player.jump();
                }
            }
            if (Flight.mc.player.motionY >= -0.1 && Flight.mc.player.motionY <= 0.1) {
                Flight.mc.player.fallDistance = 0.0f;
                MovementUtils.setMotion(0.0123);
            }
        }
    }
    
    @EventTarget
    public void onUpda(final EventUpdate e) {
        if (Flight.flyMode.currentMode.equalsIgnoreCase("Matrix")) {
            if (!this.shouldClip) {
                return;
            }
            final String lowerCase = Flight.clipMode.getCurrentMode().toLowerCase();
            switch (lowerCase) {
                case "1": {
                    this.blinkTime = 736.0f;
                    this.clipTime = 909.0f;
                }
                case "2":
                    Label_0251: {
                        this.blinkTime = 1000.0f;
                        this.clipTime = 909.0f;
                        if (this.clipTimes == 2.0f) {
                            if (this.timer2.hasReached(350.0)) {
                                this.shouldClip = false;
                                try {
                                    this.disableLogger = true;
                                    this.disableLogger = false;
                                }
                                catch (final Exception ex) {
                                    this.disableLogger = false;
                                }
                            }
                            break Label_0251;
                        }
                        else {
                            if (this.clipTimes > 2.0f) {
                                this.toggle();
                            }
                            break Label_0251;
                        }
                        break;
                    }
                case "3": {
                    this.blinkTime = 909.0f;
                    this.clipTime = 1000.0f;
                }
                case "custom": {
                    this.blinkTime = Flight.customBlink.getNumberValue();
                    this.clipTime = Flight.customClip.getNumberValue();
                    break;
                }
            }
            Flight.mc.player.motionY = 0.0;
            Flight.mc.player.motionX = 0.0;
            Flight.mc.player.motionZ = 0.0;
            if (this.timer.hasReached((double)(long)this.blinkTime)) {
                this.timer.reset();
                try {
                    this.disableLogger = true;
                    this.disableLogger = false;
                }
                catch (final Exception ex2) {
                    this.disableLogger = false;
                }
            }
            final int oldPos = (int)Flight.mc.player.posY;
            if (Flight.clipMode.getCurrentMode().equalsIgnoreCase("Custom")) {
                if (this.timer3.hasReached((double)(long)(this.clipTime - 50.0f))) {
                    this.timer3.reset();
                    Flight.mc.player.setPosition(Flight.mc.player.posX, oldPos + 1, Flight.mc.player.posZ);
                }
                if (this.timer4.hasReached((double)(long)(this.clipTime - 40.0f))) {
                    this.timer4.reset();
                    Flight.mc.player.setPosition(Flight.mc.player.posX, oldPos + 2, Flight.mc.player.posZ);
                }
                if (this.timer5.hasReached((double)(long)(this.clipTime - 30.0f))) {
                    this.timer5.reset();
                    Flight.mc.player.setPosition(Flight.mc.player.posX, oldPos + 1, Flight.mc.player.posZ);
                }
                if (this.timer5.hasReached((double)(long)(this.clipTime - 20.0f))) {
                    this.timer5.reset();
                    Flight.mc.player.setPosition(Flight.mc.player.posX, oldPos - 1, Flight.mc.player.posZ);
                }
                if (this.timer5.hasReached((double)(long)(this.clipTime - 10.0f))) {
                    this.timer5.reset();
                    Flight.mc.player.setPosition(Flight.mc.player.posX, oldPos + 1000, Flight.mc.player.posZ);
                }
            }
            if (this.timer2.hasReached((double)(long)this.clipTime)) {
                this.timer2.reset();
                ++this.clipTimes;
                Flight.mc.player.setPosition(Flight.mc.player.posX, Flight.mc.player.posY + Flight.yclip.getNumberValue(), Flight.mc.player.posZ);
            }
        }
    }
    
    @EventTarget
    public void onPreUpdate(final EventPreMotion event) {
        final String mode = Flight.flyMode.getOptions();
        this.setSuffix("" + mode);
        if (mode.equalsIgnoreCase("Matrix Elytra")) {
            int eIndex = -1;
            for (int i = 0; i < 45; ++i) {
                if (Flight.mc.player.inventory.getStackInSlot(i).getItem() == Items.ELYTRA && eIndex == -1) {
                    eIndex = i;
                }
            }
            Flight.mc.player.motionY = 0.37;
            if (Flight.mc.player.ticksExisted % 4 == 0) {
                Flight.mc.playerController.windowClick(0, eIndex, 1, ClickType.PICKUP, Flight.mc.player);
                Flight.mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, Flight.mc.player);
                Flight.mc.player.connection.sendPacket(new CPacketEntityAction(Flight.mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                Flight.mc.playerController.windowClick(0, 6, 1, ClickType.PICKUP, Flight.mc.player);
                Flight.mc.playerController.windowClick(0, eIndex, 1, ClickType.PICKUP, Flight.mc.player);
            }
            Flight.mc.player.jumpMovementFactor = 0.115f;
            if (this.flaging) {
                Flight.mc.player.jumpMovementFactor = 0.0f;
                Flight.mc.player.motionY = -0.5;
            }
        }
        else if (mode.equalsIgnoreCase("Matrix Rtp")) {
            if (Flight.mc.player.onGround) {
                Flight.mc.player.jump();
                this.timerHelper.reset();
            }
            else if (!Flight.mc.player.onGround && this.timerHelper.hasReached(280.0)) {
                Flight.mc.player.motionY = -0.04;
                MovementUtils.setSpeed(this.speed.getNumberValue());
            }
        }
        else if (mode.equalsIgnoreCase("Vanilla")) {
            Flight.mc.player.capabilities.isFlying = true;
            Flight.mc.player.capabilities.allowFlying = true;
            if (Flight.mc.gameSettings.keyBindJump.pressed) {
                Flight.mc.player.motionY = 2.0;
            }
            if (Flight.mc.gameSettings.keyBindSneak.pressed) {
                Flight.mc.player.motionY = -2.0;
            }
            MovementUtils.setSpeed(this.speed.getNumberValue());
        }
        else if (mode.equalsIgnoreCase("Matrix Pearl")) {
            if (Flight.mc.player.hurtTime > 0) {
                final EntityPlayerSP player = Flight.mc.player;
                player.motionY += 0.13;
                if (Flight.mc.gameSettings.keyBindForward.pressed && !Flight.mc.player.onGround) {
                    final EntityPlayerSP player2 = Flight.mc.player;
                    player2.motionX -= MathHelper.sin((float)Math.toRadians(Flight.mc.player.rotationYaw));
                    final EntityPlayerSP player3 = Flight.mc.player;
                    player3.motionZ += MathHelper.cos((float)Math.toRadians(Flight.mc.player.rotationYaw));
                }
            }
        }
        else if (mode.equalsIgnoreCase("Matrix Web")) {
            if (Flight.mc.player.isInWeb) {
                Flight.mc.player.isInWeb = false;
                final EntityPlayerSP player4 = Flight.mc.player;
                player4.motionY *= ((Flight.mc.player.ticksExisted % 2 == 0) ? -100.0 : -0.05);
            }
        }
        else if (mode.equalsIgnoreCase("Matrix Fall")) {
            if (this.damage && Flight.mc.player.fallDistance > 0.0f && Flight.mc.player.ticksExisted % 4 == 0) {
                Flight.mc.player.motionY = -0.003;
            }
            if (Flight.mc.player.fallDistance >= 3.0f) {
                this.damage = true;
                Flight.mc.timer.timerSpeed = 2.0f;
                Flight.mc.player.motionY = 0.0;
                Flight.mc.player.connection.sendPacket(new CPacketPlayer.Position(Flight.mc.player.posX, Flight.mc.player.posY, Flight.mc.player.posZ, false));
                Flight.mc.player.connection.sendPacket(new CPacketPlayer.Position(Flight.mc.player.posX, Flight.mc.player.posY, Flight.mc.player.posZ, true));
                Flight.mc.player.motionY = -0.003;
                Flight.mc.timer.timerSpeed = 1.0f;
                Flight.mc.player.fallDistance = 0.0f;
            }
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        Flight.mc.player.connection.sendPacket(new CPacketPlayer(Flight.mc.player.onGround));
        Flight.mc.player.capabilities.isFlying = false;
        Flight.mc.player.capabilities.allowFlying = false;
        Flight.mc.timer.timerSpeed = 1.0f;
        this.jumpTicks = 0;
    }
    
    static {
        Flight.flyMode = new ListSetting("Flight Mode", "Matrix Rtp", () -> true, new String[] { "Vanilla", "Matrix Fall", "Matrix Elytra", "Matrix Rtp", "Matrix Pearl", "Matrix Web", "Funtime", "Matrix", "Spartan" });
        clipMode = new ListSetting("BoostMode", "1", () -> Flight.flyMode.currentMode.equalsIgnoreCase("Matrix"), new String[] { "1", "2", "3", "custom" });
        yclip = new NumberSetting("Y Boost", 10.0f, 5.0f, 20.0f, 0.5f, () -> Flight.flyMode.currentMode.equalsIgnoreCase("Matrix"));
        customClip = new NumberSetting("Ymotion", 750.0f, 1.0f, 1500.0f, 10.0f, () -> Flight.flyMode.currentMode.equalsIgnoreCase("Matrix"));
        customBlink = new NumberSetting("BlinkDelay", 900.0f, 1.0f, 1500.0f, 10.0f, () -> Flight.flyMode.currentMode.equalsIgnoreCase("Matrix"));
    }
}
