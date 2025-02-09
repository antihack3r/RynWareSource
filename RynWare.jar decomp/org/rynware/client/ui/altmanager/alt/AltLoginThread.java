// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.altmanager.alt;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextFormatting;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import java.net.Proxy;
import org.rynware.client.ui.altmanager.api.AltService;
import org.rynware.client.ui.altmanager.GuiAltManager;
import net.minecraft.util.Session;
import net.minecraft.client.Minecraft;

public class AltLoginThread extends Thread
{
    private final Alt alt;
    private final Minecraft mc;
    private String status;
    
    public AltLoginThread(final Alt alt) {
        this.mc = Minecraft.getMinecraft();
        this.alt = alt;
        this.status = "\u0412§7Waiting...";
    }
    
    private Session createSession(final String username, final String password) {
        try {
            GuiAltManager.altService.switchService(AltService.EnumAltService.MOJANG);
            final YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
            final YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)service.createUserAuthentication(Agent.MINECRAFT);
            auth.setUsername(username);
            auth.setPassword(password);
            try {
                auth.logIn();
                return new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang");
            }
            catch (final AuthenticationException e) {
                return null;
            }
        }
        catch (final Exception e2) {
            return null;
        }
    }
    
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(final String status) {
        this.status = status;
    }
    
    @Override
    public void run() {
        if (this.alt.getPassword().equals("")) {
            this.mc.session = new Session(this.alt.getUsername(), "", "", "mojang");
            this.status = TextFormatting.GREEN + "Logged in - " + ChatFormatting.WHITE + this.alt.getUsername() + ChatFormatting.BOLD + ChatFormatting.RED + " (Not License)";
        }
        else {
            this.status = "Logging in...";
            final Session auth = this.createSession(this.alt.getUsername(), this.alt.getPassword());
            if (auth == null) {
                this.status = "Connect failed!";
                if (this.alt.getStatus().equals(Alt.Status.Unchecked)) {
                    this.alt.setStatus(Alt.Status.NotWorking);
                }
            }
            else {
                AltManager.lastAlt = new Alt(this.alt.getUsername(), this.alt.getPassword());
                this.status = TextFormatting.GREEN + "Logged in - " + ChatFormatting.WHITE + auth.getUsername() + ChatFormatting.BOLD + ChatFormatting.RED + " (License)";
                this.alt.setMask(auth.getUsername());
                this.mc.session = auth;
            }
        }
    }
}
