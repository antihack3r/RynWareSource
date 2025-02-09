// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.misc;

import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketKeepAlive;
import org.rynware.client.event.events.impl.packet.EventSendPacket;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.event.EventTarget;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import java.util.Random;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import org.rynware.client.event.events.impl.player.EventPreMotion;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.utils.math.TimerHelper;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.MultipleBoolSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.feature.Feature;

public class Disabler extends Feature
{
    public ListSetting disablerMode;
    public static MultipleBoolSetting disablers;
    public ListSetting packetMoving;
    public ListSetting packetGround;
    public ListSetting pGround;
    public NumberSetting packetY;
    public NumberSetting timerSpeed;
    public NumberSetting strafeSpeed;
    public NumberSetting inputSpeed;
    public BooleanSetting inputJumping;
    public BooleanSetting inputSneaking;
    public BooleanSetting sendInput;
    public BooleanSetting stormGround;
    public TimerHelper timerHelper;
    private int tsTicks;
    
    public Disabler() {
        super("Disabler", FeatureCategory.Misc);
        this.disablerMode = new ListSetting("Disabler Mode", "Movement", () -> true, new String[] { "Movement" });
        this.packetMoving = new ListSetting("CPlayer moving", "False", () -> Disabler.disablers.getSetting("CPlayer moving").getBoolValue(), new String[] { "False", "True" });
        this.packetGround = new ListSetting("CPlayer ground", "False", () -> Disabler.disablers.getSetting("CPacketPlayer send").getBoolValue(), new String[] { "False", "True" });
        this.pGround = new ListSetting("RealGround", "False", () -> Disabler.disablers.getSetting("Player's Ground").getBoolValue(), new String[] { "False", "True" });
        this.packetY = new NumberSetting("CPacket Y", 0.01f, -1.0f, 1.0f, 0.01f, () -> Disabler.disablers.getSetting("CPlayer y").getBoolValue());
        this.timerSpeed = new NumberSetting("TimerSpeed", 0.6f, 0.01f, 15.0f, 0.1f, () -> Disabler.disablers.getSetting("TimerUse").getBoolValue());
        this.strafeSpeed = new NumberSetting("Input Strafe", 1.0f, 0.0f, 10.0f, 0.1f, () -> Disabler.disablers.getSetting("Custom CInput").getBoolValue());
        this.inputSpeed = new NumberSetting("Input Speed", 1.0f, 0.0f, 10.0f, 0.1f, () -> Disabler.disablers.getSetting("Custom CInput").getBoolValue());
        this.inputJumping = new BooleanSetting("Input Jumping", true, () -> Disabler.disablers.getSetting("Custom CInput").getBoolValue());
        this.inputSneaking = new BooleanSetting("Input Sneaking", true, () -> Disabler.disablers.getSetting("Custom CInput").getBoolValue());
        this.sendInput = new BooleanSetting("Input Send", false, () -> Disabler.disablers.getSetting("Custom CInput").getBoolValue());
        this.stormGround = new BooleanSetting("Storm Ground", false, () -> Disabler.disablers.getSetting("Old Storm").getBoolValue());
        this.timerHelper = new TimerHelper();
        this.tsTicks = 25565;
        this.addSettings(this.disablerMode, Disabler.disablers, this.packetMoving, this.packetGround, this.packetY, this.pGround, this.stormGround, this.timerSpeed, this.strafeSpeed, this.inputSpeed, this.inputJumping, this.inputSneaking, this.sendInput);
    }
    
