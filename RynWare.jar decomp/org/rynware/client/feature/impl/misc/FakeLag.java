// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.misc;

import net.minecraft.network.play.client.CPacketPlayer;
import org.rynware.client.event.events.impl.packet.EventSendPacket;
import org.rynware.client.event.EventTarget;
import java.util.Iterator;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.utils.Helper;
import org.rynware.client.ui.settings.Setting;
import java.util.ArrayList;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.utils.math.TimerHelper;
import net.minecraft.network.Packet;
import java.util.List;
import java.util.LinkedList;
import org.rynware.client.feature.Feature;

public class FakeLag extends Feature
{
    public final LinkedList<double[]> positions;
    private boolean enableFakeLags;
    public List<Packet<?>> packets;
    public TimerHelper pulseTimer;
    public NumberSetting ticks;
    
    public FakeLag() {
        super("FakeLag", FeatureCategory.Misc);
        this.positions = new LinkedList<double[]>();
        this.packets = new ArrayList<Packet<?>>();
        this.pulseTimer = new TimerHelper();
        this.ticks = new NumberSetting("Ticks", 8.0f, 1.0f, 30.0f, 1.0f, () -> true);
        this.addSettings(this.ticks);
    }
    
    @Override
    public void onEnable() {
        synchronized (this.positions) {
            this.positions.add(new double[] { Helper.mc.player.posX, Helper.mc.player.getEntityBoundingBox().minY + Helper.mc.player.getEyeHeight() / 2.0f, Helper.mc.player.posZ });
            this.positions.add(new double[] { Helper.mc.player.posX, Helper.mc.player.getEntityBoundingBox().minY, Helper.mc.player.posZ });
        }
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        this.packets.clear();
        this.positions.clear();
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        synchronized (this.positions) {
            this.positions.add(new double[] { Helper.mc.player.posX, Helper.mc.player.getEntityBoundingBox().minY, Helper.mc.player.posZ });
        }
        if (this.pulseTimer.hasReached(this.ticks.getNumberValue() * 50.0f)) {
            try {
                this.enableFakeLags = true;
                final Iterator<Packet<?>> packetIterator = this.packets.iterator();
                while (packetIterator.hasNext()) {
                    Helper.mc.player.connection.sendPacket(packetIterator.next());
                    packetIterator.remove();
                }
                this.enableFakeLags = false;
            }
            catch (final Exception e) {
                this.enableFakeLags = false;
            }
            synchronized (this.positions) {
                this.positions.clear();
            }
            this.pulseTimer.reset();
        }
    }
    
    @EventTarget
    public void onSendPacket(final EventSendPacket event) {
        if (Helper.mc.player == null || !(event.getPacket() instanceof CPacketPlayer) || this.enableFakeLags) {
            return;
        }
        event.setCancelled(true);
        if (!(event.getPacket() instanceof CPacketPlayer.Position) && !(event.getPacket() instanceof CPacketPlayer.PositionRotation)) {
            return;
        }
        this.packets.add(event.getPacket());
    }
}
