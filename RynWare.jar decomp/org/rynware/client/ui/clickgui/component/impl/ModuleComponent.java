// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.clickgui.component.impl;

import org.rynware.client.utils.render.RenderUtils;
import org.lwjgl.input.Keyboard;
import org.rynware.client.utils.render.ClientHelper;
import org.rynware.client.utils.render.ColorUtils;
import org.rynware.client.feature.impl.hud.ClickGUI;
import java.awt.Color;
import org.rynware.client.ui.clickgui.ClickGuiScreen;
import java.util.Comparator;
import org.rynware.client.ui.components.SorterHelper;
import net.minecraft.client.gui.ScaledResolution;
import java.util.Iterator;
import org.rynware.client.ui.settings.impl.MultipleBoolSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.ColorSetting;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.ui.clickgui.component.Component;
import org.rynware.client.utils.math.TimerHelper;
import org.rynware.client.feature.Feature;
import net.minecraft.client.Minecraft;
import org.rynware.client.ui.clickgui.component.ExpandableComponent;

public final class ModuleComponent extends ExpandableComponent
{
    Minecraft mc;
    private final Feature module;
    public static TimerHelper timerHelper;
    private boolean binding;
    int alpha;
    private final TimerHelper descTimer;
    public boolean ready;
    static String i;
    
    public ModuleComponent(final Component parent, final Feature module, final int x, final int y, final int width, final int height) {
        super(parent, module.getLabel(), x, y, width, height);
        this.mc = Minecraft.getMinecraft();
        this.alpha = 0;
        this.descTimer = new TimerHelper();
        this.ready = false;
        this.module = module;
        final int propertyX = 1;
        for (final Setting setting : module.getSettings()) {
            if (setting instanceof BooleanSetting) {
                this.components.add(new BooleanSettingComponent(this, (BooleanSetting)setting, propertyX, height, width - 2, 21));
            }
            else if (setting instanceof ColorSetting) {
                this.components.add(new ColorPickerComponent(this, (ColorSetting)setting, propertyX, height, width - 2, 15));
            }
            else if (setting instanceof NumberSetting) {
                this.components.add(new NumberSettingComponent(this, (NumberSetting)setting, propertyX, height, width - 2, 20));
            }
            else if (setting instanceof ListSetting) {
                this.components.add(new ListSettingComponent(this, (ListSetting)setting, propertyX, height, width - 2, 17));
            }
            else {
                if (!(setting instanceof MultipleBoolSetting)) {
                    continue;
                }
                this.components.add(new MultipleBoolSettingComponent(this, (MultipleBoolSetting)setting, propertyX, height, width - 2, 16));
            }
        }
    }
    
    String getI(final String s) {
        if (!ModuleComponent.timerHelper.hasReached(5.0)) {
            return ModuleComponent.i;
        }
        ModuleComponent.timerHelper.reset();
        if (ModuleComponent.i.length() < s.length()) {
            this.ready = false;
            return ModuleComponent.i += s.charAt(ModuleComponent.i.length());
        }
        this.ready = true;
        return ModuleComponent.i;
    }
    
