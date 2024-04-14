// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.event.events.impl.render;

import org.rynware.client.event.events.Event;

public class EventFogColor implements Event
{
    public float red;
    public float green;
    public float blue;
    public int alpha;
    
    public EventFogColor(final float red, final float green, final float blue, final int alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }
    
    public float getRed() {
        return this.red;
    }
    
    public void setRed(final float red) {
        this.red = red;
    }
    
    public float getGreen() {
        return this.green;
    }
    
    public void setGreen(final float green) {
        this.green = green;
    }
    
    public float getBlue() {
        return this.blue;
    }
    
    public void setBlue(final float blue) {
        this.blue = blue;
    }
    
    public int getAlpha() {
        return this.alpha;
    }
    
    public void setAlpha(final int alpha) {
        this.alpha = alpha;
    }
    
    public static class FogUpdate implements Event
    {
        private float density;
        
        public FogUpdate(final float density) {
            this.setDensity(density);
        }
        
        public float getDensity() {
            return this.density;
        }
        
        public void setDensity(final float density) {
            this.density = density;
        }
    }
}
