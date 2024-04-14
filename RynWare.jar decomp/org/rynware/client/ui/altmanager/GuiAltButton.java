// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.altmanager;

import org.rynware.client.utils.render.RenderUtils;
import java.awt.Color;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class GuiAltButton extends GuiButton
{
    private int opacity;
    
    public GuiAltButton(final int buttonId, final int x, final int y, final String buttonText) {
        this(buttonId, x, y, 200, 20, buttonText);
    }
    
    public GuiAltButton(final int buttonId, final int x, final int y, final int widthIn, final int heightIn, final String buttonText) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.opacity = 40;
    }
    
    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY, final float mouseButton) {
        if (this.visible) {
            mc.getTextureManager().bindTexture(GuiAltButton.BUTTON_TEXTURES);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.hovered = (mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height);
            if (this.hovered) {
                if (this.opacity < 40) {
                    ++this.opacity;
                }
            }
            else if (this.opacity > 22) {
                --this.opacity;
            }
            final boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            final Color color = new Color(25, 25, 25, 73);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.blendFunc(770, 771);
            if (flag) {
                RenderUtils.drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, new Color(this.opacity, this.opacity, this.opacity, 150).getRGB());
                mc.sfui18.drawCenteredString(this.displayString, (float)(this.xPosition + this.width / 2), (float)(this.yPosition + (this.height - 2) / 3), -1);
            }
            else {
                RenderUtils.drawOutlineRect((float)this.xPosition, (float)this.yPosition, (float)this.width, (float)this.height, color, new Color(255, 255, 255, 10));
                mc.sfui18.drawCenteredString(this.displayString, (float)(this.xPosition + this.width / 2), (float)(this.yPosition + (this.height - 2) / 3), -1);
            }
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }
}
