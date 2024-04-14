// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.combat;

import org.rynware.client.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.init.MobEffects;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import org.rynware.client.event.events.impl.packet.EventReceivePacket;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.feature.Feature;

public class Velocity extends Feature
{
    public static BooleanSetting cancelOtherDamage;
    public static ListSetting velocityMode;
    
    public Velocity() {
        super("Velocity", FeatureCategory.Combat);
        Velocity.velocityMode = new ListSetting("Velocity Mode", "Packet", () -> true, new String[] { "Packet", "Matrix", "PacketZero" });
        Velocity.cancelOtherDamage = new BooleanSetting("Cancel Other Damage", true, () -> true);
        this.addSettings(Velocity.velocityMode, Velocity.cancelOtherDamage);
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket event) {
        final String mode = Velocity.velocityMode.getOptions();
        this.setSuffix("" + mode);
        if (Velocity.cancelOtherDamage.getBoolValue() && Velocity.mc.player.hurtTime > 0 && event.getPacket() instanceof SPacketEntityVelocity && !Velocity.mc.player.isPotionActive(MobEffects.FIRE_RESISTANCE) && (Velocity.mc.player.isPotionActive(MobEffects.POISON) || Velocity.mc.player.isPotionActive(MobEffects.WITHER) || Velocity.mc.player.isBurning())) {
            event.setCancelled(true);
        }
        if (mode.equalsIgnoreCase("Packet")) {
            if ((event.getPacket() instanceof SPacketEntityVelocity || event.getPacket() instanceof SPacketExplosion) && ((SPacketEntityVelocity)event.getPacket()).getEntityID() == Velocity.mc.player.getEntityId()) {
                event.setCancelled(true);
            }
        }
        else if (mode.equalsIgnoreCase("PacketZero")) {
            if ((event.getPacket() instanceof SPacketEntityVelocity || event.getPacket() instanceof SPacketExplosion) && ((SPacketEntityVelocity)event.getPacket()).getEntityID() == Velocity.mc.player.getEntityId()) {
                event.setPacket(new SPacketEntityVelocity(Velocity.mc.player.getEntityId(), 0.0, 0.0, 0.0));
            }
        }
        else if (mode.equals("Matrix") && Velocity.mc.player.hurtTime > 8) {
            Velocity.mc.player.onGround = true;
        }
    }
}
