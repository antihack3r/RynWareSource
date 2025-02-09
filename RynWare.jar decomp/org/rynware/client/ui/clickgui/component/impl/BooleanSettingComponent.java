// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.clickgui.component.impl;

import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.hud.ClickGUI;
import org.rynware.client.utils.math.AnimationHelper;
import org.rynware.client.utils.render.RenderUtils;
import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.clickgui.component.PropertyComponent;
import org.rynware.client.ui.clickgui.component.Component;

public class BooleanSettingComponent extends Component implements PropertyComponent
{
    public float textHoverAnimate;
    public float leftRectAnimation;
    public float rightRectAnimation;
    public BooleanSetting booleanSetting;
    Minecraft mc;
    
    public BooleanSettingComponent(final Component parent, final BooleanSetting booleanSetting, final int x, final int y, final int width, final int height) {
        super(parent, booleanSetting.getName(), x, y, width, height);
        this.textHoverAnimate = 0.0f;
        this.leftRectAnimation = 0.0f;
        this.rightRectAnimation = 0.0f;
        this.mc = Minecraft.getMinecraft();
        this.booleanSetting = booleanSetting;
    }
    
    @Override
    public void drawComponent(final ScaledResolution scaledResolution, final int mouseX, final int mouseY) {
        if (this.booleanSetting.isVisible()) {
            final int x = this.getX();
            final int y = this.getY();
            final int width = this.getWidth();
            final int height = this.getHeight();
            final int middleHeight = this.getHeight() / 2;
            final boolean hovered = this.isHovered(mouseX, mouseY);
            RenderUtils.drawRect(x, y, x + width, y + height, new Color(20, 20, 20, 111).getRGB());
            this.mc.neverlose500_13.drawStringWithShadow(this.getName(), x + 3, y + middleHeight - 2, Color.GRAY.getRGB());
            this.textHoverAnimate = AnimationHelper.animation(this.textHoverAnimate, hovered ? 2.3f : 2.0f, 0.0f);
            this.leftRectAnimation = AnimationHelper.animation(this.leftRectAnimation, this.booleanSetting.getBoolValue() ? 10.0f : 17.0f, 0.0f);
            this.rightRectAnimation = AnimationHelper.animation(this.rightRectAnimation, (float)(this.booleanSetting.getBoolValue() ? 3 : 10), 0.0f);
            RenderUtils.drawSmoothRect(x + width - 18, y + 7, x + width - 2, y + height - 5, new Color(21, 21, 21).getRGB());
            RenderUtils.drawRect(x + width - this.leftRectAnimation, y + 7.5f, x + width - this.rightRectAnimation, y + height - 6, this.booleanSetting.getBoolValue() ? ClickGUI.color.getColorValue() : new Color(50, 50, 50).getRGB());
            RenderUtils.drawBlurredShadow(x + width - this.leftRectAnimation + 1.0f, y + 7.5f, this.rightRectAnimation + 3.0f, 6.0f, 8, this.booleanSetting.getBoolValue() ? new Color(ClickGUI.color.getColorValue()) : new Color(50, 50, 50, 0));
            if (hovered && this.booleanSetting.getDesc() != null) {
                RenderUtils.drawSmoothRect(x + width + 20, y + height / 1.5f + 4.5f, x + width + 25 + this.mc.rubik_18.getStringWidth(this.booleanSetting.getDesc()), y + 6.5f, new Color(0, 0, 0, 80).getRGB());
                this.mc.rubik_18.drawStringWithShadow(this.booleanSetting.getDesc(), x + width + 22, y + height / 1.35f - 5.0f, -1);
            }
        }
    }
    
    @Override
    public void onMouseClick(final int mouseX, final int mouseY, final int button) {
        if (button == 0 && this.isHovered(mouseX, mouseY) && this.booleanSetting.isVisible()) {
            this.booleanSetting.setBoolValue(!this.booleanSetting.getBoolValue());
        }
    }
    
    @Override
    public Setting getSetting() {
        return this.booleanSetting;
    }
}
