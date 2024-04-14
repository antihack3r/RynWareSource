// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.components;

import org.rynware.client.ui.clickgui.component.impl.ModuleComponent;
import org.rynware.client.ui.clickgui.component.Component;
import java.util.Comparator;

public class SorterHelper implements Comparator<Component>
{
    @Override
    public int compare(final Component component, final Component component2) {
        if (component instanceof ModuleComponent && component2 instanceof ModuleComponent) {
            return component.getName().compareTo(component2.getName());
        }
        return 0;
    }
}
