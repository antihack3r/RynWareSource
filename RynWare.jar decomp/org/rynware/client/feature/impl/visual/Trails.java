// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.visual;

import java.util.Iterator;
import net.minecraft.client.renderer.GlStateManager;
import org.rynware.client.utils.render.ColorUtils;
import org.rynware.client.utils.render.ClientHelper;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import org.rynware.client.utils.render.RenderUtils;
import org.rynware.client.event.events.impl.render.EventRender3D;
import org.rynware.client.event.events.impl.player.EventPreMotion;
import org.rynware.client.event.EventTarget;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import net.minecraft.util.math.Vec3d;
import java.util.List;
import org.rynware.client.ui.settings.impl.ColorSetting;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import java.util.ArrayList;
import org.rynware.client.feature.Feature;

public class Trails extends Feature
{
    ArrayList<Point> points;
    public static ListSetting trailsMode;
    private final BooleanSetting timeoutBool;
    private final NumberSetting timeout;
    public static ListSetting colorMode;
    public static ColorSetting onecolor;
    public static ColorSetting twocolor;
    public static NumberSetting removeticks;
    public static NumberSetting alpha;
    public static BooleanSetting smoothending;
    public static NumberSetting saturation;
    private final BooleanSetting onlyMeTrails;
    List<Vec3d> path;
    
    public Trails() {
        super("Trails", FeatureCategory.Visuals);
        this.points = new ArrayList<Point>();
        this.timeoutBool = new BooleanSetting("Timeout", true, () -> Trails.trailsMode.currentMode.equalsIgnoreCase("Circle"));
        this.timeout = new NumberSetting("Time", 15.0f, 1.0f, 150.0f, 0.1f, () -> true);
        this.onlyMeTrails = new BooleanSetting("Only Me", true, () -> true);
        this.path = new ArrayList<Vec3d>();
        this.addSettings(Trails.trailsMode, Trails.colorMode, Trails.onecolor, Trails.twocolor, this.timeoutBool, this.timeout, Trails.saturation, Trails.removeticks, Trails.alpha, Trails.smoothending, this.onlyMeTrails);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        this.setSuffix(Trails.colorMode.currentMode);
    }
    
    @EventTarget
    public void onPreMotion(final EventPreMotion event) {
        if (Trails.trailsMode.currentMode.equalsIgnoreCase("Circle")) {
            if (Trails.mc.player.lastTickPosX != Trails.mc.player.posX || Trails.mc.player.lastTickPosY != Trails.mc.player.posY || Trails.mc.player.lastTickPosZ != Trails.mc.player.posZ) {
                this.path.add(new Vec3d(Trails.mc.player.posX, Trails.mc.player.posY, Trails.mc.player.posZ));
            }
            if (this.timeoutBool.getBoolValue()) {
                while (this.path.size() > (int)this.timeout.getNumberValue()) {
                    this.path.remove(0);
                }
            }
        }
    }
    
    @EventTarget
    public void onRender3DEvent(final EventRender3D event3D) {
        if (Trails.trailsMode.currentMode.equalsIgnoreCase("Circle")) {
            RenderUtils.renderBreadCrumbs(this.path);
        }
    }
    
