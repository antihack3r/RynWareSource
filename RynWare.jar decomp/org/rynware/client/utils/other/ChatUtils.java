// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.utils.other;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import org.rynware.client.utils.Helper;

public class ChatUtils implements Helper
{
    public static String chatPrefix;
    
    public static void addChatMessage(final String message) {
        ChatUtils.mc.player.addChatMessage(new TextComponentString(ChatUtils.chatPrefix + message));
    }
    
    static {
        ChatUtils.chatPrefix = TextFormatting.RED + "§7[" + TextFormatting.GRAY + "RynWare" + TextFormatting.RED + "§7] -> " + ChatFormatting.RESET;
    }
}