    @Override
    public void drawComponent(final ScaledResolution scaledResolution, final int mouseX, final int mouseY) {
        this.components.sort(new SorterHelper());
        final float x = (float)this.getX();
        final float y = (float)(this.getY() - 2);
        final int width = this.getWidth();
        final int height = this.getHeight();
        if (this.isExpanded()) {
            int childY = 15;
            for (final Component child : this.components) {
                int cHeight = child.getHeight();
                if (child instanceof BooleanSettingComponent) {
                    final BooleanSettingComponent booleanSettingComponent = (BooleanSettingComponent)child;
                    if (!booleanSettingComponent.booleanSetting.isVisible()) {
                        continue;
                    }
                }
                if (child instanceof NumberSettingComponent) {
                    final NumberSettingComponent numberSettingComponent = (NumberSettingComponent)child;
                    if (!numberSettingComponent.numberSetting.isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ColorPickerComponent) {
                    final ColorPickerComponent colorPickerComponent = (ColorPickerComponent)child;
                    if (!colorPickerComponent.getSetting().isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ListSettingComponent) {
                    final ListSettingComponent listSettingComponent = (ListSettingComponent)child;
                    if (!listSettingComponent.getSetting().isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ExpandableComponent) {
                    final ExpandableComponent expandableComponent = (ExpandableComponent)child;
                    if (expandableComponent.isExpanded()) {
                        cHeight = expandableComponent.getHeightWithExpand();
                    }
                }
                child.setY(childY);
                child.drawComponent(scaledResolution, mouseX, mouseY);
                childY += cHeight;
            }
        }
        if (!ClickGuiScreen.search.getText().isEmpty() && !this.module.getLabel().toLowerCase().contains(ClickGuiScreen.search.getText().toLowerCase())) {
            return;
        }
        Color color = Color.WHITE;
        final String currentMode = ClickGUI.guiColor.currentMode;
        switch (currentMode) {
            case "Custom": {
                color = new Color(ClickGUI.color.getColorValue());
                break;
            }
            case "Astolfo": {
                color = ColorUtils.astolfo(0.0f, 4.0f);
                break;
            }
            case "Rainbow": {
                color = ColorUtils.rainbow(12, 1.0f, 1.0f);
                break;
            }
            case "Client": {
                color = ClientHelper.getClientColor();
                break;
            }
        }
        final Color color2 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 140);
        final boolean hovered = this.isHovered(mouseX, mouseY);
        this.components.sort(new SorterHelper());
        if (hovered && this.module.getDesc() != null) {
            final ScaledResolution sr = new ScaledResolution(this.mc);
            this.mc.sfui16.drawCenteredStringWithShadow((this.module == null) ? "null pointer :(" : this.getI(this.module.getDesc()), sr.getScaledWidth() / 2.0f, (float)(sr.getScaledHeight() - 10), -1);
            if (!ClickGUI.potato_mode.getBoolValue()) {
                this.mc.sfui18.drawCenteredBlurredString(this.module.getLabel(), sr.getScaledWidth() / 2, sr.getScaledHeight() - 21, 8, new Color(255, 255, 255, 50), -1);
            }
            else {
                this.mc.sfui18.drawCenteredString(this.module.getLabel(), (float)(sr.getScaledWidth() / 2), (float)(sr.getScaledHeight() - 21), -1);
            }
            if (this.ready && !ModuleComponent.i.equals(this.module.getDesc())) {
                ModuleComponent.i = "";
            }
        }
        else {
            this.ready = false;
        }
        if (this.module.isEnabled()) {
            if (!ClickGUI.potato_mode.getBoolValue() && ClickGUI.glow.getBoolValue()) {
                this.mc.neverlose500_16.drawCenteredBlurredStringWithShadow(this.binding ? ("Press a key.. " + Keyboard.getKeyName(this.module.getBind())) : this.getName(), x + 53.5f, y + height / 2.0f - 3.0f, (int)ClickGUI.glowRadius2.getNumberValue(), this.module.isEnabled() ? RenderUtils.injectAlpha(new Color(new Color(color.getRed(), color.getGreen(), color.getBlue()).getRGB()), 100) : Color.GRAY, this.module.isEnabled() ? ClickGUI.color.getColorValue() : Color.GRAY.getRGB());
            }
            else {
                this.mc.neverlose500_16.drawCenteredStringWithShadow(this.binding ? ("Press a key.. " + Keyboard.getKeyName(this.module.getBind())) : this.getName(), x + 53.5f, y + height / 2.0f - 3.0f, this.module.isEnabled() ? color.getRGB() : Color.GRAY.getRGB());
            }
        }
        else {
            this.mc.neverlose500_16.drawCenteredStringWithShadow(this.binding ? ("Press a key.. " + Keyboard.getKeyName(this.module.getBind())) : this.getName(), x + 53.5f, y + height / 2.0f - 3.0f, this.module.isEnabled() ? color.getRGB() : Color.GRAY.getRGB());
        }
    }
    
    @Override
    public boolean canExpand() {
        return !this.components.isEmpty();
    }
    
    @Override
    public void onPress(final int mouseX, final int mouseY, final int button) {
        switch (button) {
            case 0: {
                this.module.toggle();
                break;
            }
            case 2: {
                this.binding = !this.binding;
                break;
            }
        }
    }
    
    @Override
    public void onKeyPress(final int keyCode) {
        if (this.binding) {
            ClickGuiScreen.escapeKeyInUse = true;
            this.module.setBind((keyCode == 211) ? 0 : keyCode);
            this.binding = false;
        }
    }
    
    @Override
    public int getHeightWithExpand() {
        int height = this.getHeight();
        if (this.isExpanded()) {
            for (final Component child : this.components) {
                int cHeight = child.getHeight();
                if (child instanceof BooleanSettingComponent) {
                    final BooleanSettingComponent booleanSettingComponent = (BooleanSettingComponent)child;
                    if (!booleanSettingComponent.booleanSetting.isVisible()) {
                        continue;
                    }
                }
                if (child instanceof NumberSettingComponent) {
                    final NumberSettingComponent numberSettingComponent = (NumberSettingComponent)child;
                    if (!numberSettingComponent.numberSetting.isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ColorPickerComponent) {
                    final ColorPickerComponent colorPickerComponent = (ColorPickerComponent)child;
                    if (!colorPickerComponent.getSetting().isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ListSettingComponent) {
                    final ListSettingComponent listSettingComponent = (ListSettingComponent)child;
                    if (!listSettingComponent.getSetting().isVisible()) {
                        continue;
                    }
                }
                if (child instanceof ExpandableComponent) {
                    final ExpandableComponent expandableComponent = (ExpandableComponent)child;
                    if (expandableComponent.isExpanded()) {
                        cHeight = expandableComponent.getHeightWithExpand();
                    }
                }
                height += cHeight;
            }
        }
        return height;
    }
    
    static {
        ModuleComponent.timerHelper = new TimerHelper();
        ModuleComponent.i = " ";
    }
}
