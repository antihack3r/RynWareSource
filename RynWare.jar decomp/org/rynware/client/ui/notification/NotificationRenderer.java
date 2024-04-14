// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.notification;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Iterator;
import org.rynware.client.utils.render.RoundedUtil;
import org.rynware.client.utils.render.RenderUtils;
import org.rynware.client.utils.render.ColorUtils;
import org.rynware.client.utils.render.ClientHelper;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import org.rynware.client.ui.clickgui.ClickGuiScreen;
import org.rynware.client.feature.Feature;
import org.rynware.client.feature.impl.hud.Notifications;
import org.rynware.client.Main;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import java.util.List;
import org.rynware.client.utils.Helper;

public final class NotificationRenderer implements Helper
{
    private static final List<Notification> NOTIFICATIONS;
    
    public static void queue(final String title, final String content, final int second, final NotificationMode type) {
        NotificationRenderer.NOTIFICATIONS.add(new Notification(title, content, type, second * 1000, Minecraft.getMinecraft().neverlose500_18));
    }
    
    public static void publish(final ScaledResolution sr) {
        if (Main.instance.featureManager.getFeature(Notifications.class).isEnabled() && Notifications.notifMode.currentMode.equalsIgnoreCase("Rect") && !NotificationRenderer.mc.gameSettings.showDebugInfo && NotificationRenderer.mc.world != null && !(NotificationRenderer.mc.currentScreen instanceof ClickGuiScreen) && !NotificationRenderer.NOTIFICATIONS.isEmpty()) {
            int y = sr.getScaledHeight() - 30;
            for (final Notification notification : NotificationRenderer.NOTIFICATIONS) {
                final double better = Minecraft.getMinecraft().neverlose500_18.getStringWidth(notification.getTitle() + " " + notification.getContent());
                if (!notification.getTimer().hasReached(notification.getTime() / 2)) {
                    notification.notificationTimeBarWidth = 360.0;
                }
                else {
                    notification.notificationTimeBarWidth = MathHelper.EaseOutBack((float)notification.notificationTimeBarWidth, 0.0f, (float)(4.0 * Main.deltaTime()));
                }
                if (!notification.getTimer().hasReached(notification.getTime())) {
                    notification.x = MathHelper.EaseOutBack((float)notification.x, (float)(notification.sr.getScaledWidth() - better), (float)(5.0 * Main.deltaTime()));
                    notification.y = MathHelper.EaseOutBack((float)notification.y, (float)y, (float)(5.0 * Main.deltaTime()));
                }
                else {
                    notification.x = MathHelper.EaseOutBack((float)notification.x, (float)(notification.sr.getScaledWidth() + 50), (float)(5.0 * Main.deltaTime()));
                    notification.y = MathHelper.EaseOutBack((float)notification.y, (float)y, (float)(5.0 * Main.deltaTime()));
                    if (notification.x > notification.sr.getScaledWidth() + 24 && NotificationRenderer.mc.player != null && NotificationRenderer.mc.world != null && !NotificationRenderer.mc.gameSettings.showDebugInfo) {
                        NotificationRenderer.NOTIFICATIONS.remove(notification);
                    }
                }
                GlStateManager.pushMatrix();
                GlStateManager.disableBlend();
                Color c = new Color(0, 0, 0, 0);
                final String currentMode = Notifications.notifColorModes.currentMode;
                switch (currentMode) {
                    case "Custom": {
                        c = new Color(Notifications.notifColor.getColorValue());
                        break;
                    }
                    case "Client": {
                        c = ClientHelper.getClientColor();
                        break;
                    }
                    case "Rainbow": {
                        c = ColorUtils.rainbow(20, 1.0f, 1.0f);
                        break;
                    }
                    case "Astolfo": {
                        c = ColorUtils.astolfo(4.0f, 0.0f);
                        break;
                    }
                }
                if (Notifications.notifBlur.getBoolValue()) {
                    RenderUtils.drawBlurredShadow((float)notification.x, (float)notification.y, NotificationRenderer.mc.rubik_17.getStringWidth(notification.getContent()) + 15.0f, 15.0f, 12, RenderUtils.injectAlpha(c, (int)Notifications.blurAlpha.getNumberValue()));
                }
                RoundedUtil.drawRoundOutline((float)notification.x, (float)notification.y, NotificationRenderer.mc.rubik_17.getStringWidth(notification.getContent()) + 15.0f, 15.0f, 5.0f, 1.0f, new Color(30, 28, 28, 187), c);
                Minecraft.getMinecraft().rubik_17.drawString(notification.getContent(), (float)(notification.x + 10.0), (float)(notification.y + 3.5), -1);
                GlStateManager.popMatrix();
                y -= 23;
            }
        }
    }
    
    static {
        NOTIFICATIONS = new CopyOnWriteArrayList<Notification>();
    }
}
