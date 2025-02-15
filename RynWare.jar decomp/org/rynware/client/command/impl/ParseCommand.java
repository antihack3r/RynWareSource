// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.command.impl;

import java.util.Iterator;
import java.util.List;
import org.lwjgl.Sys;
import org.rynware.client.ui.notification.NotificationRenderer;
import org.rynware.client.ui.notification.NotificationMode;
import org.rynware.client.utils.other.ChatUtils;
import net.minecraft.client.network.NetworkPlayerInfo;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import com.mojang.realmsclient.gui.ChatFormatting;
import org.rynware.client.command.CommandAbstract;

public class ParseCommand extends CommandAbstract
{
    public ParseCommand() {
        super("parser", "parser", ChatFormatting.RED + ".parser" + ChatFormatting.WHITE + " parse | dir", new String[] { "parser" });
    }
    
    @Override
    public void execute(final String... args) {
        if (args.length >= 2) {
            final String upperCase = args[1].toUpperCase();
            if (args.length == 2 && upperCase.equalsIgnoreCase("PARSE")) {
                try {
                    final List<NetworkPlayerInfo> players = GuiPlayerTabOverlay.ENTRY_ORDERING.sortedCopy((Iterable)ParseCommand.mc.player.connection.getPlayerInfoMap());
                    final File fileFolder = new File("C://RynWare/", "parser");
                    if (!fileFolder.exists()) {
                        fileFolder.mkdirs();
                    }
                    final File file = new File("C://RynWare/parser/", ParseCommand.mc.getCurrentServerData().serverIP.split(":")[0] + ".txt");
                    final BufferedWriter out = new BufferedWriter(new FileWriter(file));
                    if (file.exists()) {
                        file.delete();
                    }
                    else {
                        file.createNewFile();
                    }
                    for (final NetworkPlayerInfo n : players) {
                        if (n.getPlayerTeam().getColorPrefix().length() <= 3) {
                            continue;
                        }
                        out.write(n.getPlayerTeam().getColorPrefix() + " : " + n.getGameProfile().getName());
                        out.write("\r\n");
                    }
                    out.close();
                    ChatUtils.addChatMessage(ChatFormatting.GREEN + "Successfully parsed! " + ChatFormatting.WHITE + "please check your game directory");
                    NotificationRenderer.queue(ChatFormatting.GREEN + "Parse Manager", ChatFormatting.GREEN + "Successfully parsed! " + ChatFormatting.WHITE + "please check your game directory", 5, NotificationMode.SUCCESS);
                }
                catch (final Exception ex) {}
            }
            else if (args.length == 2 && upperCase.equalsIgnoreCase("DIR")) {
                final File file2 = new File("C:\\RynWare\\parser");
                Sys.openURL(file2.getAbsolutePath());
            }
        }
        else {
            ChatUtils.addChatMessage(this.getUsage());
        }
    }
}
