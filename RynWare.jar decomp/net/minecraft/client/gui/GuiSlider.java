// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class GuiSlider extends GuiButton
{
    private float sliderPosition;
    public boolean isMouseDown;
    private final String name;
    private final float min;
    private final float max;
    private final GuiPageButtonList.GuiResponder responder;
    private FormatHelper formatHelper;
    
    public GuiSlider(final GuiPageButtonList.GuiResponder guiResponder, final int idIn, final int x, final int y, final String name, final float min, final float max, final float defaultValue, final FormatHelper formatter) {
        super(idIn, x, y, 150, 20, "");
        this.sliderPosition = 1.0f;
        this.name = name;
        this.min = min;
        this.max = max;
        this.sliderPosition = (defaultValue - min) / (max - min);
        this.formatHelper = formatter;
        this.responder = guiResponder;
        this.displayString = this.getDisplayString();
    }
    
    public float getSliderValue() {
        return this.min + (this.max - this.min) * this.sliderPosition;
    }
    
    public void setSliderValue(final float p_175218_1_, final boolean p_175218_2_) {
        this.sliderPosition = (p_175218_1_ - this.min) / (this.max - this.min);
        this.displayString = this.getDisplayString();
        if (p_175218_2_) {
            this.responder.setEntryValue(this.id, this.getSliderValue());
        }
    }
    
    public float getSliderPosition() {
        return this.sliderPosition;
    }
    
    private String getDisplayString() {
        return (this.formatHelper == null) ? (I18n.format(this.name, new Object[0]) + ": " + this.getSliderValue()) : this.formatHelper.getText(this.id, I18n.format(this.name, new Object[0]), this.getSliderValue());
    }
    
    @Override
    protected int getHoverState(final boolean mouseOver) {
        return 0;
    }
    
    @Override
    protected void mouseDragged(final Minecraft mc, final int mouseX, final int mouseY) {
        if (this.visible) {
            if (this.isMouseDown) {
                this.sliderPosition = (mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
                if (this.sliderPosition < 0.0f) {
                    this.sliderPosition = 0.0f;
                }
                if (this.sliderPosition > 1.0f) {
                    this.sliderPosition = 1.0f;
                }
                this.displayString = this.getDisplayString();
                this.responder.setEntryValue(this.id, this.getSliderValue());
            }
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderPosition * (this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderPosition * (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }
    
    public void setSliderPosition(final float p_175219_1_) {
        this.sliderPosition = p_175219_1_;
        this.displayString = this.getDisplayString();
        this.responder.setEntryValue(this.id, this.getSliderValue());
    }
    
    @Override
    public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            this.sliderPosition = (mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
            if (this.sliderPosition < 0.0f) {
                this.sliderPosition = 0.0f;
            }
            if (this.sliderPosition > 1.0f) {
                this.sliderPosition = 1.0f;
            }
            this.displayString = this.getDisplayString();
            this.responder.setEntryValue(this.id, this.getSliderValue());
            return this.isMouseDown = true;
        }
        return false;
    }
    
    @Override
    public void mouseReleased(final int mouseX, final int mouseY) {
        this.isMouseDown = false;
    }
    
    public interface FormatHelper
    {
        String getText(final int p0, final String p1, final float p2);
    }
}
