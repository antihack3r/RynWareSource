// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.combat;

import org.rynware.client.event.EventTarget;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemEgg;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.item.ItemBow;
import net.minecraft.util.EnumHand;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import org.rynware.client.event.events.impl.packet.EventSendPacket;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.Packet;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.feature.Feature;

public class SuperItems extends Feature
{
    private BooleanSetting bow;
    private BooleanSetting eggs;
    private BooleanSetting pearls;
    private BooleanSetting snowballs;
    private NumberSetting shotPower;
    
    public SuperItems() {
        super("SuperItems", "\u0412\u0440\u044f\u0434 \u043b\u0438 \u0440\u0430\u0431\u043e\u0442\u0430\u0435\u0442=(", FeatureCategory.Combat);
        this.shotPower = new NumberSetting("Shot Power", 70.0f, 1.0f, 100.0f, 5.0f, () -> true);
        this.bow = new BooleanSetting("Bow", true, () -> true);
        this.eggs = new BooleanSetting("Eggs", false, () -> true);
        this.pearls = new BooleanSetting("Pearls", false, () -> true);
        this.snowballs = new BooleanSetting("Snowballs", false, () -> true);
        this.addSettings(this.shotPower, this.bow, this.eggs, this.pearls, this.snowballs);
    }
    
    private void doShot() {
        SuperItems.mc.player.connection.sendPacket(new CPacketEntityAction(SuperItems.mc.player, CPacketEntityAction.Action.START_SPRINTING));
        for (int index = 0; index < this.shotPower.getNumberValue(); ++index) {
            SuperItems.mc.player.connection.sendPacket(new CPacketPlayer.Position(SuperItems.mc.player.posX, SuperItems.mc.player.posY + 1.0E-10, SuperItems.mc.player.posZ, false));
            SuperItems.mc.player.connection.sendPacket(new CPacketPlayer.Position(SuperItems.mc.player.posX, SuperItems.mc.player.posY - 1.0E-10, SuperItems.mc.player.posZ, true));
        }
    }
    
    @EventTarget
    public void onSendPacket(final EventSendPacket event) {
        if (event.getPacket() instanceof CPacketPlayerDigging) {
            final CPacketPlayerDigging packet = (CPacketPlayerDigging)event.getPacket();
            final ItemStack handStack2;
            if (packet.getAction() == CPacketPlayerDigging.Action.RELEASE_USE_ITEM && !(handStack2 = SuperItems.mc.player.getHeldItem(EnumHand.MAIN_HAND)).func_190926_b() && handStack2.getItem() instanceof ItemBow && this.bow.getBoolValue()) {
                this.doShot();
            }
        }
        else {
            final ItemStack handStack3;
            if (event.getPacket() instanceof CPacketPlayerTryUseItem && ((CPacketPlayerTryUseItem)event.getPacket()).getHand() == EnumHand.MAIN_HAND && !(handStack3 = SuperItems.mc.player.getHeldItem(EnumHand.MAIN_HAND)).func_190926_b()) {
                if (handStack3.getItem() instanceof ItemEgg && this.eggs.getBoolValue()) {
                    this.doShot();
                }
                else if (handStack3.getItem() instanceof ItemEnderPearl && this.pearls.getBoolValue()) {
                    this.doShot();
                }
                else if (handStack3.getItem() instanceof ItemSnowball && this.snowballs.getBoolValue()) {
                    this.doShot();
                }
            }
        }
    }
}
