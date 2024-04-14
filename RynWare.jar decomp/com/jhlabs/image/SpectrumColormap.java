// 
// Decompiled by Procyon v0.6.0
// 

package com.jhlabs.image;

public class SpectrumColormap implements Colormap
{
    @Override
    public int getColor(final float v) {
        return Spectrum.wavelengthToRGB(380.0f + 400.0f * ImageMath.clamp(v, 0.0f, 1.0f));
    }
}
