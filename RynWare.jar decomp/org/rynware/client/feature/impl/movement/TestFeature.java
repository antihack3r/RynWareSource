// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.movement;

import net.minecraft.network.play.server.SPacketPlayerPosLook;
import org.rynware.client.event.events.impl.packet.EventReceivePacket;
import org.rynware.client.event.EventTarget;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import org.rynware.client.utils.movement.MovementUtils;
import org.rynware.client.event.events.impl.player.EventPreMotion;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.feature.Feature;

public class TestFeature extends Feature
{
    private int stage;
    private boolean jumping;
    
    public TestFeature() {
        super("TestFeature", FeatureCategory.Movement);
    }
    
    @Override
    public void onEnable() {
        this.jumping = false;
        this.stage = 0;
        super.onEnable();
    }
    
    @EventTarget
    public void onPreMotion(final EventPreMotion e) {
        if (this.stage == 0) {
            MovementUtils.setMotion(0.0);
            TestFeature.mc.timer.timerSpeed = 0.33f;
            if (TestFeature.mc.player.onGround) {
                TestFeature.mc.player.jump();
            }
            this.stage = 1;
        }
        if (this.stage == 1) {
            TestFeature.mc.player.setPositionAndUpdate(TestFeature.mc.player.posX, TestFeature.mc.player.posY - 1.0E-10, TestFeature.mc.player.posZ);
            TestFeature.mc.player.onGround = true;
            this.stage = ((TestFeature.mc.player.motionY == 0.0) ? 2 : 1);
        }
        if (this.stage == 2) {
            this.jumping = true;
            TestFeature.mc.player.connection.sendPacket(new CPacketPlayer.Position(TestFeature.mc.player.posX, TestFeature.mc.player.posY + (TestFeature.mc.player.onGround ? 9.0f : 8.335f), TestFeature.mc.player.posZ, false));
            TestFeature.mc.player.motionY = (TestFeature.mc.player.onGround ? 9.0 : 8.335000038146973);
            TestFeature.mc.timer.timerSpeed = 0.65f;
            this.stage = 3;
        }
        if (this.stage == 3) {
            if (TestFeature.mc.player.motionY <= 0.0) {
                this.jumping = false;
            }
            this.toggle();
        }
    }
    
    @EventTarget
    public void onReceive(final EventReceivePacket e) {
        if (e.getPacket() instanceof SPacketPlayerPosLook) {
            e.setCancelled(this.jumping);
            final SPacketPlayerPosLook packet = (SPacketPlayerPosLook)e.getPacket();
            TestFeature.mc.player.connection.sendPacket(new CPacketPlayer.Position(packet.x, packet.y, packet.z, false));
            TestFeature.mc.player.setPositionAndUpdate(packet.x, packet.y, packet.z);
        }
    }
    
    @Override
    public void onDisable() {
        this.jumping = false;
        TestFeature.mc.timer.timerSpeed = 1.0f;
        super.onDisable();
    }
}
