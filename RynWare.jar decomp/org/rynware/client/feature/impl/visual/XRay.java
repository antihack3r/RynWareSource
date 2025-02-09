// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.visual;

import org.rynware.client.event.events.impl.player.BlockHelper;
import org.rynware.client.utils.math.RotationHelper;
import net.minecraft.block.BlockAir;
import org.rynware.client.utils.render.RenderUtils;
import org.rynware.client.event.events.impl.render.EventRender3D;
import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.network.play.server.SPacketBlockChange;
import org.rynware.client.event.events.impl.packet.EventReceivePacket;
import org.rynware.client.event.events.impl.render.EventRenderBlock;
import org.rynware.client.event.EventTarget;
import net.minecraft.util.EnumFacing;
import org.rynware.client.event.events.impl.player.EventPreMotion;
import net.minecraft.block.state.IBlockState;
import java.util.Iterator;
import net.minecraft.block.Block;
import org.rynware.client.ui.settings.Setting;
import java.util.concurrent.CopyOnWriteArrayList;
import org.rynware.client.feature.impl.FeatureCategory;
import net.minecraft.util.math.Vec3i;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.utils.math.TimerHelper;
import org.rynware.client.feature.Feature;

public class XRay extends Feature
{
    public static TimerHelper timerHelper;
    public static int done;
    public static int all;
    public static BooleanSetting brutForce;
    public static BooleanSetting diamond;
    public static BooleanSetting gold;
    public static BooleanSetting iron;
    public static BooleanSetting emerald;
    public static BooleanSetting redstone;
    public static BooleanSetting lapis;
    public static BooleanSetting coal;
    private final NumberSetting checkSpeed;
    private final NumberSetting renderDist;
    private final NumberSetting rxz;
    private final NumberSetting ry;
    private final ArrayList<BlockPos> ores;
    private final ArrayList<BlockPos> toCheck;
    private final List<Vec3i> blocks;
    
    public XRay() {
        super("XRay", "\u041f\u0440\u043e\u0441\u0432\u0435\u0447\u0438\u0432\u0430\u0435\u0442 \u0440\u0443\u0434\u044b", FeatureCategory.Misc);
        this.ores = new ArrayList<BlockPos>();
        this.toCheck = new ArrayList<BlockPos>();
        this.blocks = new CopyOnWriteArrayList<Vec3i>();
        XRay.brutForce = new BooleanSetting("BrutForce", false, () -> true);
        this.renderDist = new NumberSetting("Render Distance", 35.0f, 15.0f, 150.0f, 5.0f, () -> !XRay.brutForce.getBoolValue());
        XRay.diamond = new BooleanSetting("Diamond", true, () -> true);
        XRay.gold = new BooleanSetting("Gold", false, () -> true);
        XRay.iron = new BooleanSetting("Iron", false, () -> true);
        XRay.emerald = new BooleanSetting("Emerald", false, () -> true);
        XRay.redstone = new BooleanSetting("Redstone", false, () -> true);
        XRay.lapis = new BooleanSetting("Lapis", false, () -> true);
        XRay.coal = new BooleanSetting("Coal", false, () -> true);
        this.checkSpeed = new NumberSetting("CheckSpeed", 4.0f, 1.0f, 10.0f, 1.0f, XRay.brutForce::getBoolValue);
        this.rxz = new NumberSetting("Radius XZ", 20.0f, 5.0f, 200.0f, 1.0f, XRay.brutForce::getBoolValue);
        this.ry = new NumberSetting("Radius Y", 6.0f, 2.0f, 50.0f, 1.0f, XRay.brutForce::getBoolValue);
        this.addSettings(this.renderDist, XRay.brutForce, this.checkSpeed, this.rxz, this.ry, XRay.diamond, XRay.gold, XRay.iron, XRay.emerald, XRay.redstone, XRay.lapis, XRay.coal);
    }
    
    @Override
    public void onEnable() {
        if (XRay.brutForce.getBoolValue()) {
            this.ores.clear();
            this.toCheck.clear();
            final int radXZ = (int)this.rxz.getNumberValue();
            final int radY = (int)this.ry.getNumberValue();
            final ArrayList<BlockPos> blockPositions = this.getBlocks(radXZ, radY, radXZ);
            for (final BlockPos pos : blockPositions) {
                final IBlockState state = XRay.mc.world.getBlockState(pos);
                if (this.isCheckableOre(Block.getIdFromBlock(state.getBlock()))) {
                    this.toCheck.add(pos);
                }
            }
            XRay.all = this.toCheck.size();
            XRay.done = 0;
            super.onEnable();
        }
    }
    
