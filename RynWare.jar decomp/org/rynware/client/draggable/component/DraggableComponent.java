// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.draggable.component;

import net.minecraft.util.math.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class DraggableComponent
{
    public ScaledResolution sr;
    private final String name;
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean drag;
    private int prevX;
    private int prevY;
    
    public String getName() {
        return this.name;
    }
    
    public int getX() {
        return this.x;
    }
    
    public int getY() {
        return this.y;
    }
    
    public int getWidth() {
        return this.width;
    }
    
    public int getHeight() {
        return this.height;
    }
    
    public void setX(final int x) {
        this.x = x;
    }
    
    public void setY(final int y) {
        this.y = y;
    }
    
    public void setWidth(final int width) {
        this.width = width;
    }
    
    public void setHeight(final int height) {
        this.height = height;
    }
    
    public DraggableComponent(final String name, final int x, final int y, final int width, final int height) {
        this.sr = new ScaledResolution(Minecraft.getMinecraft());
        this.name = name;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public void draw(final int mouseX, final int mouseY) {
        if (this.drag) {
            this.x = mouseX - this.prevX;
            this.y = mouseY - this.prevY;
        }
    }
    
    public void click(final int mouseX, final int mouseY) {
        if (MathHelper.isMouseHoveringOnRect(this.x, this.y, this.width, this.height, mouseX, mouseY)) {
            this.drag = true;
            this.prevX = mouseX - this.x;
            this.prevY = mouseY - this.y;
        }
    }
    
    public void release() {
        this.drag = false;
    }
    
    public boolean allowDraw() {
        return true;
    }
}
