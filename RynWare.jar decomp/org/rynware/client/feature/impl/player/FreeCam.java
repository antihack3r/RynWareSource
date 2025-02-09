// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.player;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import org.rynware.client.utils.movement.MovementUtils;
import org.rynware.client.event.events.impl.player.EventUpdate;
import net.minecraft.client.gui.ScaledResolution;
import org.rynware.client.event.events.impl.render.EventRender2D;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import org.rynware.client.event.EventTarget;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.rynware.client.event.events.impl.packet.EventReceivePacket;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.feature.Feature;

public class FreeCam extends Feature
{
    private final NumberSetting speed;
    private final BooleanSetting reallyWorld;
    double x;
    double y;
    double z;
    
    public FreeCam() {
        super("FreeCam", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u043b\u0435\u0442\u0430\u0442\u044c \u0432 \u0441\u0432\u043e\u0431\u043e\u0434\u043d\u043e\u0439 \u043a\u0430\u043c\u0435\u0440\u0435", FeatureCategory.Player);
        this.speed = new NumberSetting("Flying Speed", 0.5f, 0.1f, 1.0f, 0.1f, () -> true);
        this.reallyWorld = new BooleanSetting("ReallyWorld Bypass", false, () -> true);
        this.addSettings(this.speed, this.reallyWorld);
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket event) {
        if (FreeCam.mc.player.isDead) {
            this.toggle();
        }
        if (event.getPacket() instanceof SPacketPlayerPosLook && ((this.reallyWorld.getBoolValue() && FreeCam.mc.player != null) || FreeCam.mc.world != null)) {
            event.setCancelled(true);
        }
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
        if (FreeCam.mc.player.isDead) {
            this.toggle();
        }
        this.x = FreeCam.mc.player.posX;
        this.y = FreeCam.mc.player.posY;
        this.z = FreeCam.mc.player.posZ;
        final EntityOtherPlayerMP ent = new EntityOtherPlayerMP(FreeCam.mc.world, FreeCam.mc.player.getGameProfile());
        ent.inventory = FreeCam.mc.player.inventory;
        ent.inventoryContainer = FreeCam.mc.player.inventoryContainer;
        ent.setHealth(FreeCam.mc.player.getHealth());
        ent.setPositionAndRotation(this.x, FreeCam.mc.player.getEntityBoundingBox().minY, this.z, FreeCam.mc.player.rotationYaw, FreeCam.mc.player.rotationPitch);
        ent.rotationYawHead = FreeCam.mc.player.rotationYawHead;
        FreeCam.mc.world.addEntityToWorld(-1, ent);
    }
    
    @EventTarget
    public void onScreen(final EventRender2D e) {
        final ScaledResolution sr = new ScaledResolution(FreeCam.mc);
        final String yCoord = "" + Math.round(FreeCam.mc.player.posY - this.y);
        final String str = "Y: " + yCoord;
        FreeCam.mc.rubik_18.drawStringWithOutline(str, (sr.getScaledWidth() - FreeCam.mc.fontRendererObj.getStringWidth(str)) / 1.98, sr.getScaledHeight() / 1.8 - 20.0, -1);
    }
    
    @EventTarget
    public void onPreMotion(final EventUpdate e) {
        if (FreeCam.mc.player == null || FreeCam.mc.world == null) {
            return;
        }
        FreeCam.mc.player.motionY = 0.0;
        if (FreeCam.mc.gameSettings.keyBindJump.pressed) {
            FreeCam.mc.player.motionY = this.speed.getNumberValue();
        }
        if (FreeCam.mc.gameSettings.keyBindSneak.pressed) {
            FreeCam.mc.player.motionY = -this.speed.getNumberValue();
        }
        FreeCam.mc.player.noClip = true;
        MovementUtils.setSpeed(this.speed.getNumberValue());
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        FreeCam.mc.player.setPosition(this.x, this.y, this.z);
        FreeCam.mc.getConnection().sendPacket(new CPacketPlayer.Position(FreeCam.mc.player.posX, FreeCam.mc.player.posY + 0.01, FreeCam.mc.player.posZ, FreeCam.mc.player.onGround));
        FreeCam.mc.player.capabilities.isFlying = false;
        FreeCam.mc.player.noClip = false;
        FreeCam.mc.world.removeEntityFromWorld(-1);
    }
}
