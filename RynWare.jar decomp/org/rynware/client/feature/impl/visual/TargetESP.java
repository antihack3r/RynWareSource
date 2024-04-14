// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.visual;

import org.rynware.client.event.EventTarget;
import net.minecraft.util.math.MathHelper;
import org.rynware.client.utils.render.RenderUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import org.rynware.client.utils.render.ColorUtils;
import org.rynware.client.utils.render.ClientHelper;
import org.rynware.client.Main;
import net.minecraft.entity.Entity;
import org.rynware.client.feature.impl.combat.KillAura;
import org.rynware.client.event.events.impl.render.EventRender3D;
import org.rynware.client.ui.settings.Setting;
import java.awt.Color;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.ColorSetting;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.feature.Feature;

public class TargetESP extends Feature
{
    double height;
    boolean animat;
    private double circleAnim;
    private double circleValue;
    private boolean canDown;
    public ListSetting bebraPonyxana;
    public NumberSetting circlesize;
    public NumberSetting circleSpeed;
    public NumberSetting points;
    public BooleanSetting depthTest;
    public ColorSetting targetEspColor;
    public ListSetting jelloMode;
    
    public TargetESP() {
        super("TargetESP", FeatureCategory.Visuals);
        this.jelloMode = new ListSetting("Jello Color", "Client", () -> this.bebraPonyxana.currentMode.equals("Jello"), new String[] { "Astolfo", "Rainbow", "Client", "Custom" });
        this.bebraPonyxana = new ListSetting("TargetESP Mode", "Jello", () -> true, new String[] { "Jello", "Astolfo" });
        this.circlesize = new NumberSetting("Circle Size", 0.1f, 0.1f, 0.5f, 0.1f, () -> this.bebraPonyxana.currentMode.equalsIgnoreCase("Jello") || this.bebraPonyxana.currentMode.equalsIgnoreCase("Astolfo"));
        this.points = new NumberSetting("Points", 30.0f, 3.0f, 30.0f, 1.0f, () -> this.bebraPonyxana.currentMode.equalsIgnoreCase("Astolfo"));
        this.depthTest = new BooleanSetting("DepthTest", false, () -> this.bebraPonyxana.currentMode.equalsIgnoreCase("Jello"));
        this.targetEspColor = new ColorSetting("TargetESP Color", Color.PINK.getRGB(), () -> true);
        this.circleSpeed = new NumberSetting("Circle Speed", 0.01f, 0.001f, 0.05f, 0.001f, () -> this.bebraPonyxana.currentMode.equals("Jello"));
        this.addSettings(this.bebraPonyxana, this.jelloMode, this.circleSpeed, this.circlesize, this.points, this.targetEspColor, this.depthTest);
    }
    
