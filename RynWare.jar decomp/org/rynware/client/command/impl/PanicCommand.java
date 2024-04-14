// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.command.impl;

import java.util.Iterator;
import org.rynware.client.feature.Feature;
import org.rynware.client.Main;
import org.rynware.client.command.CommandAbstract;

public class PanicCommand extends CommandAbstract
{
    public PanicCommand() {
        super("panic", "Disabled all modules", ".panic", new String[] { "panic" });
    }
    
    @Override
    public void execute(final String... args) {
        if (args[0].equalsIgnoreCase("panic")) {
            for (final Feature feature : Main.instance.featureManager.getAllFeatures()) {
                if (feature.isEnabled()) {
                    feature.toggle();
                }
            }
        }
    }
}
