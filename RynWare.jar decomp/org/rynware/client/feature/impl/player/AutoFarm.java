// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.player;

import net.minecraft.network.play.server.SPacketMultiBlockChange;
import net.minecraft.network.play.server.SPacketBlockChange;
import org.rynware.client.event.events.impl.packet.EventReceivePacket;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.world.World;
import net.minecraft.block.BlockCrops;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import net.minecraft.item.ItemHoe;
import java.util.Comparator;
import net.minecraft.entity.Entity;
import org.rynware.client.utils.math.RotationHelper;
import java.util.function.Predicate;
import org.rynware.client.event.events.impl.player.BlockHelper;
import org.rynware.client.event.events.impl.player.EventPreMotion;
import java.util.Iterator;
import org.rynware.client.utils.render.RenderUtils;
import org.rynware.client.event.events.impl.render.EventRender3D;
import net.minecraft.item.ItemFood;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import org.rynware.client.event.EventTarget;
import net.minecraft.util.EnumHand;
import net.minecraft.client.settings.KeyBinding;
import org.rynware.client.event.events.impl.player.EventUpdate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.BlockFarmland;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSeeds;
import org.rynware.client.ui.settings.Setting;
import java.awt.Color;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.ColorSetting;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.utils.math.TimerHelper;
import net.minecraft.util.math.BlockPos;
import java.util.ArrayList;
import org.rynware.client.feature.Feature;

public class AutoFarm extends Feature
{
    private boolean isActive;
    private int oldSlot;
    public static boolean isEating;
    ArrayList<BlockPos> crops;
    ArrayList<BlockPos> check;
    TimerHelper timerHelper;
    TimerHelper timerHelper2;
    private final BooleanSetting autoFarm;
    private ListSetting farmMode;
    private final NumberSetting delay;
    private final NumberSetting radius;
    private final BooleanSetting autoHoe;
    private final BooleanSetting farmESP;
    private final ColorSetting color;
    private final BooleanSetting autoEat;
    private final NumberSetting feed;
    
    public AutoFarm() {
        super("AutoFarm", "\u0410\u0432\u0442\u043e\u0444\u0430\u0440\u043c\u0438\u0442", FeatureCategory.Player);
        this.crops = new ArrayList<BlockPos>();
        this.check = new ArrayList<BlockPos>();
        this.timerHelper = new TimerHelper();
        this.timerHelper2 = new TimerHelper();
        this.autoFarm = new BooleanSetting("Auto Farm", true, () -> true);
        this.farmMode = new ListSetting("AutoFarm Mode", "Harvest", this.autoFarm::getBoolValue, new String[] { "Harvest", "Plant" });
        this.delay = new NumberSetting("AutoFarm Delay", 2.0f, 0.0f, 10.0f, 0.1f, () -> true);
        this.radius = new NumberSetting("AutoFarm Radius", 4.0f, 1.0f, 7.0f, 0.1f, () -> true);
        this.autoHoe = new BooleanSetting("Auto Hoe", false, () -> true);
        this.farmESP = new BooleanSetting("Draw Box", true, () -> this.autoFarm.getBoolValue() && this.farmMode.currentMode.equals("Harvest"));
        this.color = new ColorSetting("Box Color", new Color(16777215).getRGB(), this.farmESP::getBoolValue);
        this.autoEat = new BooleanSetting("Auto Eat", true, () -> true);
        this.feed = new NumberSetting("Feed Value", 15.0f, 1.0f, 20.0f, 1.0f, this.autoEat::getBoolValue);
        this.addSettings(this.farmMode, this.autoFarm, this.farmESP, this.color, this.autoHoe, this.delay, this.radius, this.autoEat, this.feed);
    }
    
    public static boolean doesHaveSeeds() {
        for (int i = 0; i < 9; ++i) {
            AutoFarm.mc.player.inventory.getStackInSlot(i);
            if (AutoFarm.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemSeeds) {
                return true;
            }
        }
        return false;
    }
    