    @EventTarget
    public void onRender(final EventRender3D event3D) {
        final String mode = this.bebraPonyxana.getOptions();
        this.setSuffix(mode);
        if (this.bebraPonyxana.currentMode.equals("Jello") && !KillAura.target.isDead) {
            if (KillAura.mc.player.getDistanceToEntity(KillAura.target) <= KillAura.range.getNumberValue()) {
                if (KillAura.target != null && Main.instance.featureManager.getFeature(KillAura.class).isEnabled() && KillAura.mc.player.getDistanceToEntity(KillAura.target) <= KillAura.range.getNumberValue()) {
                    final int oneColor = this.targetEspColor.getColorValue();
                    int color = 0;
                    final String currentMode = this.jelloMode.currentMode;
                    switch (currentMode) {
                        case "Client": {
                            color = ClientHelper.getClientColor().getRGB();
                            break;
                        }
                        case "Custom": {
                            color = oneColor;
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
                    final double x = KillAura.target.lastTickPosX + (KillAura.target.posX - KillAura.target.lastTickPosX) * KillAura.mc.timer.renderPartialTicks - KillAura.mc.getRenderManager().renderPosX;
                    final double y = KillAura.target.lastTickPosY + (KillAura.target.posY - KillAura.target.lastTickPosY) * KillAura.mc.timer.renderPartialTicks - KillAura.mc.getRenderManager().renderPosY;
                    final double z = KillAura.target.lastTickPosZ + (KillAura.target.posZ - KillAura.target.lastTickPosZ) * KillAura.mc.timer.renderPartialTicks - KillAura.mc.getRenderManager().renderPosZ;
                    this.circleValue += this.circleSpeed.getNumberValue() * (Minecraft.frameTime * 0.1);
                    final float targetHeight = (float)(0.5 * (1.0 + Math.sin(6.283185307179586 * (this.circleValue * 0.30000001192092896))));
                    final float size = KillAura.target.width + this.circlesize.getNumberValue();
                    final float endYValue = (float)((float)(KillAura.target.height * 1.0 + 0.2) * (double)targetHeight);
                    if (targetHeight > 0.99) {
                        this.canDown = true;
                    }
                    else if (targetHeight < 0.01) {
                        this.canDown = false;
                    }
                    GlStateManager.enableBlend();
                    GL11.glBlendFunc(770, 771);
                    GL11.glEnable(2848);
                    if (this.depthTest.getBoolValue()) {
                        GlStateManager.disableDepth();
                    }
                    GlStateManager.disableTexture2D();
                    GlStateManager.disableAlpha();
                    GL11.glLineWidth(2.0f);
                    GL11.glShadeModel(7425);
                    GL11.glDisable(2884);
                    GL11.glBegin(5);
                    final float alpha = (this.canDown ? (255.0f * targetHeight) : (255.0f * (1.0f - targetHeight))) / 255.0f;
                    final float red = (color >> 16 & 0xFF) / 255.0f;
                    final float green = (color >> 8 & 0xFF) / 255.0f;
                    final float blue = (color & 0xFF) / 255.0f;
                    for (int i = 0; i < 2166; ++i) {
                        RenderUtils.color(red, green, blue, alpha);
                        final double iSin = Math.sin(Math.toRadians(i)) * size;
                        final double iCos = Math.cos(Math.toRadians(i)) * size;
                        GL11.glVertex3d(x + iCos, y + endYValue, z - iSin);
                        RenderUtils.color(red, green, blue, 0.0);
                        GL11.glVertex3d(x + iCos, y + endYValue + (this.canDown ? (-0.5f * (1.0f - targetHeight)) : (0.5f * targetHeight)), z - iSin);
                    }
                    GL11.glEnd();
                    GL11.glBegin(2);
                    RenderUtils.color(color);
                    for (int i = 0; i < 361; ++i) {
                        final double iSin = Math.sin(Math.toRadians(i)) * size;
                        final double iCos = Math.cos(Math.toRadians(i)) * size;
                        GL11.glVertex3d(x + iCos, y + endYValue, z - iSin);
                    }
                    GL11.glEnd();
                    GlStateManager.enableAlpha();
                    GL11.glShadeModel(7424);
                    GL11.glDisable(2848);
                    GL11.glEnable(2884);
                    GlStateManager.enableTexture2D();
                    if (this.depthTest.getBoolValue()) {
                        GlStateManager.enableDepth();
                    }
                    GlStateManager.disableBlend();
                    GlStateManager.resetColor();
                }
            }
            else {
                this.circleAnim = 0.0;
            }
        }
        else if (mode.equalsIgnoreCase("Astolfo") && KillAura.target != null) {
            if (KillAura.target.getHealth() > 0.0f) {
                this.circleAnim += 0.014999999664723873 * Minecraft.frameTime / 10.0;
                RenderUtils.drawCircle3D(KillAura.target, this.circleAnim + 0.001, event3D.getPartialTicks(), (int)this.points.getNumberValue(), 4.0f, Color.black.getRGB());
                RenderUtils.drawCircle3D(KillAura.target, this.circleAnim - 0.001, event3D.getPartialTicks(), (int)this.points.getNumberValue(), 4.0f, Color.black.getRGB());
                RenderUtils.drawCircle3D(KillAura.target, this.circleAnim, event3D.getPartialTicks(), (int)this.points.getNumberValue(), 2.0f, this.targetEspColor.getColorValue());
                this.circleAnim = MathHelper.clamp(this.circleAnim, 0.0, KillAura.target.width + this.circlesize.getNumberValue() * 0.5f);
            }
            else {
                this.circleAnim = 0.0;
            }
        }
    }
}