    @EventTarget
    public void onPre(final EventPreMotion e) {
        if (XRay.brutForce.getBoolValue()) {
            final String allDone = (XRay.done == XRay.all) ? ("Done: " + XRay.all) : ("" + XRay.done + " / " + XRay.all);
            this.setSuffix(allDone);
            if (XRay.timerHelper.hasReached(this.checkSpeed.getNumberValue()) && this.toCheck.size() >= 1) {
                final BlockPos blockPos = this.toCheck.get(0);
                XRay.mc.playerController.clickBlock(blockPos, EnumFacing.UP);
                this.toCheck.remove(0);
                ++XRay.done;
            }
            XRay.timerHelper.reset();
        }
    }
    
    @EventTarget
    public void onRenderBlock(final EventRenderBlock event) {
        final BlockPos pos = event.getPos();
        final IBlockState blockState = event.getState();
        if (this.isEnabledOre(Block.getIdFromBlock(blockState.getBlock()))) {
            final Vec3i vec3i = new Vec3i(pos.getX(), pos.getY(), pos.getZ());
            this.blocks.add(vec3i);
        }
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket e) {
        if (XRay.brutForce.getBoolValue()) {
            if (e.getPacket() instanceof SPacketBlockChange) {
                final SPacketBlockChange p = (SPacketBlockChange)e.getPacket();
                if (this.isEnabledOre(Block.getIdFromBlock(p.getBlockState().getBlock()))) {
                    this.ores.add(p.getBlockPosition());
                }
            }
            else if (e.getPacket() instanceof SPacketMultiBlockChange) {
                final SPacketMultiBlockChange p2 = (SPacketMultiBlockChange)e.getPacket();
                for (final SPacketMultiBlockChange.BlockUpdateData dat : p2.getChangedBlocks()) {
                    if (this.isEnabledOre(Block.getIdFromBlock(dat.getBlockState().getBlock()))) {
                        this.ores.add(dat.getPos());
                    }
                }
            }
        }
    }
    
