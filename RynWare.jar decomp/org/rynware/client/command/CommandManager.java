// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.command;

import java.util.Iterator;
import java.util.List;
import org.rynware.client.command.impl.CombarCommand;
import org.rynware.client.command.impl.TpCommand;
import org.rynware.client.command.impl.BindCommand;
import org.rynware.client.command.impl.ClipCommand;
import org.rynware.client.command.impl.GPSCommand;
import org.rynware.client.command.impl.PanicCommand;
import org.rynware.client.command.impl.ParseCommand;
import org.rynware.client.command.impl.HelpCommand;
import org.rynware.client.command.impl.FakeNameCommand;
import org.rynware.client.command.impl.FriendCommand;
import org.rynware.client.command.impl.MacroCommand;
import org.rynware.client.command.impl.ConfigCommand;
import org.rynware.client.event.EventManager;
import java.util.ArrayList;

public class CommandManager
{
    private final ArrayList<Command> commands;
    
    public CommandManager() {
        this.commands = new ArrayList<Command>();
        EventManager.register(new CommandHandler(this));
        this.commands.add(new ConfigCommand());
        this.commands.add(new MacroCommand());
        this.commands.add(new FriendCommand());
        this.commands.add(new FakeNameCommand());
        this.commands.add(new HelpCommand());
        this.commands.add(new ParseCommand());
        this.commands.add(new PanicCommand());
        this.commands.add(new GPSCommand());
        this.commands.add(new ClipCommand());
        this.commands.add(new BindCommand());
        this.commands.add(new TpCommand());
        this.commands.add(new CombarCommand());
    }
    
    public List<Command> getCommands() {
        return this.commands;
    }
    
    public boolean execute(final String args) {
        final String noPrefix = args.substring(1);
        final String[] split = noPrefix.split(" ");
        if (split.length > 0) {
            for (final Command command : this.commands) {
                final CommandAbstract abstractCommand = (CommandAbstract)command;
                final String[] aliases;
                final String[] commandAliases = aliases = abstractCommand.getAliases();
                for (final String alias : aliases) {
                    if (split[0].equalsIgnoreCase(alias)) {
                        abstractCommand.execute(split);
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
