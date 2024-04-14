// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.player;

import org.rynware.client.event.EventTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.init.Items;
import org.rynware.client.event.events.impl.input.EventMouse;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.feature.Feature;

public class MiddleClickPearl extends Feature
{
    public MiddleClickPearl() {
        super("MiddleClickPearl", "\u041f\u0451\u0440\u043b \u043d\u0430 \u0441\u0440\u0435\u0434\u043d\u044e\u044e \u043a\u043d\u043e\u043f\u043a\u0443 \u043c\u044b\u0448\u0438", FeatureCategory.Player);
    }
    
    @EventTarget
    public void onMouseEvent(final EventMouse event) {
        if (event.getKey() == 2) {
            for (int i = 0; i < 9; ++i) {
                final ItemStack itemStack = MiddleClickPearl.mc.player.inventory.getStackInSlot(i);
                if (itemStack.getItem() == Items.ENDER_PEARL) {
                    MiddleClickPearl.mc.player.connection.sendPacket(new CPacketHeldItemChange(i));
                    MiddleClickPearl.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    MiddleClickPearl.mc.player.connection.sendPacket(new CPacketHeldItemChange(MiddleClickPearl.mc.player.inventory.currentItem));
                }
            }
        }
    }
    
    @Override
    public void onDisable() {
        MiddleClickPearl.mc.player.connection.sendPacket(new CPacketHeldItemChange(MiddleClickPearl.mc.player.inventory.currentItem));
        super.onDisable();
    }
}
