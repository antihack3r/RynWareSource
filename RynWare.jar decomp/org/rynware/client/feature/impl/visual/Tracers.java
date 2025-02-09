// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.visual;

import org.rynware.client.event.EventTarget;
import java.util.Iterator;
import net.minecraft.util.math.Vec3d;
import org.rynware.client.utils.render.RenderUtils;
import org.rynware.client.Main;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import org.rynware.client.event.events.impl.render.EventRender3D;
import net.minecraft.entity.Entity;
import org.rynware.client.ui.settings.Setting;
import java.awt.Color;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.settings.impl.ColorSetting;
import org.rynware.client.feature.Feature;

public class Tracers extends Feature
{
    private final ColorSetting colorGlobal;
    private final ColorSetting friendColor;
    private final BooleanSetting friend;
    private final BooleanSetting onlyPlayer;
    private final NumberSetting width;
    private final BooleanSetting seeOnly;
    
    public Tracers() {
        super("Tracers", FeatureCategory.Visuals);
        this.onlyPlayer = new BooleanSetting("Only Player", true, () -> true);
        this.seeOnly = new BooleanSetting("See Only", false, () -> true);
        this.friend = new BooleanSetting("Friend Highlight", true, () -> true);
        this.friendColor = new ColorSetting("Friend Color", new Color(0, 255, 0).getRGB(), this.friend::getBoolValue);
        this.colorGlobal = new ColorSetting("Tracers Color", new Color(16777215).getRGB(), () -> true);
        this.width = new NumberSetting("Tracers Width", 1.5f, 0.1f, 5.0f, 0.1f, () -> true);
        this.addSettings(this.colorGlobal, this.friend, this.friendColor, this.seeOnly, this.onlyPlayer, this.width);
    }
    
    public static boolean canSeeEntityAtFov(final Entity entityLiving, final float scope) {
        final double diffX = entityLiving.posX - Tracers.mc.player.posX;
        final double diffZ = entityLiving.posZ - Tracers.mc.player.posZ;
        final float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0);
        final double difference = angleDifference(yaw, Tracers.mc.player.rotationYaw);
        return difference <= scope;
    }
    
    public static double angleDifference(final float oldYaw, final float newYaw) {
        float yaw = Math.abs(oldYaw - newYaw) % 360.0f;
        if (yaw > 180.0f) {
            yaw = 360.0f - yaw;
        }
        return yaw;
    }
    
    @EventTarget
    public void onEvent3D(final EventRender3D event) {
        for (final Entity entity : Tracers.mc.world.loadedEntityList) {
            if (entity != Tracers.mc.player) {
                if (this.onlyPlayer.getBoolValue() && !(entity instanceof EntityPlayer)) {
                    continue;
                }
                if (this.seeOnly.getBoolValue() && !canSeeEntityAtFov(entity, 150.0f)) {
                    return;
                }
                final boolean old = Tracers.mc.gameSettings.viewBobbing;
                Tracers.mc.gameSettings.viewBobbing = false;
                Tracers.mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
                Tracers.mc.gameSettings.viewBobbing = old;
                final double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks() - Tracers.mc.getRenderManager().renderPosX;
                final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks() - Tracers.mc.getRenderManager().renderPosY - 1.0;
                final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks() - Tracers.mc.getRenderManager().renderPosZ;
                GlStateManager.pushMatrix();
                GlStateManager.blendFunc(770, 771);
                GL11.glEnable(3042);
                GL11.glEnable(2848);
                GlStateManager.glLineWidth(this.width.getNumberValue());
                GL11.glDisable(3553);
                GL11.glDisable(2929);
                GlStateManager.depthMask(false);
                if (Main.instance.friendManager.isFriend(entity.getName()) && this.friend.getBoolValue()) {
                    RenderUtils.glColor(new Color(this.friendColor.getColorValue()));
                }
                else {
                    RenderUtils.glColor(new Color(this.colorGlobal.getColorValue()));
                }
                GlStateManager.glBegin(3);
                final Vec3d vec = new Vec3d(0.0, 0.0, 1.0).rotatePitch((float)(-Math.toRadians(Tracers.mc.player.rotationPitch))).rotateYaw((float)(-Math.toRadians(Tracers.mc.player.rotationYaw)));
                GL11.glVertex3d(vec.xCoord, Tracers.mc.player.getEyeHeight() + vec.yCoord, vec.zCoord);
                GL11.glVertex3d(x, y + 1.1, z);
                GlStateManager.glEnd();
                GL11.glEnable(3553);
                GL11.glDisable(2848);
                GL11.glEnable(2929);
                GlStateManager.depthMask(true);
                GL11.glDisable(3042);
                GlStateManager.resetColor();
                GlStateManager.popMatrix();
            }
        }
    }
}