    @EventTarget
    public void onRender3D(final EventRender3D e) {
        if (XRay.brutForce.getBoolValue()) {
            for (final BlockPos pos : this.ores) {
                final IBlockState state = XRay.mc.world.getBlockState(pos);
                final Block mat = state.getBlock();
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 56 && XRay.diamond.getBoolValue() && Block.getIdFromBlock(mat) == 56) {
                    RenderUtils.blockEspFrame(pos, 0.0f, 255.0f, 255.0f);
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 14 && XRay.gold.getBoolValue() && Block.getIdFromBlock(mat) == 14) {
                    RenderUtils.blockEspFrame(pos, 255.0f, 215.0f, 0.0f);
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 15 && XRay.iron.getBoolValue() && Block.getIdFromBlock(mat) == 15) {
                    RenderUtils.blockEspFrame(pos, 213.0f, 213.0f, 213.0f);
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 129 && XRay.emerald.getBoolValue() && Block.getIdFromBlock(mat) == 129) {
                    RenderUtils.blockEspFrame(pos, 0.0f, 255.0f, 77.0f);
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 73 && XRay.redstone.getBoolValue() && Block.getIdFromBlock(mat) == 73) {
                    RenderUtils.blockEspFrame(pos, 255.0f, 0.0f, 0.0f);
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 16 && XRay.coal.getBoolValue() && Block.getIdFromBlock(mat) == 16) {
                    RenderUtils.blockEspFrame(pos, 0.0f, 0.0f, 0.0f);
                }
                if (Block.getIdFromBlock(mat) != 0 && Block.getIdFromBlock(mat) == 21 && XRay.lapis.getBoolValue() && Block.getIdFromBlock(mat) == 21) {
                    RenderUtils.blockEspFrame(pos, 38.0f, 97.0f, 156.0f);
                }
            }
        }
        else {
            for (final Vec3i neededBlock : this.blocks) {
                final BlockPos pos2 = new BlockPos(neededBlock);
                final IBlockState state2 = XRay.mc.world.getBlockState(pos2);
                final Block stateBlock = state2.getBlock();
                final Block block = XRay.mc.world.getBlockState(pos2).getBlock();
                if (!(block instanceof BlockAir)) {
                    if (Block.getIdFromBlock(block) == 0) {
                        continue;
                    }
                    if (RotationHelper.getDistance(XRay.mc.player.posX, XRay.mc.player.posZ, neededBlock.getX(), neededBlock.getZ()) > this.renderDist.getNumberValue()) {
                        this.blocks.remove(neededBlock);
                    }
                    else {
                        switch (Block.getIdFromBlock(block)) {
                            case 56: {
                                if (XRay.diamond.getBoolValue()) {
                                    RenderUtils.blockEspFrame(pos2, 0.0f, 255.0f, 255.0f);
                                    continue;
                                }
                                continue;
                            }
                            case 14: {
                                if (XRay.gold.getBoolValue()) {
                                    RenderUtils.blockEspFrame(pos2, 255.0f, 215.0f, 0.0f);
                                    continue;
                                }
                                continue;
                            }
                            case 15: {
                                if (XRay.iron.getBoolValue()) {
                                    RenderUtils.blockEspFrame(pos2, 213.0f, 213.0f, 213.0f);
                                    continue;
                                }
                                continue;
                            }
                            case 129: {
                                if (XRay.emerald.getBoolValue()) {
                                    RenderUtils.blockEspFrame(pos2, 0.0f, 255.0f, 77.0f);
                                    continue;
                                }
                                continue;
                            }
                            case 73: {
                                if (XRay.redstone.getBoolValue()) {
                                    RenderUtils.blockEspFrame(pos2, 255.0f, 0.0f, 0.0f);
                                    continue;
                                }
                                continue;
                            }
                            case 16: {
                                if (XRay.coal.getBoolValue()) {
                                    RenderUtils.blockEspFrame(pos2, 0.0f, 0.0f, 0.0f);
                                    continue;
                                }
                                continue;
                            }
                            case 21: {
                                if (XRay.lapis.getBoolValue()) {
                                    RenderUtils.blockEspFrame(pos2, 38.0f, 97.0f, 156.0f);
                                    continue;
                                }
                                continue;
                            }
                        }
                    }
                }
            }
        }
    }
    
    private boolean isCheckableOre(final int id) {
        int check = 0;
        int check2 = 0;
        int check3 = 0;
        int check4 = 0;
        int check5 = 0;
        int check6 = 0;
        int check7 = 0;
        if (XRay.diamond.getBoolValue() && id != 0) {
            check = 56;
        }
        if (XRay.gold.getBoolValue() && id != 0) {
            check2 = 14;
        }
        if (XRay.iron.getBoolValue() && id != 0) {
            check3 = 15;
        }
        if (XRay.emerald.getBoolValue() && id != 0) {
            check4 = 129;
        }
        if (XRay.redstone.getBoolValue() && id != 0) {
            check5 = 73;
        }
        if (XRay.coal.getBoolValue() && id != 0) {
            check6 = 16;
        }
        if (XRay.lapis.getBoolValue() && id != 0) {
            check7 = 21;
        }
        return id != 0 && (id == check || id == check2 || id == check3 || id == check4 || id == check5 || id == check6 || id == check7);
    }
    
    private boolean isEnabledOre(final int id) {
        int check = 0;
        int check2 = 0;
        int check3 = 0;
        int check4 = 0;
        int check5 = 0;
        int check6 = 0;
        int check7 = 0;
        if (XRay.diamond.getBoolValue() && id != 0) {
            check = 56;
        }
        if (XRay.gold.getBoolValue() && id != 0) {
            check2 = 14;
        }
        if (XRay.iron.getBoolValue() && id != 0) {
            check3 = 15;
        }
        if (XRay.emerald.getBoolValue() && id != 0) {
            check4 = 129;
        }
        if (XRay.redstone.getBoolValue() && id != 0) {
            check5 = 73;
        }
        if (XRay.coal.getBoolValue() && id != 0) {
            check6 = 16;
        }
        if (XRay.lapis.getBoolValue() && id != 0) {
            check7 = 21;
        }
        return id != 0 && (id == check || id == check2 || id == check3 || id == check4 || id == check5 || id == check6 || id == check7);
    }
    
    private ArrayList<BlockPos> getBlocks(final int x, final int y, final int z) {
        final BlockPos min = new BlockPos(XRay.mc.player.posX - x, XRay.mc.player.posY - y, XRay.mc.player.posZ - z);
        final BlockPos max = new BlockPos(XRay.mc.player.posX + x, XRay.mc.player.posY + y, XRay.mc.player.posZ + z);
        return BlockHelper.getAllInBox(min, max);
    }
    
    static {
        XRay.timerHelper = new TimerHelper();
    }
}
