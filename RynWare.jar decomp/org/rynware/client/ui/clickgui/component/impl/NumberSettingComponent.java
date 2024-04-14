// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.clickgui.component.impl;

import org.rynware.client.ui.settings.Setting;
import org.rynware.client.utils.render.RenderUtils;
import org.rynware.client.utils.math.AnimationHelper;
import java.awt.Color;
import org.rynware.client.feature.impl.hud.ClickGUI;
import org.rynware.client.utils.math.MathematicHelper;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.clickgui.component.PropertyComponent;
import org.rynware.client.ui.clickgui.component.Component;

public class NumberSettingComponent extends Component implements PropertyComponent
{
    public NumberSetting numberSetting;
    public float currentValueAnimate;
    private boolean sliding;
    Minecraft mc;
    
    public NumberSettingComponent(final Component parent, final NumberSetting numberSetting, final int x, final int y, final int width, final int height) {
        super(parent, numberSetting.getName(), x, y, width, height);
        this.currentValueAnimate = 0.0f;
        this.mc = Minecraft.getMinecraft();
        this.numberSetting = numberSetting;
    }
    
    @Override
    public void drawComponent(final ScaledResolution scaledResolution, final int mouseX, final int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);
        final int x = this.getX();
        final int y = this.getY();
        final int width = this.getWidth();
        final int height = this.getHeight();
        final double min = this.numberSetting.getMinValue();
        final double max = this.numberSetting.getMaxValue();
        final boolean hovered = this.isHovered(mouseX, mouseY);
        if (this.sliding) {
            this.numberSetting.setValueNumber((float)MathematicHelper.round((mouseX - x) * (max - min) / width + min, this.numberSetting.getIncrement()));
            if (this.numberSetting.getNumberValue() > max) {
                this.numberSetting.setValueNumber((float)max);
            }
            else if (this.numberSetting.getNumberValue() < min) {
                this.numberSetting.setValueNumber((float)min);
            }
        }
        final float amountWidth = (float)((this.numberSetting.getNumberValue() - min) / (max - min));
        final Color onecolor = new Color(ClickGUI.color.getColorValue());
        this.currentValueAnimate = AnimationHelper.animation(this.currentValueAnimate, amountWidth, 0.0f);
        final float optionValue = (float)MathematicHelper.round(this.numberSetting.getNumberValue(), this.numberSetting.getIncrement());
        RenderUtils.drawRect(x, y, x + width, y + height, new Color(20, 20, 20, 111).getRGB());
        RenderUtils.drawRect(x + 3, y + height - 5, x + (width - 3), y + 13, new Color(40, 39, 39).getRGB());
        RenderUtils.drawRect(x + 3, y + 13.5, x + 5 + this.currentValueAnimate * (width - 8), y + 15.0f, ClickGUI.color.getColorValue());
        RenderUtils.drawBlurredShadow((float)(x + 3), (float)(y + 13.5), this.currentValueAnimate * (width - 8), 1.0f, 8, RenderUtils.injectAlpha(new Color(ClickGUI.color.getColorValue()), 115));
        RenderUtils.drawFilledCircle((float)(int)(x + 5 + this.currentValueAnimate * (width - 8)), (float)(int)(y + 14.0f), 2.5f, new Color(ClickGUI.color.getColorValue()));
        RenderUtils.drawBlurredShadow((float)(int)(x + 3 + this.currentValueAnimate * (width - 8)), (float)(y + 11), 6.0f, 5.0f, 8, RenderUtils.injectAlpha(new Color(ClickGUI.color.getColorValue()), 115));
        String valueString = "";
        final NumberSetting.NumberType numberType = this.numberSetting.getType();
        switch (numberType) {
            case PERCENTAGE: {
                valueString += '%';
                break;
            }
            case MS: {
                valueString += "ms";
                break;
            }
            case DISTANCE: {
                valueString += 'm';
            }
            case SIZE: {
                valueString += "SIZE";
            }
            case APS: {
                valueString += "APS";
                break;
            }
            default: {
                valueString = "";
                break;
            }
        }
        this.mc.neverlose500_13.drawStringWithShadow(this.numberSetting.getName(), x + 2.0f, y + height / 2.5f - 4.0f, Color.lightGray.getRGB());
        this.mc.neverlose500_14.drawStringWithShadow(optionValue + " " + valueString, x + width - this.mc.neverlose500_16.getStringWidth(optionValue + " " + valueString) - 5, y + height / 2.5f - 4.0f, Color.GRAY.getRGB());
        if (hovered && this.numberSetting.getDesc() != null) {
            RenderUtils.drawSmoothRect(x + width + 20, y + height / 1.5f + 4.5f, x + width + 25 + this.mc.rubik_18.getStringWidth(this.numberSetting.getDesc()), y + 5.5f, new Color(0, 0, 0, 90).getRGB());
            this.mc.rubik_18.drawStringWithShadow(this.numberSetting.getDesc(), x + width + 22, y + height / 1.35f - 5.0f, -1);
        }
    }
    
    @Override
    public void onMouseClick(final int mouseX, final int mouseY, final int button) {
        if (!this.sliding && button == 0 && this.isHovered(mouseX, mouseY)) {
            this.sliding = true;
        }
    }
    
    @Override
    public void onMouseRelease(final int button) {
        this.sliding = false;
    }
    
    @Override
    public Setting getSetting() {
        return this.numberSetting;
    }
}
