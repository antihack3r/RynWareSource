// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.command.impl;

import java.util.Iterator;
import org.rynware.client.feature.impl.misc.CombatDisabler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import org.rynware.client.command.CommandAbstract;

public class CombarCommand extends CommandAbstract
{
    public CombarCommand() {
        super("combat", "combat", "combat add/reset", new String[] { "c" });
    }
    
    @Override
    public void execute(final String... args) {
        if (args.length >= 1) {
            if (args[1].equalsIgnoreCase("add")) {
                for (final Entity en : CombarCommand.mc.world.loadedEntityList) {
                    if (en instanceof EntityPlayer && en.getName().equalsIgnoreCase(args[2])) {
                        CombatDisabler.entities.add(en);
                    }
                }
            }
            else if (args[1].equalsIgnoreCase("reset") || args[1].equalsIgnoreCase("clear")) {
                CombatDisabler.entities.clear();
            }
        }
    }
}
