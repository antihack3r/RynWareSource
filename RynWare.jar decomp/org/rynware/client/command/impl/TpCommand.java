// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.command.impl;

import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import org.rynware.client.utils.other.ChatUtils;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.client.Minecraft;
import org.rynware.client.command.CommandAbstract;

public class TpCommand extends CommandAbstract
{
    public TpCommand() {
        super("tp", "Teleports you to x/y/z", ".tp", new String[] { "teleport", "tp" });
    }
    
    @Override
    public void execute(final String... args) {
        if (args.length == 4) {
            try {
                final int x = Integer.parseInt(args[1]);
                final int y = Integer.parseInt(args[2]);
                final int z = Integer.parseInt(args[3]);
                Minecraft.getMinecraft().player.setSprinting(false);
                if (Minecraft.getMinecraft().player.onGround) {
                    Minecraft.getMinecraft().player.jump();
                    TpCommand.mc.player.motionY = 0.41999998688697815;
                }
                for (int i = 0; i <= 3; ++i) {
                    Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Position(x, y - 2.0f, z, TpCommand.mc.player.onGround));
                }
                for (int i = 0; i <= 3; ++i) {
                    Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Position(x, y - 2.0f, z, true));
                }
                ChatUtils.addChatMessage(TextFormatting.GREEN + "" + x + " " + y + " " + z + "..");
                return;
            }
            catch (final Exception e) {
                e.printStackTrace();
                return;
            }
        }
        if (args.length == 2) {
            try {
                for (final EntityPlayer e2 : Minecraft.getMinecraft().world.playerEntities) {
                    if (args[1].equalsIgnoreCase(e2.getName())) {
                        final int x2 = (int)e2.posX;
                        final int y2 = (int)e2.posY;
                        final int z2 = (int)e2.posZ;
                        Minecraft.getMinecraft().player.setSprinting(false);
                        if (Minecraft.getMinecraft().player.onGround) {
                            Minecraft.getMinecraft().player.jump();
                        }
                        for (int j = 0; j <= 3; ++j) {
                            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Position(x2, y2, z2, false));
                        }
                        Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer(true));
                        ChatUtils.addChatMessage(TextFormatting.GREEN + "" + x2 + " " + y2 + " " + z2 + "..");
                    }
                }
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
}
