// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.command.macro;

import java.util.ArrayList;
import java.util.List;

public class MacroManager
{
    public List<Macro> macros;
    
    public MacroManager() {
        this.macros = new ArrayList<Macro>();
    }
    
    public List<Macro> getMacros() {
        return this.macros;
    }
    
    public void addMacro(final Macro macro) {
        this.macros.add(macro);
    }
    
    public void deleteMacroByKey(final int key) {
        this.macros.removeIf(macro -> macro.getKey() == key);
    }
}
