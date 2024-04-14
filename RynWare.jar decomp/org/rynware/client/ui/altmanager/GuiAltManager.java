// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.altmanager;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.Iterator;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.entity.AbstractClientPlayer;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import org.rynware.client.utils.render.RenderUtils;
import org.rynware.client.utils.render.ColorUtils;
import org.rynware.client.feature.Feature;
import org.rynware.client.feature.impl.misc.NameProtect;
import org.rynware.client.Main;
import org.lwjgl.input.Mouse;
import net.minecraft.client.gui.Gui;
import java.awt.Color;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.gui.GuiMultiplayer;
import org.rynware.client.ui.mainmenu.NewMainMenu;
import org.apache.commons.lang3.RandomStringUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.IImageBuffer;
import java.io.File;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import org.rynware.client.ui.altmanager.alt.AltManager;
import net.minecraft.util.text.TextFormatting;
import org.rynware.client.ui.mainmenu.Shader;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.gui.GuiTextField;
import org.rynware.client.ui.altmanager.alt.AltLoginThread;
import org.rynware.client.ui.altmanager.alt.Alt;
import org.rynware.client.ui.altmanager.api.AltService;
import net.minecraft.client.gui.GuiScreen;

public class GuiAltManager extends GuiScreen
{
    public static final AltService altService;
    public Alt selectedAlt;
    public String status;
    private GuiAltButton login;
    private GuiAltButton remove;
    private GuiAltButton rename;
    private AltLoginThread loginThread;
    private float offset;
    private GuiTextField searchField;
    private ResourceLocation resourceLocation;
    private Shader shaderH;
    
    public GuiAltManager() {
        this.selectedAlt = null;
        this.status = TextFormatting.DARK_GRAY + "(" + TextFormatting.GRAY + AltManager.registry.size() + TextFormatting.DARK_GRAY + ")";
    }
    
    private void getDownloadImageSkin(final ResourceLocation resourceLocationIn, final String username) {
        final TextureManager textureManager = this.mc.getTextureManager();
        textureManager.getTexture(resourceLocationIn);
        final ThreadDownloadImageData textureObject = new ThreadDownloadImageData(null, String.format("https://minotar.net/avatar/%s/64.png", StringUtils.stripControlCodes(username)), DefaultPlayerSkin.getDefaultSkin(EntityPlayer.getOfflineUUID(username)), new ImageBufferDownload());
        textureManager.loadTexture(resourceLocationIn, textureObject);
    }
    
    public void actionPerformed(final GuiButton button) {
        switch (button.id) {
            case 1: {
                (this.loginThread = new AltLoginThread(this.selectedAlt)).start();
                break;
            }
            case 2: {
                if (this.loginThread != null) {
                    this.loginThread = null;
                }
                AltManager.registry.remove(this.selectedAlt);
                this.status = TextFormatting.GREEN + "Removed.";
                this.selectedAlt = null;
                break;
            }
            case 3: {
                this.mc.displayGuiScreen(new GuiAddAlt(this));
                break;
            }
            case 4: {
                this.mc.displayGuiScreen(new GuiAltLogin(this));
                break;
            }
            case 5: {
                final String randomName = RandomStringUtils.randomAlphabetic(5).toLowerCase() + RandomStringUtils.randomNumeric(2);
                (this.loginThread = new AltLoginThread(new Alt(randomName, ""))).start();
                AltManager.registry.add(new Alt(randomName, ""));
                break;
            }
            case 6: {
                this.mc.displayGuiScreen(new GuiRenameAlt(this));
                break;
            }
            case 7: {
                this.mc.displayGuiScreen(new NewMainMenu());
                break;
            }
            case 8: {
                this.status = TextFormatting.RED + "Refreshed!";
                break;
            }
            case 8931: {
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            }
            case 4545: {
                this.mc.displayGuiScreen(new GuiConnecting(this, this.mc, new ServerData(I18n.format("selectServer.defaultName", new Object[0]), "play.hypixel.net", false)));
                break;
            }
        }
    }
    
