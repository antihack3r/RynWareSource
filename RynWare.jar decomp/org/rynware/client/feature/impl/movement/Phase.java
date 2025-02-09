// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.movement;

import org.rynware.client.event.EventTarget;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import org.rynware.client.utils.math.MathematicHelper;
import net.minecraft.util.math.MathHelper;
import org.rynware.client.utils.movement.MovementUtils;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.feature.Feature;

public class Phase extends Feature
{
    private final ListSetting phaseMode;
    
    public Phase() {
        super("Phase", FeatureCategory.Movement);
        this.phaseMode = new ListSetting("Phase Mode", "Matrix", () -> true, new String[] { "Matrix", "Spartan" });
        this.addSettings(this.phaseMode);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        if (this.phaseMode.currentMode.equals("Matrix")) {
            if (Phase.mc.player.isCollidedHorizontally) {
                MovementUtils.setSpeed(0.0);
                final float yaw = MovementUtils.getAllDirection();
                final double x = Phase.mc.player.posX + -MathHelper.sin(yaw) * 1.0E-8;
                final double z = Phase.mc.player.posZ + MathHelper.cos(yaw) * 1.0E-8;
                final double x2 = Phase.mc.player.posX + -MathHelper.sin(yaw) * 0.85;
                final double z2 = Phase.mc.player.posZ + MathHelper.cos(yaw) * 0.85;
                final double y = Phase.mc.player.posY;
                Phase.mc.player.connection.sendPacket(new CPacketConfirmTeleport((int)MathematicHelper.getRandomFloat(4.0f, 1.0f)));
                Phase.mc.player.connection.sendPacket(new CPacketPlayer.Position(x, y, z, true));
                Phase.mc.player.connection.sendPacket(new CPacketPlayer.Position(x2, y, z2, true));
            }
        }
        else if (Phase.mc.player.isCollidedHorizontally) {
            MovementUtils.setMotion(0.0);
            Phase.mc.player.noClip = true;
            Phase.mc.player.onGround = false;
            final double x3 = Phase.mc.player.posX + -MathHelper.sin(MovementUtils.getAllDirection()) * 1.0E-8;
            final double z3 = Phase.mc.player.posZ + MathHelper.cos(MovementUtils.getAllDirection()) * 1.0E-8;
            final double x4 = Phase.mc.player.posX + -MathHelper.sin(MovementUtils.getAllDirection()) * 0.85;
            final double z4 = Phase.mc.player.posZ + MathHelper.cos(MovementUtils.getAllDirection()) * 0.85;
            Phase.mc.player.setPositionAndUpdate(x3, Phase.mc.player.posY, z3);
            for (int i = 0; i <= 3; ++i) {
                Phase.mc.player.connection.sendPacket(new CPacketConfirmTeleport(i));
                Phase.mc.player.setPosition(x4, Phase.mc.player.posY, z4);
            }
        }
    }
}
