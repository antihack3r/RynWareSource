// 
// Decompiled by Procyon v0.6.0
// 

package com.jhlabs.image;

public class AverageFilter extends ConvolveFilter
{
    protected static float[] theMatrix;
    
    public AverageFilter() {
        super(AverageFilter.theMatrix);
    }
    
    @Override
    public String toString() {
        return "Blur/Average Blur";
    }
    
    static {
        AverageFilter.theMatrix = new float[] { 0.1f, 0.1f, 0.1f, 0.1f, 0.2f, 0.1f, 0.1f, 0.1f, 0.1f };
    }
}
