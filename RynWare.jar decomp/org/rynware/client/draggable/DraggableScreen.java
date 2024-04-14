// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.draggable;

import net.minecraft.util.math.MathHelper;
import java.util.Iterator;
import org.rynware.client.draggable.component.DraggableComponent;
import org.rynware.client.Main;

public class DraggableScreen
{
    public void draw(final int mouseX, final int mouseY) {
        for (final DraggableComponent draggableComponent : Main.instance.draggableHUD.getComponents()) {
            if (draggableComponent.allowDraw()) {
                this.drawComponent(mouseX, mouseY, draggableComponent);
            }
        }
    }
    
    private void drawComponent(final int mouseX, final int mouseY, final DraggableComponent draggableComponent) {
        draggableComponent.draw(mouseX, mouseY);
        final boolean hover = MathHelper.isMouseHoveringOnRect(draggableComponent.getX(), draggableComponent.getY(), draggableComponent.getWidth(), draggableComponent.getHeight(), mouseX, mouseY);
    }
    
    public void click(final int mouseX, final int mouseY) {
        for (final DraggableComponent draggableComponent : Main.instance.draggableHUD.getComponents()) {
            if (draggableComponent.allowDraw()) {
                draggableComponent.click(mouseX, mouseY);
            }
        }
    }
    
    public void release() {
        for (final DraggableComponent draggableComponent : Main.instance.draggableHUD.getComponents()) {
            draggableComponent.release();
        }
    }
}
