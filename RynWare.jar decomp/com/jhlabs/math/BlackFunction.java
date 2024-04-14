// 
// Decompiled by Procyon v0.6.0
// 

package com.jhlabs.math;

public class BlackFunction implements BinaryFunction
{
    @Override
    public boolean isBlack(final int rgb) {
        return rgb == -16777216;
    }
}
