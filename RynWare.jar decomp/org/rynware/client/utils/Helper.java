// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.utils;

import net.minecraft.network.Packet;
import net.minecraft.client.gui.ScaledResolution;
import org.rynware.client.utils.math.TimerHelper;
import java.util.Random;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.Minecraft;

public interface Helper
{
    public static final Minecraft mc = Minecraft.getInstance();
    public static final Gui gui = new Gui();
    public static final Random random = new Random();
    public static final TimerHelper timerHelper = new TimerHelper();
    public static final ScaledResolution sr = new ScaledResolution(Helper.mc);
    
    default void sendPacket(final Packet<?> packet) {
        Helper.mc.player.connection.sendPacket(packet);
    }
}
