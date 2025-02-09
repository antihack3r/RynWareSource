// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.button;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.init.SoundEvents;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.renderer.GlStateManager;
import org.rynware.client.utils.render.RenderUtils;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Mouse;
import org.rynware.client.ui.font.MCFontRenderer;
import net.minecraft.client.gui.GuiButton;

public class GuiMainMenuButton extends GuiButton
{
    private int fade;
    private MCFontRenderer fontRenderer;
    private boolean blurred;
    
    public GuiMainMenuButton(final MCFontRenderer font, final int buttonId, final int x, final int y, final String buttonText, final boolean blurred) {
        this(font, buttonId, x, y, 200, 35, buttonText, blurred);
        this.fontRenderer = font;
        this.blurred = blurred;
    }
    
    public GuiMainMenuButton(final MCFontRenderer font, final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText, final boolean blurred) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.fade = 20;
        this.fontRenderer = font;
        this.blurred = blurred;
    }
    
    public static int getMouseX() {
        return Mouse.getX() * GuiMainMenuButton.sr.getScaledWidth() / Minecraft.getMinecraft().displayWidth;
    }
    
    public static int getMouseY() {
        return GuiMainMenuButton.sr.getScaledHeight() - Mouse.getY() * GuiMainMenuButton.sr.getScaledHeight() / Minecraft.getMinecraft().displayHeight - 1;
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY, final float mouseButton) {
        if (this.visible) {
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height + 10);
            Color text = new Color(241, 246, 246, 204);
            final Color color = new Color(this.fade + 14, this.fade + 14, this.fade + 14, 105);
            if (this.hovered) {
                if (this.fade < 100) {
                    this.fade += 8;
                }
                text = RenderUtils.injectAlpha(Color.white, 255);
            }
            else if (this.fade > 20) {
                this.fade -= 8;
            }
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            final int height = this.height + 11;
            RenderUtils.drawShadow(15.0f, 1.0f, () -> RenderUtils.drawBlurredShadow((float)this.xPosition, (float)this.yPosition, (float)this.width, (float)height, 2, color));
            this.fontRenderer.drawCenteredBlurredStringWithShadow(this.displayString, this.xPosition + this.width / 2.0f, this.yPosition + (this.height - 5), 13, RenderUtils.injectAlpha(text, this.blurred ? 31 : 0), text.getRGB());
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }
    
    @Override
    protected void mouseDragged(final Minecraft mc, final int mouseX, final int mouseY) {
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY) {
    }
    
    @Override
    public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
        return this.enabled && this.visible && mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height + 10;
    }
    
    @Override
    public boolean isMouseOver() {
        return this.hovered;
    }
    
    @Override
    public void drawButtonForegroundLayer(final int mouseX, final int mouseY) {
    }
    
    @Override
    public void playPressSound(final SoundHandler soundHandlerIn) {
        soundHandlerIn.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0f));
    }
    
    @Override
    public int getButtonWidth() {
        return this.width;
    }
    
    @Override
    public void setWidth(final int width) {
        this.width = width;
    }
}
