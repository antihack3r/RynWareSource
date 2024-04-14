// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.misc;

import net.minecraft.inventory.Slot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.event.EventTarget;
import org.rynware.client.event.events.impl.packet.EventReceivePacket;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.utils.math.TimerHelper;
import net.minecraft.network.play.server.SPacketWindowItems;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.feature.Feature;

public class AirDropStealer extends Feature
{
    public static NumberSetting scrollerDelay;
    private SPacketWindowItems packet;
    TimerHelper timer;
    
    public AirDropStealer() {
        super("AirDropStealer", FeatureCategory.Misc);
        this.timer = new TimerHelper();
        AirDropStealer.scrollerDelay = new NumberSetting("Steal Delay", 7.0f, 0.0f, 1000.0f, 1.0f, () -> true);
        this.addSettings(AirDropStealer.scrollerDelay);
    }
    
    @EventTarget
    public void onReceive(final EventReceivePacket e) {
        if (e.getPacket() instanceof SPacketWindowItems) {
            this.packet = (SPacketWindowItems)e.getPacket();
        }
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate e) {
        if (AirDropStealer.mc.inGameHasFocus && this.packet != null && AirDropStealer.mc.player.inventoryContainer.windowId == this.packet.getWindowId() && AirDropStealer.mc.currentScreen instanceof GuiContainer) {
            final GuiContainer chest = (GuiContainer)AirDropStealer.mc.currentScreen;
            for (int i = 0; i < chest.inventorySlots.getInventory().size() - 36; ++i) {
                final Slot slot = chest.inventorySlots.getSlot(i);
                if (slot.getHasStack()) {
                    if (this.timer.hasReached((AirDropStealer.scrollerDelay.getNumberValue() == 0.0f) ? 1.0 : ((double)AirDropStealer.scrollerDelay.getNumberValue()))) {
                        AirDropStealer.mc.playerController.windowClick(AirDropStealer.mc.player.inventoryContainer.windowId, i, 1, ClickType.QUICK_MOVE, AirDropStealer.mc.player);
                        this.timer.reset();
                    }
                }
            }
        }
    }
}
