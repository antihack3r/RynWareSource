// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.player;

import net.minecraft.client.entity.EntityPlayerSP;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.event.EventTarget;
import org.rynware.client.utils.movement.MovementUtils;
import net.minecraft.network.play.client.CPacketPlayer;
import org.rynware.client.event.events.impl.packet.EventSendPacket;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.feature.Feature;

public class NoSlowDown extends Feature
{
    public static NumberSetting percentage;
    private final ListSetting noSlowMode;
    private BooleanSetting autoJump;
    public int usingTicks;
    
    public NoSlowDown() {
        super("NoSlowDown", "\u0423\u0431\u0438\u0440\u0430\u0435\u0442 \u0437\u0430\u043c\u0435\u0434\u043b\u0435\u043d\u0438\u0435 \u0443 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432", FeatureCategory.Player);
        this.noSlowMode = new ListSetting("NoSlow Mode", "Matrix", () -> true, new String[] { "Vanilla", "Matrix", "Matrix New" });
        this.autoJump = new BooleanSetting("Auto jump", false, () -> true);
        this.addSettings(this.noSlowMode, NoSlowDown.percentage);
    }
    
    @EventTarget
    public void onSendPacket(final EventSendPacket eventSendPacket) {
        final CPacketPlayer packet = (CPacketPlayer)eventSendPacket.getPacket();
        if (this.noSlowMode.currentMode.equals("Matrix New") && NoSlowDown.mc.player.isHandActive() && MovementUtils.isMoving() && !NoSlowDown.mc.gameSettings.keyBindJump.pressed) {
            packet.y = ((NoSlowDown.mc.player.ticksExisted % 2 == 0) ? (packet.y + 6.0E-4) : (packet.y + 2.0E-4));
            packet.onGround = false;
            if (NoSlowDown.mc.player.onGround && this.autoJump.getBoolValue()) {
                NoSlowDown.mc.player.jump();
            }
            NoSlowDown.mc.player.onGround = false;
        }
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        this.usingTicks = (NoSlowDown.mc.player.isUsingItem() ? (++this.usingTicks) : 0);
        if (!this.isEnabled() || !NoSlowDown.mc.player.isUsingItem()) {
            return;
        }
        if (this.noSlowMode.currentMode.equals("Matrix") && NoSlowDown.mc.player.isUsingItem()) {
            if (NoSlowDown.mc.player.onGround && !NoSlowDown.mc.gameSettings.keyBindJump.isKeyDown()) {
                if (NoSlowDown.mc.player.ticksExisted % 2 == 0) {
                    final EntityPlayerSP player = NoSlowDown.mc.player;
                    player.motionX *= 0.35;
                    final EntityPlayerSP player2 = NoSlowDown.mc.player;
                    player2.motionZ *= 0.35;
                }
            }
            else if (NoSlowDown.mc.player.fallDistance > 0.2) {
                final EntityPlayerSP player3 = NoSlowDown.mc.player;
                player3.motionX *= 0.9100000262260437;
                final EntityPlayerSP player4 = NoSlowDown.mc.player;
                player4.motionZ *= 0.9100000262260437;
            }
        }
    }
    
    static {
        NoSlowDown.percentage = new NumberSetting("Percentage", 100.0f, 0.0f, 100.0f, 1.0f, () -> true, NumberSetting.NumberType.PERCENTAGE);
    }
}
