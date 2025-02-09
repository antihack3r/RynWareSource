// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.settings.impl;

import java.util.function.Supplier;
import org.rynware.client.ui.settings.Setting;

public class ColorSetting extends Setting
{
    public int color;
    
    public ColorSetting(final String name, final int color, final Supplier<Boolean> visible) {
        this.name = name;
        this.color = color;
        this.setVisible(visible);
    }
    
    public int getColorValue() {
        return this.color;
    }
    
    public void setColorValue(final int color) {
        this.color = color;
    }
}
