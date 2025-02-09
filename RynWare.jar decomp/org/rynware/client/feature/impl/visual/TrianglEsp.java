// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.visual;

import org.rynware.client.event.EventTarget;
import java.util.Iterator;
import org.rynware.client.utils.render.RenderUtils;
import org.rynware.client.utils.math.RotationHelper;
import org.lwjgl.opengl.GL11;
import org.rynware.client.utils.render.ClientHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.client.gui.ScaledResolution;
import org.rynware.client.event.events.impl.render.EventRender2D;
import org.rynware.client.ui.settings.Setting;
import java.awt.Color;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.ColorSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.feature.Feature;

public class TrianglEsp extends Feature
{
    public static ListSetting triMod;
    public static ColorSetting triColor;
    
    public TrianglEsp() {
        super("TriangleEsp", "\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u0441\u0442\u0440\u0435\u043b\u043a\u0438 \u043d\u0430 \u0438\u0433\u0440\u043e\u043a\u043e\u0432", FeatureCategory.Visuals);
        TrianglEsp.triMod = new ListSetting("TriangleEsp Mode", "Client", () -> true, new String[] { "Client", "Custom" });
        TrianglEsp.triColor = new ColorSetting("TriangleEsp Color", Color.PINK.getRGB(), () -> TrianglEsp.triMod.currentMode.equalsIgnoreCase("Custom"));
        this.addSettings(TrianglEsp.triMod, TrianglEsp.triColor);
    }
    
    @EventTarget
    public void onE2d(final EventRender2D event) {
        final ScaledResolution sr = new ScaledResolution(TrianglEsp.mc);
        final float size = 50.0f;
        final float xOffset = sr.getScaledWidth() / 2.0f - 24.5f;
        final float yOffset = sr.getScaledHeight() / 2.0f - 25.2f;
        for (final Entity entity : TrianglEsp.mc.world.loadedEntityList) {
            if (entity != TrianglEsp.mc.player) {
                if (!(entity instanceof EntityPlayer)) {
                    continue;
                }
                int color = 0;
                final String currentMode = TrianglEsp.triMod.currentMode;
                switch (currentMode) {
                    case "Client": {
                        color = ClientHelper.getClientColor().getRGB();
                        break;
                    }
                    case "Custom": {
                        color = TrianglEsp.triColor.getColorValue();
                        break;
                    }
                }
                GL11.glPushMatrix();
                final int x = event.getResolution().getScaledWidth() / 2;
                final int y = event.getResolution().getScaledHeight() / 2;
                GL11.glTranslatef((float)x, (float)y, 0.0f);
                GL11.glRotatef(RotationHelper.getAngle(entity) % 360.0f + 180.0f, 0.0f, 0.0f, 1.0f);
                GL11.glTranslatef((float)(-x), (float)(-y), 0.0f);
                RenderUtils.drawTriangle(x - 5.0f, (float)(y + 50), 5.0f, 10.0f, new Color(color).darker().getRGB(), color);
                GL11.glTranslatef((float)x, (float)y, 0.0f);
                GL11.glRotatef(-(RotationHelper.getAngle(entity) % 360.0f + 180.0f), 0.0f, 0.0f, 1.0f);
                GL11.glTranslatef((float)(-x), (float)(-y), 0.0f);
                GL11.glPopMatrix();
            }
        }
    }
}
