// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.command.impl;

import org.rynware.client.utils.other.ChatUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import org.rynware.client.command.CommandAbstract;

public class HelpCommand extends CommandAbstract
{
    public HelpCommand() {
        super("help", "help", ".help", new String[] { "help" });
    }
    
    @Override
    public void execute(final String... args) {
        if (args.length == 1) {
            if (args[0].equals("help")) {
                ChatUtils.addChatMessage(ChatFormatting.RED + "All Commands:");
                ChatUtils.addChatMessage(ChatFormatting.WHITE + ".bind");
                ChatUtils.addChatMessage(ChatFormatting.WHITE + ".macro");
                ChatUtils.addChatMessage(ChatFormatting.WHITE + ".gps");
                ChatUtils.addChatMessage(ChatFormatting.WHITE + ".parser");
                ChatUtils.addChatMessage(ChatFormatting.WHITE + ".vclip | .hclip");
                ChatUtils.addChatMessage(ChatFormatting.WHITE + ".fakename");
                ChatUtils.addChatMessage(ChatFormatting.WHITE + ".friend");
                ChatUtils.addChatMessage(ChatFormatting.WHITE + ".cfg");
            }
        }
        else {
            ChatUtils.addChatMessage(this.getUsage());
        }
    }
}
