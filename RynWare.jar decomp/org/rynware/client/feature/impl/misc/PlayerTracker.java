// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.misc;

import org.rynware.client.ui.notification.NotificationRenderer;
import org.rynware.client.ui.notification.NotificationMode;
import org.rynware.client.utils.other.ChatUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.network.play.server.SPacketBlockChange;
import org.rynware.client.event.events.impl.packet.EventReceivePacket;
import org.rynware.client.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.rynware.client.utils.math.MathematicHelper;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import org.rynware.client.event.events.impl.player.EventPreMotion;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.feature.Feature;

public class PlayerTracker extends Feature
{
    public NumberSetting radius;
    
    public PlayerTracker() {
        super("PlayerTracker", "\u041d\u0430\u0445\u043e\u0434\u0438\u0442 \u0438\u0433\u0440\u043e\u043a\u043e\u0432.", FeatureCategory.Misc);
        this.radius = new NumberSetting("Radius", 1000.0f, 100.0f, 5000.0f, 10.0f, () -> true);
        this.addSettings(this.radius);
    }
    
    @EventTarget
    public void onFind(final EventPreMotion eventPreMotionUpdate) {
        PlayerTracker.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, new BlockPos(MathematicHelper.getRandomInRange(-this.radius.getNumberValue(), this.radius.getNumberValue()), 0.0, MathematicHelper.getRandomInRange(-this.radius.getNumberValue(), this.radius.getNumberValue())), EnumFacing.DOWN));
    }
    
    @EventTarget
    public void onFindReceive(final EventReceivePacket eventReceivePacket) {
        final SPacketBlockChange packetBlockChange = (SPacketBlockChange)eventReceivePacket.getPacket();
        if (eventReceivePacket.getPacket() instanceof SPacketBlockChange) {
            ChatUtils.addChatMessage(TextFormatting.WHITE + " " + TextFormatting.RED + packetBlockChange.getBlockPosition().getX() + " " + packetBlockChange.getBlockPosition().getZ());
            NotificationRenderer.queue("Player Tracker", " " + TextFormatting.RED + packetBlockChange.getBlockPosition().getX() + " " + packetBlockChange.getBlockPosition().getZ(), 2, NotificationMode.INFO);
        }
    }
}
