// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.settings.impl;

import java.util.function.Supplier;
import org.rynware.client.ui.settings.Setting;

public class BooleanSetting extends Setting
{
    private boolean state;
    private String desc;
    
    public BooleanSetting(final String name, final String desc, final boolean state, final Supplier<Boolean> visible) {
        this.name = name;
        this.desc = desc;
        this.state = state;
        this.setVisible(visible);
    }
    
    public BooleanSetting(final String name, final boolean state, final Supplier<Boolean> visible) {
        this.name = name;
        this.state = state;
        this.setVisible(visible);
    }
    
    public BooleanSetting(final String name, final boolean state) {
        this.name = name;
        this.state = state;
    }
    
    public BooleanSetting(final String name) {
        this(name, false);
    }
    
    public String getDesc() {
        return this.desc;
    }
    
    public void setDesc(final String desc) {
        this.desc = desc;
    }
    
    public boolean getBoolValue() {
        return this.state;
    }
    
    public void setBoolValue(final boolean state) {
        this.state = state;
    }
}
