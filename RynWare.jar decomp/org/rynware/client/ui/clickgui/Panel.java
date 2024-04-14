// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.clickgui;

import org.rynware.client.ui.clickgui.component.ExpandableComponent;
import org.rynware.client.utils.render.RenderUtils;
import org.rynware.client.utils.render.RoundedUtil;
import org.rynware.client.utils.render.ColorUtils;
import org.rynware.client.feature.impl.hud.ClickGUI;
import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import java.util.Iterator;
import org.rynware.client.ui.clickgui.component.impl.ModuleComponent;
import org.rynware.client.Main;
import org.rynware.client.ui.clickgui.component.Component;
import org.rynware.client.ui.clickgui.component.AnimationState;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.feature.Feature;
import java.util.List;
import net.minecraft.client.Minecraft;
import org.rynware.client.ui.clickgui.component.DraggablePanel;

public final class Panel extends DraggablePanel
{
    Minecraft mc;
    public static final int HEADER_WIDTH = 107;
    public static final int X_ITEM_OFFSET = 1;
    public static final int ITEM_HEIGHT = 15;
    public static final int HEADER_HEIGHT = 17;
    public List<Feature> features;
    public FeatureCategory type;
    public AnimationState state;
    private int prevX;
    private int prevY;
    private boolean dragging;
    
    public Panel(final FeatureCategory category, final int x, final int y) {
        super(null, category.name(), x, y, 107, 17);
        this.mc = Minecraft.getMinecraft();
        int moduleY = 17;
        this.state = AnimationState.STATIC;
        this.features = Main.instance.featureManager.getFeaturesCategory(category);
        for (final Feature feature : this.features) {
            this.components.add(new ModuleComponent(this, feature, 1, moduleY, 105, 15));
            moduleY += 15;
        }
        this.type = category;
    }
    
    @Override
    public void drawComponent(final ScaledResolution scaledResolution, final int mouseX, final int mouseY) {
        if (this.dragging) {
            this.setX(mouseX - this.prevX);
            this.setY(mouseY - this.prevY);
        }
        final int x = this.getX();
        final int y = this.getY();
        final int width = this.getWidth();
        int height = this.getHeight();
        final int heightWithExpand = this.getHeightWithExpand();
        final int headerHeight = this.isExpanded() ? heightWithExpand : height;
        final float startAlpha1 = 0.14f;
        final int size1 = 25;
        final float left1 = x + 1.0f;
        final float right1 = (float)(x + width);
        final float bottom1 = y + headerHeight - 6.0f;
        final float top1 = y + headerHeight - 2.0f;
        final float top2 = y + 13.0f;
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
        }
        final float extendedHeight = 2.0f;
        RoundedUtil.drawRound((float)x, (float)y, (float)width, this.getHeight() - 2.5f + (this.isExpanded() ? (headerHeight - extendedHeight - 14.5f) : 0.0f), 5.0f, new Color(17, 17, 17, 165));
        if (this.isExpanded()) {
            RenderUtils.drawRect(x + 2, y + this.getHeight() - 1, x + width - 2, y + this.getHeight() - 2, new Color(88, 91, 117, 240).getRGB());
        }
        this.mc.neverlose500_16.drawCenteredString(this.getName().toUpperCase(), x + 53.5f, y + 8.5f - 4.0f, Color.LIGHT_GRAY.getRGB());
        super.drawComponent(scaledResolution, mouseX, mouseY);
        if (this.isExpanded()) {
            for (final Component component : this.components) {
                component.setY(height);
                component.drawComponent(scaledResolution, mouseX, mouseY);
                int cHeight = component.getHeight();
                if (component instanceof ExpandableComponent) {
                    final ExpandableComponent expandableComponent = (ExpandableComponent)component;
                    if (expandableComponent.isExpanded()) {
                        cHeight = expandableComponent.getHeightWithExpand() + 5;
                    }
                }
                height += cHeight;
            }
        }
    }
    
    @Override
    public void onPress(final int mouseX, final int mouseY, final int button) {
        if (button == 0 && !this.dragging) {
            this.dragging = true;
            this.prevX = mouseX - this.getX();
            this.prevY = mouseY - this.getY();
        }
    }
    
    @Override
    public void onMouseRelease(final int button) {
        super.onMouseRelease(button);
        this.dragging = false;
    }
    
    @Override
    public boolean canExpand() {
        return !this.features.isEmpty();
    }
    
    @Override
    public int getHeightWithExpand() {
        int height = this.getHeight();
        if (this.isExpanded()) {
            for (final Component component : this.components) {
                int cHeight = component.getHeight();
                if (component instanceof ExpandableComponent) {
                    final ExpandableComponent expandableComponent = (ExpandableComponent)component;
                    if (expandableComponent.isExpanded()) {
                        cHeight = expandableComponent.getHeightWithExpand() + 5;
                    }
                }
                height += cHeight;
            }
        }
        return height;
    }
}
