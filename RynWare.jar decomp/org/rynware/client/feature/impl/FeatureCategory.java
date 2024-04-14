// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl;

public enum FeatureCategory
{
    Combat("Combat"), 
    Movement("Movement"), 
    Visuals("Render"), 
    Player("Player"), 
    Misc("Util"), 
    Hud("Hud");
    
    private final String displayName;
    
    private FeatureCategory(final String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
}
