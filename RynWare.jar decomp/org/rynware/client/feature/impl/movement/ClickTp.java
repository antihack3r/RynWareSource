// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.movement;

import org.rynware.client.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.RayTraceResult;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.feature.Feature;

public class ClickTp extends Feature
{
    public ClickTp() {
        super("ClickTp", FeatureCategory.Player);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate e) {
        if (ClickTp.mc.gameSettings.keyBindUseItem.isKeyDown()) {
            final RayTraceResult object = ClickTp.mc.objectMouseOver;
            if (object.typeOfHit == RayTraceResult.Type.BLOCK) {
                ClickTp.mc.player.setSprinting(false);
                if (ClickTp.mc.player.onGround) {
                    ClickTp.mc.player.jump();
                    ClickTp.mc.player.motionY = 0.41999998688697815;
                }
                for (int i2 = 0; i2 <= 3; ++i2) {
                    ClickTp.mc.player.connection.sendPacket(new CPacketPlayer.Position(object.getBlockPos().getX(), object.getBlockPos().getY() + 1.0f, object.getBlockPos().getZ(), false));
                }
                for (int i2 = 0; i2 <= 3; ++i2) {
                    ClickTp.mc.player.connection.sendPacket(new CPacketPlayer.Position(object.getBlockPos().getX(), object.getBlockPos().getY(), object.getBlockPos().getZ(), false));
                }
            }
        }
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
    }
}
