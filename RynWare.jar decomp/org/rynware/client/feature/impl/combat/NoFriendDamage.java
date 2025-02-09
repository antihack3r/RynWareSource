// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.combat;

import org.rynware.client.event.EventTarget;
import org.rynware.client.Main;
import net.minecraft.network.play.client.CPacketUseEntity;
import org.rynware.client.event.events.impl.packet.EventSendPacket;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.feature.Feature;

public class NoFriendDamage extends Feature
{
    public NoFriendDamage() {
        super("NoFriendDamage", "\u041e\u0442\u043a\u043b\u044e\u0447\u0435\u043d\u043d\u044b\u0439 \u0434\u0430\u043c\u0430\u0433 \u043d\u0430 \u0434\u0440\u0443\u0437\u044c\u044f\u0445", FeatureCategory.Combat);
    }
    
    @EventTarget
    public void onSendPacket(final EventSendPacket event) {
        if (event.getPacket() instanceof CPacketUseEntity) {
            final CPacketUseEntity cpacketUseEntity = (CPacketUseEntity)event.getPacket();
            if (cpacketUseEntity.getAction().equals(CPacketUseEntity.Action.ATTACK) && Main.instance.friendManager.isFriend(NoFriendDamage.mc.objectMouseOver.entityHit.getName())) {
                event.setCancelled(true);
            }
        }
    }
}
