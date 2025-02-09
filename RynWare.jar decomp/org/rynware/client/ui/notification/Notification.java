// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.notification;

import net.minecraft.client.Minecraft;
import org.rynware.client.ui.clickgui.ScreenHelper;
import org.rynware.client.ui.font.MCFontRenderer;
import org.rynware.client.utils.math.TimerHelper;
import net.minecraft.client.gui.ScaledResolution;

public class Notification
{
    public final ScaledResolution sr;
    public static final int HEIGHT = 30;
    private final String title;
    private final String content;
    private final int time;
    private final NotificationMode type;
    private final TimerHelper timer;
    private final MCFontRenderer fontRenderer;
    public double x;
    public double y;
    public double notificationTimeBarWidth;
    private final ScreenHelper screenHelper;
    
    public Notification(final String title, final String content, final NotificationMode type, final int second, final MCFontRenderer fontRenderer) {
        this.sr = new ScaledResolution(Minecraft.getMinecraft());
        this.x = this.sr.getScaledWidth();
        this.y = this.sr.getScaledHeight();
        this.title = title;
        this.content = content;
        this.time = second;
        this.type = type;
        this.timer = new TimerHelper();
        this.fontRenderer = fontRenderer;
        this.screenHelper = new ScreenHelper((float)(this.sr.getScaledWidth() - this.getWidth() + this.getWidth()), (float)(this.sr.getScaledHeight() - 60));
    }
    
    public final int getWidth() {
        return Math.max(100, Math.max(this.fontRenderer.getStringWidth(this.title), this.fontRenderer.getStringWidth(this.content)) + 90);
    }
    
    public final String getTitle() {
        return this.title;
    }
    
    public final String getContent() {
        return this.content;
    }
    
    public final int getTime() {
        return this.time;
    }
    
    public final int getY() {
        return (int)this.y;
    }
    
    public final NotificationMode getType() {
        return this.type;
    }
    
    public final TimerHelper getTimer() {
        return this.timer;
    }
    
    public ScreenHelper getTranslate() {
        return this.screenHelper;
    }
}
