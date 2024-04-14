// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.combat;

import org.rynware.client.event.EventTarget;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.item.ItemBow;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.feature.Feature;

public class FastBow extends Feature
{
    private final NumberSetting bowDelay;
    
    public FastBow() {
        super("FastBow", "\u0411\u044b\u0441\u0442\u0440\u043e \u0441\u0442\u0440\u0435\u043b\u044f\u0435\u0442 \u0438\u0437 \u043b\u0443\u043a\u0430", FeatureCategory.Combat);
        this.bowDelay = new NumberSetting("Bow Delay", 1.0f, 1.0f, 10.0f, 0.5f, () -> true);
        this.addSettings(this.bowDelay);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate e) {
        if (FastBow.mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && FastBow.mc.player.isBowing() && FastBow.mc.player.getItemInUseMaxCount() >= this.bowDelay.getNumberValue()) {
            FastBow.mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, FastBow.mc.player.getHorizontalFacing()));
            FastBow.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
            FastBow.mc.player.stopActiveHand();
        }
    }
}
