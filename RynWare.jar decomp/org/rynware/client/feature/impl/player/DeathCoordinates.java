// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.player;

import org.rynware.client.event.EventTarget;
import org.rynware.client.utils.other.ChatUtils;
import org.rynware.client.ui.notification.NotificationRenderer;
import org.rynware.client.ui.notification.NotificationMode;
import net.minecraft.client.gui.GuiGameOver;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.feature.Feature;

public class DeathCoordinates extends Feature
{
    public DeathCoordinates() {
        super("DeathCoordinates", "\u041f\u043e\u043a\u0430\u0437\u044b\u0432\u0430\u0435\u0442 \u043a\u043e\u043e\u0440\u0434\u0438\u043d\u0430\u0442\u044b \u0441\u043c\u0435\u0440\u0442\u0438", FeatureCategory.Misc);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (DeathCoordinates.mc.player.getHealth() < 1.0f && DeathCoordinates.mc.currentScreen instanceof GuiGameOver) {
            final int x = DeathCoordinates.mc.player.getPosition().getX();
            final int y = DeathCoordinates.mc.player.getPosition().getY();
            final int z = DeathCoordinates.mc.player.getPosition().getZ();
            if (DeathCoordinates.mc.player.deathTime < 1) {
                NotificationRenderer.queue("Death Coordinates", "X: " + x + " Y: " + y + " Z: " + z, 10, NotificationMode.INFO);
                ChatUtils.addChatMessage("Death Coordinates: X: " + x + " Y: " + y + " Z: " + z);
            }
        }
    }
}
