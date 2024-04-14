// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.command.impl;

import org.rynware.client.friend.Friend;
import org.rynware.client.Main;
import org.rynware.client.utils.other.ChatUtils;
import net.minecraft.client.Minecraft;
import com.mojang.realmsclient.gui.ChatFormatting;
import org.rynware.client.command.CommandAbstract;

public class FriendCommand extends CommandAbstract
{
    public FriendCommand() {
        super("friend", "friend list", "\u0412§6.friend" + ChatFormatting.LIGHT_PURPLE + " add \u0412§3<nickname> | \u0412§6.friend" + ChatFormatting.LIGHT_PURPLE + " del \u0412§3<nickname> | \u0412§6.friend" + ChatFormatting.LIGHT_PURPLE + " list | \u0412§6.friend" + ChatFormatting.LIGHT_PURPLE + " clear", new String[] { "friend" });
    }
    
    @Override
    public void execute(final String... arguments) {
        try {
            if (arguments.length > 1) {
                if (arguments[0].equalsIgnoreCase("friend")) {
                    if (arguments[1].equalsIgnoreCase("add")) {
                        final String name = arguments[2];
                        if (name.equals(Minecraft.getMinecraft().player.getName())) {
                            ChatUtils.addChatMessage(ChatFormatting.RED + "You can't add yourself!");
                            return;
                        }
                        if (!Main.instance.friendManager.isFriend(name)) {
                            Main.instance.friendManager.addFriend(name);
                            ChatUtils.addChatMessage("Friend " + ChatFormatting.GREEN + name + ChatFormatting.WHITE + " successfully added to your friend list!");
                        }
                    }
                    if (arguments[1].equalsIgnoreCase("del")) {
                        final String name = arguments[2];
                        if (Main.instance.friendManager.isFriend(name)) {
                            Main.instance.friendManager.removeFriend(name);
                            ChatUtils.addChatMessage("Friend " + ChatFormatting.RED + name + ChatFormatting.WHITE + " deleted from your friend list!");
                        }
                    }
                    if (arguments[1].equalsIgnoreCase("clear")) {
                        if (Main.instance.friendManager.getFriends().isEmpty()) {
                            ChatUtils.addChatMessage(ChatFormatting.RED + "Your friend list is empty!");
                            return;
                        }
                        Main.instance.friendManager.getFriends().clear();
                        ChatUtils.addChatMessage("Your " + ChatFormatting.GREEN + "friend list " + ChatFormatting.WHITE + "was cleared!");
                    }
                    if (arguments[1].equalsIgnoreCase("list")) {
                        if (Main.instance.friendManager.getFriends().isEmpty()) {
                            ChatUtils.addChatMessage(ChatFormatting.RED + "Your friend list is empty!");
                            return;
                        }
                        Main.instance.friendManager.getFriends().forEach(friend -> ChatUtils.addChatMessage(ChatFormatting.GREEN + "Friend list: " + ChatFormatting.RED + friend.getName()));
                    }
                }
            }
            else {
                ChatUtils.addChatMessage(this.getUsage());
            }
        }
        catch (final Exception e) {
            ChatUtils.addChatMessage("\u0412§cNo, no, no. Usage: " + this.getUsage());
        }
    }
}
