// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.visual;

import java.awt.Color;
import java.util.ArrayList;
import net.minecraft.util.math.Vec3d;
import java.util.Iterator;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import java.util.Collections;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import org.rynware.client.event.events.impl.render.EventRender3D;
import org.rynware.client.event.EventTarget;
import net.minecraft.entity.Entity;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.ColorSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import java.util.List;
import org.rynware.client.feature.Feature;

public class JumpCircle extends Feature
{
    static final int TYPE = 0;
    static final byte MAX_JC_TIME = 20;
    static List<Circle> circles;
    private ListSetting jumpcircleMode;
    public static ColorSetting jumpCircleColor;
    static float pt;
    
    public JumpCircle() {
        super("JumpCircles", FeatureCategory.Visuals);
        this.jumpcircleMode = new ListSetting("JumpCircle Mode", "Default", () -> true, new String[] { "Default", "Disc" });
        this.addSettings(this.jumpcircleMode, JumpCircle.jumpCircleColor);
    }
    
    @EventTarget
    public void onJump(final EventUpdate event) {
        if (JumpCircle.mc.player.motionY == 0.33319999363422365 && !JumpCircle.mc.player.otherCheck()) {
            handleEntityJump(JumpCircle.mc.player);
        }
        onLocalPlayerUpdate(JumpCircle.mc.player);
    }
    
    @EventTarget
    public void onRender(final EventRender3D event) {
        final String mode = this.jumpcircleMode.getOptions();
        final EntityPlayerSP client = Minecraft.getMinecraft().player;
        final Minecraft mc = Minecraft.getMinecraft();
        final double ix = -(client.lastTickPosX + (client.posX - client.lastTickPosX) * mc.getRenderPartialTicks());
        final double iy = -(client.lastTickPosY + (client.posY - client.lastTickPosY) * mc.getRenderPartialTicks());
        final double iz = -(client.lastTickPosZ + (client.posZ - client.lastTickPosZ) * mc.getRenderPartialTicks());
        if (mode.equalsIgnoreCase("Disc")) {
            GL11.glPushMatrix();
            GL11.glTranslated(ix, iy, iz);
            GL11.glDisable(2884);
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glDisable(3008);
            GL11.glDisable(2929);
            GL11.glBlendFunc(770, 771);
            GL11.glShadeModel(7425);
            Collections.reverse(JumpCircle.circles);
            try {
                for (final Circle c : JumpCircle.circles) {
                    final float k = c.existed / 20.0f;
                    final double x = c.position().xCoord;
                    final double y = c.position().yCoord - k * 0.5;
                    final double z = c.position().zCoord;
                    final float start = k;
                    final float end = start + 1.0f - k;
                    GL11.glBegin(8);
                    for (int i = 0; i <= 360; i += 5) {
                        GL11.glColor4f((float)c.color().xCoord, (float)c.color().yCoord, (float)c.color().zCoord, 0.2f * (1.0f - c.existed / 20.0f));
                        GL11.glVertex3d(x + Math.cos(Math.toRadians(i * 4)) * start, y, z + Math.sin(Math.toRadians(i * 4)) * start);
                        GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.01f * (1.0f - c.existed / 20.0f));
                        GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * end, y + Math.sin(k * 8.0f) * 0.5, z + Math.sin(Math.toRadians(i) * end));
                    }
                    GL11.glEnd();
                }
            }
            catch (final Exception ex) {}
            Collections.reverse(JumpCircle.circles);
            GL11.glEnable(3553);
            GL11.glDisable(3042);
            GL11.glShadeModel(7424);
            GL11.glEnable(2884);
            GL11.glEnable(2929);
            GL11.glEnable(3008);
            GlStateManager.resetColor();
            GL11.glPopMatrix();
        }
        else if (mode.equalsIgnoreCase("Default")) {
            GL11.glPushMatrix();
            GL11.glTranslated(ix, iy, iz);
            GL11.glDisable(2884);
            GL11.glEnable(3042);
            GL11.glDisable(3553);
            GL11.glDisable(3008);
            GL11.glBlendFunc(770, 771);
            GL11.glShadeModel(7425);
            Collections.reverse(JumpCircle.circles);
            for (final Circle c : JumpCircle.circles) {
                final int red = (int)((JumpCircle.jumpCircleColor.getColorValue() >> 16 & 0xFF) / 100.0f);
                final int green = (int)((JumpCircle.jumpCircleColor.getColorValue() >> 8 & 0xFF) / 100.0f);
                final int blue = (int)((JumpCircle.jumpCircleColor.getColorValue() & 0xFF) / 100.0f);
                final double x2 = c.position().xCoord;
                final double y2 = c.position().yCoord;
                final double z2 = c.position().zCoord;
                final float j = c.existed / 20.0f;
                final float start2 = j * 1.5f;
                final float end2 = start2 + 0.5f - j;
                GL11.glBegin(8);
                for (int l = 0; l <= 360; l += 5) {
                    GL11.glColor4f((float)c.color().xCoord, (float)c.color().yCoord, (float)c.color().zCoord, 0.7f * (1.0f - c.existed / 35.0f));
                    GL11.glVertex3d(x2 + Math.cos(Math.toRadians(l)) * start2, y2, z2 + Math.sin(Math.toRadians(l)) * start2);
                    GL11.glColor4f((float)red, (float)green, (float)blue, 0.01f * (1.0f - c.existed / 35.0f));
                    GL11.glVertex3d(x2 + Math.cos(Math.toRadians(l)) * end2, y2, z2 + Math.sin(Math.toRadians(l)) * end2);
                }
                GL11.glEnd();
            }
            Collections.reverse(JumpCircle.circles);
            GL11.glEnable(3553);
            GL11.glDisable(3042);
            GL11.glShadeModel(7424);
            GL11.glEnable(2884);
            GL11.glEnable(3008);
            GlStateManager.resetColor();
            GL11.glPopMatrix();
        }
    }
    
    public static void onLocalPlayerUpdate(final EntityPlayerSP instance) {
        JumpCircle.circles.removeIf(Circle::update);
    }
    
    public static void handleEntityJump(final Entity entity) {
        final int red = (int)((JumpCircle.jumpCircleColor.getColorValue() >> 16 & 0xFF) / 100.0f);
        final int green = (int)((JumpCircle.jumpCircleColor.getColorValue() >> 8 & 0xFF) / 100.0f);
        final int blue = (int)((JumpCircle.jumpCircleColor.getColorValue() & 0xFF) / 100.0f);
        final Vec3d color = new Vec3d(red, green, blue);
        JumpCircle.circles.add(new Circle(entity.getPositionVector(), color));
    }
    
    static {
        JumpCircle.circles = new ArrayList<Circle>();
        JumpCircle.jumpCircleColor = new ColorSetting("JumpCircle Color", new Color(16777215).getRGB(), () -> true);
    }
    
    static class Circle
    {
        private final Vec3d vec;
        private final Vec3d color;
        byte existed;
        
        Circle(final Vec3d vec, final Vec3d color) {
            this.vec = vec;
            this.color = color;
        }
        
        Vec3d position() {
            return this.vec;
        }
        
        Vec3d color() {
            return this.color;
        }
        
        boolean update() {
            final byte existed = (byte)(this.existed + 1);
            this.existed = existed;
            return existed > 20;
        }
    }
}
