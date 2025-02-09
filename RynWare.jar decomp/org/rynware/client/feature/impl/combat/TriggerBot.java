// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.combat;

import org.rynware.client.event.EventTarget;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumHand;
import org.rynware.client.utils.movement.MovementUtils;
import net.minecraft.entity.item.EntityEnderCrystal;
import org.rynware.client.event.events.impl.player.EventUpdate;
import java.util.Iterator;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import org.rynware.client.friend.Friend;
import org.rynware.client.Main;
import net.minecraft.entity.EntityLivingBase;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.feature.Feature;

public class TriggerBot extends Feature
{
    public static NumberSetting range;
    public static BooleanSetting players;
    public static BooleanSetting mobs;
    public static BooleanSetting onlyCrit;
    public static NumberSetting critFallDist;
    
    public TriggerBot() {
        super("TriggerBot", FeatureCategory.Combat);
        TriggerBot.players = new BooleanSetting("Players", true, () -> true);
        TriggerBot.mobs = new BooleanSetting("Mobs", false, () -> true);
        TriggerBot.range = new NumberSetting("Trigger Range", 4.0f, 1.0f, 6.0f, 0.1f, () -> true);
        this.addSettings(TriggerBot.range, TriggerBot.players, TriggerBot.mobs, TriggerBot.onlyCrit, TriggerBot.critFallDist);
    }
    
    public static boolean canTrigger(final EntityLivingBase player) {
        for (final Friend friend : Main.instance.friendManager.getFriends()) {
            if (!player.getName().equals(friend.getName())) {
                continue;
            }
            return false;
        }
        if (player instanceof EntityPlayer || player instanceof EntityAnimal || player instanceof EntityMob || player instanceof EntityVillager) {
            if (player instanceof EntityPlayer && !TriggerBot.players.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityAnimal && !TriggerBot.mobs.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityMob && !TriggerBot.mobs.getBoolValue()) {
                return false;
            }
            if (player instanceof EntityVillager && !TriggerBot.mobs.getBoolValue()) {
                return false;
            }
        }
        return player != TriggerBot.mc.player;
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        final Entity entity = TriggerBot.mc.objectMouseOver.entityHit;
        if (entity == null || TriggerBot.mc.player.getDistanceToEntity(entity) > TriggerBot.range.getNumberValue() || entity instanceof EntityEnderCrystal || entity.isDead || ((EntityLivingBase)entity).getHealth() <= 0.0f) {
            return;
        }
        if (MovementUtils.isBlockAboveHead()) {
            if (TriggerBot.mc.player.fallDistance < TriggerBot.critFallDist.getNumberValue() && TriggerBot.mc.player.getCooledAttackStrength(0.8f) == 1.0f && TriggerBot.onlyCrit.getBoolValue() && !TriggerBot.mc.player.isOnLadder() && !TriggerBot.mc.player.isInLiquid() && !TriggerBot.mc.player.isInWeb && TriggerBot.mc.player.getRidingEntity() == null) {
                return;
            }
        }
        else if (TriggerBot.mc.player.fallDistance != 0.0f && TriggerBot.onlyCrit.getBoolValue() && !TriggerBot.mc.player.isOnLadder() && !TriggerBot.mc.player.isInLiquid() && !TriggerBot.mc.player.isInWeb && TriggerBot.mc.player.getRidingEntity() == null) {
            return;
        }
        if (canTrigger((EntityLivingBase)entity) && TriggerBot.mc.player.getCooledAttackStrength(0.0f) == 1.0f) {
            TriggerBot.mc.playerController.attackEntity(TriggerBot.mc.player, entity);
            TriggerBot.mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }
    
    static {
        TriggerBot.onlyCrit = new BooleanSetting("Only Crits", false, () -> true);
        TriggerBot.critFallDist = new NumberSetting("Fall Distance", 0.2f, 0.08f, 1.0f, 0.01f, () -> TriggerBot.onlyCrit.getBoolValue());
    }
}
