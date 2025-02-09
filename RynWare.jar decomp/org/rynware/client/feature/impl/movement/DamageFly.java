// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.movement;

import org.rynware.client.event.events.impl.player.EventPreMotion;
import org.rynware.client.event.EventTarget;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import org.rynware.client.event.events.impl.packet.EventReceivePacket;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.feature.Feature;

public class DamageFly extends Feature
{
    boolean velocity;
    int ticks;
    double motion;
    boolean damage;
    boolean isVelocity;
    public NumberSetting maxTicks;
    
    public DamageFly() {
        super("DamageFly", FeatureCategory.Movement);
        this.maxTicks = new NumberSetting("Max Ticks", 24.0f, 1.0f, 27.0f, 1.0f, () -> true);
        this.addSettings(this.maxTicks);
    }
    
    @EventTarget
    private void onPacket(final EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketEntityVelocity) {
            if (((SPacketEntityVelocity)event.getPacket()).getMotionY() > 0) {
                this.isVelocity = true;
            }
            if (((SPacketEntityVelocity)event.getPacket()).getMotionY() / 8000.0 > 0.2) {
                this.motion = ((SPacketEntityVelocity)event.getPacket()).getMotionY() / 8000.0;
                this.velocity = true;
            }
        }
    }
    
    @EventTarget
    private void onUpdate(final EventPreMotion event) {
        if (DamageFly.mc.player.hurtTime == 9) {
            this.damage = true;
        }
        if (this.damage && this.isVelocity) {
            if (this.velocity) {
                DamageFly.mc.player.motionY = this.motion;
                DamageFly.mc.player.jumpMovementFactor = 0.415f;
                ++this.ticks;
            }
            if (this.ticks >= 27) {
                this.isVelocity = false;
                this.velocity = false;
                this.damage = false;
                this.ticks = 0;
            }
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        this.damage = false;
        this.velocity = false;
        this.ticks = 0;
    }
}
