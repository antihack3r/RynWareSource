// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.settings;

import java.util.List;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;

public class Configurable
{
    private final ArrayList<Setting> settingList;
    
    public Configurable() {
        this.settingList = new ArrayList<Setting>();
    }
    
    public final void addSettings(final Setting... options) {
        this.settingList.addAll(Arrays.asList(options));
    }
    
    public final List<Setting> getSettings() {
        return this.settingList;
    }
}