    @EventTarget
    public void onPreMotion(final EventPreMotion e) {
        if (Disabler.disablers.getSetting("CTransaction send").getBoolValue()) {
            Disabler.mc.player.connection.sendPacket(new CPacketConfirmTransaction(this.tsTicks, (short)0, true));
            --this.tsTicks;
        }
        if (Disabler.disablers.getSetting("CPacketPlayer send").getBoolValue()) {
            Disabler.mc.player.connection.sendPacket(new CPacketPlayer(this.packetGround.currentMode.equals("True")));
        }
        if (Disabler.disablers.getSetting("Player's Ground").getBoolValue()) {
            Disabler.mc.player.onGround = this.pGround.currentMode.equalsIgnoreCase("True");
        }
        if (Disabler.disablers.getSetting("Old Storm").getBoolValue()) {
            e.setOnGround(this.stormGround.getBoolValue());
        }
        if (Disabler.disablers.getSetting("CTeleport send").getBoolValue()) {
            Disabler.mc.player.connection.sendPacket(new CPacketConfirmTeleport(new Random().nextInt(4)));
        }
        if (this.sendInput.getBoolValue()) {
            Disabler.mc.player.connection.sendPacket(new CPacketInput(this.strafeSpeed.getNumberValue(), this.inputSpeed.getNumberValue(), this.inputJumping.getBoolValue(), this.inputSneaking.getBoolValue()));
        }
    }
    
    @EventTarget
    public void onUpd(final EventUpdate e) {
        if (Disabler.disablers.getSetting("TimerUse").getBoolValue()) {
            Disabler.mc.timer.timerSpeed = this.timerSpeed.getNumberValue();
        }
    }
    
    @EventTarget
    public void onUpdate(final EventSendPacket e) {
        if (e.getPacket() instanceof CPacketPlayer) {
            if (Disabler.disablers.getSetting("CPlayer moving").getBoolValue()) {
                ((CPacketPlayer)e.getPacket()).moving = this.packetMoving.currentMode.equals("True");
            }
            if (Disabler.disablers.getSetting("CPlayer y").getBoolValue()) {
                final CPacketPlayer cPacketPlayer = (CPacketPlayer)e.getPacket();
                cPacketPlayer.y += this.packetY.getNumberValue();
            }
        }
        if (e.getPacket() instanceof CPacketKeepAlive && Disabler.disablers.getSetting("CKeep cancel").getBoolValue()) {
            e.setCancelled(true);
        }
        if (e.getPacket() instanceof CPacketEntityAction && Disabler.disablers.getSetting("CEA Cancel").getBoolValue()) {
            e.setCancelled(true);
        }
        if (e.getPacket() instanceof CPacketInput) {
            if (Disabler.disablers.getSetting("Custom CInput").getBoolValue()) {
                ((CPacketInput)e.getPacket()).strafeSpeed = this.strafeSpeed.getNumberValue();
                ((CPacketInput)e.getPacket()).field_192621_b = this.inputSpeed.getNumberValue();
                ((CPacketInput)e.getPacket()).jumping = this.inputJumping.getBoolValue();
                ((CPacketInput)e.getPacket()).sneaking = this.inputSneaking.getBoolValue();
            }
            else if (Disabler.disablers.getSetting("CInput Cancel").getBoolValue()) {
                e.setCancelled(true);
            }
        }
    }
    
    @Override
    public void onDisable() {
        Disabler.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
    
    static {
        Disabler.disablers = new MultipleBoolSetting("DisablerComponent", new BooleanSetting[] { new BooleanSetting("CKeep cancel", true), new BooleanSetting("CTransaction send", false), new BooleanSetting("TimerUse", false), new BooleanSetting("CPacketPlayer send", false), new BooleanSetting("CPacketPlayer cancel", false), new BooleanSetting("CPlayer non-ground", false), new BooleanSetting("Old Storm", false), new BooleanSetting("CPlayer y", false), new BooleanSetting("CPlayer moving", false), new BooleanSetting("CPacketClientStatus send", false), new BooleanSetting("Player's Ground", false), new BooleanSetting("CEA Cancel", false), new BooleanSetting("Custom CInput", false), new BooleanSetting("CInput Cancel", false), new BooleanSetting("CTeleport send", false) });
    }
}
