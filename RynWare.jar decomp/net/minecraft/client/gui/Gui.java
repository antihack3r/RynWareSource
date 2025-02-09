// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.Tessellator;
import org.rynware.client.utils.render.GLUtil;
import org.lwjgl.opengl.GL11;
import org.rynware.client.utils.render.RenderUtils;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class Gui
{
    public static final ResourceLocation OPTIONS_BACKGROUND;
    public static final ResourceLocation STAT_ICONS;
    public static final ResourceLocation ICONS;
    protected static float zLevel;
    
    protected void drawHorizontalLine(int startX, int endX, final int y, final int color) {
        if (endX < startX) {
            final int i = startX;
            startX = endX;
            endX = i;
        }
        drawRect(startX, y, endX + 1, y + 1, color);
    }
    
    protected void drawVerticalLine(final int x, int startY, int endY, final int color) {
        if (endY < startY) {
            final int i = startY;
            startY = endY;
            endY = i;
        }
        drawRect(x, startY + 1, x + 1, endY, color);
    }
    
    public static void drawRect2(final double x, final double y, final double width, final double height, final int color) {
        GlStateManager.resetColor();
        GLUtil.setup2DRendering(() -> GLUtil.render(7, () -> {
            RenderUtils.color(color);
            GL11.glVertex2d(x, y);
            GL11.glVertex2d(x, y + height);
            GL11.glVertex2d(x + width, y + height);
            GL11.glVertex2d(x + width, y);
        }));
    }
    
    public static void drawGradientRect(final float f8, final float sY, final double d, final double e, final int startColor, final int endColor) {
        final float f9 = (startColor >> 24 & 0xFF) / 255.0f;
        final float f10 = (startColor >> 16 & 0xFF) / 255.0f;
        final float f11 = (startColor >> 8 & 0xFF) / 255.0f;
        final float f12 = (startColor & 0xFF) / 255.0f;
        final float f13 = (endColor >> 24 & 0xFF) / 255.0f;
        final float f14 = (endColor >> 16 & 0xFF) / 255.0f;
        final float f15 = (endColor >> 8 & 0xFF) / 255.0f;
        final float f16 = (endColor & 0xFF) / 255.0f;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(d, sY, Gui.zLevel).color(f10, f11, f12, f9).endVertex();
        bufferbuilder.pos(f8, sY, Gui.zLevel).color(f14, f15, f16, f13).endVertex();
        bufferbuilder.pos(f8, e, Gui.zLevel).color(f14, f15, f16, f13).endVertex();
        bufferbuilder.pos(d, e, Gui.zLevel).color(f10, f11, f12, f9).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public static void drawRect(double left, double top, double right, double bottom, final int color) {
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
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f4, f5, f6, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, top, 0.0).endVertex();
        bufferbuilder.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    protected void drawGradientRect(final int left, final int top, final int right, final int bottom, final int startColor, final int endColor) {
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
        bufferbuilder.pos(right, top, Gui.zLevel).color(f2, f3, f4, f).endVertex();
        bufferbuilder.pos(left, top, Gui.zLevel).color(f2, f3, f4, f).endVertex();
        bufferbuilder.pos(left, bottom, Gui.zLevel).color(f6, f7, f8, f5).endVertex();
        bufferbuilder.pos(right, bottom, Gui.zLevel).color(f6, f7, f8, f5).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public void drawCenteredString(final FontRenderer fontRendererIn, final String text, final int x, final int y, final int color) {
        fontRendererIn.drawStringWithShadow(text, (float)(x - fontRendererIn.getStringWidth(text) / 2), (float)y, color);
    }
    
    public void drawString(final FontRenderer fontRendererIn, final String text, final int x, final int y, final int color) {
        fontRendererIn.drawStringWithShadow(text, (float)x, (float)y, color);
    }
    
    public void drawTexturedModalRect(final int x, final int y, final int textureX, final int textureY, final int width, final int height) {
        final float f = 0.00390625f;
        final float f2 = 0.00390625f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x + 0, y + height, Gui.zLevel).tex((textureX + 0) * 0.00390625f, (textureY + height) * 0.00390625f).endVertex();
        bufferbuilder.pos(x + width, y + height, Gui.zLevel).tex((textureX + width) * 0.00390625f, (textureY + height) * 0.00390625f).endVertex();
        bufferbuilder.pos(x + width, y + 0, Gui.zLevel).tex((textureX + width) * 0.00390625f, (textureY + 0) * 0.00390625f).endVertex();
        bufferbuilder.pos(x + 0, y + 0, Gui.zLevel).tex((textureX + 0) * 0.00390625f, (textureY + 0) * 0.00390625f).endVertex();
        tessellator.draw();
    }
    
    public void drawTexturedModalRect(final float xCoord, final float yCoord, final int minU, final int minV, final int maxU, final int maxV) {
        final float f = 0.00390625f;
        final float f2 = 0.00390625f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(xCoord + 0.0f, yCoord + maxV, Gui.zLevel).tex((minU + 0) * 0.00390625f, (minV + maxV) * 0.00390625f).endVertex();
        bufferbuilder.pos(xCoord + maxU, yCoord + maxV, Gui.zLevel).tex((minU + maxU) * 0.00390625f, (minV + maxV) * 0.00390625f).endVertex();
        bufferbuilder.pos(xCoord + maxU, yCoord + 0.0f, Gui.zLevel).tex((minU + maxU) * 0.00390625f, (minV + 0) * 0.00390625f).endVertex();
        bufferbuilder.pos(xCoord + 0.0f, yCoord + 0.0f, Gui.zLevel).tex((minU + 0) * 0.00390625f, (minV + 0) * 0.00390625f).endVertex();
        tessellator.draw();
    }
    
    public void drawTexturedModalRect(final int xCoord, final int yCoord, final TextureAtlasSprite textureSprite, final int widthIn, final int heightIn) {
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(xCoord + 0, yCoord + heightIn, Gui.zLevel).tex(textureSprite.getMinU(), textureSprite.getMaxV()).endVertex();
        bufferbuilder.pos(xCoord + widthIn, yCoord + heightIn, Gui.zLevel).tex(textureSprite.getMaxU(), textureSprite.getMaxV()).endVertex();
        bufferbuilder.pos(xCoord + widthIn, yCoord + 0, Gui.zLevel).tex(textureSprite.getMaxU(), textureSprite.getMinV()).endVertex();
        bufferbuilder.pos(xCoord + 0, yCoord + 0, Gui.zLevel).tex(textureSprite.getMinU(), textureSprite.getMinV()).endVertex();
        tessellator.draw();
    }
    
    public static void drawModalRectWithCustomSizedTexture(final float x, final float y, final float u, final float v, final float width, final float height, final float textureWidth, final float textureHeight) {
        final float f = 1.0f / textureWidth;
        final float f2 = 1.0f / textureHeight;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, 0.0).tex(u * f, (v + height) * f2).endVertex();
        bufferbuilder.pos(x + width, y + height, 0.0).tex((u + width) * f, (v + height) * f2).endVertex();
        bufferbuilder.pos(x + width, y, 0.0).tex((u + width) * f, v * f2).endVertex();
        bufferbuilder.pos(x, y, 0.0).tex(u * f, v * f2).endVertex();
        tessellator.draw();
    }
    
    public static void drawScaledCustomSizeModalRect(final float x, final float y, final float u, final float v, final int uWidth, final int vHeight, final int width, final int height, final float tileWidth, final float tileHeight) {
        final float f = 1.0f / tileWidth;
        final float f2 = 1.0f / tileHeight;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, 0.0).tex(u * f, (v + vHeight) * f2).endVertex();
        bufferbuilder.pos(x + width, y + height, 0.0).tex((u + uWidth) * f, (v + vHeight) * f2).endVertex();
        bufferbuilder.pos(x + width, y, 0.0).tex((u + uWidth) * f, v * f2).endVertex();
        bufferbuilder.pos(x, y, 0.0).tex(u * f, v * f2).endVertex();
        tessellator.draw();
    }
    
    public static void drawScaledCustomSizeModalRect(final float x, final float y, final float u, final float v, final float uWidth, final float vHeight, final float width, final float height, final float tileWidth, final float tileHeight) {
        final float f = 1.0f / tileWidth;
        final float f2 = 1.0f / tileHeight;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x, y + height, 0.0).tex(u * f, (v + vHeight) * f2).endVertex();
        bufferbuilder.pos(x + width, y + height, 0.0).tex((u + uWidth) * f, (v + vHeight) * f2).endVertex();
        bufferbuilder.pos(x + width, y, 0.0).tex((u + uWidth) * f, v * f2).endVertex();
        bufferbuilder.pos(x, y, 0.0).tex(u * f, v * f2).endVertex();
        tessellator.draw();
    }
    
    static {
        OPTIONS_BACKGROUND = new ResourceLocation("textures/gui/options_background.png");
        STAT_ICONS = new ResourceLocation("textures/gui/container/stats_icons.png");
        ICONS = new ResourceLocation("textures/gui/icons.png");
    }
}
