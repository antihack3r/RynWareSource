// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.player;

import org.rynware.client.event.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import java.util.Comparator;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.ui.settings.Setting;
import java.util.ArrayList;
import org.rynware.client.feature.impl.FeatureCategory;
import java.util.List;
import net.minecraft.item.ItemStack;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.feature.Feature;

public class AutoTool extends Feature
{
    private BooleanSetting swapBack;
    private BooleanSetting saveItem;
    public BooleanSetting silentSwitch;
    public int itemIndex;
    private boolean swap;
    private long swapDelay;
    private ItemStack swapedItem;
    private List<Integer> lastItem;
    
    public AutoTool() {
        super("AutoTool", "\u0411\u0435\u0440\u0451\u0442 \u043d\u0443\u0436\u043d\u044b\u0439 \u0438\u043d\u0441\u0442\u0440\u0443\u043c\u0435\u043d\u0442 \u043f\u0440\u0438 \u043a\u043b\u0438\u043a\u0435 \u043d\u0430 \u0431\u043b\u043e\u043a", FeatureCategory.Player);
        this.swapBack = new BooleanSetting("Swap Back", true, () -> true);
        this.saveItem = new BooleanSetting("Save Item", true, () -> true);
        this.silentSwitch = new BooleanSetting("Silent Switch", true, () -> true);
        this.swapedItem = null;
        this.lastItem = new ArrayList<Integer>();
        this.addSettings(this.swapBack, this.saveItem, this.silentSwitch);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate update) {
        Block hoverBlock = null;
        if (AutoTool.mc.objectMouseOver.getBlockPos() == null) {
            return;
        }
        hoverBlock = AutoTool.mc.world.getBlockState(AutoTool.mc.objectMouseOver.getBlockPos()).getBlock();
        final List<Integer> bestItem = new ArrayList<Integer>();
        for (int i = 0; i < 9 && hoverBlock != null; ++i) {
            if (AutoTool.mc.player.inventory.getStackInSlot(i).getMaxDamage() - AutoTool.mc.player.inventory.getStackInSlot(i).getItemDamage() > 1 || !this.saveItem.getBoolValue()) {
                if (AutoTool.mc.player.getDigSpeed(AutoTool.mc.world.getBlockState(AutoTool.mc.objectMouseOver.getBlockPos()), AutoTool.mc.player.inventory.getStackInSlot(i)) > 1.0f) {
                    bestItem.add(i);
                }
            }
        }
        bestItem.sort(Comparator.comparingDouble(x -> -AutoTool.mc.player.getDigSpeed(AutoTool.mc.world.getBlockState(AutoTool.mc.objectMouseOver.getBlockPos()), AutoTool.mc.player.inventory.getStackInSlot(x))));
        if (!bestItem.isEmpty() && AutoTool.mc.gameSettings.keyBindAttack.pressed) {
            if (AutoTool.mc.player.inventory.getCurrentItem() != this.swapedItem) {
                this.lastItem.add(AutoTool.mc.player.inventory.currentItem);
                if (this.silentSwitch.getBoolValue()) {
                    AutoTool.mc.player.connection.sendPacket(new CPacketHeldItemChange(bestItem.get(0)));
                }
                else {
                    AutoTool.mc.player.inventory.currentItem = bestItem.get(0);
                }
                this.itemIndex = bestItem.get(0);
                this.swap = true;
            }
            this.swapDelay = System.currentTimeMillis();
        }
        else if (this.swap && !this.lastItem.isEmpty() && System.currentTimeMillis() >= this.swapDelay + 300L && this.swapBack.getBoolValue()) {
            if (this.silentSwitch.getBoolValue()) {
                AutoTool.mc.player.connection.sendPacket(new CPacketHeldItemChange(this.lastItem.get(0)));
            }
            else {
                AutoTool.mc.player.inventory.currentItem = this.lastItem.get(0);
            }
            this.itemIndex = this.lastItem.get(0);
            this.lastItem.clear();
            this.swap = false;
        }
    }
}
