// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.draggable.component.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import org.rynware.client.feature.impl.combat.KillAura;
import org.rynware.client.feature.Feature;
import org.rynware.client.feature.impl.hud.TargetHUD;
import org.rynware.client.Main;
import org.rynware.client.draggable.component.DraggableComponent;

public class DraggableTargetHUD extends DraggableComponent
{
    public DraggableTargetHUD() {
        super("TargetHUD", 350, 25, 1, 1);
    }
    
    @Override
    public boolean allowDraw() {
        return Main.instance.featureManager.getFeature(TargetHUD.class).isEnabled() && (KillAura.target != null || Minecraft.getMinecraft().currentScreen instanceof GuiChat);
    }
}