    @EventTarget
    public void onRender(final EventRender3D event) {
        if (Trails.trailsMode.currentMode.equalsIgnoreCase("Line")) {
            if (Trails.mc.gameSettings.thirdPersonView == 1 || Trails.mc.gameSettings.thirdPersonView == 2) {
                final float x = (float)(Trails.mc.player.lastTickPosX + (Trails.mc.player.posX - Trails.mc.player.lastTickPosX) * event.getPartialTicks());
                final float y = (float)(Trails.mc.player.lastTickPosY + (Trails.mc.player.posY - Trails.mc.player.lastTickPosY) * event.getPartialTicks());
                final float z = (float)(Trails.mc.player.lastTickPosZ + (Trails.mc.player.posZ - Trails.mc.player.lastTickPosZ) * event.getPartialTicks());
                this.points.add(new Point(x, y, z));
                GL11.glPushMatrix();
                GL11.glDisable(3008);
                GL11.glEnable(3042);
                GL11.glEnable(2848);
                GL11.glDisable(3553);
                GL11.glBlendFunc(770, 771);
                GL11.glDisable(2884);
                for (final Point t : this.points) {
                    if (this.points.indexOf(t) >= this.points.size() - 1) {
                        continue;
                    }
                    final Point temp = this.points.get(this.points.indexOf(t) + 1);
                    float a = Trails.alpha.getNumberValue();
                    if (Trails.smoothending.getBoolValue()) {
                        a = Trails.alpha.getNumberValue() * (this.points.indexOf(t) / (float)this.points.size());
                    }
                    Color color = Color.WHITE;
                    final Color firstcolor = new Color(Trails.onecolor.getColorValue());
                    final String currentMode = Trails.colorMode.currentMode;
                    switch (currentMode) {
                        case "Client": {
                            color = ClientHelper.getClientColor(t.age / 16.0f, 5.0f, t.age, 5);
                            break;
                        }
                        case "Astolfo": {
                            color = ColorUtils.astolfo(t.age - t.age, t.age, Trails.saturation.getNumberValue(), 4.0f);
                            break;
                        }
                        case "Rainbow": {
                            color = ColorUtils.rainbow((int)(t.age * 16.0f), 0.5f, 1.0f);
                            break;
                        }
                        case "Pulse": {
                            color = ColorUtils.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 6.0f * (t.age / 16.0f) / 60.0f);
                            break;
                        }
                        case "Custom": {
                            color = ColorUtils.TwoColoreffect(new Color(Trails.onecolor.getColorValue()), new Color(Trails.twocolor.getColorValue()), Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 3.0f * (t.age / 16.0f) / 60.0f);
                            break;
                        }
                        case "Static": {
                            color = firstcolor;
                            break;
                        }
                    }
                    final Color c = RenderUtils.injectAlpha(color, (int)a);
                    GL11.glBegin(8);
                    final double x2 = t.x - Trails.mc.getRenderManager().renderPosX;
                    final double y2 = t.y - Trails.mc.getRenderManager().renderPosY;
                    final double z2 = t.z - Trails.mc.getRenderManager().renderPosZ;
                    final double x3 = temp.x - Trails.mc.getRenderManager().renderPosX;
                    final double y3 = temp.y - Trails.mc.getRenderManager().renderPosY;
                    final double z3 = temp.z - Trails.mc.getRenderManager().renderPosZ;
                    RenderUtils.glColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 0).getRGB());
                    GL11.glVertex3d(x2, y2 + Trails.mc.player.height - 0.1, z2);
                    RenderUtils.glColor(c.getRGB());
                    GL11.glVertex3d(x2, y2 + 0.2, z2);
                    GL11.glVertex3d(x3, y3 + Trails.mc.player.height - 0.1, z3);
                    GL11.glVertex3d(x3, y3 + 0.2, z3);
                    GL11.glEnd();
                    final Point point = t;
                    ++point.age;
                }
                GlStateManager.resetColor();
                GL11.glDisable(3042);
                GL11.glEnable(3008);
                GL11.glEnable(3553);
                GL11.glEnable(2884);
                GL11.glDisable(2848);
                GL11.glDisable(3042);
                GL11.glPopMatrix();
            }
            this.points.removeIf(p -> p.age >= Trails.removeticks.getNumberValue());
        }
    }
    
    @Override
    public void onDisable() {
        this.points.clear();
        super.onDisable();
    }
    
    public static Color getGradientOffset(final Color color1, final Color color2, double offset, final int alpha) {
        if (offset > 1.0) {
            final double left = offset % 1.0;
            final int off = (int)offset;
            offset = ((off % 2 == 0) ? left : (1.0 - left));
        }
        final double inverse_percent = 1.0 - offset;
        final int redPart = (int)(color1.getRed() * inverse_percent + color2.getRed() * offset);
        final int greenPart = (int)(color1.getGreen() * inverse_percent + color2.getGreen() * offset);
        final int bluePart = (int)(color1.getBlue() * inverse_percent + color2.getBlue() * offset);
        return new Color(redPart, greenPart, bluePart, alpha);
    }
    
    static {
        Trails.trailsMode = new ListSetting("Trails Mode", "Line", () -> true, new String[] { "Line", "Circle" });
        Trails.colorMode = new ListSetting("Trails Color", "Astolfo", () -> true, new String[] { "Astolfo", "Rainbow", "Pulse", "Custom", "Client", "Static" });
        Trails.onecolor = new ColorSetting("One Color", new Color(255, 255, 255).getRGB(), () -> Trails.colorMode.currentMode.equalsIgnoreCase("Static") || Trails.colorMode.currentMode.equalsIgnoreCase("Custom"));
        Trails.twocolor = new ColorSetting("Two Color", new Color(255, 255, 255).getRGB(), () -> Trails.colorMode.currentMode.equalsIgnoreCase("Custom"));
        Trails.removeticks = new NumberSetting("Remove Ticks", 100.0f, 1.0f, 500.0f, 1.0f, () -> Trails.trailsMode.currentMode.equalsIgnoreCase("Line"));
        Trails.alpha = new NumberSetting("Alpha Trails", 255.0f, 1.0f, 255.0f, 1.0f, () -> Trails.trailsMode.currentMode.equalsIgnoreCase("Line"));
        Trails.smoothending = new BooleanSetting("Smooth Ending", true, () -> Trails.trailsMode.currentMode.equalsIgnoreCase("Line"));
        Trails.saturation = new NumberSetting("Saturation", 0.7f, 0.1f, 1.0f, 0.1f, () -> Trails.colorMode.currentMode.equalsIgnoreCase("Astolfo"));
    }
    
    class Point
    {
        public final float x;
        public final float y;
        public final float z;
        public float age;
        
        public Point(final float x, final float y, final float z) {
            this.age = 0.0f;
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}
