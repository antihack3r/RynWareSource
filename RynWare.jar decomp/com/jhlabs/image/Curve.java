// 
// Decompiled by Procyon v0.6.0
// 

package com.jhlabs.image;

public class Curve
{
    public float[] x;
    public float[] y;
    
    public Curve() {
        this.x = new float[] { 0.0f, 1.0f };
        this.y = new float[] { 0.0f, 1.0f };
    }
    
    public Curve(final Curve curve) {
        this.x = curve.x.clone();
        this.y = curve.y.clone();
    }
    
    public int addKnot(final float kx, final float ky) {
        int pos = -1;
        final int numKnots = this.x.length;
        final float[] nx = new float[numKnots + 1];
        final float[] ny = new float[numKnots + 1];
        int j = 0;
        for (int i = 0; i < numKnots; ++i) {
            if (pos == -1 && this.x[i] > kx) {
                pos = j;
                nx[j] = kx;
                ny[j] = ky;
                ++j;
            }
            nx[j] = this.x[i];
            ny[j] = this.y[i];
            ++j;
        }
        if (pos == -1) {
            pos = j;
            nx[j] = kx;
            ny[j] = ky;
        }
        this.x = nx;
        this.y = ny;
        return pos;
    }
    
    public void removeKnot(final int n) {
        final int numKnots = this.x.length;
        if (numKnots <= 2) {
            return;
        }
        final float[] nx = new float[numKnots - 1];
        final float[] ny = new float[numKnots - 1];
        int j = 0;
        for (int i = 0; i < numKnots - 1; ++i) {
            if (i == n) {
                ++j;
            }
            nx[i] = this.x[j];
            ny[i] = this.y[j];
            ++j;
        }
        this.x = nx;
        this.y = ny;
    }
    
    private void sortKnots() {
        for (int numKnots = this.x.length, i = 1; i < numKnots - 1; ++i) {
            for (int j = 1; j < i; ++j) {
                if (this.x[i] < this.x[j]) {
                    float t = this.x[i];
                    this.x[i] = this.x[j];
                    this.x[j] = t;
                    t = this.y[i];
                    this.y[i] = this.y[j];
                    this.y[j] = t;
                }
            }
        }
    }
    
    protected int[] makeTable() {
        final int numKnots = this.x.length;
        final float[] nx = new float[numKnots + 2];
        final float[] ny = new float[numKnots + 2];
        System.arraycopy(this.x, 0, nx, 1, numKnots);
        System.arraycopy(this.y, 0, ny, 1, numKnots);
        nx[0] = nx[1];
        ny[0] = ny[1];
        nx[numKnots + 1] = nx[numKnots];
        ny[numKnots + 1] = ny[numKnots];
        final int[] table = new int[256];
        for (int i = 0; i < 1024; ++i) {
            final float f = i / 1024.0f;
            int x = (int)(255.0f * ImageMath.spline(f, nx.length, nx) + 0.5f);
            int y = (int)(255.0f * ImageMath.spline(f, nx.length, ny) + 0.5f);
            x = ImageMath.clamp(x, 0, 255);
            y = ImageMath.clamp(y, 0, 255);
            table[x] = y;
        }
        return table;
    }
}
