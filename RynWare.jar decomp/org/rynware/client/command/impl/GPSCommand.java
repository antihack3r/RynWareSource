// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.command.impl;

import org.rynware.client.utils.other.ChatUtils;
import org.rynware.client.command.CommandAbstract;

public class GPSCommand extends CommandAbstract
{
    public static int x;
    public static int z;
    public static String mode;
    
    public GPSCommand() {
        super("gps", "gps coommand", "\u0412§bUsage: \u0412§6.gps <x> <z> <off/on>", new String[] { "gps" });
    }
    
    @Override
    public void execute(final String... args) {
        if (args.length < 4) {
            ChatUtils.addChatMessage(this.getUsage());
        }
        else {
            GPSCommand.mode = args[3].toLowerCase();
            if (GPSCommand.mode.equalsIgnoreCase("on")) {
                GPSCommand.x = Integer.parseInt(args[1]);
                GPSCommand.z = Integer.parseInt(args[2]);
            }
            else if (GPSCommand.mode.equalsIgnoreCase("off")) {
                GPSCommand.x = 0;
                GPSCommand.z = 0;
            }
        }
    }
}
