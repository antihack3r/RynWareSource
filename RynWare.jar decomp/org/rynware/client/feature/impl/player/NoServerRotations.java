// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.player;

import org.rynware.client.event.EventTarget;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.rynware.client.event.events.impl.packet.EventReceivePacket;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.feature.Feature;

public class NoServerRotations extends Feature
{
    public NoServerRotations() {
        super("NoServerRotation", "\u0418\u0433\u043e\u043d\u0440\u0438\u0440\u0443\u0435\u0442 \u0441\u0435\u0440\u0432\u0435\u0440\u043d\u0443\u044e \u0440\u043e\u0442\u0430\u0446\u0438\u044e", FeatureCategory.Player);
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            final SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.getPacket();
            packet.yaw = NoServerRotations.mc.player.rotationYaw;
            packet.pitch = NoServerRotations.mc.player.rotationPitch;
        }
    }
}
