// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.command.impl;

import org.rynware.client.utils.other.ChatUtils;
import org.rynware.client.files.impl.MacroConfig;
import org.rynware.client.command.macro.Macro;
import org.lwjgl.input.Keyboard;
import org.rynware.client.Main;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextFormatting;
import org.rynware.client.command.CommandAbstract;

public class MacroCommand extends CommandAbstract
{
    public MacroCommand() {
        super("macros", "macro", TextFormatting.GRAY + ".macro" + ChatFormatting.WHITE + " add \u0412§3<key> /home_home | \u0412§7.macro" + ChatFormatting.WHITE + " remove \u0412§3<key> |" + TextFormatting.GRAY + " .macro" + ChatFormatting.WHITE + " clear \u0412§3| \u0412§7.macro" + ChatFormatting.WHITE + " list", new String[] { "\u0412§7.macro" + ChatFormatting.WHITE + " add \u0412§3<key> </home_home> | \u0412§7.macro" + ChatFormatting.WHITE + " remove \u0412§3<key> | \u0412§7.macro" + ChatFormatting.WHITE + " clear | \u0412§7.macro" + ChatFormatting.WHITE + " list", "macro" });
    }
    
    @Override
    public void execute(final String... arguments) {
        try {
            if (arguments.length > 1) {
                if (arguments[0].equals("macro")) {
                    if (arguments[1].equals("add")) {
                        final StringBuilder command = new StringBuilder();
                        for (int i = 3; i < arguments.length; ++i) {
                            command.append(arguments[i]).append(" ");
                        }
                        Main.instance.macroManager.addMacro(new Macro(Keyboard.getKeyIndex(arguments[2].toUpperCase()), command.toString()));
                        Main.instance.fileManager.getFile(MacroConfig.class).saveFile();
                        ChatUtils.addChatMessage(ChatFormatting.GREEN + "Added macros for key" + ChatFormatting.RED + " \"" + arguments[2].toUpperCase() + ChatFormatting.RED + "\" " + ChatFormatting.WHITE + "with value " + ChatFormatting.RED + (Object)command);
                    }
                    if (arguments[1].equals("clear")) {
                        if (Main.instance.macroManager.getMacros().isEmpty()) {
                            ChatUtils.addChatMessage(ChatFormatting.RED + "Your macros list is empty!");
                            return;
                        }
                        Main.instance.macroManager.getMacros().clear();
                        ChatUtils.addChatMessage(ChatFormatting.GREEN + "Your macros list " + ChatFormatting.WHITE + " successfully cleared!");
                    }
                    if (arguments[1].equals("remove")) {
                        Main.instance.macroManager.deleteMacroByKey(Keyboard.getKeyIndex(arguments[2].toUpperCase()));
                        ChatUtils.addChatMessage(ChatFormatting.GREEN + "Macro " + ChatFormatting.WHITE + "was deleted from key " + ChatFormatting.RED + "\"" + arguments[2].toUpperCase() + "\"");
                    }
                    if (arguments[1].equals("list")) {
                        if (Main.instance.macroManager.getMacros().isEmpty()) {
                            ChatUtils.addChatMessage(ChatFormatting.RED + "Your macros list is empty!");
                            return;
                        }
                        Main.instance.macroManager.getMacros().forEach(macro -> ChatUtils.addChatMessage(ChatFormatting.GREEN + "Macros list: " + ChatFormatting.WHITE + "Macros Name: " + ChatFormatting.RED + macro.getValue() + ChatFormatting.WHITE + ", Macro Bind: " + ChatFormatting.RED + Keyboard.getKeyName(macro.getKey())));
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
