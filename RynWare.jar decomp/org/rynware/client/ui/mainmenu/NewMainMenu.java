// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.mainmenu;

import org.lwjgl.Sys;
import java.io.File;
import java.net.URISyntaxException;
import java.io.IOException;
import java.net.URI;
import java.awt.Desktop;
import net.minecraft.client.gui.GuiOptions;
import org.rynware.client.ui.altmanager.GuiAltManager;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.gui.GuiButton;
import org.rynware.client.Main;
import java.awt.Color;
import net.minecraft.client.gui.Gui;
import org.rynware.client.ui.button.GuiMainMenuButton;
import org.rynware.client.utils.math.animations.impl.DecelerateAnimation;
import net.minecraft.client.gui.ScaledResolution;
import org.rynware.client.utils.math.animations.Animation;
import net.minecraft.client.gui.GuiScreen;

public class NewMainMenu extends GuiScreen
{
    private int width;
    private Shader shaderH;
    private GuiScreen lastScreen;
    public float scale;
    private int height;
    private final long initTime;
    private Animation initAnimation;
    
    public NewMainMenu() {
        this.scale = 2.0f;
        this.initTime = System.currentTimeMillis();
    }
    
    @Override
    public void initGui() {
        this.shaderH = new Shader("glsandbox2.fsh");
        final ScaledResolution sr = new ScaledResolution(this.mc);
        this.width = sr.getScaledWidth();
        this.height = sr.getScaledHeight();
        this.initAnimation = new DecelerateAnimation(300, 1.0);
        this.buttonList.add(new GuiMainMenuButton(this.mc.mntsb_18, 0, this.width / 2 - 75, this.height / 2 + 3, 150, 12, "single", false));
        this.buttonList.add(new GuiMainMenuButton(this.mc.mntsb_18, 1, this.width / 2 - 75, this.height / 2 + 31, 150, 12, "multiplayer", false));
        this.buttonList.add(new GuiMainMenuButton(this.mc.mntsb_18, 2, this.width / 2 - 75, this.height / 2 + 59, 150, 12, "alt manager", false));
        this.buttonList.add(new GuiMainMenuButton(this.mc.mntsb_18, 3, this.width / 2 - 75, this.height / 2 + 87, 150, 12, "options", false));
        this.buttonList.add(new GuiMainMenuButton(this.mc.stylesIcons, 5, this.width / 2 - 50, this.height / 2 + 116, 30, 15, "E", true));
        this.buttonList.add(new GuiMainMenuButton(this.mc.stylesIcons, 4, this.width / 2 - 15, this.height / 2 + 116, 30, 15, "I", true));
        this.buttonList.add(new GuiMainMenuButton(this.mc.ikonki2, 6, this.width / 2 + 20, this.height / 2 + 116, 30, 15, "E", true));
    }
    
    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        final ScaledResolution res = new ScaledResolution(this.mc);
        Gui.drawRect(0.0, 0.0, res.getScaledWidth(), res.getScaledHeight(), -1);
        this.shaderH.draw(mouseX / 300.0f, (res.getScaledHeight() - mouseY) / 300.0f);
        final ScaledResolution sr = new ScaledResolution(this.mc);
        this.mc.latoBig.drawCenteredBlurredString("RYNWARE.CC", (float)(sr.getScaledWidth() / 2), (float)(sr.getScaledHeight() / 2.3), 12, new Color(246, 179, 243, 59), new Color(217, 198, 215, 255).getRGB());
        this.mc.latoSmall.drawCenteredBlurredString("CLIENT v" + Main.instance.version, (float)(sr.getScaledWidth() / 2), (float)((sr.getScaledHeight() + 17) / 2.2), 12, new Color(183, 176, 182, 59), new Color(131, 125, 130, 255).getRGB());
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
    
    public void actionPerformed(final GuiButton button) throws IOException {
        switch (button.id) {
            case 0: {
                this.mc.displayGuiScreen(new GuiWorldSelection(this));
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            }
            case 2: {
                this.mc.displayGuiScreen(new GuiAltManager());
                break;
            }
            case 3: {
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            }
            case 4: {
                Main.instance.configManager.saveConfig("default");
                Main.instance.fileManager.saveFiles();
                System.exit(0);
                break;
            }
            case 5: {
                try {
                    Desktop.getDesktop().browse(new URI("https://disk.yandex.ru/d/Vs0UKuTnHTaMkw"));
                }
                catch (final IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
                break;
            }
            case 6: {
                final File file = new File("C:\\RynWare", "");
                Sys.openURL(file.getAbsolutePath());
                break;
            }
        }
        super.actionPerformed(button);
    }
}
