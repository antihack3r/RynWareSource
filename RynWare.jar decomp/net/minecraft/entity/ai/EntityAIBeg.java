// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.ai;

import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.passive.EntityWolf;

public class EntityAIBeg extends EntityAIBase
{
    private final EntityWolf theWolf;
    private EntityPlayer thePlayer;
    private final World worldObject;
    private final float minPlayerDistance;
    private int timeoutCounter;
    
    public EntityAIBeg(final EntityWolf wolf, final float minDistance) {
        this.theWolf = wolf;
        this.worldObject = wolf.world;
        this.minPlayerDistance = minDistance;
        this.setMutexBits(2);
    }
    
    @Override
    public boolean shouldExecute() {
        this.thePlayer = this.worldObject.getClosestPlayerToEntity(this.theWolf, this.minPlayerDistance);
        return this.thePlayer != null && this.hasPlayerGotBoneInHand(this.thePlayer);
    }
    
    @Override
    public boolean continueExecuting() {
        return this.thePlayer.isEntityAlive() && this.theWolf.getDistanceSqToEntity(this.thePlayer) <= this.minPlayerDistance * this.minPlayerDistance && this.timeoutCounter > 0 && this.hasPlayerGotBoneInHand(this.thePlayer);
    }
    
    @Override
    public void startExecuting() {
        this.theWolf.setBegging(true);
        this.timeoutCounter = 40 + this.theWolf.getRNG().nextInt(40);
    }
    
    @Override
    public void resetTask() {
        this.theWolf.setBegging(false);
        this.thePlayer = null;
    }
    
    @Override
    public void updateTask() {
        this.theWolf.getLookHelper().setLookPosition(this.thePlayer.posX, this.thePlayer.posY + this.thePlayer.getEyeHeight(), this.thePlayer.posZ, 10.0f, (float)this.theWolf.getVerticalFaceSpeed());
        --this.timeoutCounter;
    }
    
    private boolean hasPlayerGotBoneInHand(final EntityPlayer player) {
        for (final EnumHand enumhand : EnumHand.values()) {
            final ItemStack itemstack = player.getHeldItem(enumhand);
            if (this.theWolf.isTamed() && itemstack.getItem() == Items.BONE) {
                return true;
            }
            if (this.theWolf.isBreedingItem(itemstack)) {
                return true;
            }
        }
        return false;
    }
}
