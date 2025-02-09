// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.altmanager;

import java.io.IOException;
import org.lwjgl.input.Keyboard;
import net.minecraft.util.text.TextFormatting;
import org.rynware.client.utils.render.RenderUtils;
import net.minecraft.util.math.MathHelper;
import org.rynware.client.utils.render.ColorUtils;
import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import java.awt.datatransfer.DataFlavor;
import java.awt.Toolkit;
import org.rynware.client.ui.altmanager.alt.Alt;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import org.rynware.client.ui.altmanager.alt.AltLoginThread;
import net.minecraft.client.gui.GuiScreen;

public final class GuiAltLogin extends GuiScreen
{
    private final GuiScreen previousScreen;
    private PasswordField password;
    private AltLoginThread thread;
    private GuiTextField username;
    
    public GuiAltLogin(final GuiScreen previousScreen) {
        this.previousScreen = previousScreen;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) {
        try {
            switch (button.id) {
                case 0: {
                    (this.thread = new AltLoginThread(new Alt(this.username.getText(), this.password.getText()))).start();
                    break;
                }
                case 1: {
                    this.mc.displayGuiScreen(this.previousScreen);
                    break;
                }
                case 2: {
                    final String data = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                    if (data.contains(":")) {
                        final String[] credentials = data.split(":");
                        this.username.setText(credentials[0]);
                        this.password.setText(credentials[1]);
                        break;
                    }
                    break;
                }
            }
        }
        catch (final Throwable e) {
            throw new RuntimeException();
        }
    }
    
    @Override
    public void drawScreen(final int x, final int y, final float z) {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        RenderUtils.drawGradientRect(0.0, 0.0, sr.getScaledWidth(), sr.getScaledHeight(), ColorUtils.TwoColoreffect(new Color(127, 255, 123, 125), new Color(253, 6, 241, 125), Math.abs(System.currentTimeMillis() / 6L) / 100.0 + 0.1275).getRGB() - 16, RenderUtils.injectAlpha(ColorUtils.astolfo(0.0f, (float)MathHelper.clamp(15, 0, 300)), 125).getRGB());
        RenderUtils.drawGradientSideways(0.0, 0.0, sr.getScaledWidth(), sr.getScaledHeight(), ColorUtils.TwoColoreffect(new Color(173, 234, 253, 120), new Color(131, 7, 213, 120), Math.abs(System.currentTimeMillis() / 12L) / 100.0 + 0.1275).getRGB() - 16, RenderUtils.injectAlpha(ColorUtils.astolfo(0.0f, (float)MathHelper.clamp(15, 0, 300)), 120).getRGB());
        RenderUtils.drawSmoothRect(this.width / 2 - 110, 7.0, this.width / 2 + 110, this.height / 4 + 160, new Color(17, 25, 16, 235).getRGB());
        this.username.drawTextBox();
        this.password.drawTextBox();
        this.mc.neverlose500_18.drawStringWithShadow("Alt Login", this.width / 2.0f - 10.0f, 20.0, -1);
        this.mc.neverlose500_18.drawStringWithShadow((this.thread == null) ? (TextFormatting.GRAY + "Alts...") : this.thread.getStatus(), this.width / 2.0f, 29.0, -1);
        if (this.username.getText().isEmpty() && !this.username.isFocused()) {
            this.mc.neverlose500_18.drawStringWithShadow("Username / E-Mail", this.width / 2 - 96, 66.0, -7829368);
        }
        if (this.password.getText().isEmpty() && !this.password.isFocused()) {
            this.mc.neverlose500_18.drawStringWithShadow("Password", this.width / 2 - 96, 106.0, -7829368);
        }
        super.drawScreen(x, y, z);
    }
    
    @Override
    public void initGui() {
        final int height1 = this.height / 4 + 24;
        this.buttonList.add(new GuiAltButton(0, this.width / 2 - 100, height1 + 72 + 12, "Login"));
        this.buttonList.add(new GuiAltButton(1, this.width / 2 - 100, height1 + 72 + 12 + 24, "Back"));
        this.buttonList.add(new GuiAltButton(2, this.width / 2 - 100, height1 + 72 + 12 - 24, "Import User:Pass"));
        this.username = new GuiTextField(height1, this.mc.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(this.mc.fontRendererObj, this.width / 2 - 100, 100, 200, 20);
        this.username.setFocused(true);
        Keyboard.enableRepeatEvents(true);
    }
    
    @Override
    protected void keyTyped(final char character, final int key) {
        try {
            super.keyTyped(character, key);
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
        if (character == '\t') {
            if (!this.username.isFocused() && !this.password.isFocused()) {
                this.username.setFocused(true);
            }
            else {
                this.username.setFocused(this.password.isFocused());
                this.password.setFocused(!this.username.isFocused());
            }
        }
        if (character == '\r') {
            this.actionPerformed(this.buttonList.get(0));
        }
        this.username.textboxKeyTyped(character, key);
        this.password.textboxKeyTyped(character, key);
    }
    
    @Override
    protected void mouseClicked(final int x, final int y, final int button) {
        try {
            super.mouseClicked(x, y, button);
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
        this.username.mouseClicked(x, y, button);
        this.password.mouseClicked(x, y, button);
    }
    
    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }
    
    @Override
    public void updateScreen() {
        this.username.updateCursorCounter();
        this.password.updateCursorCounter();
    }
}
