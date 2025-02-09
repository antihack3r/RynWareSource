// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.utils.render;

import java.awt.Graphics;
import net.minecraft.client.renderer.texture.TextureUtil;
import com.jhlabs.image.GaussianFilter;
import java.awt.image.BufferedImage;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import java.util.Iterator;
import java.util.ConcurrentModificationException;
import org.rynware.client.feature.impl.visual.Trails;
import net.minecraft.util.math.Vec3d;
import java.util.List;
import net.minecraft.client.shader.Shader;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import java.util.HashMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.renderer.culling.Frustum;
import org.rynware.client.utils.Helper;

public class RenderUtils implements Helper
{
    protected static float zLevel;
    public static Frustum frustum;
    private static ShaderGroup blurShader;
    private static Framebuffer buffer;
    private static int lastScale;
    private static int lastScaleWidth;
    private static int lastScaleHeight;
    private static ResourceLocation shader;
    private static HashMap<Integer, Integer> shadowCache;
    public static HashMap<Integer, Integer> blurSpotCache;
    private static Framebuffer bloomFramebuffer;
    
    public static double interpolate(final double current, final double old, final double scale) {
        return old + (current - old) * scale;
    }
    
    public static void inShaderFBO() {
        try {
            (RenderUtils.blurShader = new ShaderGroup(RenderUtils.mc.getTextureManager(), RenderUtils.mc.getResourceManager(), RenderUtils.mc.getFramebuffer(), RenderUtils.shader)).createBindFramebuffers(RenderUtils.mc.displayWidth, RenderUtils.mc.displayHeight);
            RenderUtils.buffer = RenderUtils.blurShader.mainFramebuffer;
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void renderShadowVertical(final float lineWidth, final double startAlpha, final int size, final double posX, final double posY1, final double posY2, final boolean right, final boolean edges, final float red, final float green, final float blue) {
        double alpha = startAlpha;
        GlStateManager.alphaFunc(516, 0.0f);
        GL11.glLineWidth(lineWidth);
        if (right) {
            for (double x = 0.5; x < size; x += 0.5) {
                GL11.glColor4d((double)red, (double)green, (double)blue, alpha);
                GL11.glBegin(1);
                GL11.glVertex2d(posX + x, posY1 - (edges ? x : 0.0));
                GL11.glVertex2d(posX + x, posY2 + (edges ? x : 0.0));
                GL11.glEnd();
                alpha = startAlpha - x / size;
            }
        }
        else {
            for (double x = 0.0; x < size; x += 0.5) {
                GL11.glColor4d((double)red, (double)green, (double)blue, alpha);
                GL11.glBegin(1);
                GL11.glVertex2d(posX - x, posY1 - (edges ? x : 0.0));
                GL11.glVertex2d(posX - x, posY2 + (edges ? x : 0.0));
                GL11.glEnd();
                alpha = startAlpha - x / size;
            }
        }
    }
    
    public static void drawColorRect(final double left, final double top, final double right, final double bottom, final Color color1, final Color color2, final Color color3, final Color color4) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        glColor(color2);
        GL11.glVertex2d(left, bottom);
        glColor(color3);
        GL11.glVertex2d(right, bottom);
        glColor(color4);
        GL11.glVertex2d(right, top);
        glColor(color1);
        GL11.glVertex2d(left, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
        Gui.drawRect(0.0, 0.0, 0.0, 0.0, 0);
    }
    
    public static void renderShadowVertical(final Color c, final float lineWidth, final double startAlpha, final int size, final double posX, final double posY1, final double posY2, final boolean right, final boolean edges) {
        GlStateManager.resetColor();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        renderShadowVertical(lineWidth, startAlpha, size, posX, posY1, posY2, right, edges, c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
        GL11.glEnable(3042);
    }
    
    public static void setColor(final int color) {
        GL11.glColor4ub((byte)(color >> 16 & 0xFF), (byte)(color >> 8 & 0xFF), (byte)(color & 0xFF), (byte)(color >> 24 & 0xFF));
    }
    
    public static void drawCone(final float radius, final float height, final int segments, final boolean flag) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        if (!flag) {
            GL11.glEnable(2884);
        }
        final float[][] verteces = new float[segments][3];
        final float[] topVertex = { 0.0f, 0.0f, 0.0f };
        for (int i = 0; i < segments; ++i) {
            verteces[i][0] = (float)Math.cos(6.283185307179586 / segments * i) * radius;
            verteces[i][1] = -height;
            verteces[i][2] = (float)Math.sin(6.283185307179586 / segments * i) * radius;
        }
        GL11.glBegin(9);
        for (int i = 0; i < segments; ++i) {
            GL11.glVertex3f(verteces[i][0], verteces[i][1], verteces[i][2]);
        }
        GL11.glEnd();
        for (int i = 0; i < segments; ++i) {
            GL11.glBegin(4);
            GL11.glVertex3f(verteces[i][0], verteces[i][1], verteces[i][2]);
            GL11.glVertex3f(topVertex[0], topVertex[1], topVertex[2]);
            GL11.glVertex3f(verteces[(i + 1) % segments][0], verteces[(i + 1) % segments][1], verteces[(i + 1) % segments][2]);
            GL11.glEnd();
        }
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glPopMatrix();
    }
    
    public static void drawBorderedRect1(final double x, final double y, final double width, final double height, final double lineSize, final int borderColor, final int color) {
        drawRect(x, y, x + width, y + height, color);
        drawRect(x, y, x + width, y + lineSize, borderColor);
        drawRect(x, y, x + lineSize, y + height, borderColor);
        drawRect(x + width, y, x + width - lineSize, y + height, borderColor);
        drawRect(x, y + height, x + width, y + height - lineSize, borderColor);
    }
    
    public static void renderBlur(final int x, final int y, final int width, final int height, final int blurWidth, final int blurHeight, final int blurRadius) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        blurAreaBoarder((float)x, (float)y, (float)width, (float)height, (float)blurRadius, (float)blurWidth, (float)blurHeight);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
        GL11.glEnable(3042);
    }
    
    public static void blurAreaBoarder(final float x, final float f, final float width, final float height, final float intensity, final float blurWidth, final float blurHeight) {
        final ScaledResolution scale = new ScaledResolution(RenderUtils.mc);
        final int factor = scale.getScaleFactor();
        final int factor2 = scale.getScaledWidth();
        final int factor3 = scale.getScaledHeight();
        if (RenderUtils.lastScale != factor || RenderUtils.lastScaleWidth != factor2 || RenderUtils.lastScaleHeight != factor3 || RenderUtils.buffer == null || RenderUtils.blurShader == null) {
            inShaderFBO();
        }
        RenderUtils.lastScale = factor;
        RenderUtils.lastScaleWidth = factor2;
        RenderUtils.lastScaleHeight = factor3;
        GL11.glScissor((int)(x * factor), (int)(RenderUtils.mc.displayHeight - f * factor - height * factor) + 1, (int)(width * factor), (int)(height * factor));
        GL11.glEnable(3089);
        shaderConfigFix(intensity, blurWidth, blurHeight);
        RenderUtils.buffer.bindFramebuffer(true);
        RenderUtils.blurShader.loadShaderGroup(RenderUtils.mc.timer.renderPartialTicks);
        RenderUtils.mc.getFramebuffer().bindFramebuffer(true);
        GL11.glDisable(3089);
    }
    
    public static void blurAreaBoarder(final int x, final int y, final int width, final int height, final float intensity) {
        final ScaledResolution scale = new ScaledResolution(RenderUtils.mc);
        final int factor = scale.getScaleFactor();
        final int factor2 = scale.getScaledWidth();
        final int factor3 = scale.getScaledHeight();
        if (RenderUtils.lastScale != factor || RenderUtils.lastScaleWidth != factor2 || RenderUtils.lastScaleHeight != factor3 || RenderUtils.buffer == null || RenderUtils.blurShader == null) {
            inShaderFBO();
        }
        RenderUtils.lastScale = factor;
        RenderUtils.lastScaleWidth = factor2;
        RenderUtils.lastScaleHeight = factor3;
        GL11.glScissor(x * factor, RenderUtils.mc.displayHeight - y * factor - height * factor, width * factor, height * factor);
        GL11.glEnable(3089);
        shaderConfigFix(intensity, 1.0f, 0.0f);
        RenderUtils.buffer.bindFramebuffer(true);
        RenderUtils.blurShader.loadShaderGroup(RenderUtils.mc.timer.renderPartialTicks);
        RenderUtils.mc.getFramebuffer().bindFramebuffer(true);
        GL11.glDisable(3089);
    }
    
    private static void shaderConfigFix(final float intensity, final float blurWidth, final float blurHeight) {
        RenderUtils.blurShader.getShaders().get(0).getShaderManager().getShaderUniform("Radius").set(intensity);
        RenderUtils.blurShader.getShaders().get(1).getShaderManager().getShaderUniform("Radius").set(intensity);
        RenderUtils.blurShader.getShaders().get(0).getShaderManager().getShaderUniform("BlurDir").set(blurWidth, blurHeight);
        RenderUtils.blurShader.getShaders().get(1).getShaderManager().getShaderUniform("BlurDir").set(blurHeight, blurWidth);
    }
    
    public static void renderBlur(final int x, final int y, final int width, final int height, final int blurRadius) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2884);
        blurAreaBoarder(x, y, width, height, (float)blurRadius);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glEnable(2884);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
        GL11.glEnable(3042);
    }
    
    public static void bindTexture(final int texture) {
        GL11.glBindTexture(3553, texture);
    }
    
    public static void setAlphaLimit(final float limit) {
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, (float)(limit * 0.01));
    }
    
    public static void drawCircle(final float x, final float y, final float radius, final int color) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glPushMatrix();
        GL11.glLineWidth(1.0f);
        GL11.glBegin(9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d(x + Math.sin(i * 3.141592653589793 / 180.0) * radius, y + Math.cos(i * 3.141592653589793 / 180.0) * radius);
        }
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static void connectPoints(final float xOne, final float yOne, final float xTwo, final float yTwo) {
        GL11.glPushMatrix();
        GL11.glEnable(2848);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.8f);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(0.5f);
        GL11.glBegin(1);
        GL11.glVertex2f(xOne, yOne);
        GL11.glVertex2f(xTwo, yTwo);
        GL11.glEnd();
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }
    
    public static void renderBreadCrumbs(final List<Vec3d> vec3s) {
        GlStateManager.disableDepth();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        int i = 0;
        try {
            for (final Vec3d v : vec3s) {
                ++i;
                boolean draw = true;
                final double x = v.xCoord - RenderUtils.mc.getRenderManager().renderPosX;
                final double y = v.yCoord - RenderUtils.mc.getRenderManager().renderPosY;
                final double z = v.zCoord - RenderUtils.mc.getRenderManager().renderPosZ;
                final double distanceFromPlayer = RenderUtils.mc.player.getDistance(v.xCoord, v.yCoord - 1.0, v.zCoord);
                int quality = (int)(distanceFromPlayer * 4.0 + 10.0);
                if (quality > 350) {
                    quality = 350;
                }
                if (i % 10 != 0 && distanceFromPlayer > 25.0) {
                    draw = false;
                }
                if (i % 3 == 0 && distanceFromPlayer > 15.0) {
                    draw = false;
                }
                if (draw) {
                    GL11.glPushMatrix();
                    GL11.glTranslated(x, y, z);
                    final float scale = 0.04f;
                    GL11.glScalef(-0.04f, -0.04f, -0.04f);
                    GL11.glRotated((double)(-RenderUtils.mc.getRenderManager().playerViewY), 0.0, 1.0, 0.0);
                    GL11.glRotated((double)RenderUtils.mc.getRenderManager().playerViewX, 1.0, 0.0, 0.0);
                    Color c = Color.WHITE;
                    final Color firstcolor = new Color(Trails.onecolor.getColorValue());
                    final String currentMode = Trails.colorMode.currentMode;
                    switch (currentMode) {
                        case "Client": {
                            c = ClientHelper.getClientColor((float)(i / 16), 5.0f, (float)i, 5);
                            break;
                        }
                        case "Astolfo": {
                            c = ColorUtils.astolfo((float)(i - i + 1), (float)i, Trails.saturation.getNumberValue(), 10.0f);
                            break;
                        }
                        case "Rainbow": {
                            c = ColorUtils.rainbow(i * 16, 0.5f, 1.0f);
                            break;
                        }
                        case "Pulse": {
                            c = ColorUtils.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 6.0f * (i / 16) / 60.0f);
                            break;
                        }
                        case "Custom": {
                            c = ColorUtils.TwoColoreffect(new Color(Trails.onecolor.getColorValue()), new Color(Trails.twocolor.getColorValue()), Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 3.0f * (i / 16) / 60.0f);
                            break;
                        }
                        case "Static": {
                            c = firstcolor;
                            break;
                        }
                    }
                    drawFilledCircleNoGL(0, 0, 0.7, c.hashCode(), quality);
                    if (distanceFromPlayer < 4.0) {
                        drawFilledCircleNoGL(0, 0, 1.4, new Color(c.getRed(), c.getGreen(), c.getBlue(), 50).hashCode(), quality);
                    }
                    if (distanceFromPlayer < 20.0) {
                        drawFilledCircleNoGL(0, 0, 2.3, new Color(c.getRed(), c.getGreen(), c.getBlue(), 30).hashCode(), quality);
                    }
                    GL11.glScalef(0.8f, 0.8f, 0.8f);
                    GL11.glPopMatrix();
                }
            }
        }
        catch (final ConcurrentModificationException ex) {}
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GlStateManager.enableDepth();
        GL11.glColor3d(255.0, 255.0, 255.0);
    }
    
    public static void drawFilledCircleNoGL(final int x, final int y, final double r, final int c, final int quality) {
        final float f = (c >> 24 & 0xFF) / 255.0f;
        final float f2 = (c >> 16 & 0xFF) / 255.0f;
        final float f3 = (c >> 8 & 0xFF) / 255.0f;
        final float f4 = (c & 0xFF) / 255.0f;
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glBegin(6);
        for (int i = 0; i <= 360 / quality; ++i) {
            final double x2 = Math.sin(i * quality * 3.141592653589793 / 180.0) * r;
            final double y2 = Math.cos(i * quality * 3.141592653589793 / 180.0) * r;
            GL11.glVertex2d(x + x2, y + y2);
        }
        GL11.glEnd();
    }
    
    public static void drawCircle(final float x, final float y, float start, float end, final float radius, final boolean filled, final Color color) {
        GlStateManager.color(0.0f, 0.0f, 0.0f, 0.0f);
        if (start > end) {
            final float endOffset = end;
            end = start;
            start = endOffset;
        }
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        setColor(color.getRGB());
        GL11.glEnable(2848);
        GL11.glLineWidth(2.0f);
        GL11.glBegin(3);
        for (float i = end; i >= start; i -= 4.0f) {
            final float cos = (float)(Math.cos(i * 3.141592653589793 / 180.0) * radius * 1.0);
            final float sin = (float)(Math.sin(i * 3.141592653589793 / 180.0) * radius * 1.0);
            GL11.glVertex2f(x + cos, y + sin);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(2848);
        GL11.glBegin(filled ? 6 : 3);
        for (float i = end; i >= start; i -= 4.0f) {
            final float cos = (float)Math.cos(i * 3.141592653589793 / 180.0) * radius;
            final float sin = (float)Math.sin(i * 3.141592653589793 / 180.0) * radius;
            GL11.glVertex2f(x + cos, y + sin);
        }
        GL11.glEnd();
        GL11.glDisable(2848);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawCircle(final float x, final float y, final float radius, final boolean filled, final Color color) {
        drawCircle(x, y, 0.0f, 360.0f, radius, filled, color);
    }
    
    public static void blockEsp(final BlockPos blockPos, final Color c, final boolean outline, final double length, final double length2) {
        final double x = blockPos.getX() - RenderUtils.mc.getRenderManager().renderPosX;
        final double y = blockPos.getY() - RenderUtils.mc.getRenderManager().renderPosY;
        final double z = blockPos.getZ() - RenderUtils.mc.getRenderManager().renderPosZ;
        GL11.glPushMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GlStateManager.color(c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, 0.15f);
        drawColorBox(new AxisAlignedBB(x, y, z, x + length2, y + 1.0, z + length), 0.0f, 0.0f, 0.0f, 0.0f);
        if (outline) {
            GlStateManager.color(0.0f, 0.0f, 0.0f, 0.5f);
            drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + length2, y + 1.0, z + length));
        }
        GL11.glLineWidth(2.0f);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
    
    public static void blockEsp(final BlockPos blockPos, final Color color, final boolean outline) {
        final double x = blockPos.getX() - RenderUtils.mc.getRenderManager().renderPosX;
        final double y = blockPos.getY() - RenderUtils.mc.getRenderManager().renderPosY;
        final double z = blockPos.getZ() - RenderUtils.mc.getRenderManager().renderPosZ;
        GL11.glPushMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GlStateManager.color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.15f);
        drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0), 0.0f, 0.0f, 0.0f, 0.0f);
        if (outline) {
            GlStateManager.color(0.0f, 0.0f, 0.0f, 0.5f);
            drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        }
        GL11.glLineWidth(2.0f);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GlStateManager.resetColor();
        GL11.glPopMatrix();
    }
    
    public static void blockEspFrame(final BlockPos blockPos, final float red, final float green, final float blue) {
        final double x = blockPos.getX() - RenderUtils.mc.getRenderManager().renderPosX;
        final double y = blockPos.getY() - RenderUtils.mc.getRenderManager().renderPosY;
        final double z = blockPos.getZ() - RenderUtils.mc.getRenderManager().renderPosZ;
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glLineWidth(2.0f);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GlStateManager.color(red, green, blue, 1.0f);
        drawSelectionBoundingBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
    }
    
    public static void drawCircle3D(final Entity entity, final double radius, final float partialTicks, final int points, final float width, final int color) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glDisable(2929);
        GL11.glLineWidth(width);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(2929);
        GL11.glBegin(3);
        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks - RenderUtils.mc.getRenderManager().renderPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks - RenderUtils.mc.getRenderManager().renderPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks - RenderUtils.mc.getRenderManager().renderPosZ;
        setColor(color);
        for (int i = 0; i <= points; ++i) {
            GL11.glVertex3d(x + radius * Math.cos(i * 6.2831855f / points), y, z + radius * Math.sin(i * 6.2831855f / points));
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glEnable(2929);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }
    
    public static void drawTriangle(final float x, final float y, final float size, final float vector, final int color) {
        GL11.glTranslated((double)x, (double)y, 0.0);
        GL11.glRotatef(180.0f + vector, 0.0f, 0.0f, 1.0f);
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
        GL11.glLineWidth(1.0f);
        GL11.glBegin(6);
        GL11.glVertex2d(0.0, (double)size);
        GL11.glVertex2d((double)(1.0f * size), (double)(-size));
        GL11.glVertex2d((double)(-(1.0f * size)), (double)(-size));
        GL11.glEnd();
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glRotatef(-180.0f - vector, 0.0f, 0.0f, 1.0f);
        GL11.glTranslated((double)(-x), (double)(-y), 0.0);
    }
    
    public static void drawTriangle(final float x, final float y, final float width, final float height, final int firstColor, final int secondColor) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        enableSmoothLine(1.0f);
        GL11.glBegin(9);
        glColor(firstColor, 1.0f);
        GL11.glVertex2f(x, y - 2.0f);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x, y - 2.0f);
        GL11.glEnd();
        GL11.glBegin(9);
        glColor(secondColor, 1.0f);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x + width * 2.0f, y - 2.0f);
        GL11.glVertex2f(x + width, y);
        GL11.glEnd();
        GL11.glBegin(3);
        glColor(firstColor, 1.0f);
        GL11.glVertex2f(x, y - 2.0f);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x, y - 2.0f);
        GL11.glEnd();
        GL11.glBegin(3);
        glColor(secondColor, 1.0f);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x + width * 2.0f, y - 2.0f);
        GL11.glVertex2f(x + width, y);
        GL11.glEnd();
        disableSmoothLine();
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }
    
    public static void glColor(final Color color) {
        final float red = color.getRed() / 255.0f;
        final float green = color.getGreen() / 255.0f;
        final float blue = color.getBlue() / 255.0f;
        final float alpha = color.getAlpha() / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
    }
    
    public static void glColor(final Color color, final int alpha) {
        glColor(color, alpha / 255.0f);
    }
    
    public static void glColor(final Color color, final float alpha) {
        final float red = color.getRed() / 255.0f;
        final float green = color.getGreen() / 255.0f;
        final float blue = color.getBlue() / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
    }
    
    public static final void color(final double red, final double green, final double blue, final double alpha) {
        GL11.glColor4d(red, green, blue, alpha);
    }
    
    public static final void color(Color color) {
        if (color == null) {
            color = Color.white;
        }
        color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
    }
    
    public static void color(final int color) {
        color(color, (color >> 24 & 0xFF) / 255.0f);
    }
    
    public static void color(final int color, final float alpha) {
        final float r = (color >> 16 & 0xFF) / 255.0f;
        final float g = (color >> 8 & 0xFF) / 255.0f;
        final float b = (color & 0xFF) / 255.0f;
        GlStateManager.color(r, g, b, alpha);
    }
    
    public static void glColor(final int hex) {
        final float alpha = (hex >> 24 & 0xFF) / 255.0f;
        final float red = (hex >> 16 & 0xFF) / 255.0f;
        final float green = (hex >> 8 & 0xFF) / 255.0f;
        final float blue = (hex & 0xFF) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }
    
    public static void glColor(final int hex, final int alpha) {
        final float red = (hex >> 16 & 0xFF) / 255.0f;
        final float green = (hex >> 8 & 0xFF) / 255.0f;
        final float blue = (hex & 0xFF) / 255.0f;
        GlStateManager.color(red, green, blue, alpha / 255.0f);
    }
    
    public static void glColor(final int hex, final float alpha) {
        final float red = (hex >> 16 & 0xFF) / 255.0f;
        final float green = (hex >> 8 & 0xFF) / 255.0f;
        final float blue = (hex & 0xFF) / 255.0f;
        GlStateManager.color(red, green, blue, alpha);
    }
    
    public static void enableSmoothLine(final float width) {
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        GL11.glLineWidth(width);
    }
    
    public static void disableSmoothLine() {
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    
    public static void drawEntityBox(final Entity entity, final Color color, final boolean fullBox, final float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.blendFunc(770, 771);
        GL11.glEnable(3042);
        GlStateManager.glLineWidth(2.0f);
        GlStateManager.disableTexture2D();
        GL11.glDisable(2929);
        GlStateManager.depthMask(false);
        final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * RenderUtils.mc.timer.renderPartialTicks - RenderUtils.mc.getRenderManager().renderPosX;
        final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * RenderUtils.mc.timer.renderPartialTicks - RenderUtils.mc.getRenderManager().renderPosY;
        final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * RenderUtils.mc.timer.renderPartialTicks - RenderUtils.mc.getRenderManager().renderPosZ;
        final AxisAlignedBB axisAlignedBB = entity.getEntityBoundingBox();
        final AxisAlignedBB axisAlignedBB2 = new AxisAlignedBB(axisAlignedBB.minX - entity.posX + x - 0.05, axisAlignedBB.minY - entity.posY + y, axisAlignedBB.minZ - entity.posZ + z - 0.05, axisAlignedBB.maxX - entity.posX + x + 0.05, axisAlignedBB.maxY - entity.posY + y + 0.15, axisAlignedBB.maxZ - entity.posZ + z + 0.05);
        GlStateManager.glLineWidth(2.0f);
        GL11.glEnable(2848);
        GlStateManager.color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, alpha);
        if (fullBox) {
            drawColorBox(axisAlignedBB2, color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, alpha);
            GlStateManager.color(0.0f, 0.0f, 0.0f, 0.5f);
        }
        drawSelectionBoundingBox(axisAlignedBB2);
        GlStateManager.glLineWidth(2.0f);
        GlStateManager.enableTexture2D();
        GL11.glEnable(2929);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
    
    public static void drawSelectionBoundingBox(final AxisAlignedBB boundingBox) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(3, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        tessellator.draw();
        vertexbuffer.begin(1, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.minZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.minY, boundingBox.maxZ).endVertex();
        vertexbuffer.pos(boundingBox.minX, boundingBox.maxY, boundingBox.maxZ).endVertex();
        tessellator.draw();
    }
    
    public static void drawColorBox(final AxisAlignedBB axisalignedbb, final float red, final float green, final float blue, final float alpha) {
        final Tessellator ts = Tessellator.getInstance();
        final BufferBuilder vb = ts.getBuffer();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
        vb.begin(7, DefaultVertexFormats.POSITION_TEX);
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.minZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.maxY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        vb.pos(axisalignedbb.maxX, axisalignedbb.minY, axisalignedbb.maxZ).color(red, green, blue, alpha).endVertex();
        ts.draw();
    }
    
    public static void renderItem(final ItemStack itemStack, final int x, final int y) {
        final Minecraft mc = Minecraft.getMinecraft();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.enableDepth();
        RenderHelper.enableGUIStandardItemLighting();
        mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y);
        mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, itemStack, x, y);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.disableDepth();
    }
    
    public static void scissorRect(final float x, final float y, final float width, final double height) {
        final ScaledResolution sr = new ScaledResolution(RenderUtils.mc);
        final int factor = sr.getScaleFactor();
        GL11.glScissor((int)(x * factor), (int)(((float)sr.getScaledHeight() - height) * (float)factor), (int)((width - x) * factor), (int)((height - y) * (float)factor));
    }
    
    public static void drawBorderedRect(final double left, final double top, final double right, final double bottom, final double borderWidth, final int insideColor, final int borderColor, final boolean borderIncludedInBounds) {
        drawRect(left - (borderIncludedInBounds ? 0.0 : borderWidth), top - (borderIncludedInBounds ? 0.0 : borderWidth), right + (borderIncludedInBounds ? 0.0 : borderWidth), bottom + (borderIncludedInBounds ? 0.0 : borderWidth), borderColor);
        drawRect(left + (borderIncludedInBounds ? borderWidth : 0.0), top + (borderIncludedInBounds ? borderWidth : 0.0), right - (borderIncludedInBounds ? borderWidth : 0.0), bottom - (borderIncludedInBounds ? borderWidth : 0.0), insideColor);
    }
    
    public static void drawBorderedRect(final double x, final double y, final double x2, final double y2, final double width, final int color1, final int color2) {
        drawRect(x, y, x2, y2, color2);
        drawBorderedRect(x, y, x2, y2, color1, width);
    }
    
    public static void drawBorderedRect(final double x, final double y, final double width, final double height, final int color, final double lwidth) {
        drawHLine(x, y, width, y, (float)lwidth, color);
        drawHLine(width, y, width, height, (float)lwidth, color);
        drawHLine(x, height, width, height, (float)lwidth, color);
        drawHLine(x, height, x, y, (float)lwidth, color);
    }
    
    public static void drawHLine(final double x, final double y, final double x1, final double y1, final float width, final int color) {
        final float alpha = (color >> 24 & 0xFF) / 255.0f;
        final float red = (color >> 16 & 0xFF) / 255.0f;
        final float green = (color >> 8 & 0xFF) / 255.0f;
        final float blue = (color & 0xFF) / 255.0f;
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(red, green, blue, alpha);
        GL11.glPushMatrix();
        GL11.glLineWidth(width);
        GL11.glBegin(3);
        GL11.glVertex2d(x, y);
        GL11.glVertex2d(x1, y1);
        GL11.glEnd();
        GL11.glLineWidth(1.0f);
        GL11.glPopMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    public static boolean isInViewFrustum(final Entity entity) {
        return isInViewFrustum(entity.getEntityBoundingBox()) || entity.ignoreFrustumCheck;
    }
    
    public static void drawGradientRected(final float f, final float sY, final double width, final double height, final int colour1, final int colour2) {
        Gui.drawGradientRect(f, sY, f + width, sY + height, colour1, colour2);
    }
    
    public static void drawRect2(final double x, final double y, final double width, final double height, final int color) {
        drawRect(x, y, x + width, y + height, color);
    }
    
    public static void drawHead(final EntityLivingBase player, final double x, final double y, final double width, final double height, final Color color) {
        GlStateManager.color(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(Minecraft.getMinecraft().getConnection().getPlayerInfo(player.getUniqueID()).getLocationSkin());
        Gui.drawScaledCustomSizeModalRect((float)x, (float)y, 8.0f, 8.0f, 8, 8, (int)width, (int)height, 64.0f, 64.0f);
    }
    
    public static Framebuffer createFrameBuffer(final Framebuffer framebuffer) {
        if (framebuffer == null || framebuffer.framebufferWidth != RenderUtils.mc.displayWidth || framebuffer.framebufferHeight != RenderUtils.mc.displayHeight) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            return new Framebuffer(RenderUtils.mc.displayWidth, RenderUtils.mc.displayHeight, true);
        }
        return framebuffer;
    }
    
    public static void stuffToBlur(final boolean bloom) {
    }
    
    public static void drawShadow(final float radius, final float offset, final Runnable data) {
        (RenderUtils.bloomFramebuffer = createFrameBuffer(RenderUtils.bloomFramebuffer)).framebufferClear();
        RenderUtils.bloomFramebuffer.bindFramebuffer(true);
        data.run();
        stuffToBlur(true);
        RenderUtils.bloomFramebuffer.unbindFramebuffer();
        BloomUtil.renderBlur(RenderUtils.bloomFramebuffer.framebufferTexture, (int)radius, (int)offset);
    }
    
    public static void drawBlur(final float radius, final Runnable data) {
        StencilUtil.initStencilToWrite();
        data.run();
        StencilUtil.readStencilBuffer(1);
        GaussianBlur.renderBlur(radius);
        StencilUtil.uninitStencilBuffer();
    }
    
    private static boolean isInViewFrustum(final AxisAlignedBB bb) {
        final Entity current = RenderUtils.mc.getRenderViewEntity();
        if (current != null) {
            RenderUtils.frustum.setPosition(current.posX, current.posY, current.posZ);
        }
        return RenderUtils.frustum.isBoundingBoxInFrustum(bb);
    }
    
    public static void drawFilledCircle(final float xx, final float yy, final float radius, final Color color) {
        final int sections = 50;
        final double dAngle = 6.283185307179586 / sections;
        GL11.glPushAttrib(8192);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glBegin(6);
        for (int i = 0; i < sections; ++i) {
            final float x = (float)(radius * Math.sin(i * dAngle));
            final float y = (float)(radius * Math.cos(i * dAngle));
            GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
            GL11.glVertex2f(xx + x, yy + y);
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glEnd();
        GL11.glPopAttrib();
    }
    
    public static Color injectAlpha(final Color color, int alpha) {
        alpha = MathHelper.clamp(alpha, 0, 255);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
    
    public static void drawImage(final ResourceLocation resourceLocation, final float x, final float y, final float width, final float height, final Color color) {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        setColor(color.getRGB());
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
    }
    
    public static void drawImage(final ResourceLocation resourceLocation, final float x, final float y, final float width, final float height) {
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        GL11.glColor4f(0.6f, 0.6f, 0.6f, 1.0f);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0.0f, 0.0f, width, height, width, height);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glEnable(2929);
        GlStateManager.resetColor();
    }
    
    public static void drawGradientRect(final double d, final double e, final double e2, final double g, final int startColor, final int endColor) {
        final float f = (startColor >> 24 & 0xFF) / 255.0f;
        final float f2 = (startColor >> 16 & 0xFF) / 255.0f;
        final float f3 = (startColor >> 8 & 0xFF) / 255.0f;
        final float f4 = (startColor & 0xFF) / 255.0f;
        final float f5 = (endColor >> 24 & 0xFF) / 255.0f;
        final float f6 = (endColor >> 16 & 0xFF) / 255.0f;
        final float f7 = (endColor >> 8 & 0xFF) / 255.0f;
        final float f8 = (endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(e2, e, RenderUtils.zLevel).color(f2, f3, f4, f).endVertex();
        bufferbuilder.pos(d, e, RenderUtils.zLevel).color(f2, f3, f4, f).endVertex();
        bufferbuilder.pos(d, g, RenderUtils.zLevel).color(f6, f7, f8, f5).endVertex();
        bufferbuilder.pos(e2, g, RenderUtils.zLevel).color(f6, f7, f8, f5).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public static void drawSmoothRect(final double left, final double top, final double right, final double bottom, final int color) {
        GlStateManager.resetColor();
        GL11.glEnable(3042);
        GL11.glEnable(2848);
        drawRect(left, top, right, bottom, color);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        drawRect(left * 2.0 - 1.0, top * 2.0, left * 2.0, bottom * 2.0 - 1.0, color);
        drawRect(left * 2.0, top * 2.0 - 1.0, right * 2.0, top * 2.0, color);
        drawRect(right * 2.0, top * 2.0, right * 2.0 + 1.0, bottom * 2.0 - 1.0, color);
        GL11.glDisable(3042);
        GL11.glScalef(2.0f, 2.0f, 2.0f);
    }
    
    public static void drawGradientSideways(final double left, final double top, final double right, final double bottom, final int col1, final int col2) {
        GlStateManager.resetColor();
        final float f = (col1 >> 24 & 0xFF) / 255.0f;
        final float f2 = (col1 >> 16 & 0xFF) / 255.0f;
        final float f3 = (col1 >> 8 & 0xFF) / 255.0f;
        final float f4 = (col1 & 0xFF) / 255.0f;
        final float f5 = (col2 >> 24 & 0xFF) / 255.0f;
        final float f6 = (col2 >> 16 & 0xFF) / 255.0f;
        final float f7 = (col2 >> 8 & 0xFF) / 255.0f;
        final float f8 = (col2 & 0xFF) / 255.0f;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f2, f3, f4, f);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glColor4f(f6, f7, f8, f5);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }
    
    public static void drawOutlineRect(final float x, final float y, final float width, final float height, final Color color, final Color colorTwo) {
        drawRect(x, y, x + width, y + height, color.getRGB());
        final int colorRgb = colorTwo.getRGB();
        drawRect(x - 1.0f, y, x, y + height, colorRgb);
        drawRect(x + width, y, x + width + 1.0f, y + height, colorRgb);
        drawRect(x - 1.0f, y - 1.0f, x + width + 1.0f, y, colorRgb);
        drawRect(x - 1.0f, y + height, x + width + 1.0f, y + height + 1.0f, colorRgb);
    }
    
    public static void drawBlurredShadow(float x, float y, float width, float height, final int blurRadius, final Color color) {
        GL11.glPushMatrix();
        GlStateManager.alphaFunc(516, 0.01f);
        width += blurRadius * 2;
        height += blurRadius * 2;
        x -= blurRadius;
        y -= blurRadius;
        final float _X = x - 0.25f;
        final float _Y = y + 0.25f;
        final int identifier = (int)(width * height + width + color.hashCode() * blurRadius + blurRadius);
        GL11.glEnable(3553);
        GL11.glDisable(2884);
        GL11.glEnable(3008);
        GlStateManager.enableBlend();
        int texId = -1;
        if (RenderUtils.shadowCache.containsKey(identifier)) {
            texId = RenderUtils.shadowCache.get(identifier);
            GlStateManager.bindTexture(texId);
        }
        else {
            if (width <= 0.0f) {
                width = 1.0f;
            }
            if (height <= 0.0f) {
                height = 1.0f;
            }
            final BufferedImage original = new BufferedImage((int)width, (int)height, 3);
            final Graphics g = original.getGraphics();
            g.setColor(color);
            g.fillRect(blurRadius, blurRadius, (int)(width - blurRadius * 2), (int)(height - blurRadius * 2));
            g.dispose();
            final GaussianFilter op = new GaussianFilter((float)blurRadius);
            final BufferedImage blurred = op.filter(original, null);
            texId = TextureUtil.uploadTextureImageAllocate(TextureUtil.glGenTextures(), blurred, true, false);
            RenderUtils.shadowCache.put(identifier, texId);
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glBegin(7);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex2f(_X, _Y);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex2f(_X, _Y + height);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex2f(_X + width, _Y + height);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex2f(_X + width, _Y);
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.resetColor();
        GL11.glEnable(2884);
        GL11.glPopMatrix();
    }
    
    public static void drawRect(double left, double top, double right, double bottom, final int color) {
        GlStateManager.resetColor();
        if (left < right) {
            final double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final double j = top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder vertexbuffer = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f4, f5, f6, f3);
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION);
        vertexbuffer.pos(left, bottom, 0.0).endVertex();
        vertexbuffer.pos(right, bottom, 0.0).endVertex();
        vertexbuffer.pos(right, top, 0.0).endVertex();
        vertexbuffer.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void roundedBorder(final float x, final float y, final float x2, final float y2, final float radius, final int color) {
        final float left = x;
        final float top = y;
        final float bottom = y2;
        final float right = x2;
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glDepthMask(true);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glHint(3155, 4354);
        setColor(color);
        GlStateManager.glLineWidth(2.0f);
        GL11.glBegin(2);
        GL11.glVertex2d((double)left, (double)(top + radius));
        GL11.glVertex2f(left + radius, top);
        GL11.glVertex2f(right - radius, top);
        GL11.glVertex2f(right, top + radius);
        GL11.glVertex2f(right, bottom - radius);
        GL11.glVertex2f(right - radius, bottom);
        GL11.glVertex2f(left + radius, bottom);
        GL11.glVertex2f(left, bottom - radius);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(2848);
        GL11.glHint(3154, 4352);
        GL11.glHint(3155, 4352);
    }
    
    static {
        RenderUtils.frustum = new Frustum();
        RenderUtils.shadowCache = new HashMap<Integer, Integer>();
        RenderUtils.bloomFramebuffer = new Framebuffer(1, 1, false);
        RenderUtils.shader = new ResourceLocation("shaders/post/blur.json");
        RenderUtils.blurSpotCache = new HashMap<Integer, Integer>();
    }
}
