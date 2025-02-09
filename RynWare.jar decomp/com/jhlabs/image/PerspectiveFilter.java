// 
// Decompiled by Procyon v0.6.0
// 

package com.jhlabs.image;

import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class PerspectiveFilter extends TransformFilter
{
    private float x0;
    private float y0;
    private float x1;
    private float y1;
    private float x2;
    private float y2;
    private float x3;
    private float y3;
    private float dx1;
    private float dy1;
    private float dx2;
    private float dy2;
    private float dx3;
    private float dy3;
    private float A;
    private float B;
    private float C;
    private float D;
    private float E;
    private float F;
    private float G;
    private float H;
    private float I;
    private float a11;
    private float a12;
    private float a13;
    private float a21;
    private float a22;
    private float a23;
    private float a31;
    private float a32;
    private float a33;
    private boolean scaled;
    private boolean clip;
    
    public PerspectiveFilter() {
        this(0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f);
    }
    
    public PerspectiveFilter(final float x0, final float y0, final float x1, final float y1, final float x2, final float y2, final float x3, final float y3) {
        this.clip = false;
        this.unitSquareToQuad(x0, y0, x1, y1, x2, y2, x3, y3);
    }
    
    public void setClip(final boolean clip) {
        this.clip = clip;
    }
    
    public boolean getClip() {
        return this.clip;
    }
    
    public void setCorners(final float x0, final float y0, final float x1, final float y1, final float x2, final float y2, final float x3, final float y3) {
        this.unitSquareToQuad(x0, y0, x1, y1, x2, y2, x3, y3);
        this.scaled = true;
    }
    
    public void unitSquareToQuad(final float x0, final float y0, final float x1, final float y1, final float x2, final float y2, final float x3, final float y3) {
        this.x0 = x0;
        this.y0 = y0;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.x3 = x3;
        this.y3 = y3;
        this.dx1 = x1 - x2;
        this.dy1 = y1 - y2;
        this.dx2 = x3 - x2;
        this.dy2 = y3 - y2;
        this.dx3 = x0 - x1 + x2 - x3;
        this.dy3 = y0 - y1 + y2 - y3;
        if (this.dx3 == 0.0f && this.dy3 == 0.0f) {
            this.a11 = x1 - x0;
            this.a21 = x2 - x1;
            this.a31 = x0;
            this.a12 = y1 - y0;
            this.a22 = y2 - y1;
            this.a32 = y0;
            final float n = 0.0f;
            this.a23 = n;
            this.a13 = n;
        }
        else {
            this.a13 = (this.dx3 * this.dy2 - this.dx2 * this.dy3) / (this.dx1 * this.dy2 - this.dy1 * this.dx2);
            this.a23 = (this.dx1 * this.dy3 - this.dy1 * this.dx3) / (this.dx1 * this.dy2 - this.dy1 * this.dx2);
            this.a11 = x1 - x0 + this.a13 * x1;
            this.a21 = x3 - x0 + this.a23 * x3;
            this.a31 = x0;
            this.a12 = y1 - y0 + this.a13 * y1;
            this.a22 = y3 - y0 + this.a23 * y3;
            this.a32 = y0;
        }
        this.a33 = 1.0f;
        this.scaled = false;
    }
    
    public void quadToUnitSquare(final float x0, final float y0, final float x1, final float y1, final float x2, final float y2, final float x3, final float y3) {
        this.unitSquareToQuad(x0, y0, x1, y1, x2, y2, x3, y3);
        final float ta11 = this.a22 * this.a33 - this.a32 * this.a23;
        final float ta12 = this.a32 * this.a13 - this.a12 * this.a33;
        final float ta13 = this.a12 * this.a23 - this.a22 * this.a13;
        final float ta14 = this.a31 * this.a23 - this.a21 * this.a33;
        final float ta15 = this.a11 * this.a33 - this.a31 * this.a13;
        final float ta16 = this.a21 * this.a13 - this.a11 * this.a23;
        final float ta17 = this.a21 * this.a32 - this.a31 * this.a22;
        final float ta18 = this.a31 * this.a12 - this.a11 * this.a32;
        final float ta19 = this.a11 * this.a22 - this.a21 * this.a12;
        final float f = 1.0f / ta19;
        this.a11 = ta11 * f;
        this.a21 = ta14 * f;
        this.a31 = ta17 * f;
        this.a12 = ta12 * f;
        this.a22 = ta15 * f;
        this.a32 = ta18 * f;
        this.a13 = ta13 * f;
        this.a23 = ta16 * f;
        this.a33 = 1.0f;
    }
    
    @Override
    public BufferedImage filter(final BufferedImage src, final BufferedImage dst) {
        this.A = this.a22 * this.a33 - this.a32 * this.a23;
        this.B = this.a31 * this.a23 - this.a21 * this.a33;
        this.C = this.a21 * this.a32 - this.a31 * this.a22;
        this.D = this.a32 * this.a13 - this.a12 * this.a33;
        this.E = this.a11 * this.a33 - this.a31 * this.a13;
        this.F = this.a31 * this.a12 - this.a11 * this.a32;
        this.G = this.a12 * this.a23 - this.a22 * this.a13;
        this.H = this.a21 * this.a13 - this.a11 * this.a23;
        this.I = this.a11 * this.a22 - this.a21 * this.a12;
        if (!this.scaled) {
            final int width = src.getWidth();
            final int height = src.getHeight();
            final float invWidth = 1.0f / width;
            final float invHeight = 1.0f / height;
            this.A *= invWidth;
            this.D *= invWidth;
            this.G *= invWidth;
            this.B *= invHeight;
            this.E *= invHeight;
            this.H *= invHeight;
        }
        return super.filter(src, dst);
    }
    
    @Override
    protected void transformSpace(final Rectangle rect) {
        if (this.scaled) {
            rect.x = (int)Math.min(Math.min(this.x0, this.x1), Math.min(this.x2, this.x3));
            rect.y = (int)Math.min(Math.min(this.y0, this.y1), Math.min(this.y2, this.y3));
            rect.width = (int)Math.max(Math.max(this.x0, this.x1), Math.max(this.x2, this.x3)) - rect.x;
            rect.height = (int)Math.max(Math.max(this.y0, this.y1), Math.max(this.y2, this.y3)) - rect.y;
            return;
        }
        if (!this.clip) {
            final float w = (float)rect.getWidth();
            final float h = (float)rect.getHeight();
            final Rectangle r = new Rectangle();
            r.add(this.getPoint2D(new Point2D.Float(0.0f, 0.0f), null));
            r.add(this.getPoint2D(new Point2D.Float(w, 0.0f), null));
            r.add(this.getPoint2D(new Point2D.Float(0.0f, h), null));
            r.add(this.getPoint2D(new Point2D.Float(w, h), null));
            rect.setRect(r);
        }
    }
    
    public float getOriginX() {
        return this.x0 - (int)Math.min(Math.min(this.x0, this.x1), Math.min(this.x2, this.x3));
    }
    
    public float getOriginY() {
        return this.y0 - (int)Math.min(Math.min(this.y0, this.y1), Math.min(this.y2, this.y3));
    }
    
    @Override
    public Rectangle2D getBounds2D(final BufferedImage src) {
        if (this.clip) {
            return new Rectangle(0, 0, src.getWidth(), src.getHeight());
        }
        final float w = (float)src.getWidth();
        final float h = (float)src.getHeight();
        final Rectangle2D r = new Rectangle2D.Float();
        r.add(this.getPoint2D(new Point2D.Float(0.0f, 0.0f), null));
        r.add(this.getPoint2D(new Point2D.Float(w, 0.0f), null));
        r.add(this.getPoint2D(new Point2D.Float(0.0f, h), null));
        r.add(this.getPoint2D(new Point2D.Float(w, h), null));
        return r;
    }
    
    @Override
    public Point2D getPoint2D(final Point2D srcPt, Point2D dstPt) {
        if (dstPt == null) {
            dstPt = new Point2D.Float();
        }
        final float x = (float)srcPt.getX();
        final float y = (float)srcPt.getY();
        final float f = 1.0f / (x * this.a13 + y * this.a23 + this.a33);
        dstPt.setLocation((x * this.a11 + y * this.a21 + this.a31) * f, (x * this.a12 + y * this.a22 + this.a32) * f);
        return dstPt;
    }
    
    @Override
    protected void transformInverse(final int x, final int y, final float[] out) {
        out[0] = this.originalSpace.width * (this.A * x + this.B * y + this.C) / (this.G * x + this.H * y + this.I);
        out[1] = this.originalSpace.height * (this.D * x + this.E * y + this.F) / (this.G * x + this.H * y + this.I);
    }
    
    @Override
    public String toString() {
        return "Distort/Perspective...";
    }
}
