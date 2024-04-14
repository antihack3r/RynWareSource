// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.command;

import org.rynware.client.event.EventTarget;
import org.rynware.client.event.events.impl.packet.EventMessage;

public class CommandHandler
{
    public CommandManager commandManager;
    
    public CommandHandler(final CommandManager commandManager) {
        this.commandManager = commandManager;
    }
    
    @EventTarget
    public void onMessage(final EventMessage event) {
        final String msg = event.getMessage();
        if (msg.length() > 0 && msg.startsWith(".")) {
            event.setCancelled(this.commandManager.execute(msg));
        }
    }
}
