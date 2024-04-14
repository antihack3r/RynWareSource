// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.command.impl;

import java.util.Iterator;
import org.rynware.client.feature.Feature;
import org.rynware.client.ui.notification.NotificationRenderer;
import org.rynware.client.ui.notification.NotificationMode;
import org.rynware.client.utils.other.ChatUtils;
import org.lwjgl.input.Keyboard;
import org.rynware.client.Main;
import net.minecraft.util.text.TextFormatting;
import com.mojang.realmsclient.gui.ChatFormatting;
import org.rynware.client.command.CommandAbstract;

public class BindCommand extends CommandAbstract
{
    public BindCommand() {
        super("bind", "bind", "\u0412§6.bind" + ChatFormatting.RED + " add \u0412§7<name> <key> " + TextFormatting.RED + "\n[" + TextFormatting.WHITE + "RynWare" + TextFormatting.GRAY + "] \u043f\u0457\u04056.bind " + ChatFormatting.RED + "remove \u0412§7<name> \u0412§7<key> \n[" + TextFormatting.WHITE + "RynWare" + TextFormatting.GRAY + "] \u0412§6.bind " + ChatFormatting.RED + "list ", new String[] { "bind" });
    }
    
    @Override
    public void execute(final String... arguments) {
        try {
            if (arguments.length == 4) {
                final String moduleName = arguments[2];
                final String bind = arguments[3].toUpperCase();
                final Feature feature = Main.instance.featureManager.getFeature(moduleName);
                if (arguments[0].equalsIgnoreCase("bind") && arguments[1].equalsIgnoreCase("add")) {
                    feature.setBind(Keyboard.getKeyIndex(bind));
                    ChatUtils.addChatMessage(ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " was set on key " + ChatFormatting.RED + "\"" + bind + "\"");
                    NotificationRenderer.queue("Bind Manager", ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " was set on key " + ChatFormatting.RED + "\"" + bind + "\"", 1, NotificationMode.SUCCESS);
                }
                else if (arguments[0].equalsIgnoreCase("bind") && arguments[1].equalsIgnoreCase("remove")) {
                    feature.setBind(0);
                    ChatUtils.addChatMessage(ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " bind was deleted from key " + ChatFormatting.RED + "\"" + bind + "\"");
                    NotificationRenderer.queue("Bind Manager", ChatFormatting.GREEN + feature.getLabel() + ChatFormatting.WHITE + " bind was deleted from key " + ChatFormatting.RED + "\"" + bind + "\"", 1, NotificationMode.SUCCESS);
                }
            }
            else if (arguments.length == 2) {
                if (arguments[0].equalsIgnoreCase("bind") && arguments[1].equalsIgnoreCase("list")) {
                    for (final Feature f : Main.instance.featureManager.getAllFeatures()) {
                        if (f.getBind() != 0) {
                            ChatUtils.addChatMessage(f.getLabel() + " : " + Keyboard.getKeyName(f.getBind()));
                        }
                    }
                }
                else {
                    ChatUtils.addChatMessage(this.getUsage());
                }
            }
            else if (arguments[0].equalsIgnoreCase("bind")) {
                ChatUtils.addChatMessage(this.getUsage());
            }
        }
        catch (final Exception ex) {}
    }
}
