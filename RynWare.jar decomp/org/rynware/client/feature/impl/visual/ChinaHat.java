// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.visual;

import org.rynware.client.event.EventTarget;
import net.minecraft.item.ItemStack;
import org.rynware.client.utils.render.RenderUtils;
import org.rynware.client.utils.render.ColorUtils;
import org.rynware.client.utils.render.ClientHelper;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemArmor;
import org.rynware.client.event.events.impl.render.EventRender3D;
import org.rynware.client.ui.settings.Setting;
import java.awt.Color;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.ColorSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.feature.Feature;

public class ChinaHat extends Feature
{
    final ListSetting colorMode;
    final ColorSetting onecolor;
    final ColorSetting twocolor;
    final NumberSetting saturation;
    final BooleanSetting hideInFirstPerson;
    
    public ChinaHat() {
        super("ChinaHat", FeatureCategory.Visuals);
        this.colorMode = new ListSetting("Hat Color", "Astolfo", () -> true, new String[] { "Astolfo", "Pulse", "China", "Custom", "Client", "Static" });
        this.onecolor = new ColorSetting("One Color", new Color(255, 255, 255).getRGB(), () -> this.colorMode.currentMode.equalsIgnoreCase("Static") || this.colorMode.currentMode.equalsIgnoreCase("Custom"));
        this.twocolor = new ColorSetting("Two Color", new Color(255, 255, 255).getRGB(), () -> this.colorMode.currentMode.equalsIgnoreCase("Custom"));
        this.saturation = new NumberSetting("Saturation", 0.7f, 0.1f, 1.0f, 0.1f, () -> this.colorMode.currentMode.equalsIgnoreCase("Astolfo"));
        this.hideInFirstPerson = new BooleanSetting("Hide In First Person", true, () -> true);
        this.addSettings(this.colorMode, this.onecolor, this.twocolor, this.saturation, this.hideInFirstPerson);
    }
    
    @EventTarget
    public void asf(final EventRender3D event) {
        if (ChinaHat.mc.gameSettings.thirdPersonView == 0 && this.hideInFirstPerson.getBoolValue()) {
            return;
        }
        final ItemStack stack = ChinaHat.mc.player.getEquipmentInSlot(4);
        final double height = (stack.getItem() instanceof ItemArmor) ? (ChinaHat.mc.player.isSneaking() ? -0.18 : 0.04) : (ChinaHat.mc.player.isSneaking() ? -0.24 : -0.02);
        GlStateManager.pushMatrix();
        GL11.glDisable(2884);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(3008);
        GL11.glBlendFunc(770, 771);
        GL11.glShadeModel(7425);
        GL11.glTranslatef(0.0f, (float)(ChinaHat.mc.player.height + height), 0.0f);
        GL11.glRotatef(-ChinaHat.mc.player.rotationYaw, 0.0f, 1.0f, 0.0f);
        Color color2 = Color.WHITE;
        final Color firstcolor2 = new Color(this.onecolor.getColorValue());
        final String currentMode = this.colorMode.currentMode;
        switch (currentMode) {
            case "Client": {
                color2 = ClientHelper.getClientColor(5.0f, 1.0f, 5);
                break;
            }
            case "Astolfo": {
                color2 = ColorUtils.astolfo(5.0f, 5.0f, this.saturation.getNumberValue(), 10.0f);
                break;
            }
            case "Pulse": {
                color2 = ColorUtils.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 0.0);
                break;
            }
            case "China": {
                color2 = ColorUtils.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 0.0);
                break;
            }
            case "Custom": {
                color2 = ColorUtils.TwoColoreffect(new Color(this.onecolor.getColorValue()), new Color(this.twocolor.getColorValue()), Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 0.0);
                break;
            }
            case "Static": {
                color2 = firstcolor2;
                break;
            }
        }
        GL11.glBegin(6);
        RenderUtils.glColor(color2, 255);
        GL11.glVertex3d(0.0, 0.3, 0.0);
        for (float i = 0.0f; i < 360.5; ++i) {
            Color color3 = Color.WHITE;
            final Color firstcolor3 = new Color(this.onecolor.getColorValue());
            final String currentMode2 = this.colorMode.currentMode;
            switch (currentMode2) {
                case "Client": {
                    color3 = ClientHelper.getClientColor(i / 16.0f, i, 5);
                    break;
                }
                case "Astolfo": {
                    color3 = ColorUtils.astolfo(i - i + 1.0f, i, this.saturation.getNumberValue(), 10.0f);
                    break;
                }
                case "Pulse": {
                    color3 = ColorUtils.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 6.0f * (i / 16.0f) / 60.0f);
                    break;
                }
                case "China": {
                    color3 = ColorUtils.TwoColoreffect(new Color(255, 50, 50), new Color(79, 9, 9), Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 6.0f * (i - i / 16.0f) / 60.0f);
                    break;
                }
                case "Custom": {
                    color3 = ColorUtils.TwoColoreffect(new Color(this.onecolor.getColorValue()), new Color(this.twocolor.getColorValue()), Math.abs(System.currentTimeMillis() / 10L) / 100.0 + 3.0f * (i / 16.0f) / 60.0f);
                    break;
                }
                case "Static": {
                    color3 = firstcolor3;
                    break;
                }
            }
            RenderUtils.glColor(color3, 180);
            GL11.glVertex3d(Math.cos(i * 3.141592653589793 / 180.0) * 0.66, 0.0, Math.sin(i * 3.141592653589793 / 180.0) * 0.66);
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glShadeModel(7424);
        GL11.glEnable(2884);
        GL11.glEnable(3008);
        GlStateManager.resetColor();
        GlStateManager.popMatrix();
    }
}