    @Override
    public void drawScreen(final int par1, final int par2, final float par3) {
        final ScaledResolution sr = new ScaledResolution(this.mc);
        Gui.drawRect(0.0, 0.0, sr.getScaledWidth(), sr.getScaledHeight(), new Color(241, 57, 225).getRGB());
        this.shaderH.draw(par1 / 300.0f, (sr.getScaledHeight() - par2) / 300.0f);
        super.drawScreen(par1, par2, par3);
        if (Mouse.hasWheel()) {
            final int wheel = Mouse.getDWheel();
            if (wheel < 0) {
                this.offset += 26.0;
                if (this.offset < 0.0) {
                    this.offset = 0.0f;
                }
            }
            else if (wheel > 0) {
                this.offset -= 26.0;
                if (this.offset < 0.0) {
                    this.offset = 0.0f;
                }
            }
        }
        final String altName = "Name: " + ((Main.instance.featureManager.getFeature(NameProtect.class).isEnabled() && NameProtect.myName.getBoolValue()) ? "Protected" : this.mc.session.getUsername());
        this.mc.sfui18.drawStringWithShadow(TextFormatting.GRAY + "~ User Info ~", 16.0, 40.0, -1);
        this.mc.sfui18.drawStringWithShadow(altName, 11.0, 50.0, 14540253);
        this.mc.sfui18.drawStringWithShadow("Account Status: " + TextFormatting.GREEN + "Working", 11.0, 60.0, 14540253);
        RenderUtils.drawRect(this.mc.sfui18.getStringWidth("Account Status: Working") + 14, this.mc.sfui18.getStringHeight("Account Status: Working") + 51, 9.0, this.mc.sfui18.getStringHeight("Account Status: Working") + 62, ColorUtils.getColor(255, 30));
        RenderUtils.drawRect(this.mc.sfui18.getStringWidth(altName) + 14, this.mc.sfui18.getStringHeight(altName) + 42, 9.0, this.mc.sfui18.getStringHeight(altName) + 51, ColorUtils.getColor(255, 30));
        GlStateManager.pushMatrix();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
        this.mc.sfui18.drawCenteredString("Account Manager", this.width / 2.0f, 10.0f, -1);
        this.mc.sfui18.drawCenteredString((this.loginThread == null) ? this.status : this.loginThread.getStatus(), this.width / 2.0f, 20.0f, -1);
        GlStateManager.pushMatrix();
        RenderUtils.scissorRect(0.0f, 33.0f, (float)this.width, this.height - 50);
        GL11.glEnable(3089);
        int y = 38;
        int number = 0;
        for (final Alt alt : this.getAlts()) {
            if (!this.isAltInArea(y)) {
                continue;
            }
            ++number;
            String name = alt.getMask().equals("") ? alt.getUsername() : alt.getMask();
            if (name.equalsIgnoreCase(this.mc.session.getUsername())) {
                name = "§n" + name;
            }
            final String prefix = alt.getStatus().equals(Alt.Status.Banned) ? "§c" : (alt.getStatus().equals(Alt.Status.NotWorking) ? "§m" : "");
            name = prefix + name + "§r §7| " + alt.getStatus().toFormatted();
            final String pass = alt.getPassword().equals("") ? "§cNot License" : alt.getPassword().replaceAll(".", "*");
            if (alt == this.selectedAlt) {
                GlStateManager.pushMatrix();
                final boolean hovering = par1 >= this.width / 1.5f + 5.0f && par1 <= this.width / 1.5 + 26.0 && par2 >= y - (double)this.offset - 4.0 && par2 <= y - (double)this.offset + 20.0;
                GlStateManager.popMatrix();
                if (this.isMouseOverAlt(par1, par2, y) && Mouse.isButtonDown(0)) {
                    RenderUtils.drawBorderedRect(this.width / 2.0f - 125.0f, y - (double)this.offset - 4.0, this.width / 1.5f, y - (double)this.offset + 30.0, 1.0, ColorUtils.getColor(255, 50), ColorUtils.getColor(40, 50));
                }
                else if (this.isMouseOverAlt(par1, par2, y - (double)this.offset)) {
                    RenderUtils.drawBorderedRect(this.width / 2.0f - 125.0f, y - (double)this.offset - 4.0, this.width / 1.5f, y - (double)this.offset + 30.0, 1.0, ColorUtils.getColor(255, 50), ColorUtils.getColor(40, 50));
                }
                else {
                    RenderUtils.drawBorderedRect(this.width / 2.0f - 125.0f, y - (double)this.offset - 4.0, this.width / 1.5f, y - (double)this.offset + 30.0, 1.0, ColorUtils.getColor(255, 50), ColorUtils.getColor(40, 50));
                }
            }
            final String numberP = "§7" + number + ". §f";
            GlStateManager.pushMatrix();
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            if (this.resourceLocation == null) {
                this.getDownloadImageSkin(this.resourceLocation = AbstractClientPlayer.getLocationSkin(name), name);
            }
            else {
                this.mc.getTextureManager().bindTexture(this.resourceLocation);
                GlStateManager.enableTexture2D();
                Gui.drawScaledCustomSizeModalRect(this.width / 2.0f - 161.0f, y - this.offset - 4.0f, 8.0f, 8.0f, 8.0f, 8.0f, 33.0f, 33.0f, 64.0f, 64.0f);
            }
            GlStateManager.popMatrix();
            this.mc.sfui18.drawCenteredString(numberP + ((Main.instance.featureManager.getFeature(NameProtect.class).isEnabled() && NameProtect.myName.getBoolValue()) ? "Protected" : name), this.width / 2.0f, (float)(y - (double)this.offset + 5.0), -1);
            this.mc.sfui18.drawCenteredString((alt.getStatus().equals(Alt.Status.NotWorking) ? "?m" : "") + pass, this.width / 2.0f, (float)(y - (double)this.offset + 15.0), ColorUtils.getColor(110));
            y += 40;
        }
        GL11.glDisable(3089);
        GL11.glPopMatrix();
        if (this.selectedAlt == null) {
            this.login.enabled = false;
            this.remove.enabled = false;
        }
        else {
            this.login.enabled = true;
            this.remove.enabled = true;
        }
        if (Keyboard.isKeyDown(200)) {
            this.offset -= 26.0;
        }
        else if (Keyboard.isKeyDown(208)) {
            this.offset += 26.0;
        }
        if (this.offset < 0.0) {
            this.offset = 0.0f;
        }
        this.searchField.drawTextBox();
        if (this.searchField.getText().isEmpty() && !this.searchField.isFocused()) {
            this.mc.sfui18.drawStringWithShadow("Search Alt", this.width / 2.0f + 120.0f, this.height - 18, ColorUtils.getColor(180));
        }
    }
    
