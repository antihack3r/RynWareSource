// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.player;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.feature.Feature;

public class AACTeleport extends Feature
{
    public AACTeleport() {
        super("Wtf", FeatureCategory.Misc);
    }
    
    @Override
    public void onEnable() {
        AACTeleport.mc.player.isCollided = false;
        AACTeleport.mc.player.noClip = true;
        AACTeleport.mc.timer.timerSpeed = 500.0f;
        AACTeleport.mc.player.connection.sendPacket(new CPacketEntityAction(AACTeleport.mc.player, CPacketEntityAction.Action.START_SPRINTING));
        if (AACTeleport.mc.player.onGround) {
            AACTeleport.mc.player.motionY = 0.30000001192092896;
            AACTeleport.mc.player.connection.sendPacket(new CPacketPlayer(true));
        }
        for (int i = 0; i < 5000; ++i) {
            AACTeleport.mc.player.connection.sendPacket(new CPacketPlayer.Position(1000.0, AACTeleport.mc.player.motionY - 1.0E-10, 1000.0, false));
            AACTeleport.mc.player.connection.sendPacket(new CPacketPlayer.Position(1000.0, 100.0, 1000.0, AACTeleport.mc.player.onGround));
        }
        super.onEnable();
        this.toggle();
    }
    
    @Override
    public void onDisable() {
        AACTeleport.mc.player.setPositionAndUpdate(AACTeleport.mc.player.posX, AACTeleport.mc.player.posY, AACTeleport.mc.player.posZ);
        AACTeleport.mc.timer.timerSpeed = 1.0f;
        AACTeleport.mc.player.noClip = false;
        super.onDisable();
    }
}
