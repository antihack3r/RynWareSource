// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.player;

import org.rynware.client.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import org.rynware.client.event.events.impl.player.EventPreMotion;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.feature.Feature;

public class NoFall extends Feature
{
    public static ListSetting noFallMode;
    
    public NoFall() {
        super("NoFall", "\u0423\u0431\u0438\u0440\u0430\u0435\u0442 \u0443\u0440\u043e\u043d \u043e\u0442 \u043f\u0430\u0434\u0435\u043d\u0438\u044f", FeatureCategory.Player);
        this.addSettings(NoFall.noFallMode);
    }
    
    @EventTarget
    public void onPreMotion(final EventPreMotion event) {
        final String mode = NoFall.noFallMode.getOptions();
        this.setSuffix(mode);
        if (mode.equalsIgnoreCase("Vanilla")) {
            if (NoFall.mc.player.fallDistance > 3.0f) {
                event.setOnGround(true);
                NoFall.mc.player.connection.sendPacket(new CPacketPlayer(true));
            }
        }
        else if (mode.equalsIgnoreCase("Matrix") && NoFall.mc.player.fallDistance >= 2.0f) {
            NoFall.mc.timer.timerSpeed = 0.01f;
            NoFall.mc.player.connection.sendPacket(new CPacketPlayer.Position(NoFall.mc.player.posX, NoFall.mc.player.posY, NoFall.mc.player.posZ, false));
            NoFall.mc.player.connection.sendPacket(new CPacketPlayer.Position(NoFall.mc.player.posX, NoFall.mc.player.posY, NoFall.mc.player.posZ, true));
            NoFall.mc.timer.timerSpeed = 1.0f;
            NoFall.mc.player.fallDistance = 0.0f;
        }
    }
    
    static {
        NoFall.noFallMode = new ListSetting("NoFall Mode", "Vanilla", () -> true, new String[] { "Vanilla", "Matrix" });
    }
}
