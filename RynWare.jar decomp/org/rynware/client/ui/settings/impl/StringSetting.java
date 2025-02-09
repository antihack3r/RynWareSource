// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.settings.impl;

import java.util.function.Supplier;
import org.rynware.client.ui.settings.Setting;

public class StringSetting extends Setting
{
    public String defaultText;
    public String currentText;
    
    public StringSetting(final String name, final String defaultText, final String currentText, final Supplier<Boolean> visible) {
        this.name = name;
        this.defaultText = defaultText;
        this.currentText = currentText;
        this.setVisible(visible);
    }
    
    public String getDefaultText() {
        return this.defaultText;
    }
    
    public void setDefaultText(final String defaultText) {
        this.defaultText = defaultText;
    }
    
    public String getCurrentText() {
        return this.currentText;
    }
    
    public void setCurrentText(final String currentText) {
        this.currentText = currentText;
    }
}
