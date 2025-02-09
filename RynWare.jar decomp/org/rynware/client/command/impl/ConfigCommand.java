// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.command.impl;

import java.util.Iterator;
import org.lwjgl.Sys;
import java.io.File;
import org.rynware.client.ui.config.Config;
import org.rynware.client.ui.config.ConfigManager;
import org.rynware.client.utils.other.ChatUtils;
import org.rynware.client.Main;
import com.mojang.realmsclient.gui.ChatFormatting;
import org.rynware.client.command.CommandAbstract;

public class ConfigCommand extends CommandAbstract
{
    public ConfigCommand() {
        super("cfg", "configurations", ChatFormatting.RED + ".cfg" + ChatFormatting.WHITE + " save <name> | load <name> | delete <name> | list | create <name> | dir" + ChatFormatting.RED, new String[] { "<name>", "cfg" });
    }
    
    @Override
    public void execute(final String... args) {
        try {
            if (args.length >= 2) {
                final String upperCase = args[1].toUpperCase();
                if (args.length == 3) {
                    final String s = upperCase;
                    switch (s) {
                        case "LOAD": {
                            if (Main.instance.configManager.loadConfig(args[2])) {
                                ChatUtils.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "loaded config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
                                break;
                            }
                            ChatUtils.addChatMessage(ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "load config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
                            break;
                        }
                        case "SAVE": {
                            if (Main.instance.configManager.saveConfig(args[2])) {
                                Main.instance.fileManager.saveFiles();
                                ChatUtils.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "saved config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
                                ConfigManager.getLoadedConfigs().clear();
                                Main.instance.configManager.load();
                                break;
                            }
                            ChatUtils.addChatMessage(ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to save config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
                            break;
                        }
                        case "DELETE": {
                            if (Main.instance.configManager.deleteConfig(args[2])) {
                                ChatUtils.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "deleted config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
                                break;
                            }
                            ChatUtils.addChatMessage(ChatFormatting.RED + "Failed " + ChatFormatting.WHITE + "to delete config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
                            break;
                        }
                        case "CREATE": {
                            Main.instance.configManager.saveConfig(args[2]);
                            ChatUtils.addChatMessage(ChatFormatting.GREEN + "Successfully " + ChatFormatting.WHITE + "created config: " + ChatFormatting.RED + "\"" + args[2] + "\"");
                            break;
                        }
                    }
                }
                else if (args.length == 2 && upperCase.equalsIgnoreCase("LIST")) {
                    ChatUtils.addChatMessage(ChatFormatting.GREEN + "Configs:");
                    for (final Config config : Main.instance.configManager.getContents()) {
                        ChatUtils.addChatMessage(ChatFormatting.RED + config.getName());
                    }
                }
                else if (args.length == 2 && upperCase.equalsIgnoreCase("DIR")) {
                    final File file = new File("C:\\RynWare\\rynware\\configs", "configs");
                    Sys.openURL(file.getAbsolutePath());
                }
            }
            else {
                ChatUtils.addChatMessage(this.getUsage());
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
