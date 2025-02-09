// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.clickgui.component.impl;

import org.rynware.client.ui.settings.Setting;
import org.rynware.client.utils.render.RenderUtils;
import org.rynware.client.feature.impl.hud.ClickGUI;
import java.util.Iterator;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import net.minecraft.client.gui.ScaledResolution;
import org.rynware.client.ui.clickgui.component.Component;
import net.minecraft.client.Minecraft;
import org.rynware.client.ui.settings.impl.MultipleBoolSetting;
import org.rynware.client.ui.clickgui.component.PropertyComponent;
import org.rynware.client.ui.clickgui.component.ExpandableComponent;

public class MultipleBoolSettingComponent extends ExpandableComponent implements PropertyComponent
{
    private final MultipleBoolSetting listSetting;
    Minecraft mc;
    
    public MultipleBoolSettingComponent(final Component parent, final MultipleBoolSetting listSetting, final int x, final int y, final int width, final int height) {
        super(parent, listSetting.getName(), x, y, width, height);
        this.mc = Minecraft.getMinecraft();
        this.listSetting = listSetting;
    }
    
    @Override
    public void drawComponent(final ScaledResolution scaledResolution, final int mouseX, final int mouseY) {
        super.drawComponent(scaledResolution, mouseX, mouseY);
        for (final BooleanSetting booleanSetting : this.listSetting.getBoolSettings()) {
            final int x = this.getX();
            final int y = this.getY();
            final int width = this.getWidth();
            final int height = this.getHeight();
            final int dropDownBoxY = y + 4;
            final int textColor = 16777215;
            Gui.drawRect(x, y, x + width, y + height, new Color(20, 20, 20, 111).getRGB());
            Gui.drawRect(x + 0.5f, dropDownBoxY, x + this.getWidth() - 0.5f, dropDownBoxY + 11, new Color(30, 30, 30).getRGB());
            this.mc.rubik_16.drawCenteredStringWithOutline(this.getName(), (float)(x + width / 2 + 1), dropDownBoxY + 3.0f, new Color(222, 222, 222).getRGB());
            if (this.isExpanded()) {
                Gui.drawRect(x + 1, y + height, x + width - 1, y + this.getHeightWithExpand(), new Color(25, 25, 25, 160).getRGB());
                this.handleRender(x, y + this.getHeight() + 2, width, textColor);
            }
        }
    }
    
    @Override
    public void onMouseClick(final int mouseX, final int mouseY, final int button) {
        super.onMouseClick(mouseX, mouseY, button);
        if (this.isExpanded()) {
            this.handleClick(mouseX, mouseY, this.getX(), this.getY() + this.getHeight() + 2, this.getWidth());
        }
    }
    
    private void handleRender(final int x, int y, final int width, final int textColor) {
        final int color = 0;
        final Color onecolor = new Color(ClickGUI.color.getColorValue());
        for (final BooleanSetting e : this.listSetting.getBoolSettings()) {
            if (e.getBoolValue()) {
                this.mc.rubik_15.drawCenteredBlurredString(e.getName(), x + 1 + width / 2 + 0.5f, y + 2.5f, 8, RenderUtils.injectAlpha(new Color(ClickGUI.color.getColorValue()), 60), ClickGUI.color.getColorValue());
            }
            else {
                this.mc.rubik_15.drawCenteredString(e.getName(), x + 1 + width / 2 + 0.5f, y + 2.5f, Color.GRAY.getRGB());
            }
            y += 12;
        }
    }
    
    private void handleClick(final int mouseX, final int mouseY, final int x, int y, final int width) {
        for (final BooleanSetting e : this.listSetting.getBoolSettings()) {
            if (mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + 15 - 3) {
                e.setBoolValue(!e.getBoolValue());
            }
            y += 12;
        }
    }
    
    @Override
    public int getHeightWithExpand() {
        return this.getHeight() + this.listSetting.getBoolSettings().toArray().length * 12;
    }
    
    @Override
    public void onPress(final int mouseX, final int mouseY, final int button) {
    }
    
    @Override
    public boolean canExpand() {
        return this.listSetting.getBoolSettings().toArray().length > 0;
    }
    
    @Override
    public Setting getSetting() {
        return this.listSetting;
    }
}
