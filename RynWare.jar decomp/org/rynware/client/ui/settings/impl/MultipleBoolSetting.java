// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.settings.impl;

import java.util.Arrays;
import java.util.List;
import org.rynware.client.ui.settings.Setting;

public class MultipleBoolSetting extends Setting
{
    private final List<BooleanSetting> boolSettings;
    
    public MultipleBoolSetting(final String name, final BooleanSetting... booleanSettings) {
        this.name = name;
        this.boolSettings = Arrays.asList(booleanSettings);
    }
    
    public BooleanSetting getSetting(final String settingName) {
        return this.boolSettings.stream().filter(booleanSetting -> booleanSetting.getName().equals(settingName)).findFirst().orElse(null);
    }
    
    public List<BooleanSetting> getBoolSettings() {
        return this.boolSettings;
    }
}
