// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.world.gen;

import net.minecraft.world.World;
import net.minecraft.block.material.Material;
import com.google.common.base.MoreObjects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.MathHelper;
import java.util.Random;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.block.state.IBlockState;

public class MapGenCaves extends MapGenBase
{
    protected static final IBlockState BLK_LAVA;
    protected static final IBlockState BLK_AIR;
    protected static final IBlockState BLK_SANDSTONE;
    protected static final IBlockState BLK_RED_SANDSTONE;
    
    protected void addRoom(final long p_180703_1_, final int p_180703_3_, final int p_180703_4_, final ChunkPrimer p_180703_5_, final double p_180703_6_, final double p_180703_8_, final double p_180703_10_) {
        this.addTunnel(p_180703_1_, p_180703_3_, p_180703_4_, p_180703_5_, p_180703_6_, p_180703_8_, p_180703_10_, 1.0f + this.rand.nextFloat() * 6.0f, 0.0f, 0.0f, -1, -1, 0.5);
    }
    
    protected void addTunnel(final long p_180702_1_, final int p_180702_3_, final int p_180702_4_, final ChunkPrimer p_180702_5_, double p_180702_6_, double p_180702_8_, double p_180702_10_, final float p_180702_12_, float p_180702_13_, float p_180702_14_, int p_180702_15_, int p_180702_16_, final double p_180702_17_) {
        final double d0 = p_180702_3_ * 16 + 8;
        final double d2 = p_180702_4_ * 16 + 8;
        float f = 0.0f;
        float f2 = 0.0f;
        final Random random = new Random(p_180702_1_);
        if (p_180702_16_ <= 0) {
            final int i = this.range * 16 - 16;
            p_180702_16_ = i - random.nextInt(i / 4);
        }
        boolean flag2 = false;
        if (p_180702_15_ == -1) {
            p_180702_15_ = p_180702_16_ / 2;
            flag2 = true;
        }
        final int j = random.nextInt(p_180702_16_ / 2) + p_180702_16_ / 4;
        final boolean flag3 = random.nextInt(6) == 0;
        while (p_180702_15_ < p_180702_16_) {
            final double d3 = 1.5 + MathHelper.sin(p_180702_15_ * 3.1415927f / p_180702_16_) * p_180702_12_;
            final double d4 = d3 * p_180702_17_;
            final float f3 = MathHelper.cos(p_180702_14_);
            final float f4 = MathHelper.sin(p_180702_14_);
            p_180702_6_ += MathHelper.cos(p_180702_13_) * f3;
            p_180702_8_ += f4;
            p_180702_10_ += MathHelper.sin(p_180702_13_) * f3;
            if (flag3) {
                p_180702_14_ *= 0.92f;
            }
            else {
                p_180702_14_ *= 0.7f;
            }
            p_180702_14_ += f2 * 0.1f;
            p_180702_13_ += f * 0.1f;
            f2 *= 0.9f;
            f *= 0.75f;
            f2 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0f;
            f += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0f;
            if (!flag2 && p_180702_15_ == j && p_180702_12_ > 1.0f && p_180702_16_ > 0) {
                this.addTunnel(random.nextLong(), p_180702_3_, p_180702_4_, p_180702_5_, p_180702_6_, p_180702_8_, p_180702_10_, random.nextFloat() * 0.5f + 0.5f, p_180702_13_ - 1.5707964f, p_180702_14_ / 3.0f, p_180702_15_, p_180702_16_, 1.0);
                this.addTunnel(random.nextLong(), p_180702_3_, p_180702_4_, p_180702_5_, p_180702_6_, p_180702_8_, p_180702_10_, random.nextFloat() * 0.5f + 0.5f, p_180702_13_ + 1.5707964f, p_180702_14_ / 3.0f, p_180702_15_, p_180702_16_, 1.0);
                return;
            }
            if (flag2 || random.nextInt(4) != 0) {
                final double d5 = p_180702_6_ - d0;
                final double d6 = p_180702_10_ - d2;
                final double d7 = p_180702_16_ - p_180702_15_;
                final double d8 = p_180702_12_ + 2.0f + 16.0f;
                if (d5 * d5 + d6 * d6 - d7 * d7 > d8 * d8) {
                    return;
                }
                if (p_180702_6_ >= d0 - 16.0 - d3 * 2.0 && p_180702_10_ >= d2 - 16.0 - d3 * 2.0 && p_180702_6_ <= d0 + 16.0 + d3 * 2.0 && p_180702_10_ <= d2 + 16.0 + d3 * 2.0) {
                    int k2 = MathHelper.floor(p_180702_6_ - d3) - p_180702_3_ * 16 - 1;
                    int l = MathHelper.floor(p_180702_6_ + d3) - p_180702_3_ * 16 + 1;
                    int l2 = MathHelper.floor(p_180702_8_ - d4) - 1;
                    int m = MathHelper.floor(p_180702_8_ + d4) + 1;
                    int i2 = MathHelper.floor(p_180702_10_ - d3) - p_180702_4_ * 16 - 1;
                    int i3 = MathHelper.floor(p_180702_10_ + d3) - p_180702_4_ * 16 + 1;
                    if (k2 < 0) {
                        k2 = 0;
                    }
                    if (l > 16) {
                        l = 16;
                    }
                    if (l2 < 1) {
                        l2 = 1;
                    }
                    if (m > 248) {
                        m = 248;
                    }
                    if (i2 < 0) {
                        i2 = 0;
                    }
                    if (i3 > 16) {
                        i3 = 16;
                    }
                    boolean flag4 = false;
                    for (int j2 = k2; !flag4 && j2 < l; ++j2) {
                        for (int k3 = i2; !flag4 && k3 < i3; ++k3) {
                            for (int l3 = m + 1; !flag4 && l3 >= l2 - 1; --l3) {
                                if (l3 >= 0 && l3 < 256) {
                                    final IBlockState iblockstate = p_180702_5_.getBlockState(j2, l3, k3);
                                    if (iblockstate.getBlock() == Blocks.FLOWING_WATER || iblockstate.getBlock() == Blocks.WATER) {
                                        flag4 = true;
                                    }
                                    if (l3 != l2 - 1 && j2 != k2 && j2 != l - 1 && k3 != i2 && k3 != i3 - 1) {
                                        l3 = l2;
                                    }
                                }
                            }
                        }
                    }
                    if (!flag4) {
                        final BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                        for (int j3 = k2; j3 < l; ++j3) {
                            final double d9 = (j3 + p_180702_3_ * 16 + 0.5 - p_180702_6_) / d3;
                            for (int i4 = i2; i4 < i3; ++i4) {
                                final double d10 = (i4 + p_180702_4_ * 16 + 0.5 - p_180702_10_) / d3;
                                boolean flag5 = false;
                                if (d9 * d9 + d10 * d10 < 1.0) {
                                    for (int j4 = m; j4 > l2; --j4) {
                                        final double d11 = (j4 - 1 + 0.5 - p_180702_8_) / d4;
                                        if (d11 > -0.7 && d9 * d9 + d11 * d11 + d10 * d10 < 1.0) {
                                            final IBlockState iblockstate2 = p_180702_5_.getBlockState(j3, j4, i4);
                                            final IBlockState iblockstate3 = (IBlockState)MoreObjects.firstNonNull((Object)p_180702_5_.getBlockState(j3, j4 + 1, i4), (Object)MapGenCaves.BLK_AIR);
                                            if (iblockstate2.getBlock() == Blocks.GRASS || iblockstate2.getBlock() == Blocks.MYCELIUM) {
                                                flag5 = true;
                                            }
                                            if (this.canReplaceBlock(iblockstate2, iblockstate3)) {
                                                if (j4 - 1 < 10) {
                                                    p_180702_5_.setBlockState(j3, j4, i4, MapGenCaves.BLK_LAVA);
                                                }
                                                else {
                                                    p_180702_5_.setBlockState(j3, j4, i4, MapGenCaves.BLK_AIR);
                                                    if (flag5 && p_180702_5_.getBlockState(j3, j4 - 1, i4).getBlock() == Blocks.DIRT) {
                                                        blockpos$mutableblockpos.setPos(j3 + p_180702_3_ * 16, 0, i4 + p_180702_4_ * 16);
                                                        p_180702_5_.setBlockState(j3, j4 - 1, i4, this.worldObj.getBiome(blockpos$mutableblockpos).topBlock.getBlock().getDefaultState());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (flag2) {
                            break;
                        }
                    }
                }
            }
            ++p_180702_15_;
        }
    }
    
    protected boolean canReplaceBlock(final IBlockState p_175793_1_, final IBlockState p_175793_2_) {
        return p_175793_1_.getBlock() == Blocks.STONE || p_175793_1_.getBlock() == Blocks.DIRT || p_175793_1_.getBlock() == Blocks.GRASS || p_175793_1_.getBlock() == Blocks.HARDENED_CLAY || p_175793_1_.getBlock() == Blocks.STAINED_HARDENED_CLAY || p_175793_1_.getBlock() == Blocks.SANDSTONE || p_175793_1_.getBlock() == Blocks.RED_SANDSTONE || p_175793_1_.getBlock() == Blocks.MYCELIUM || p_175793_1_.getBlock() == Blocks.SNOW_LAYER || ((p_175793_1_.getBlock() == Blocks.SAND || p_175793_1_.getBlock() == Blocks.GRAVEL) && p_175793_2_.getMaterial() != Material.WATER);
    }
    
    @Override
    protected void recursiveGenerate(final World worldIn, final int chunkX, final int chunkZ, final int p_180701_4_, final int p_180701_5_, final ChunkPrimer chunkPrimerIn) {
        int i = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(15) + 1) + 1);
        if (this.rand.nextInt(7) != 0) {
            i = 0;
        }
        for (int j = 0; j < i; ++j) {
            final double d0 = chunkX * 16 + this.rand.nextInt(16);
            final double d2 = this.rand.nextInt(this.rand.nextInt(120) + 8);
            final double d3 = chunkZ * 16 + this.rand.nextInt(16);
            int k = 1;
            if (this.rand.nextInt(4) == 0) {
                this.addRoom(this.rand.nextLong(), p_180701_4_, p_180701_5_, chunkPrimerIn, d0, d2, d3);
                k += this.rand.nextInt(4);
            }
            for (int l = 0; l < k; ++l) {
                final float f = this.rand.nextFloat() * 6.2831855f;
                final float f2 = (this.rand.nextFloat() - 0.5f) * 2.0f / 8.0f;
                float f3 = this.rand.nextFloat() * 2.0f + this.rand.nextFloat();
                if (this.rand.nextInt(10) == 0) {
                    f3 *= this.rand.nextFloat() * this.rand.nextFloat() * 3.0f + 1.0f;
                }
                this.addTunnel(this.rand.nextLong(), p_180701_4_, p_180701_5_, chunkPrimerIn, d0, d2, d3, f3, f, f2, 0, 0, 1.0);
            }
        }
    }
    
    static {
        BLK_LAVA = Blocks.LAVA.getDefaultState();
        BLK_AIR = Blocks.AIR.getDefaultState();
        BLK_SANDSTONE = Blocks.SANDSTONE.getDefaultState();
        BLK_RED_SANDSTONE = Blocks.RED_SANDSTONE.getDefaultState();
    }
}
