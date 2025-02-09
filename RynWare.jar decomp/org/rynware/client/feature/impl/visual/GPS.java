// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.visual;

import org.rynware.client.ui.notification.NotificationRenderer;
import org.rynware.client.ui.notification.NotificationMode;
import net.minecraft.util.text.TextFormatting;
import org.rynware.client.utils.other.ChatUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import org.rynware.client.utils.math.AnimationHelper;
import org.rynware.client.utils.math.RotationHelper;
import org.rynware.client.utils.render.RenderUtils;
import java.awt.Color;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;
import org.rynware.client.event.events.impl.render.EventRender2D;
import org.rynware.client.event.EventTarget;
import org.rynware.client.command.impl.GPSCommand;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.feature.Feature;

public class GPS extends Feature
{
    public GPS() {
        super("GPS", "\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u043f\u0443\u0442\u044c \u0434\u043e \u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442", FeatureCategory.Player);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate eventUpdate) {
        if (GPSCommand.mode.equalsIgnoreCase("on")) {
            this.setSuffix("" + GPSCommand.x + " " + GPSCommand.z);
        }
    }
    
    @EventTarget
    public void one(final EventRender2D event2D) {
        if (GPSCommand.mode.equalsIgnoreCase("on")) {
            GL11.glPushMatrix();
            final int x = event2D.getResolution().getScaledWidth() / 2;
            final int y = event2D.getResolution().getScaledHeight() / 2;
            GL11.glTranslatef((float)x, (float)y, 0.0f);
            GL11.glRotatef(getAngle(new BlockPos(Integer.parseInt(String.valueOf(GPSCommand.x)), 0, Integer.parseInt(String.valueOf(GPSCommand.z)))) % 360.0f + 180.0f, 0.0f, 0.0f, 1.0f);
            GL11.glTranslatef((float)(-x), (float)(-y), 0.0f);
            RenderUtils.drawBlurredShadow(x - 3.0f, (float)(y + 48), 5.0f, 10.0f, 15, new Color(255, 255, 255));
            RenderUtils.drawTriangle(x - 5.0f, (float)(y + 50), 5.0f, 10.0f, new Color(255, 255, 255).darker().getRGB(), new Color(255, 255, 255).getRGB());
            GL11.glTranslatef((float)x, (float)y, 0.0f);
            GL11.glRotatef(-(getAngle(new BlockPos(Integer.parseInt(String.valueOf(GPSCommand.x)), 0, Integer.parseInt(String.valueOf(GPSCommand.z)))) % 360.0f + 180.0f), 0.0f, 0.0f, 1.0f);
            GL11.glTranslatef((float)(-x), (float)(-y), 0.0f);
            GL11.glPopMatrix();
        }
    }
    
    public static float getAngle(final BlockPos entity) {
        return (float)(RotationHelper.getRotations(entity.getX(), 0.0, entity.getZ())[0] - AnimationHelper.Interpolate(GPS.mc.player.rotationYaw, GPS.mc.player.prevRotationYaw, 1.0));
    }
    
    @Override
    public void onEnable() {
        ChatUtils.addChatMessage(ChatFormatting.GREEN + "\u0420\u0459\u0420°\u0420\u0454 \u0420\u0451\u0421\u0403\u0420\u0457\u0420\u0455\u0420»\u0421\u040a\u0420·\u0420\u0455\u0420\u0406\u0420°\u0421\u201a\u0421\u040a? .gps <x> <y> <on/off>");
        NotificationRenderer.queue(TextFormatting.WHITE + "GPS", ChatFormatting.GREEN + "\u0420\u0459\u0420°\u0420\u0454 \u0420\u0451\u0421\u0403\u0420\u0457\u0420\u0455\u0420»\u0421\u040a\u0420·\u0420\u0455\u0420\u0406\u0420°\u0421\u201a\u0421\u040a? .gps <x> <y> <on/off>", 7, NotificationMode.INFO);
        super.onEnable();
    }
}
