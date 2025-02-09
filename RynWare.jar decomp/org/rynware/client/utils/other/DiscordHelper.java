// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.utils.other;

import org.rynware.client.Main;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import org.rynware.client.utils.Helper;

public class DiscordHelper implements Helper
{
    private static final String discordID = "1003220033863950376";
    private static final DiscordRichPresence discordRichPresence;
    private static final DiscordRPC discordRPC;
    
    public static void startRPC() {
        try {
            final DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
            DiscordRPC.discordInitialize("1003220033863950376", eventHandlers, true, null);
            DiscordHelper.discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
            DiscordHelper.discordRichPresence.details = "Idle";
            DiscordHelper.discordRichPresence.largeImageKey = "best";
            DiscordHelper.discordRichPresence.largeImageText = Main.instance.name;
            DiscordHelper.discordRichPresence.state = "Version: " + Main.instance.version;
            DiscordRPC.discordUpdatePresence(DiscordHelper.discordRichPresence);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void stopRPC() {
        DiscordRPC.discordShutdown();
    }
    
    static {
        discordRichPresence = new DiscordRichPresence();
        discordRPC = new DiscordRPC();
    }
}
