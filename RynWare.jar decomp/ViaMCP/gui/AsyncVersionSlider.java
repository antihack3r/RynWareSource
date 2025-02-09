// 
// Decompiled by Procyon v0.6.0
// 

package ViaMCP.gui;

import net.minecraft.client.renderer.GlStateManager;
import ViaMCP.ViaMCP;
import net.minecraft.util.math.MathHelper;
import net.minecraft.client.Minecraft;
import java.util.List;
import java.util.Collections;
import java.util.Arrays;
import ViaMCP.protocols.ProtocolCollection;
import net.minecraft.client.gui.GuiButton;

public class AsyncVersionSlider extends GuiButton
{
    private float dragValue;
    private final ProtocolCollection[] values;
    private float sliderValue;
    public boolean dragging;
    
    public AsyncVersionSlider(final int buttonId, final int x, final int y, final int widthIn, final int heightIn) {
        super(buttonId, x, y, Math.max(widthIn, 110), heightIn, "");
        this.dragValue = (ProtocolCollection.values().length - Arrays.asList(ProtocolCollection.values()).indexOf(ProtocolCollection.getProtocolCollectionById(340))) / (float)ProtocolCollection.values().length;
        this.values = ProtocolCollection.values();
        Collections.reverse(Arrays.asList(this.values));
        this.sliderValue = this.dragValue;
        this.displayString = this.values[(int)(this.sliderValue * (this.values.length - 1))].getVersion().getName();
    }
    
    public void func_191745_a(final Minecraft mc, final int mouseX, final int mouseY, final float button) {
        super.drawButton(mc, mouseX, mouseY, button);
    }
    
    @Override
    protected int getHoverState(final boolean mouseOver) {
        return 0;
    }
    
    @Override
    protected void mouseDragged(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            if (this.dragging) {
                this.sliderValue = (mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
                this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0f, 1.0f);
                this.dragValue = this.sliderValue;
                this.displayString = this.values[(int)(this.sliderValue * (this.values.length - 1))].getVersion().getName();
                ViaMCP.getInstance().setVersion(this.values[(int)(this.sliderValue * (this.values.length - 1))].getVersion().getVersion());
            }
            mc.getTextureManager().bindTexture(AsyncVersionSlider.BUTTON_TEXTURES);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }
    
    @Override
    public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            this.sliderValue = (mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
            this.sliderValue = MathHelper.clamp(this.sliderValue, 0.0f, 1.0f);
            this.dragValue = this.sliderValue;
            this.displayString = this.values[(int)(this.sliderValue * (this.values.length - 1))].getVersion().getName();
            ViaMCP.getInstance().setVersion(this.values[(int)(this.sliderValue * (this.values.length - 1))].getVersion().getVersion());
            return this.dragging = true;
        }
        return false;
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY) {
        this.dragging = false;
    }
    
    public void setVersion(final int protocol) {
        this.dragValue = (ProtocolCollection.values().length - Arrays.asList(ProtocolCollection.values()).indexOf(ProtocolCollection.getProtocolCollectionById(protocol))) / (float)ProtocolCollection.values().length;
        this.sliderValue = this.dragValue;
        this.displayString = this.values[(int)(this.sliderValue * (this.values.length - 1))].getVersion().getName();
    }
}
