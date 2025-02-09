// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.player;

import org.rynware.client.event.EventTarget;
import org.lwjgl.input.Keyboard;
import net.minecraft.client.gui.GuiChat;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.feature.Feature;

public class GuiWalk extends Feature
{
    public GuiWalk() {
        super("GuiWalk", FeatureCategory.Player);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (!(GuiWalk.mc.currentScreen instanceof GuiChat)) {
            GuiWalk.mc.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(GuiWalk.mc.gameSettings.keyBindJump.getKeyCode());
            GuiWalk.mc.gameSettings.keyBindForward.pressed = Keyboard.isKeyDown(GuiWalk.mc.gameSettings.keyBindForward.getKeyCode());
            GuiWalk.mc.gameSettings.keyBindBack.pressed = Keyboard.isKeyDown(GuiWalk.mc.gameSettings.keyBindBack.getKeyCode());
            GuiWalk.mc.gameSettings.keyBindLeft.pressed = Keyboard.isKeyDown(GuiWalk.mc.gameSettings.keyBindLeft.getKeyCode());
            GuiWalk.mc.gameSettings.keyBindRight.pressed = Keyboard.isKeyDown(GuiWalk.mc.gameSettings.keyBindRight.getKeyCode());
            GuiWalk.mc.gameSettings.keyBindSprint.pressed = Keyboard.isKeyDown(GuiWalk.mc.gameSettings.keyBindSprint.getKeyCode());
        }
    }
    
    @Override
    public void onDisable() {
        GuiWalk.mc.gameSettings.keyBindJump.pressed = false;
        GuiWalk.mc.gameSettings.keyBindForward.pressed = false;
        GuiWalk.mc.gameSettings.keyBindBack.pressed = false;
        GuiWalk.mc.gameSettings.keyBindLeft.pressed = false;
        GuiWalk.mc.gameSettings.keyBindRight.pressed = false;
        GuiWalk.mc.gameSettings.keyBindSprint.pressed = false;
        super.onDisable();
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
}
