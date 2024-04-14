// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.event.events.impl.player;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.init.Blocks;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockDirt;
import com.jhlabs.vecmath.Vector3f;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.block.BlockHopper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.block.BlockAir;
import net.minecraft.util.math.MathHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Block;
import org.rynware.client.utils.Helper;

public class BlockHelper implements Helper
{
    public static Block getBlock(final int x, final int y, final int z) {
        return BlockHelper.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
    }
    
    public static Block getBlock(final BlockPos pos) {
        return getState(pos).getBlock();
    }
    
    public static IBlockState getState(final BlockPos pos) {
        return BlockHelper.mc.world.getBlockState(pos);
    }
    
    public static BlockPos getPlayerPosLocal() {
        if (BlockHelper.mc.player == null) {
            return BlockPos.ORIGIN;
        }
        return new BlockPos(Math.floor(BlockHelper.mc.player.posX), Math.floor(BlockHelper.mc.player.posY), Math.floor(BlockHelper.mc.player.posZ));
    }
    
    public static boolean insideBlock() {
        for (int x = MathHelper.floor(Helper.mc.player.boundingBox.minX); x < MathHelper.floor(Helper.mc.player.boundingBox.maxX) + 1; ++x) {
            for (int y = MathHelper.floor(Helper.mc.player.boundingBox.minY); y < MathHelper.floor(Helper.mc.player.boundingBox.maxY) + 1; ++y) {
                for (int z = MathHelper.floor(Helper.mc.player.boundingBox.minZ); z < MathHelper.floor(Helper.mc.player.boundingBox.maxZ) + 1; ++z) {
                    final Block block = Helper.mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
                    if (block != null) {
                        if (!(block instanceof BlockAir)) {
                            AxisAlignedBB boundingBox = block.getCollisionBoundingBox(Helper.mc.world.getBlockState(new BlockPos(x, y, z)), Helper.mc.world, new BlockPos(x, y, z));
                            if (block instanceof BlockHopper) {
                                boundingBox = new AxisAlignedBB(x, y, z, x + 1, y + 1, z + 1);
                            }
                            if (boundingBox != null) {
                                if (Helper.mc.player.boundingBox.intersectsWith(boundingBox)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public static boolean blockIsSlipperiness() {
        return BlockHelper.mc.world.getBlockState(new BlockPos(BlockHelper.mc.player.posX, BlockHelper.mc.player.posY - 1.0, BlockHelper.mc.player.posZ)).getBlock().slipperiness == 0.98f;
    }
    
    public static boolean rayTrace(final BlockPos pos, final float yaw, final float pitch) {
        final Vec3d vec3d = BlockHelper.mc.player.getPositionEyes(1.0f);
        final Vec3d vec3d2 = Entity.getVectorForRotation(pitch, yaw);
        final Vec3d vec3d3 = vec3d.addVector(vec3d2.xCoord * 5.0, vec3d2.yCoord * 5.0, vec3d2.zCoord * 5.0);
        final RayTraceResult result = BlockHelper.mc.world.rayTraceBlocks(vec3d, vec3d3, false);
        return result != null && result.typeOfHit == RayTraceResult.Type.BLOCK && pos.equals(result.getBlockPos());
    }
    
    public static boolean isAboveLiquid(final Entity entity, final double posY) {
        if (entity == null) {
            return false;
        }
        final double n = entity.posY + posY;
        for (int i = MathHelper.floor(entity.posX); i < MathHelper.ceil(entity.posX); ++i) {
            for (int j = MathHelper.floor(entity.posZ); j < MathHelper.ceil(entity.posZ); ++j) {
                if (BlockHelper.mc.world.getBlockState(new BlockPos(i, (int)n, j)).getBlock() instanceof BlockLiquid) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean collideBlock(final AxisAlignedBB axisAlignedBB, final float boxYSize, final ICollide collide) {
        for (int x = MathHelper.floor(BlockHelper.mc.player.getEntityBoundingBox().minX); x < MathHelper.floor(BlockHelper.mc.player.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor(BlockHelper.mc.player.getEntityBoundingBox().minZ); z < MathHelper.floor(BlockHelper.mc.player.getEntityBoundingBox().maxZ) + 1; ++z) {
                final Block block = getBlock(new BlockPos(x, axisAlignedBB.minY + boxYSize, z));
                if (!collide.collideBlock(block)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public static boolean collideBlockIntersects(final AxisAlignedBB axisAlignedBB, final ICollide collide) {
        for (int x = MathHelper.floor(BlockHelper.mc.player.getEntityBoundingBox().minX); x < MathHelper.floor(BlockHelper.mc.player.getEntityBoundingBox().maxX) + 1; ++x) {
            for (int z = MathHelper.floor(BlockHelper.mc.player.getEntityBoundingBox().minZ); z < MathHelper.floor(BlockHelper.mc.player.getEntityBoundingBox().maxZ) + 1; ++z) {
                final BlockPos blockPos = new BlockPos(x, axisAlignedBB.minY, z);
                final Block block = getBlock(blockPos);
                final AxisAlignedBB boundingBox;
                if (block != null && collide.collideBlock(block) && (boundingBox = block.getCollisionBoundingBox(getState(blockPos), BlockHelper.mc.world, blockPos)) != null && BlockHelper.mc.player.getEntityBoundingBox().intersectsWith(boundingBox)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static Vector3f getBlock(final float radius, final int block) {
        Vector3f vector3f = null;
        float dist = radius;
        for (float i = radius; i >= -radius; --i) {
            for (float j = -radius; j <= radius; ++j) {
                for (float k = radius; k >= -radius; --k) {
                    final int posX = (int)(BlockHelper.mc.player.posX + i);
                    final int posY = (int)(BlockHelper.mc.player.posY + j);
                    final int posZ = (int)(BlockHelper.mc.player.posZ + k);
                    final float curDist = (float)BlockHelper.mc.player.getDistance(posX, posY, posZ);
                    if (Block.getIdFromBlock(getBlock(posX, posY - 1, posZ)) == block && getBlock(posX, posY, posZ) instanceof BlockAir) {
                        if (curDist <= dist) {
                            dist = curDist;
                            vector3f = new Vector3f((float)posX, (float)posY, (float)posZ);
                        }
                    }
                }
            }
        }
        return vector3f;
    }
    
    public static boolean IsValidBlockPos(final BlockPos pos) {
        final IBlockState state = BlockHelper.mc.world.getBlockState(pos);
        return (state.getBlock() instanceof BlockDirt || (state.getBlock() instanceof BlockGrass && !(state.getBlock() instanceof BlockFarmland))) && BlockHelper.mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR;
    }
    
    public static List<BlockPos> getSphere(final BlockPos loc, final float r, final int h, final boolean hollow, final boolean sphere, final int plus_y) {
        final ArrayList<BlockPos> circulate = new ArrayList<BlockPos>();
        final int cx = loc.getX();
        final int cy = loc.getY();
        final int cz = loc.getZ();
        for (int x = cx - (int)r; x <= cx + r; ++x) {
            for (int z = cz - (int)r; z <= cz + r; ++z) {
                int y = sphere ? (cy - (int)r) : cy;
                while (true) {
                    final float f2;
                    final float f = f2 = (sphere ? (cy + r) : ((float)(cy + h)));
                    if (y >= f) {
                        break;
                    }
                    final double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? ((cy - y) * (cy - y)) : 0);
                    if (dist < r * r && (!hollow || dist >= (r - 1.0f) * (r - 1.0f))) {
                        final BlockPos l = new BlockPos(x, y + plus_y, z);
                        circulate.add(l);
                    }
                    ++y;
                }
            }
        }
        return circulate;
    }
    
    public static ArrayList<BlockPos> getBlocks(final int x, final int y, final int z) {
        final BlockPos min = new BlockPos(BlockHelper.mc.player.posX - x, BlockHelper.mc.player.posY - y, BlockHelper.mc.player.posZ - z);
        final BlockPos max = new BlockPos(BlockHelper.mc.player.posX + x, BlockHelper.mc.player.posY + y, BlockHelper.mc.player.posZ + z);
        return getAllInBox(min, max);
    }
    
    public static ArrayList<BlockPos> getAllInBox(final BlockPos from, final BlockPos to) {
        final ArrayList<BlockPos> blocks = new ArrayList<BlockPos>();
        final BlockPos min = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
        final BlockPos max = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
        for (int x = min.getX(); x <= max.getX(); ++x) {
            for (int y = min.getY(); y <= max.getY(); ++y) {
                for (int z = min.getZ(); z <= max.getZ(); ++z) {
                    blocks.add(new BlockPos(x, y, z));
                }
            }
        }
        return blocks;
    }
    
    public interface ICollide
    {
        boolean collideBlock(final Block p0);
    }
}
