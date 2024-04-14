// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.player;

import java.util.Iterator;
import org.rynware.client.ui.notification.NotificationRenderer;
import org.rynware.client.ui.notification.NotificationMode;
import org.rynware.client.utils.other.ChatUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.event.EventTarget;
import net.minecraft.network.play.server.SPacketPlayerListItem;
import org.rynware.client.event.events.impl.packet.EventReceivePacket;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.feature.Feature;

public class StaffAlert extends Feature
{
    private boolean isJoined;
    
    public StaffAlert() {
        super("StaffAlert", FeatureCategory.Player);
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket event) {
        final SPacketPlayerListItem packetPlayInPlayerListItem;
        if (event.getPacket() instanceof SPacketPlayerListItem && (packetPlayInPlayerListItem = (SPacketPlayerListItem)event.getPacket()).getAction() == SPacketPlayerListItem.Action.UPDATE_LATENCY) {
            this.isJoined = true;
        }
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        for (final EntityPlayer staffPlayer : GuiPlayerTabOverlay.getPlayers()) {
            if (staffPlayer != null && staffPlayer != StaffAlert.mc.player && (staffPlayer.getDisplayName().getUnformattedText().contains("HELPER") || staffPlayer.getDisplayName().getUnformattedText().contains("ST.HELPER") || staffPlayer.getDisplayName().getUnformattedText().contains("MODER") || staffPlayer.getDisplayName().getUnformattedText().contains("ST.MODER") || staffPlayer.getDisplayName().getUnformattedText().contains("ADMIN") || staffPlayer.getDisplayName().getUnformattedText().contains("\u043f\u0457\u0405\u043f\u0457\u0405\u043f\u0457\u0405\u043f\u0457\u0405\u043f\u0457\u0405") || staffPlayer.getDisplayName().getUnformattedText().contains("\u043f\u0457\u0405\u043f\u0457\u0405\u043f\u0457\u0405\u043f\u0457\u0405\u043f\u0457\u0405\u043f\u0457\u0405") || staffPlayer.getDisplayName().getUnformattedText().contains("\u043f\u0457\u0405\u043f\u0457\u0405\u043f\u0457\u0405\u043f\u0457\u0405\u043f\u0457\u0405")) && staffPlayer.ticksExisted < 10) {
                if (!this.isJoined) {
                    continue;
                }
                ChatUtils.addChatMessage(ChatFormatting.WHITE + "\u0410\u0434\u043c\u0438\u043d\u0438\u0441\u0442\u0440\u0430\u0442\u043e\u0440 " + ChatFormatting.RESET + staffPlayer.getDisplayName().getUnformattedText() + ChatFormatting.WHITE + " \u0417\u0430\u0448\u0435\u043b \u043d\u0430 \u0441\u0435\u0440\u0432\u0435\u0440 / \u0412\u044b\u0448\u0435\u043b \u0438\u0437 \u0432\u0430\u043d\u0438\u0448\u0430");
                NotificationRenderer.queue("Staff Alert", ChatFormatting.WHITE + "\u0410\u0434\u043c\u0438\u043d\u0438\u0441\u0442\u0440\u0430\u0442\u043e\u0440 " + ChatFormatting.RESET + staffPlayer.getDisplayName().getUnformattedText() + ChatFormatting.WHITE + " \u0417\u0430\u0448\u0435\u043b \u043d\u0430 \u0441\u0435\u0440\u0432\u0435\u0440 / \u0412\u044b\u0448\u0435\u043b \u0438\u0437 \u0432\u0430\u043d\u0438\u0448\u0430", 5, NotificationMode.WARNING);
                this.isJoined = false;
            }
        }
    }
}