    public static int searchSeeds() {
        for (int i = 0; i < 45; ++i) {
            final ItemStack itemStack = AutoFarm.mc.player.inventoryContainer.getSlot(i).getStack();
            if (itemStack.getItem() instanceof ItemSeeds) {
                return i;
            }
        }
        return -1;
    }
    
    public static int getSlotWithSeeds() {
        for (int i = 0; i < 9; ++i) {
            AutoFarm.mc.player.inventory.getStackInSlot(i);
            if (AutoFarm.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemSeeds) {
                return i;
            }
        }
        return 0;
    }
    
    @Override
    public void onEnable() {
        this.crops.clear();
        this.check.clear();
        super.onEnable();
    }
    
    private boolean isOnCrops() {
        for (double x = AutoFarm.mc.player.boundingBox.minX; x < AutoFarm.mc.player.boundingBox.maxX; x += 0.009999999776482582) {
            for (double z = AutoFarm.mc.player.boundingBox.minZ; z < AutoFarm.mc.player.boundingBox.maxZ; z += 0.009999999776482582) {
                final Block block = AutoFarm.mc.world.getBlockState(new BlockPos(x, AutoFarm.mc.player.posY - 0.1, z)).getBlock();
                if (!(block instanceof BlockFarmland) && !(block instanceof BlockSoulSand) && !(block instanceof BlockSand) && !(block instanceof BlockAir)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private boolean IsValidBlockPos(final BlockPos pos) {
        final IBlockState state = AutoFarm.mc.world.getBlockState(pos);
        return (state.getBlock() instanceof BlockFarmland || state.getBlock() instanceof BlockSand || state.getBlock() instanceof BlockSoulSand) && AutoFarm.mc.world.getBlockState(pos.up()).getBlock() == Blocks.AIR;
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate e) {
        if (AutoFarm.mc.player == null || AutoFarm.mc.world == null) {
            return;
        }
        if (this.autoEat.getBoolValue()) {
            if (this.isFood()) {
                if (this.isFood() && AutoFarm.mc.player.getFoodStats().getFoodLevel() <= this.feed.getNumberValue()) {
                    this.isActive = true;
                    KeyBinding.setKeyBindState(AutoFarm.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                }
                else if (this.isActive) {
                    KeyBinding.setKeyBindState(AutoFarm.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
                    this.isActive = false;
                }
            }
            else {
                if (AutoFarm.isEating && !AutoFarm.mc.player.isHandActive()) {
                    if (this.oldSlot != -1) {
                        AutoFarm.mc.player.inventory.currentItem = this.oldSlot;
                        this.oldSlot = -1;
                    }
                    AutoFarm.isEating = false;
                    KeyBinding.setKeyBindState(AutoFarm.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
                    return;
                }
                if (AutoFarm.isEating) {
                    return;
                }
                if (this.isValid(AutoFarm.mc.player.getHeldItemOffhand(), AutoFarm.mc.player.getFoodStats().getFoodLevel())) {
                    AutoFarm.mc.player.setActiveHand(EnumHand.OFF_HAND);
                    AutoFarm.isEating = true;
                    KeyBinding.setKeyBindState(AutoFarm.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                    AutoFarm.mc.rightClickMouse();
                }
                else {
                    for (int i = 0; i < 9; ++i) {
                        if (this.isValid(AutoFarm.mc.player.inventory.getStackInSlot(i), AutoFarm.mc.player.getFoodStats().getFoodLevel())) {
                            this.oldSlot = AutoFarm.mc.player.inventory.currentItem;
                            AutoFarm.mc.player.inventory.currentItem = i;
                            AutoFarm.isEating = true;
                            KeyBinding.setKeyBindState(AutoFarm.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                            AutoFarm.mc.rightClickMouse();
                            return;
                        }
                    }
                }
            }
        }
    }
    
    private boolean itemCheck(final Item item) {
        return item != Items.ROTTEN_FLESH && item != Items.SPIDER_EYE && item != Items.POISONOUS_POTATO && (item != Items.FISH || new ItemStack(Items.FISH).getItemDamage() != 3);
    }
    
    private boolean isValid(final ItemStack stack, final int food) {
        return stack.getItem() instanceof ItemFood && this.feed.getNumberValue() - food >= ((ItemFood)stack.getItem()).getHealAmount(stack) && this.itemCheck(stack.getItem());
    }
    
    private boolean isFood() {
        return AutoFarm.mc.player.getHeldItemOffhand().getItem() instanceof ItemFood;
    }
    
    @EventTarget
    public void onRender3D(final EventRender3D event) {
        if (AutoFarm.mc.player == null || AutoFarm.mc.world == null) {
            return;
        }
        if (this.farmESP.getBoolValue() && this.farmMode.currentMode.equals("Harvest")) {
            final ArrayList<BlockPos> blockPositions = this.getBlocks(this.radius.getNumberValue(), 0.0f, this.radius.getNumberValue());
            for (final BlockPos pos : blockPositions) {
                final Color cropsColor = new Color(this.color.getColorValue());
                final BlockPos blockPos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
                RenderUtils.blockEsp(blockPos, cropsColor, false, 1.0, 1.0);
            }
        }
    }
    
    @EventTarget
    public void onPreMotion(final EventPreMotion event) {
        if (AutoFarm.mc.player == null && AutoFarm.mc.world == null) {
            return;
        }
        final BlockPos pos;
        if (this.autoHoe.getBoolValue() && (pos = BlockHelper.getSphere(BlockHelper.getPlayerPosLocal(), this.radius.getNumberValue(), 6, false, true, 0).stream().filter(BlockHelper::IsValidBlockPos).min(Comparator.comparing(blockPos -> RotationHelper.getDistanceOfEntityToBlock(AutoFarm.mc.player, blockPos))).orElse(null)) != null && AutoFarm.mc.player.getHeldItemMainhand().getItem() instanceof ItemHoe) {
            final float[] rots = RotationHelper.getRotationVector(new Vec3d(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f));
            event.setYaw(rots[0]);
            event.setPitch(rots[1]);
            AutoFarm.mc.player.renderYawOffset = rots[0];
            AutoFarm.mc.player.rotationYawHead = rots[0];
            AutoFarm.mc.player.rotationPitchHead = rots[1];
            if (this.timerHelper2.hasReached(this.delay.getNumberValue() * 100.0f)) {
                AutoFarm.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, EnumFacing.UP, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                AutoFarm.mc.player.swingArm(EnumHand.MAIN_HAND);
                this.timerHelper2.reset();
            }
        }
        if (this.farmMode.currentMode.equals("Plant") && !doesHaveSeeds() && searchSeeds() != -1) {
            AutoFarm.mc.playerController.windowClick(0, searchSeeds(), 1, ClickType.QUICK_MOVE, AutoFarm.mc.player);
        }
    }
    
    @EventTarget
    public void onPre(final EventPreMotion e) {
        if (this.autoFarm.getBoolValue()) {
            final String mode = this.farmMode.getOptions();
            if (mode.equalsIgnoreCase("Harvest")) {
                final ArrayList<BlockPos> blockPositions = this.getBlocks(this.radius.getNumberValue(), this.radius.getNumberValue(), this.radius.getNumberValue());
                for (final BlockPos pos : blockPositions) {
                    final IBlockState state = BlockHelper.getState(pos);
                    if (!this.isCheck(Block.getIdFromBlock(state.getBlock()))) {
                        continue;
                    }
                    if (!this.isCheck(0)) {
                        this.check.add(pos);
                    }
                    final Block block = AutoFarm.mc.world.getBlockState(pos).getBlock();
                    final BlockPos downPos = pos.down(1);
                    final BlockCrops crop;
                    if (!(block instanceof BlockCrops) || (crop = (BlockCrops)block).canGrow(AutoFarm.mc.world, pos, state, true) || !this.timerHelper.hasReached(this.delay.getNumberValue() * 100.0f)) {
                        continue;
                    }
                    if (pos == null) {
                        continue;
                    }
                    final float[] rots = RotationHelper.getRotationVector(new Vec3d(pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f));
                    e.setYaw(rots[0]);
                    e.setPitch(rots[1]);
                    AutoFarm.mc.player.renderYawOffset = rots[0];
                    AutoFarm.mc.player.rotationYawHead = rots[0];
                    AutoFarm.mc.player.rotationPitchHead = rots[1];
                    AutoFarm.mc.playerController.onPlayerDamageBlock(pos, AutoFarm.mc.player.getHorizontalFacing());
                    AutoFarm.mc.player.swingArm(EnumHand.MAIN_HAND);
                    if (doesHaveSeeds()) {
                        AutoFarm.mc.player.connection.sendPacket(new CPacketHeldItemChange(getSlotWithSeeds()));
                        AutoFarm.mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(downPos, EnumFacing.UP, EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                        AutoFarm.mc.player.swingArm(EnumHand.MAIN_HAND);
                    }
                    this.timerHelper.reset();
                }
            }
            else if (mode.equalsIgnoreCase("Plant")) {
                final BlockPos pos2 = BlockHelper.getSphere(BlockHelper.getPlayerPosLocal(), this.radius.getNumberValue(), 6, false, true, 0).stream().filter(this::IsValidBlockPos).min(Comparator.comparing(blockPos -> RotationHelper.getDistanceOfEntityToBlock(AutoFarm.mc.player, blockPos))).orElse(null);
                final Vec3d vec = new Vec3d(0.0, 0.0, 0.0);
                if (this.timerHelper.hasReached(this.delay.getNumberValue() * 100.0f) && this.isOnCrops() && pos2 != null && doesHaveSeeds()) {
                    final float[] rots2 = RotationHelper.getRotationVector(new Vec3d(pos2.getX() + 0.5f, pos2.getY() + 0.5f, pos2.getZ() + 0.5f));
                    e.setYaw(rots2[0]);
                    e.setPitch(rots2[1]);
                    AutoFarm.mc.player.renderYawOffset = rots2[0];
                    AutoFarm.mc.player.rotationYawHead = rots2[0];
                    AutoFarm.mc.player.rotationPitchHead = rots2[1];
                    AutoFarm.mc.player.connection.sendPacket(new CPacketHeldItemChange(getSlotWithSeeds()));
                    AutoFarm.mc.playerController.processRightClickBlock(AutoFarm.mc.player, AutoFarm.mc.world, pos2, EnumFacing.VALUES[0].getOpposite(), vec, EnumHand.MAIN_HAND);
                    this.timerHelper.reset();
                }
            }
        }
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket e) {
        if (this.autoFarm.getBoolValue()) {
            if (e.getPacket() instanceof SPacketBlockChange) {
                final SPacketBlockChange p = (SPacketBlockChange)e.getPacket();
                if (this.isEnabled(Block.getIdFromBlock(p.getBlockState().getBlock()))) {
                    this.crops.add(p.getBlockPosition());
                }
            }
            else if (e.getPacket() instanceof SPacketMultiBlockChange) {
                final SPacketMultiBlockChange p2 = (SPacketMultiBlockChange)e.getPacket();
                for (final SPacketMultiBlockChange.BlockUpdateData dat : p2.getChangedBlocks()) {
                    if (this.isEnabled(Block.getIdFromBlock(dat.getBlockState().getBlock()))) {
                        this.crops.add(dat.getPos());
                    }
                }
            }
        }
    }
    
    private boolean isCheck(final int id) {
        int check = 0;
        if (id != 0) {
            check = 59;
        }
        return id != 0 && id == check;
    }
    
    private boolean isEnabled(final int id) {
        int check = 0;
        if (id != 0) {
            check = 59;
        }
        return id != 0 && id == check;
    }
    
    private ArrayList<BlockPos> getBlocks(final float x, final float y, final float z) {
        final BlockPos min = new BlockPos(AutoFarm.mc.player.posX - x, AutoFarm.mc.player.posY - y, AutoFarm.mc.player.posZ - z);
        final BlockPos max = new BlockPos(AutoFarm.mc.player.posX + x, AutoFarm.mc.player.posY + y, AutoFarm.mc.player.posZ + z);
        return BlockHelper.getAllInBox(min, max);
    }
}
