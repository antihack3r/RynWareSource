// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.settings;

import java.util.function.Supplier;

public class Setting extends Configurable
{
    protected String name;
    protected Supplier<Boolean> visible;
    
    public boolean isVisible() {
        return this.visible.get();
    }
    
    public void setVisible(final Supplier<Boolean> visible) {
        this.visible = visible;
    }
    
    public String getName() {
        return this.name;
    }
}
