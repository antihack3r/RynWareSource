// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.visual;

import java.util.Iterator;
import org.rynware.client.utils.math.RotationHelper;
import org.rynware.client.utils.render.ColorUtils;
import org.rynware.client.utils.render.ClientHelper;
import net.minecraft.client.gui.ScaledResolution;
import org.rynware.client.event.events.impl.render.EventRender2D;
import org.rynware.client.event.EventTarget;
import net.minecraft.util.math.Vec3d;
import org.rynware.client.utils.render.RenderUtils;
import org.lwjgl.opengl.GL11;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import org.rynware.client.event.events.impl.render.EventRender3D;
import org.rynware.client.ui.settings.Setting;
import java.awt.Color;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.settings.impl.ColorSetting;
import org.rynware.client.feature.Feature;

public class PearlESP extends Feature
{
    public ColorSetting globalColor;
    public BooleanSetting tracers;
    public BooleanSetting esp;
    public BooleanSetting triangleESP;
    private final ListSetting triangleMode;
    private final ColorSetting triangleColor;
    
    public PearlESP() {
        super("PearlESP", "\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u0435\u0441\u043f \u043f\u0435\u0440\u043b\u0430", FeatureCategory.Visuals);
        this.globalColor = new ColorSetting("Global Color", Color.PINK.getRGB(), () -> true);
        this.tracers = new BooleanSetting("Tracers", true, () -> true);
        this.esp = new BooleanSetting("ESP", true, () -> true);
        this.triangleESP = new BooleanSetting("TriangleESP", true, () -> true);
        this.triangleMode = new ListSetting("Triangle Mode", "Custom", () -> this.triangleESP.getBoolValue(), new String[] { "Astolfo", "Rainbow", "Client", "Custom" });
        this.triangleColor = new ColorSetting("Triangle Color", Color.PINK.getRGB(), () -> this.triangleESP.getBoolValue() && this.triangleMode.currentMode.equals("Custom"));
        this.addSettings(this.globalColor, this.triangleESP, this.triangleMode, this.triangleColor, this.esp, this.tracers);
    }
    
    @EventTarget
    public void onRender3D(final EventRender3D event) {
        GlStateManager.pushMatrix();
        final List<EntityEnderPearl> check = PearlESP.mc.world.loadedEntityList.stream().filter(x -> x instanceof EntityEnderPearl).map(x -> x).collect((Collector<? super Object, ?, List<EntityEnderPearl>>)Collectors.toList());
        check.forEach(entity -> {
            final boolean viewBobbing = PearlESP.mc.gameSettings.viewBobbing;
            PearlESP.mc.gameSettings.viewBobbing = false;
            PearlESP.mc.entityRenderer.setupCameraTransform(event.getPartialTicks(), 0);
            PearlESP.mc.gameSettings.viewBobbing = viewBobbing;
            if (this.tracers.getBoolValue()) {
                GL11.glPushMatrix();
                GL11.glEnable(2848);
                GL11.glDisable(2929);
                GL11.glDisable(3553);
                GL11.glDisable(2896);
                GL11.glDepthMask(false);
                GL11.glBlendFunc(770, 771);
                GL11.glEnable(3042);
                GL11.glLineWidth(1.0f);
                final double x2 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.getPartialTicks() - PearlESP.mc.getRenderManager().renderPosX;
                final double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.getPartialTicks() - PearlESP.mc.getRenderManager().renderPosY - 1.0;
                final double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.getPartialTicks() - PearlESP.mc.getRenderManager().renderPosZ;
                RenderUtils.setColor(this.globalColor.getColorValue());
                final Vec3d vec = new Vec3d(0.0, 0.0, 1.0).rotatePitch((float)(-Math.toRadians(PearlESP.mc.player.rotationPitch))).rotateYaw((float)(-Math.toRadians(PearlESP.mc.player.rotationYaw)));
                GL11.glBegin(2);
                GL11.glVertex3d(vec.xCoord, PearlESP.mc.player.getEyeHeight() + vec.yCoord, vec.zCoord);
                GL11.glVertex3d(x2, y + 1.1, z);
                GL11.glEnd();
                GL11.glDisable(3042);
                GL11.glDepthMask(true);
                GL11.glEnable(3553);
                GL11.glEnable(2929);
                GL11.glDisable(2848);
                GL11.glPopMatrix();
            }
            if (this.esp.getBoolValue()) {
                RenderUtils.drawEntityBox(entity, new Color(this.globalColor.getColorValue()), true, 0.2f);
            }
            return;
        });
        GlStateManager.resetColor();
        GlStateManager.popMatrix();
    }
    
    @EventTarget
    public void onRender2D(final EventRender2D event) {
        if (!this.triangleESP.getBoolValue()) {
            return;
        }
        final ScaledResolution sr = new ScaledResolution(PearlESP.mc);
        final float size = 50.0f;
        final float xOffset = sr.getScaledWidth() / 2.0f - 24.5f;
        final float yOffset = sr.getScaledHeight() / 2.0f - 25.2f;
        for (final Entity entity : PearlESP.mc.world.loadedEntityList) {
            if (entity != null) {
                if (!(entity instanceof EntityEnderPearl)) {
                    continue;
                }
                int color = 0;
                final String currentMode = this.triangleMode.currentMode;
                switch (currentMode) {
                    case "Client": {
                        color = ClientHelper.getClientColor().getRGB();
                        break;
                    }
                    case "Custom": {
                        color = this.triangleColor.getColorValue();
                        break;
                    }
                    case "Astolfo": {
                        color = ColorUtils.astolfo(false, 1).getRGB();
                        break;
                    }
                    case "Rainbow": {
                        color = ColorUtils.rainbow(300, 1.0f, 1.0f).getRGB();
                        break;
                    }
                }
                GL11.glPushMatrix();
                final int x = event.getResolution().getScaledWidth() / 2;
                final int y = event.getResolution().getScaledHeight() / 2;
                GL11.glTranslatef((float)x, (float)y, 0.0f);
                GL11.glRotatef(RotationHelper.getAngle(entity) % 360.0f + 180.0f, 0.0f, 0.0f, 1.0f);
                GL11.glTranslatef((float)(-x), (float)(-y), 0.0f);
                RenderUtils.drawBlurredShadow(x - 3.0f, (float)(y + 48), 5.0f, 10.0f, 15, new Color(color));
                RenderUtils.drawTriangle(x - 5.0f, (float)(y + 50), 5.0f, 10.0f, new Color(color).darker().getRGB(), color);
                GL11.glTranslatef((float)x, (float)y, 0.0f);
                GL11.glRotatef(-(RotationHelper.getAngle(entity) % 360.0f + 180.0f), 0.0f, 0.0f, 1.0f);
                GL11.glTranslatef((float)(-x), (float)(-y), 0.0f);
                GL11.glPopMatrix();
            }
        }
    }
}
