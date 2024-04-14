// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.command;

import org.rynware.client.utils.other.ChatUtils;
import org.rynware.client.utils.Helper;

public abstract class CommandAbstract implements Command, Helper
{
    private final String name;
    private final String description;
    private final String usage;
    private final String[] aliases;
    
    public CommandAbstract(final String name, final String description, final String usage, final String... aliases) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        this.usage = usage;
    }
    
    public void usage() {
        ChatUtils.addChatMessage("\u043f\u0457\u0405cInvalid usage, try: " + this.usage + " or .help");
    }
    
    public String getUsage() {
        return this.usage;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public String[] getAliases() {
        return this.aliases;
    }
}
