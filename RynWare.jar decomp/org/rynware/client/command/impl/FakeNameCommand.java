// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.command.impl;

import org.rynware.client.ui.notification.NotificationRenderer;
import org.rynware.client.ui.notification.NotificationMode;
import org.rynware.client.utils.other.ChatUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import org.rynware.client.command.CommandAbstract;

public class FakeNameCommand extends CommandAbstract
{
    public static String oldName;
    public static String currentName;
    public static boolean canChange;
    
    public FakeNameCommand() {
        super("fakename", "fakename", "\u0412§6.fakename" + ChatFormatting.WHITE + " set \u0412§3<name> |" + ChatFormatting.WHITE + " reset", new String[] { "\u0412§6.fakename" + ChatFormatting.WHITE + " set \u0412§3<name> |" + ChatFormatting.WHITE + " reset", "fakename" });
    }
    
    @Override
    public void execute(final String... arguments) {
        try {
            if (arguments.length >= 2) {
                FakeNameCommand.oldName = FakeNameCommand.mc.player.getName();
                if (arguments[0].equalsIgnoreCase("fakename")) {
                    if (arguments[1].equalsIgnoreCase("set")) {
                        FakeNameCommand.currentName = arguments[2];
                        FakeNameCommand.canChange = true;
                        ChatUtils.addChatMessage(ChatFormatting.GREEN + "Successfully" + ChatFormatting.WHITE + " changed your name to " + ChatFormatting.RED + arguments[2]);
                        NotificationRenderer.queue("FakeName Manager", ChatFormatting.GREEN + "Successfully" + ChatFormatting.WHITE + " changed your name to " + ChatFormatting.RED + arguments[2], 4, NotificationMode.SUCCESS);
                    }
                    if (arguments[1].equalsIgnoreCase("reset")) {
                        FakeNameCommand.currentName = FakeNameCommand.oldName;
                        FakeNameCommand.canChange = false;
                        ChatUtils.addChatMessage(ChatFormatting.GREEN + "Successfully" + ChatFormatting.WHITE + " reset your name!");
                        NotificationRenderer.queue("FakeName Manager", ChatFormatting.GREEN + "Successfully" + ChatFormatting.WHITE + " reset your name!", 4, NotificationMode.SUCCESS);
                    }
                }
            }
            else {
                ChatUtils.addChatMessage(this.getUsage());
            }
        }
        catch (final Exception ex) {}
    }
}
