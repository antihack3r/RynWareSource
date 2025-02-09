// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.misc;

import java.util.ArrayList;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketKeepAlive;
import org.rynware.client.event.events.impl.packet.EventSendPacket;
import java.util.Iterator;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import java.util.Random;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import org.rynware.client.event.events.impl.player.EventPreMotion;
import org.rynware.client.event.EventTarget;
import net.minecraft.entity.player.EntityPlayer;
import org.rynware.client.event.events.impl.packet.EventAttackSilent;
import org.rynware.client.feature.impl.FeatureCategory;
import net.minecraft.entity.Entity;
import java.util.List;
import org.rynware.client.feature.Feature;

public class CombatDisabler extends Feature
{
    public static List<Entity> entities;
    public boolean isTeleporting;
    
    public CombatDisabler() {
        super("BackTrack", FeatureCategory.Misc);
    }
    
    @EventTarget
    public void onAttack(final EventAttackSilent e) {
        if (e.getTargetEntity() != null && e.getTargetEntity() instanceof EntityPlayer) {
            CombatDisabler.entities.clear();
            CombatDisabler.entities.add(e.getTargetEntity());
            this.toggle();
        }
    }
    
    @EventTarget
    public void onPreMotion(final EventPreMotion e) {
        if (CombatDisabler.entities.isEmpty()) {
            return;
        }
        this.isTeleporting = true;
        for (final Entity en : CombatDisabler.entities) {
            if (CombatDisabler.mc.player.ticksExisted % 8 == 0) {
                if (CombatDisabler.mc.player.onGround) {
                    CombatDisabler.mc.player.jump();
                }
                for (int i = 0; i <= 4; ++i) {
                    CombatDisabler.mc.player.connection.sendPacket(new CPacketPlayer.PositionRotation(en.posX, en.posY, en.posZ, en.prevRotationYaw, en.prevRotationPitch, false));
                    CombatDisabler.mc.player.connection.sendPacket(new CPacketConfirmTeleport(new Random().nextInt(4 - ((i < 4 && i > 0) ? i : 0))));
                }
                CombatDisabler.mc.player.connection.sendPacket(new CPacketPlayer(true));
                if (CombatDisabler.mc.player.posX == en.prevPosX && CombatDisabler.mc.player.posY == en.prevPosY && CombatDisabler.mc.player.posZ == en.prevPosZ) {
                    CombatDisabler.mc.player.setPosition(en.posX, en.posY, en.posZ);
                    this.isTeleporting = false;
                    break;
                }
                continue;
            }
        }
    }
    
    @EventTarget
    public void onSendPacket(final EventSendPacket e) {
        if (this.isEnabled() && this.isTeleporting && (e.getPacket() instanceof CPacketKeepAlive || e.getPacket() instanceof CPacketEntityAction)) {
            e.setCancelled(true);
        }
    }
    
    static {
        CombatDisabler.entities = new ArrayList<Entity>();
    }
}
