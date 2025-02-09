// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.movement;

import java.util.Arrays;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import org.rynware.client.event.EventTarget;
import net.minecraft.util.math.BlockPos;
import org.rynware.client.event.events.impl.player.EventPreMotion;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.feature.Feature;

public class AirJump extends Feature
{
    public float fall;
    
    public AirJump() {
        super("AirJump", FeatureCategory.Movement);
    }
    
    @EventTarget
    public void onUpdate(final EventPreMotion eventPreMotion) {
        final float ex2 = 1.0f;
        final float ex3 = 1.0f;
        if ((this.isBlockValid(AirJump.mc.world.getBlockState(new BlockPos(AirJump.mc.player.posX, AirJump.mc.player.posY - ex3, AirJump.mc.player.posZ)).getBlock()) || this.isBlockValid(AirJump.mc.world.getBlockState(new BlockPos(AirJump.mc.player.posX, AirJump.mc.player.posY - ex3, AirJump.mc.player.posZ)).getBlock()) || this.isBlockValid(AirJump.mc.world.getBlockState(new BlockPos(AirJump.mc.player.posX - ex2, AirJump.mc.player.posY - ex3, AirJump.mc.player.posZ - ex2)).getBlock()) || this.isBlockValid(AirJump.mc.world.getBlockState(new BlockPos(AirJump.mc.player.posX + ex2, AirJump.mc.player.posY - ex3, AirJump.mc.player.posZ + ex2)).getBlock()) || this.isBlockValid(AirJump.mc.world.getBlockState(new BlockPos(AirJump.mc.player.posX - ex2, AirJump.mc.player.posY - ex3, AirJump.mc.player.posZ + ex2)).getBlock()) || this.isBlockValid(AirJump.mc.world.getBlockState(new BlockPos(AirJump.mc.player.posX + ex2, AirJump.mc.player.posY - ex3, AirJump.mc.player.posZ - ex2)).getBlock()) || this.isBlockValid(AirJump.mc.world.getBlockState(new BlockPos(AirJump.mc.player.posX + ex2, AirJump.mc.player.posY - ex3, AirJump.mc.player.posZ)).getBlock()) || this.isBlockValid(AirJump.mc.world.getBlockState(new BlockPos(AirJump.mc.player.posX - ex2, AirJump.mc.player.posY - ex3, AirJump.mc.player.posZ)).getBlock()) || this.isBlockValid(AirJump.mc.world.getBlockState(new BlockPos(AirJump.mc.player.posX, AirJump.mc.player.posY - ex3, AirJump.mc.player.posZ + ex2)).getBlock()) || this.isBlockValid(AirJump.mc.world.getBlockState(new BlockPos(AirJump.mc.player.posX, AirJump.mc.player.posY - ex3, AirJump.mc.player.posZ - ex2)).getBlock())) && AirJump.mc.player.ticksExisted % 2 == 0 && AirJump.mc.gameSettings.keyBindJump.isKeyDown()) {
            AirJump.mc.player.jumpTicks = 0;
            AirJump.mc.player.fallDistance = 0.0f;
            eventPreMotion.setOnGround(true);
            AirJump.mc.player.onGround = true;
        }
    }
    
    public boolean isBlockValid(final Block block) {
        return block != Blocks.AIR && !Arrays.asList(6, 27, 28, 31, 32, 37, 38, 39, 40, 44, 77, 143, 175).contains(Block.getIdFromBlock(block));
    }
    
    @Override
    public void onEnable() {
        this.fall = AirJump.mc.player.fallDistance;
        super.onEnable();
    }
    
    @Override
    public void onDisable() {
        AirJump.mc.player.fallDistance = this.fall;
        super.onDisable();
    }
}