    @Override
    public void initGui() {
        this.shaderH = new Shader("gltestbox.fsh");
        this.searchField = new GuiTextField(this.eventButton, this.mc.fontRendererObj, this.width / 2 + 116, this.height - 22, 72, 16);
        this.buttonList.add(this.login = new GuiAltButton(1, this.width / 2 - 122, this.height - 48, 100, 20, "Login"));
        this.buttonList.add(this.remove = new GuiAltButton(2, this.width / 2 - 40, this.height - 24, 70, 20, "Remove"));
        this.buttonList.add(new GuiAltButton(3, this.width / 2 + 4 + 86, this.height - 48, 100, 20, "Add"));
        this.buttonList.add(new GuiAltButton(4, this.width / 2 - 16, this.height - 48, 100, 20, "Direct Login"));
        this.buttonList.add(new GuiAltButton(5, this.width / 2 - 122, this.height - 24, 78, 20, "Random"));
        this.buttonList.add(this.rename = new GuiAltButton(6, this.width / 2 + 38, this.height - 24, 70, 20, "Edit"));
        this.login.enabled = false;
        this.remove.enabled = false;
        this.rename.enabled = false;
    }
    
    @Override
    protected void keyTyped(final char par1, final int par2) {
        this.searchField.textboxKeyTyped(par1, par2);
        if ((par1 == '\t' || par1 == '\r') && this.searchField.isFocused()) {
            this.searchField.setFocused(!this.searchField.isFocused());
        }
        try {
            super.keyTyped(par1, par2);
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
    }
    
    private boolean isAltInArea(final int y) {
        return y - this.offset <= this.height - 50;
    }
    
    private boolean isMouseOverAlt(final double x, final double y, final double y1) {
        return x >= this.width / 2.0f - 125.0f && y >= y1 - 4.0 && x <= this.width / 1.5 && y <= y1 + 20.0 && x >= 0.0 && y >= 33.0 && x <= this.width && y <= this.height - 50;
    }
    
    @Override
    protected void mouseClicked(final int par1, final int par2, final int par3) {
        this.searchField.mouseClicked(par1, par2, par3);
        if (this.offset < 0.0f) {
            this.offset = 0.0f;
        }
        double y = 38.0f - this.offset;
        for (final Alt alt : this.getAlts()) {
            if (this.isMouseOverAlt(par1, par2, y)) {
                if (alt == this.selectedAlt) {
                    this.actionPerformed(this.login);
                    return;
                }
                this.selectedAlt = alt;
            }
            y += 40.0;
        }
        try {
            super.mouseClicked(par1, par2, par3);
        }
        catch (final IOException e2) {
            e2.printStackTrace();
        }
    }
    
    private List<Alt> getAlts() {
        final List<Alt> altList = new ArrayList<Alt>();
        for (final Alt alt : AltManager.registry) {
            if (this.searchField.getText().isEmpty() || alt.getMask().toLowerCase().contains(this.searchField.getText().toLowerCase()) || alt.getUsername().toLowerCase().contains(this.searchField.getText().toLowerCase())) {
                altList.add(alt);
            }
        }
        return altList;
    }
    
    static {
        altService = new AltService();
    }
}
