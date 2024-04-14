// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.draggable.component.impl;

import org.rynware.client.feature.Feature;
import org.rynware.client.feature.impl.hud.Hud;
import org.rynware.client.Main;
import org.rynware.client.draggable.component.DraggableComponent;

public class DraggableWaterMark extends DraggableComponent
{
    public DraggableWaterMark() {
        super("WaterMark", 0, 1, 4, 1);
    }
    
    @Override
    public boolean allowDraw() {
        return Main.instance.featureManager.getFeature(Hud.class).isEnabled() && Hud.waterMark.getBoolValue();
    }
}
