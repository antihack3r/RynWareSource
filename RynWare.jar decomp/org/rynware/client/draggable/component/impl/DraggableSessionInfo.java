// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.draggable.component.impl;

import org.rynware.client.feature.Feature;
import org.rynware.client.feature.impl.hud.Hud;
import org.rynware.client.Main;
import org.rynware.client.draggable.component.DraggableComponent;

public class DraggableSessionInfo extends DraggableComponent
{
    public DraggableSessionInfo() {
        super("Session Info", 0, 10, 1, 1);
    }
    
    @Override
    public boolean allowDraw() {
        return Main.instance.featureManager.getFeature(Hud.class).isEnabled() && Hud.sessionInfo.getBoolValue();
    }
}
