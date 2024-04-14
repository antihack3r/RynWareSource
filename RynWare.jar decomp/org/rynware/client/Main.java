// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client;

import org.rynware.client.command.macro.Macro;
import org.rynware.client.event.EventTarget;
import org.rynware.client.feature.Feature;
import org.rynware.client.event.events.impl.input.EventInputKey;
import org.rynware.client.utils.other.DiscordHelper;
import org.rynware.client.utils.math.TPSUtils;
import org.rynware.client.event.EventManager;
import org.rynware.client.files.impl.HudConfig;
import org.rynware.client.files.impl.MacroConfig;
import java.io.IOException;
import org.rynware.client.files.impl.FriendConfig;
import ViaMCP.ViaMCP;
import org.lwjgl.opengl.Display;
import net.minecraft.client.Minecraft;
import org.rynware.client.ui.clickgui.ClickGuiScreen;
import org.rynware.client.friend.FriendManager;
import org.rynware.client.command.CommandManager;
import org.rynware.client.ui.config.ConfigManager;
import org.rynware.client.command.macro.MacroManager;
import org.rynware.client.draggable.DraggableHUD;
import org.rynware.client.files.FileManager;
import org.rynware.client.feature.FeatureManager;

public class Main
{
    public Long time;
    public FeatureManager featureManager;
    public FileManager fileManager;
    public static long playTimeStart;
    public DraggableHUD draggableHUD;
    public MacroManager macroManager;
    public ConfigManager configManager;
    public CommandManager commandManager;
    public FriendManager friendManager;
    public ClickGuiScreen clickGui;
    public static Main instance;
    public String name;
    public String type;
    public String version;
    
    public Main() {
        this.name = "RynWare";
        this.type = "Pre";
        this.version = "1.0";
    }
    
    public static double deltaTime() {
        return (Minecraft.getDebugFPS() > 0) ? (1.0 / Minecraft.getDebugFPS()) : 1.0;
    }
    
    public void init() {
        this.time = System.currentTimeMillis();
        Display.setTitle(this.name + " " + this.type);
        (this.fileManager = new FileManager()).loadFiles();
        this.friendManager = new FriendManager();
        this.featureManager = new FeatureManager();
        this.macroManager = new MacroManager();
        this.configManager = new ConfigManager();
        this.draggableHUD = new DraggableHUD();
        this.commandManager = new CommandManager();
        this.clickGui = new ClickGuiScreen();
        try {
            ViaMCP.getInstance().start();
            ViaMCP.getInstance().initAsyncSlider();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        try {
            this.fileManager.getFile(FriendConfig.class).loadFile();
        }
        catch (final IOException e2) {
            e2.printStackTrace();
        }
        try {
            this.fileManager.getFile(MacroConfig.class).loadFile();
        }
        catch (final IOException e2) {
            e2.printStackTrace();
        }
        try {
            this.fileManager.getFile(HudConfig.class).loadFile();
        }
        catch (final IOException e2) {
            e2.printStackTrace();
        }
        EventManager.register(this);
        EventManager.register(new TPSUtils());
    }
    
    public void stop() {
        Main.instance.configManager.saveConfig("default");
        this.fileManager.saveFiles();
        DiscordHelper.stopRPC();
        EventManager.unregister(this);
    }
    
    @EventTarget
    public void onInputKey(final EventInputKey event) {
        this.featureManager.getAllFeatures().stream().filter(module -> module.getBind() == event.getKey()).forEach(Feature::toggle);
        this.macroManager.getMacros().stream().filter(macros -> macros.getKey() == event.getKey()).forEach(macros -> Minecraft.getMinecraft().player.sendChatMessage(macros.getValue()));
    }
    
    static {
        Main.playTimeStart = 0L;
        Main.instance = new Main();
    }
}
