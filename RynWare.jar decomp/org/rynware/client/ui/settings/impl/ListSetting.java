// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.settings.impl;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.List;
import org.rynware.client.ui.settings.Setting;

public class ListSetting extends Setting
{
    public final List<String> modes;
    public String currentMode;
    public int index;
    
    public ListSetting(final String name, final String currentMode, final Supplier<Boolean> visible, final String... options) {
        this.name = name;
        this.modes = Arrays.asList(options);
        this.index = this.modes.indexOf(currentMode);
        this.currentMode = this.modes.get(this.index);
        this.setVisible(visible);
        this.addSettings(this);
    }
    
    public String getCurrentMode() {
        return this.currentMode;
    }
    
    public void setListMode(final String selected) {
        this.currentMode = selected;
        this.index = this.modes.indexOf(selected);
    }
    
    public List<String> getModes() {
        return this.modes;
    }
    
    public String getOptions() {
        return this.modes.get(this.index);
    }
}
