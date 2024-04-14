// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.combat;

import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import java.util.Iterator;
import net.minecraft.init.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import org.rynware.client.event.EventTarget;
import net.minecraft.init.MobEffects;
import org.rynware.client.event.events.impl.player.EventPreMotion;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import net.minecraft.item.ItemStack;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.feature.Feature;

public class AutoPotion extends Feature
{
    public NumberSetting delay;
    public BooleanSetting onlyGround;
    public BooleanSetting strenght;
    public BooleanSetting speed;
    public BooleanSetting fire_resistance;
    ItemStack held;
    
    public AutoPotion() {
        super("AutoPotion", "\u0410\u0432\u0442\u043e\u043c\u0430\u0442\u0438\u0447\u0435\u0441\u043a\u0438 \u0431\u0440\u043e\u0441\u0430\u0435\u0442 Splash \u0437\u0435\u043b\u044c\u044f \u043d\u0430\u0445\u043e\u0434\u044f\u0449\u0438\u0435\u0441\u044f \u0432 \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u0435", FeatureCategory.Combat);
        this.onlyGround = new BooleanSetting("Only Ground", true, () -> true);
        this.strenght = new BooleanSetting("Strenght", true, () -> true);
        this.speed = new BooleanSetting("Speed", true, () -> true);
        this.fire_resistance = new BooleanSetting("Fire Resistance", true, () -> true);
        this.delay = new NumberSetting("Throw Delay", 300.0f, 10.0f, 800.0f, 10.0f, () -> true);
        this.addSettings(this.delay, this.onlyGround, this.strenght, this.speed, this.fire_resistance);
    }
    
    @EventTarget
    public void onPre(final EventPreMotion event) {
        if (AutoPotion.timerHelper.hasReached(this.delay.getNumberValue())) {
            if (this.strenght.getBoolValue() && !AutoPotion.mc.player.isPotionActive(MobEffects.STRENGTH)) {
                this.throwPotion(5);
            }
            if (this.speed.getBoolValue() && !AutoPotion.mc.player.isPotionActive(MobEffects.SPEED)) {
                this.throwPotion(1);
            }
            if (this.fire_resistance.getBoolValue() && !AutoPotion.mc.player.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
                this.throwPotion(12);
            }
            AutoPotion.timerHelper.reset();
        }
    }
    
    private int getPotionIndexHb(final int id) {
        for (int i = 0; i < 9; ++i) {
            for (final PotionEffect potion : PotionUtils.getEffectsFromStack(AutoPotion.mc.player.inventory.getStackInSlot(i))) {
                if (potion.getPotion() == Potion.getPotionById(id) && AutoPotion.mc.player.inventory.getStackInSlot(i).getItem() == Items.SPLASH_POTION) {
                    return i;
                }
            }
        }
        return -1;
    }
    
    private void throwPotion(final int potionId) {
        if (this.onlyGround.getBoolValue() && !AutoPotion.mc.player.onGround) {
            return;
        }
        int index = -1;
        if (this.getPotionIndexHb(potionId) == -1) {
            return;
        }
        index = this.getPotionIndexHb(potionId);
        this.throwPot(index);
    }
    
    void throwPot(final int index) {
        AutoPotion.mc.player.connection.sendPacket(new CPacketHeldItemChange(index));
        AutoPotion.mc.player.connection.sendPacket(new CPacketPlayer.Rotation(AutoPotion.mc.player.rotationYaw, 90.0f, AutoPotion.mc.player.onGround));
        AutoPotion.mc.player.rotationPitchHead = 90.0f;
        AutoPotion.mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
        AutoPotion.mc.player.connection.sendPacket(new CPacketHeldItemChange(AutoPotion.mc.player.inventory.currentItem));
    }
    
    enum Potions
    {
        STRENGTH, 
        SPEED, 
        FIRERES;
    }
}
