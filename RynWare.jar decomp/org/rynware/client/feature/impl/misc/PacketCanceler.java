// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.misc;

import org.rynware.client.event.EventTarget;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.rynware.client.event.events.impl.packet.EventReceivePacket;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.feature.Feature;

public class PacketCanceler extends Feature
{
    public PacketCanceler() {
        super("PacketCanceler", FeatureCategory.Misc);
    }
    
    @EventTarget
    public void onPacket(final EventReceivePacket e) {
        if (!this.isEnabled()) {
            return;
        }
        if (e.getPacket() instanceof SPacketPlayerPosLook) {
            ((SPacketPlayerPosLook)e.getPacket()).flags.stream().limit(2L);
        }
    }
}
