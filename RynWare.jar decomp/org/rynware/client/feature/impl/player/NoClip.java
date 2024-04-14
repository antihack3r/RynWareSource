// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.player;

import org.rynware.client.Main;
import net.minecraft.entity.Entity;
import org.rynware.client.event.EventTarget;
import org.rynware.client.event.events.impl.player.BlockHelper;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import org.rynware.client.utils.math.MathematicHelper;
import net.minecraft.util.math.MathHelper;
import org.rynware.client.utils.movement.MovementUtils;
import org.rynware.client.event.events.impl.player.EventPreMotion;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.feature.Feature;

public class NoClip extends Feature
{
    private boolean falled;
    public static ListSetting noclipkMode;
    
    public NoClip() {
        super("NoClip", "\u041f\u043e\u0437\u0432\u043e\u043b\u044f\u0435\u0442 \u0445\u043e\u0434\u0438\u0442\u044c \u0441\u043a\u0432\u043e\u0437\u044c \u0431\u043b\u043e\u043a\u0438", FeatureCategory.Player);
        this.falled = false;
        this.addSettings(NoClip.noclipkMode);
    }
    
    @EventTarget
    public void onUpdate(final EventPreMotion event) {
        if (NoClip.mc.player != null) {
            NoClip.mc.player.noClip = true;
            if (NoClip.noclipkMode.currentMode.equalsIgnoreCase("Vanilla")) {
                event.setOnGround(true);
                NoClip.mc.player.motionY = -1.0E-5;
                if (NoClip.mc.gameSettings.keyBindJump.isKeyDown()) {
                    NoClip.mc.player.motionY = 0.42;
                }
                if (NoClip.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    event.setOnGround(NoClip.mc.player.onGround = false);
                    NoClip.mc.player.fallDistance = 0.4f;
                    NoClip.mc.player.motionY = -0.30000001192092896;
                    this.falled = true;
                }
                else if (this.falled) {
                    event.setOnGround(NoClip.mc.player.onGround = true);
                    this.falled = false;
                }
            }
            else if (NoClip.noclipkMode.currentMode.equalsIgnoreCase("Matrix")) {
                NoClip.mc.player.motionY = -0.0010000000474974513;
                final double y = NoClip.mc.player.posY;
                if (!NoClip.mc.gameSettings.keyBindForward.isKeyDown()) {
                    return;
                }
                MovementUtils.setMotion(0.0010000000474974513);
                final float yaw = MovementUtils.getAllDirection();
                final double x = NoClip.mc.player.posX + -MathHelper.sin(yaw) * 1.0E-8;
                final double z = NoClip.mc.player.posZ + MathHelper.cos(yaw) * 1.0E-8;
                final double x2 = NoClip.mc.player.posX + -MathHelper.sin(yaw) * 0.85;
                final double z2 = NoClip.mc.player.posZ + MathHelper.cos(yaw) * 0.85;
                NoClip.mc.player.connection.sendPacket(new CPacketConfirmTeleport((int)MathematicHelper.getRandomFloat(4.0f, 1.0f)));
                NoClip.mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, true));
                NoClip.mc.player.connection.sendPacket(new CPacketPlayer.Position(x2, y, z2, true));
                NoClip.mc.player.connection.sendPacket(new CPacketPlayer.Position(NoClip.mc.player.posX, y - 1.0E-10, NoClip.mc.player.posZ, false));
                NoClip.mc.player.connection.sendPacket(new CPacketPlayer.Position(NoClip.mc.player.posX, y, NoClip.mc.player.posZ, true));
            }
            else if (NoClip.noclipkMode.currentMode.equalsIgnoreCase("AAC5")) {
                NoClip.mc.player.motionY = 9.999999747378752E-5;
                if (!BlockHelper.insideBlock()) {
                    NoClip.mc.timer.timerSpeed = 15.0f;
                }
                NoClip.mc.player.onGround = true;
            }
            else if (NoClip.noclipkMode.currentMode.equalsIgnoreCase("Matrix2")) {
                NoClip.mc.player.motionY = 0.0;
                MovementUtils.setMotion(0.10999999940395355);
                if (!NoClip.mc.gameSettings.keyBindForward.isKeyDown()) {
                    return;
                }
                NoClip.mc.player.setPosition(NoClip.mc.player.posX, NoClip.mc.player.posY - 1.0E-10, NoClip.mc.player.posZ);
                NoClip.mc.player.connection.sendPacket(new CPacketPlayer.Position(NoClip.mc.player.posX, NoClip.mc.player.posY + 1.0E-10, NoClip.mc.player.posZ, NoClip.mc.player.onGround));
            }
        }
    }
    
    public static boolean isNoClip(final Entity entity) {
        return (Main.instance.featureManager.getFeature(NoClip.class).isEnabled() && NoClip.mc.player != null && (NoClip.mc.player.ridingEntity == null || entity == NoClip.mc.player.ridingEntity)) || entity.noClip;
    }
    
    @Override
    public void onDisable() {
        NoClip.mc.timer.timerSpeed = 1.0f;
        NoClip.mc.player.noClip = false;
        super.onDisable();
    }
    
    static {
        NoClip.noclipkMode = new ListSetting("Noclip Mode", "Vanilla", () -> true, new String[] { "Vanilla", "AAC5", "Matrix", "Matrix2" });
    }
}
