// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.draggable;

import java.util.Iterator;
import org.rynware.client.draggable.component.impl.DraggableIndicators;
import org.rynware.client.draggable.component.impl.DraggableTimer;
import org.rynware.client.draggable.component.impl.DraggableUserInfo;
import org.rynware.client.draggable.component.impl.DraggableSessionInfo;
import org.rynware.client.draggable.component.impl.DraggablePotionHUD;
import org.rynware.client.draggable.component.impl.DraggableCoordsInfo;
import org.rynware.client.draggable.component.impl.DraggableWaterMark;
import org.rynware.client.draggable.component.impl.DraggableTargetHUD;
import com.google.common.collect.Lists;
import org.rynware.client.draggable.component.DraggableComponent;
import java.util.List;

public class DraggableHUD
{
    private DraggableScreen screen;
    private final List<DraggableComponent> components;
    
    public DraggableHUD() {
        this.screen = new DraggableScreen();
        (this.components = Lists.newArrayList()).add(new DraggableTargetHUD());
        this.components.add(new DraggableWaterMark());
        this.components.add(new DraggableCoordsInfo());
        this.components.add(new DraggablePotionHUD());
        this.components.add(new DraggableSessionInfo());
        this.components.add(new DraggableUserInfo());
        this.components.add(new DraggableTimer());
        this.components.add(new DraggableIndicators());
    }
    
    public DraggableScreen getScreen() {
        return this.screen;
    }
    
    public List<DraggableComponent> getComponents() {
        return this.components;
    }
    
    public DraggableComponent getDraggableComponentByClass(final Class<? extends DraggableComponent> classs) {
        for (final DraggableComponent draggableComponent : this.components) {
            if (draggableComponent.getClass() == classs) {
                return draggableComponent;
            }
        }
        return null;
    }
}
