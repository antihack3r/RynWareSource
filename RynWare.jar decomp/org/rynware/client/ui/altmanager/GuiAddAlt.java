// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.altmanager;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.realmsclient.gui.ChatFormatting;
import org.rynware.client.ui.altmanager.alt.Alt;
import org.rynware.client.ui.altmanager.alt.AltManager;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import java.io.IOException;
import org.lwjgl.input.Keyboard;
import org.rynware.client.utils.render.RenderUtils;
import net.minecraft.util.math.MathHelper;
import org.rynware.client.utils.render.ColorUtils;
import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import java.awt.datatransfer.DataFlavor;
import java.awt.Toolkit;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiScreen;

public class GuiAddAlt extends GuiScreen
{
    private final GuiAltManager manager;
    private PasswordField password;
    private String status;
    private GuiTextField username;
    
    GuiAddAlt(final GuiAltManager manager) {
        this.status = TextFormatting.GRAY + "Idle...";
        this.manager = manager;
    }
    
    private static void setStatus(final GuiAddAlt guiAddAlt, final String status) {
        guiAddAlt.status = status;
    }
    
    @Override
    protected void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 0: {
                final AddAltThread login = new AddAltThread(this.username.getText(), this.password.getText());
                login.start();
                break;
            }
            case 1: {
                this.mc.displayGuiScreen(this.manager);
                break;
            }
            case 2: {
                String data;
                try {
                    data = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                }
                catch (final Exception var4) {
                    return;
                }
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
    
    @Override
    public void drawScreen(final int i, final int j, final float f) {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        RenderUtils.drawGradientRect(0.0, 0.0, sr.getScaledWidth(), sr.getScaledHeight(), ColorUtils.TwoColoreffect(new Color(127, 255, 123, 125), new Color(253, 6, 241, 125), Math.abs(System.currentTimeMillis() / 6L) / 100.0 + 0.1275).getRGB() - 16, RenderUtils.injectAlpha(ColorUtils.astolfo(0.0f, (float)MathHelper.clamp(15, 0, 300)), 125).getRGB());
        RenderUtils.drawGradientSideways(0.0, 0.0, sr.getScaledWidth(), sr.getScaledHeight(), ColorUtils.TwoColoreffect(new Color(173, 234, 253, 120), new Color(131, 7, 213, 120), Math.abs(System.currentTimeMillis() / 12L) / 100.0 + 0.1275).getRGB() - 16, RenderUtils.injectAlpha(ColorUtils.astolfo(0.0f, (float)MathHelper.clamp(15, 0, 300)), 120).getRGB());
        RenderUtils.drawSmoothRect(this.width / 2 - 110, 7.0, this.width / 2 + 110, this.height / 4 + 155, new Color(17, 25, 16, 235).getRGB());
        this.username.drawTextBox();
        this.password.drawTextBox();
        this.mc.neverlose500_18.drawCenteredString("Add Account", this.width / 2.0f, 15.0f, -1);
        if (this.username.getText().isEmpty() && !this.username.isFocused()) {
            this.mc.neverlose500_18.drawStringWithShadow("Username / E-Mail", this.width / 2 - 96, 66.0, -7829368);
        }
        if (this.password.getText().isEmpty() && !this.password.isFocused()) {
            this.mc.neverlose500_18.drawStringWithShadow("Password", this.width / 2 - 96, 106.0, -7829368);
        }
        this.mc.neverlose500_18.drawCenteredString(this.status, this.width / 2.0f, 30.0f, -1);
        super.drawScreen(i, j, f);
    }
    
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();
        this.buttonList.add(new GuiAltButton(0, this.width / 2 - 100, this.height / 4 + 92 + 12, "Login"));
        this.buttonList.add(new GuiAltButton(1, this.width / 2 - 100, this.height / 4 + 116 + 12, "Back"));
        this.buttonList.add(new GuiAltButton(2, this.width / 2 - 100, this.height / 4 + 92 - 12, "Import User:Pass"));
        this.username = new GuiTextField(this.eventButton, this.mc.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
        this.password = new PasswordField(this.mc.fontRendererObj, this.width / 2 - 100, 100, 200, 20);
    }
    
    @Override
    protected void keyTyped(final char par1, final int par2) {
        this.username.textboxKeyTyped(par1, par2);
        this.password.textboxKeyTyped(par1, par2);
        if (par1 == '\t' && (this.username.isFocused() || this.password.isFocused())) {
            this.username.setFocused(!this.username.isFocused());
            this.password.setFocused(!this.password.isFocused());
        }
        if (par1 == '\r') {
            this.actionPerformed(this.buttonList.get(0));
        }
    }
    
    @Override
    protected void mouseClicked(final int par1, final int par2, final int par3) {
        try {
            super.mouseClicked(par1, par2, par3);
        }
        catch (final IOException var5) {
            var5.printStackTrace();
        }
        this.username.mouseClicked(par1, par2, par3);
        this.password.mouseClicked(par1, par2, par3);
    }
    
    private class AddAltThread extends Thread
    {
        private final String password;
        private final String username;
        
        AddAltThread(final String username, final String password) {
            this.username = username;
            this.password = password;
            setStatus(GuiAddAlt.this, TextFormatting.GRAY + "Idle...");
        }
        
        private void checkAndAddAlt(final String username, final String password) {
            try {
                final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
                final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
                auth.setUsername(username);
                auth.setPassword(password);
                try {
                    auth.logIn();
                    AltManager.registry.add(new Alt(username, password, auth.getSelectedProfile().getName(), Alt.Status.Working));
                    setStatus(GuiAddAlt.this, TextFormatting.GREEN + "Added alt - " + ChatFormatting.RED + this.username + ChatFormatting.BOLD + "(license)");
                }
                catch (final AuthenticationException var7) {
                    setStatus(GuiAddAlt.this, TextFormatting.RED + "Connect failed!");
                    var7.printStackTrace();
                }
            }
            catch (final Throwable e) {
                setStatus(GuiAddAlt.this, TextFormatting.RED + "Error");
                e.printStackTrace();
            }
        }
        
        @Override
        public void run() {
            if (this.password.equals("")) {
                AltManager.registry.add(new Alt(this.username, ""));
                setStatus(GuiAddAlt.this, TextFormatting.GREEN + "Added alt - " + ChatFormatting.RED + this.username + ChatFormatting.BOLD + "(non license)");
            }
            else {
                setStatus(GuiAddAlt.this, TextFormatting.AQUA + "Trying connect...");
                this.checkAndAddAlt(this.username, this.password);
            }
        }
    }
}
