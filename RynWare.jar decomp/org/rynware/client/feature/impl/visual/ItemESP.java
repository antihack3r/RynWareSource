// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.visual;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;
import net.minecraft.client.renderer.GLAllocation;
import org.rynware.client.event.EventTarget;
import java.util.Iterator;
import org.lwjgl.opengl.GL11;
import javax.vecmath.Vector4d;
import javax.vecmath.Vector3d;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.item.EntityItem;
import org.rynware.client.utils.render.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.GlStateManager;
import org.rynware.client.event.events.impl.render.EventRender2D;
import org.rynware.client.ui.settings.Setting;
import java.awt.Color;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.settings.impl.ColorSetting;
import org.rynware.client.feature.Feature;

public class ItemESP extends Feature
{
    public ColorSetting itemESPColor;
    private final BooleanSetting box;
    
    public ItemESP() {
        super("ItemESP", FeatureCategory.Visuals);
        this.itemESPColor = new ColorSetting("ItemESP Color", new Color(17, 158, 255).getRGB(), () -> true);
        this.box = new BooleanSetting("Box", true, () -> true);
        this.addSettings(this.itemESPColor, this.box);
    }
    
    @EventTarget
    public void onRender2D(final EventRender2D event) {
        final float partialTicks = ItemESP.mc.timer.renderPartialTicks;
        final int scaleFactor = event.getResolution().getScaleFactor();
        final double scaling = scaleFactor / Math.pow(scaleFactor, 2.0);
        GlStateManager.scale(scaling, scaling, scaling);
        final float scale = 1.0f;
        for (final Entity entity : ItemESP.mc.world.loadedEntityList) {
            if (this.isValid(entity) && RenderUtils.isInViewFrustum(entity)) {
                final EntityItem entityItem = (EntityItem)entity;
                final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * ItemESP.mc.getRenderPartialTicks();
                final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * ItemESP.mc.getRenderPartialTicks();
                final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * ItemESP.mc.getRenderPartialTicks();
                final AxisAlignedBB axisAlignedBB2 = entity.getEntityBoundingBox();
                final AxisAlignedBB axisAlignedBB3 = new AxisAlignedBB(axisAlignedBB2.minX - entity.posX + x - 0.05, axisAlignedBB2.minY - entity.posY + y, axisAlignedBB2.minZ - entity.posZ + z - 0.05, axisAlignedBB2.maxX - entity.posX + x + 0.05, axisAlignedBB2.maxY - entity.posY + y + 0.15, axisAlignedBB2.maxZ - entity.posZ + z + 0.05);
                final Vector3d[] vectors = { new Vector3d(axisAlignedBB3.minX, axisAlignedBB3.minY, axisAlignedBB3.minZ), new Vector3d(axisAlignedBB3.minX, axisAlignedBB3.maxY, axisAlignedBB3.minZ), new Vector3d(axisAlignedBB3.maxX, axisAlignedBB3.minY, axisAlignedBB3.minZ), new Vector3d(axisAlignedBB3.maxX, axisAlignedBB3.maxY, axisAlignedBB3.minZ), new Vector3d(axisAlignedBB3.minX, axisAlignedBB3.minY, axisAlignedBB3.maxZ), new Vector3d(axisAlignedBB3.minX, axisAlignedBB3.maxY, axisAlignedBB3.maxZ), new Vector3d(axisAlignedBB3.maxX, axisAlignedBB3.minY, axisAlignedBB3.maxZ), new Vector3d(axisAlignedBB3.maxX, axisAlignedBB3.maxY, axisAlignedBB3.maxZ) };
                ItemESP.mc.entityRenderer.setupCameraTransform(partialTicks, 0);
                Vector4d position = null;
                for (Vector3d vector : vectors) {
                    vector = this.project2D(scaleFactor, vector.x - ItemESP.mc.getRenderManager().renderPosX, vector.y - ItemESP.mc.getRenderManager().renderPosY, vector.z - ItemESP.mc.getRenderManager().renderPosZ);
                    if (vector != null && vector.z > 0.0 && vector.z < 1.0) {
                        if (position == null) {
                            position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                        }
                        position.x = Math.min(vector.x, position.x);
                        position.y = Math.min(vector.y, position.y);
                        position.z = Math.max(vector.x, position.z);
                        position.w = Math.max(vector.y, position.w);
                    }
                }
                if (position == null) {
                    continue;
                }
                ItemESP.mc.entityRenderer.setupOverlayRendering();
                final double posX = position.x;
                final double posY = position.y;
                final double endPosX = position.z;
                final double endPosY = position.w;
                final float diff = (float)(endPosX - posX) / 2.0f;
                final float textWidth = ItemESP.mc.fontRendererObj.getStringWidth(entityItem.getEntityItem().getDisplayName()) * scale;
                final float tagX = (float)((posX + diff - textWidth / 2.0f) * scale);
                if (this.box.getBoolValue()) {
                    RenderUtils.drawRect(posX, posY, endPosX, endPosY, new Color(0, 0, 0, 40).getRGB());
                }
                ItemESP.mc.rubik_18.drawBlurredStringWithShadow(entityItem.getEntityItem().getDisplayName(), tagX + 5.0f, (float)posY - 10.0f, 8, RenderUtils.injectAlpha(new Color(this.itemESPColor.getColorValue()), 40), this.itemESPColor.getColorValue());
            }
        }
        GL11.glEnable(2929);
        ItemESP.mc.entityRenderer.setupOverlayRendering();
    }
    
    private Vector3d project2D(final int scaleFactor, final double x, final double y, final double z) {
        final float xPos = (float)x;
        final float yPos = (float)y;
        final float zPos = (float)z;
        final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
        final FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
        final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
        final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
        GL11.glGetFloat(2982, modelview);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        if (GLU.gluProject(xPos, yPos, zPos, modelview, projection, viewport, vector)) {
            return new Vector3d(vector.get(0) / scaleFactor, (Display.getHeight() - vector.get(1)) / scaleFactor, vector.get(2));
        }
        return null;
    }
    
    private boolean isValid(final Entity entity) {
        return entity instanceof EntityItem;
    }
}
