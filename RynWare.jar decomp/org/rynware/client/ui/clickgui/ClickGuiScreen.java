// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.clickgui;

import org.rynware.client.ui.clickgui.component.ExpandableComponent;
import java.io.IOException;
import org.lwjgl.input.Mouse;
import java.util.Iterator;
import org.rynware.client.utils.render.RenderUtils;
import org.rynware.client.utils.render.ColorUtils;
import org.rynware.client.feature.impl.hud.ClickGUI;
import java.awt.Color;
import org.rynware.client.utils.math.animations.impl.DecelerateAnimation;
import net.minecraft.client.gui.ScaledResolution;
import java.util.ArrayList;
import net.minecraft.util.ResourceLocation;
import org.rynware.client.utils.math.animations.Animation;
import org.rynware.client.ui.clickgui.component.Component;
import org.rynware.client.feature.impl.FeatureCategory;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;

public class ClickGuiScreen extends GuiScreen
{
    public static boolean escapeKeyInUse;
    public float scale;
    public List<Panel> components;
    public ScreenHelper screenHelper;
    public boolean exit;
    public FeatureCategory type;
    private Component selectedPanel;
    private Animation initAnimation;
    private static ResourceLocation ANIME_GIRL;
    public static GuiSearcher search;
    
    public ClickGuiScreen() {
        this.scale = 2.0f;
        this.components = new ArrayList<Panel>();
        this.exit = false;
        int x = 10;
        final int y = 80;
        for (final FeatureCategory type : FeatureCategory.values()) {
            this.type = type;
            this.components.add(new Panel(type, x, y));
            this.selectedPanel = new Panel(type, x, y);
            x += this.width + 112;
        }
        this.screenHelper = new ScreenHelper(0.0f, 0.0f);
    }
    
    @Override
    public void initGui() {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        this.initAnimation = new DecelerateAnimation(600, 1.0);
        this.screenHelper = new ScreenHelper(0.0f, 0.0f);
        ClickGuiScreen.search = new GuiSearcher(1337, this.mc.fontRendererObj, 15, sr.getScaledHeight() - 30, 150, 18);
        super.initGui();
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        Color color = Color.WHITE;
        final Color onecolor = new Color(ClickGUI.bgcolor.getColorValue());
        final String currentMode = ClickGUI.backGroundColor.currentMode;
        switch (currentMode) {
            case "Astolfo": {
                color = ColorUtils.astolfo(true, this.width);
                break;
            }
            case "Rainbow": {
                color = ColorUtils.rainbow(300, 1.0f, 1.0f);
                break;
            }
            case "Static": {
                color = onecolor;
                break;
            }
        }
        if (!ClickGUI.potato_mode.getBoolValue() && ClickGUI.blur.getBoolValue() && ClickGUI.blurInt.getNumberValue() > 0.0f) {
            if (this.mc.gameSettings.ofFastRender) {
                this.mc.gameSettings.ofFastRender = false;
            }
            RenderUtils.renderBlur(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), (int)ClickGUI.blurInt.getNumberValue());
        }
        if (!ClickGUI.potato_mode.getBoolValue()) {
            final Color color2 = new Color(color.getRed(), color.getBlue(), color.getGreen(), 90);
            final Color color3 = new Color(color.getRed(), color.getGreen(), color.getBlue(), 180);
            RenderUtils.drawGradientRect(0.0, 0.0, sr.getScaledWidth(), sr.getScaledHeight(), color2.getRGB(), color3.getRGB());
            this.drawDefaultBackground();
        }
        if (ClickGUI.girl.getBoolValue()) {
            String animeGirlStr = "";
            if (ClickGUI.girlmode.currentMode.equals("Girl")) {
                animeGirlStr = "girl1";
            }
            else if (ClickGUI.girlmode.currentMode.equals("Rem")) {
                animeGirlStr = "girl2";
            }
            else if (ClickGUI.girlmode.currentMode.equals("Kaneki")) {
                animeGirlStr = "girl3";
            }
            else if (ClickGUI.girlmode.currentMode.equals("Violet")) {
                animeGirlStr = "girl4";
            }
            else if (ClickGUI.girlmode.currentMode.equals("Kirshtein")) {
                animeGirlStr = "girl5";
            }
            else if (ClickGUI.girlmode.currentMode.equals("002")) {
                animeGirlStr = "girl7";
            }
            RenderUtils.drawImage(new ResourceLocation("rynware/anime/" + animeGirlStr + ".png"), (float)(sr.getScaledWidth() - 280), (float)(sr.getScaledHeight() - 380.0 * this.initAnimation.getOutput()), 280.0f, 380.0f);
        }
        ClickGuiScreen.search.drawTextBox();
        if (ClickGuiScreen.search.getText().isEmpty() && !ClickGuiScreen.search.isFocused()) {
            this.mc.rubik_18.drawStringWithShadow("Search Feature...", sr.getScaledWidth() / 2.0f + 362.0f, 86.0, -1);
        }
        for (final Panel panel : this.components) {
            panel.drawComponent(sr, mouseX, mouseY);
        }
        this.updateMouseWheel();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void updateMouseWheel() {
        final int scrollWheel = Mouse.getDWheel();
        for (final Component panel : this.components) {
            if (scrollWheel > 0) {
                panel.setY(panel.getY() + 15);
            }
            if (scrollWheel < 0) {
                panel.setY(panel.getY() - 15);
            }
        }
    }
    
    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException {
        this.selectedPanel.onKeyPress(keyCode);
        if (!ClickGuiScreen.escapeKeyInUse) {
            super.keyTyped(typedChar, keyCode);
        }
        ClickGuiScreen.search.textboxKeyTyped(typedChar, keyCode);
        if ((typedChar == '\t' || typedChar == '\r') && ClickGuiScreen.search.isFocused()) {
            ClickGuiScreen.search.setFocused(!ClickGuiScreen.search.isFocused());
        }
        ClickGuiScreen.escapeKeyInUse = false;
    }
    
    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) {
        ClickGuiScreen.search.setFocused(false);
        ClickGuiScreen.search.setText("");
        ClickGuiScreen.search.mouseClicked(mouseX, mouseY, mouseButton);
        for (final Component component : this.components) {
            final int x = component.getX();
            final int y = component.getY();
            int cHeight = component.getHeight();
            if (component instanceof ExpandableComponent) {
                final ExpandableComponent expandableComponent = (ExpandableComponent)component;
                if (expandableComponent.isExpanded()) {
                    cHeight = expandableComponent.getHeightWithExpand();
                }
            }
            if (mouseX > x && mouseY > y && mouseX < x + component.getWidth() && mouseY < y + cHeight) {
                (this.selectedPanel = component).onMouseClick(mouseX, mouseY, mouseButton);
                break;
            }
        }
    }
    
    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int state) {
        this.selectedPanel.onMouseRelease(state);
    }
    
    @Override
    public void onGuiClosed() {
        this.screenHelper = new ScreenHelper(0.0f, 0.0f);
        this.mc.entityRenderer.theShaderGroup = null;
        super.onGuiClosed();
    }
}
