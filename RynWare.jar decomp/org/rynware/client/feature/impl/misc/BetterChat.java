// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.misc;

import org.rynware.client.event.EventTarget;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.network.Packet;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.ChatType;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.network.play.server.SPacketCloseWindow;
import org.rynware.client.event.events.impl.packet.EventReceivePacket;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.feature.Feature;

public class BetterChat extends Feature
{
    private String lastMessage;
    private int amount;
    private int line;
    
    public BetterChat() {
        super("BetterChat", "\u0423\u0431\u0438\u0440\u0430\u0435\u0442 \u0441\u043f\u0430\u043c", FeatureCategory.Misc);
        this.lastMessage = "";
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket event) {
        final Packet<?> packet = event.getPacket();
        if (packet instanceof SPacketCloseWindow) {
            if (BetterChat.mc.currentScreen instanceof GuiChat) {
                event.setCancelled(true);
            }
        }
        else {
            final SPacketChat sPacketChat;
            if (packet instanceof SPacketChat && (sPacketChat = (SPacketChat)packet).getChatType() == ChatType.CHAT) {
                final ITextComponent message = sPacketChat.getChatComponent();
                final String rawMessage = message.getFormattedText();
                final GuiNewChat chatGui = BetterChat.mc.ingameGUI.getChatGUI();
                if (this.lastMessage.equals(rawMessage)) {
                    chatGui.deleteChatLine(this.line);
                    ++this.amount;
                    sPacketChat.getChatComponent().appendText(TextFormatting.GRAY + " [x" + this.amount + "]");
                }
                else {
                    this.amount = 1;
                }
                ++this.line;
                this.lastMessage = rawMessage;
                chatGui.printChatMessageWithOptionalDeletion(message, this.line);
                if (this.line > 256) {
                    this.line = 0;
                }
                event.setCancelled(true);
            }
        }
    }
}
