// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.command.impl;

import net.minecraft.util.math.MathHelper;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import org.rynware.client.utils.other.ChatUtils;
import net.minecraft.client.Minecraft;
import org.rynware.client.command.CommandAbstract;

public class ClipCommand extends CommandAbstract
{
    Minecraft mc;
    
    public ClipCommand() {
        super("vclip", "vclip | hclip", ".vclip | (hclip) <value>", new String[] { "vclip", "hclip" });
        this.mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void execute(final String... args) {
        if (args.length > 1) {
            if (args[0].equalsIgnoreCase("vclip")) {
                try {
                    ChatUtils.addChatMessage("vclipped " + Double.valueOf(args[1]) + " blocks.");
                    for (int i = 0; i < 10; ++i) {
                        this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY, this.mc.player.posZ, false));
                    }
                    for (int i = 0; i < 10; ++i) {
                        this.mc.player.connection.sendPacket(new CPacketPlayer.Position(this.mc.player.posX, this.mc.player.posY + Double.parseDouble(args[1]), this.mc.player.posZ, false));
                    }
                    this.mc.player.setPosition(this.mc.player.posX, this.mc.player.posY + Double.parseDouble(args[1]), this.mc.player.posZ);
                }
                catch (final Exception ex) {}
            }
            if (args[0].equalsIgnoreCase("hclip")) {
                try {
                    ChatUtils.addChatMessage("hclipped " + Double.valueOf(args[1]) + " blocks.");
                    final float f = this.mc.player.rotationYaw * 0.017453292f;
                    final double speed = Double.parseDouble(args[1]);
                    final double x = -(MathHelper.sin(f) * speed);
                    final double z = MathHelper.cos(f) * speed;
                    this.mc.player.setPosition(this.mc.player.posX + x, this.mc.player.posY, this.mc.player.posZ + z);
                }
                catch (final Exception ex2) {}
            }
        }
        else {
            ChatUtils.addChatMessage(this.getUsage());
        }
    }
}
