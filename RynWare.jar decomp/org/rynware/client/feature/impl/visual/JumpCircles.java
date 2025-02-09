// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.visual;

import org.rynware.client.utils.math.animations.Direction;
import net.minecraft.util.math.Vec3d;
import org.rynware.client.utils.render.ColorUtils;
import java.awt.Color;
import java.util.Iterator;
import net.minecraft.client.entity.EntityPlayerSP;
import java.util.Collections;
import org.lwjgl.opengl.GL11;
import org.rynware.client.event.events.impl.render.EventRender3D;
import org.rynware.client.event.EventTarget;
import org.rynware.client.utils.math.animations.Animation;
import org.rynware.client.utils.math.animations.impl.DecelerateAnimation;
import net.minecraft.client.Minecraft;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.ui.settings.Setting;
import com.google.common.collect.Lists;
import org.rynware.client.utils.render.ClientHelper;
import org.rynware.client.feature.impl.FeatureCategory;
import java.util.List;
import org.rynware.client.ui.settings.impl.ColorSetting;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.feature.Feature;

public class JumpCircles extends Feature
{
    public final ListSetting colorMode;
    public final NumberSetting colorSpeed;
    public final ColorSetting color;
    public final ColorSetting analogColor;
    private final ListSetting mode;
    private final ListSetting jumpMode;
    private final NumberSetting delay;
    private final List<Circle> circles;
    private final NumberSetting startRange;
    private final NumberSetting Range;
    
    public JumpCircles() {
        super("JumpCircles", FeatureCategory.Visuals);
        this.colorMode = new ListSetting("Color Mode", "Analogous", () -> true, new String[] { "Astolfo", "Rainbow", "Fade", "Analogous" });
        this.colorSpeed = new NumberSetting("Color Speed", 12.0f, 2.0f, 30.0f, 1.0f, () -> true);
        this.color = new ColorSetting("Color", ClientHelper.getClientColor().getRGB(), () -> this.colorMode.getCurrentMode().equalsIgnoreCase("Fade") || this.colorMode.getCurrentMode().equalsIgnoreCase("Double Color") || this.colorMode.getCurrentMode().equalsIgnoreCase("Analogous"));
        this.analogColor = new ColorSetting("Analogous Color", ClientHelper.getClientColor().getRGB(), () -> this.colorMode.getCurrentMode().equalsIgnoreCase("Analogous"));
        this.mode = new ListSetting("Mode", "Default", () -> true, new String[] { "Medusa", "Default" });
        this.jumpMode = new ListSetting("JumpMode", "Jump", () -> true, new String[] { "Jump" });
        this.delay = new NumberSetting("Delay (ms)", 1550.0f, 300.0f, 10000.0f, 1.0f, () -> true);
        this.circles = Lists.newArrayList();
        this.startRange = new NumberSetting("Start multiplier", 1.0f, 1.0f, 3.0f, 0.01f, () -> this.mode.currentMode.equals("Default"));
        this.Range = new NumberSetting("Range", 0.2f, 0.05f, 1.0f, 0.001f, () -> this.mode.currentMode.equals("Default"));
        this.addSettings(this.colorMode, this.colorSpeed, this.color, this.Range, this.analogColor, this.mode, this.jumpMode, this.delay, this.startRange);
    }
    
    @EventTarget
    public void onJump(final EventUpdate e) {
        if (JumpCircles.mc.player.motionY == 0.33319999363422365 && !JumpCircles.mc.player.otherCheck()) {
            this.circles.add(new Circle(Minecraft.getMinecraft().player.getPositionVector(), this.getColor(0), new DecelerateAnimation((int)this.delay.getNumberValue(), 1.0), new DecelerateAnimation((int)this.delay.getNumberValue() + 50000, 1000.0)));
        }
    }
    
    @EventTarget
    public void onRender3d(final EventRender3D event) {
        this.startRange.setMinValue(0.1f);
        if (this.circles.isEmpty()) {
            return;
        }
        final EntityPlayerSP player = JumpCircles.mc.player;
        final int delay = (int)this.delay.getNumberValue();
        final Minecraft mc = Minecraft.getMinecraft();
        final double ix = -(player.lastTickPosX + (player.posX - player.lastTickPosX) * mc.getRenderPartialTicks());
        final double iy = -(player.lastTickPosY + (player.posY - player.lastTickPosY) * mc.getRenderPartialTicks());
        final double iz = -(player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * mc.getRenderPartialTicks());
        GL11.glPushMatrix();
        GL11.glTranslated(ix, iy, iz);
        GL11.glDisable(2884);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(3008);
        GL11.glDisable(2929);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        GL11.glAlphaFunc(516, 0.1f);
        Collections.reverse(this.circles);
        for (final Circle c : this.circles) {
            final float range = this.Range.getNumberValue();
            final double x = c.getPosition().xCoord;
            final double y = c.getPosition().yCoord - 0.30000001192092896;
            final double z = c.getPosition().zCoord;
            final float k = c.animation2.duration / (float)delay;
            final float start = k * this.startRange.getNumberValue() + 0.2f;
            final float start2 = k * this.startRange.getNumberValue() + 0.1f * k * 2.0f - k;
            final float end = start + range * 1.7f;
            final float end2 = start - 0.2f;
            GL11.glDisable(2832);
            GL11.glBegin(8);
            for (int i = 0; i <= 360; ++i) {
                final Color color = this.getColor(i);
                GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.7f * (1.0f - c.animation2.duration / (delay * 0.84f)));
                GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * start * 2.0, y, z + Math.sin(Math.toRadians(i)) * start * 2.0);
                GL11.glColor4f(c.getColor().getRed() / 255.0f, c.getColor().getGreen() / 255.0f, c.getColor().getBlue() / 255.0f, 1.0E-11f * (0.81f - c.animation2.duration / (delay * 0.14f)));
                GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * end * 2.0, y, z + Math.sin(Math.toRadians(i)) * end * 2.0);
            }
            for (int i = 0; i <= 360; ++i) {
                final Color color = this.getColor(i);
                GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, 0.7f * (1.0f - c.animation2.duration / (delay * 0.84f)));
                GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * start * 2.0, y, z + Math.sin(Math.toRadians(i)) * start * 2.0);
                GL11.glColor4f(c.getColor().getRed() / 255.0f, c.getColor().getGreen() / 255.0f, c.getColor().getBlue() / 255.0f, 1.0E-11f * (0.81f - c.animation2.duration / (delay * 0.14f)));
                GL11.glVertex3d(x + Math.cos(Math.toRadians(i)) * end2 * 2.0, y, z + Math.sin(Math.toRadians(i)) * end2 * 2.0);
            }
            GL11.glEnd();
        }
        Collections.reverse(this.circles);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glShadeModel(7424);
        GL11.glEnable(2884);
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glPopMatrix();
        Collections.reverse(this.circles);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glShadeModel(7424);
        GL11.glEnable(2884);
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glPopMatrix();
    }
    
    private Color getColor(final int count) {
        final int index = count;
        return ColorUtils.astolfo(4.0f, (float)index);
    }
    
    static class Circle
    {
        private final Vec3d vec;
        private final Color color;
        public final Animation animation1;
        public final Animation animation2;
        
        Circle(final Vec3d vec, final Color color, final Animation anim, final Animation anim2) {
            this.vec = vec;
            this.color = color;
            this.animation1 = anim;
            this.animation2 = anim2;
        }
        
        public Vec3d getPosition() {
            return this.vec;
        }
        
        public Color getColor() {
            return this.color;
        }
        
        public boolean update() {
            return this.animation2.finished(Direction.FORWARDS);
        }
    }
}
