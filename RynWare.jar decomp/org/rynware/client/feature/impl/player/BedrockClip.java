// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.player;

import org.rynware.client.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.feature.Feature;

public class BedrockClip extends Feature
{
    public BedrockClip() {
        super("BedrockClip", "\u0422\u0435\u043b\u0435\u043f\u043e\u0440\u0442\u0438\u0440\u0443\u0435\u0442 \u0432\u0430\u0441 \u043f\u043e\u0434 \u0431\u0435\u0434\u0440\u043e\u043a", FeatureCategory.Movement);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (BedrockClip.mc.player.isInWater() || BedrockClip.mc.player.hurtTime > 0) {
            for (int i = 0; i < 10; ++i) {
                BedrockClip.mc.player.connection.sendPacket(new CPacketPlayer(false));
            }
            for (int i = 0; i < 10; ++i) {
                BedrockClip.mc.player.connection.sendPacket(new CPacketPlayer.Position(BedrockClip.mc.player.posX, BedrockClip.mc.player.posY - (BedrockClip.mc.player.posY + 2.0), BedrockClip.mc.player.posZ, true));
            }
            BedrockClip.mc.player.setPosition(BedrockClip.mc.player.posX, BedrockClip.mc.player.posY - (BedrockClip.mc.player.posY + 2.0), BedrockClip.mc.player.posZ);
            this.toggle();
        }
    }
}
