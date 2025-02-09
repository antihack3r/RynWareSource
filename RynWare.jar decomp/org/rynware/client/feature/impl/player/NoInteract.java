// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.player;

import java.util.ArrayList;
import net.minecraft.block.Block;
import java.util.List;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.feature.Feature;

public class NoInteract extends Feature
{
    public static BooleanSetting armorStands;
    public static BooleanSetting chest;
    public static BooleanSetting doors;
    public static BooleanSetting hopper;
    public static BooleanSetting buttons;
    public static BooleanSetting dispenser;
    public static BooleanSetting noteBlock;
    public static BooleanSetting craftingTable;
    public static BooleanSetting trapDoor;
    public static BooleanSetting furnace;
    public static BooleanSetting gate;
    public static BooleanSetting anvil;
    public static BooleanSetting lever;
    
    public NoInteract() {
        super("NoInteract", "\u041e\u0442\u043a\u043b\u044e\u0447\u0430\u0435\u0442 \u0431\u043b\u043e\u043a\u0438 \u0432 \u0442\u0440\u0430\u043f\u043a\u0430\u0445", FeatureCategory.Player);
        NoInteract.armorStands = new BooleanSetting("Armor Stand", true, () -> true);
        NoInteract.chest = new BooleanSetting("Chest", false, () -> true);
        NoInteract.doors = new BooleanSetting("Doors", true, () -> true);
        NoInteract.hopper = new BooleanSetting("Hopper", true, () -> true);
        NoInteract.buttons = new BooleanSetting("Buttons", true, () -> true);
        NoInteract.dispenser = new BooleanSetting("Dispenser", true, () -> true);
        NoInteract.noteBlock = new BooleanSetting("Note Block", true, () -> true);
        NoInteract.craftingTable = new BooleanSetting("Crafting Table", true, () -> true);
        NoInteract.trapDoor = new BooleanSetting("TrapDoor", true, () -> true);
        NoInteract.furnace = new BooleanSetting("Furnace", true, () -> true);
        NoInteract.gate = new BooleanSetting("Gate", true, () -> true);
        NoInteract.anvil = new BooleanSetting("Anvil", true, () -> true);
        NoInteract.lever = new BooleanSetting("Lever", true, () -> true);
        this.addSettings(NoInteract.armorStands, NoInteract.chest, NoInteract.doors, NoInteract.hopper, NoInteract.buttons, NoInteract.dispenser, NoInteract.noteBlock, NoInteract.craftingTable, NoInteract.trapDoor, NoInteract.furnace, NoInteract.gate, NoInteract.anvil, NoInteract.lever);
    }
    
    public static List<Block> getRightClickableBlocks() {
        final ArrayList<Block> rightClickableBlocks = new ArrayList<Block>();
        if (NoInteract.doors.getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(64));
            rightClickableBlocks.add(Block.getBlockById(71));
            rightClickableBlocks.add(Block.getBlockById(193));
            rightClickableBlocks.add(Block.getBlockById(194));
            rightClickableBlocks.add(Block.getBlockById(195));
            rightClickableBlocks.add(Block.getBlockById(196));
            rightClickableBlocks.add(Block.getBlockById(197));
        }
        if (NoInteract.hopper.getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(154));
        }
        if (NoInteract.buttons.getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(77));
            rightClickableBlocks.add(Block.getBlockById(143));
        }
        if (NoInteract.noteBlock.getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(84));
            rightClickableBlocks.add(Block.getBlockById(25));
        }
        if (NoInteract.trapDoor.getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(96));
            rightClickableBlocks.add(Block.getBlockById(167));
        }
        if (NoInteract.furnace.getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(61));
            rightClickableBlocks.add(Block.getBlockById(62));
        }
        if (NoInteract.chest.getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(130));
            rightClickableBlocks.add(Block.getBlockById(146));
            rightClickableBlocks.add(Block.getBlockById(54));
        }
        if (NoInteract.craftingTable.getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(58));
        }
        if (NoInteract.gate.getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(107));
            rightClickableBlocks.add(Block.getBlockById(183));
            rightClickableBlocks.add(Block.getBlockById(184));
            rightClickableBlocks.add(Block.getBlockById(185));
            rightClickableBlocks.add(Block.getBlockById(186));
            rightClickableBlocks.add(Block.getBlockById(187));
        }
        if (NoInteract.anvil.getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(145));
        }
        if (NoInteract.dispenser.getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(23));
        }
        if (NoInteract.lever.getBoolValue()) {
            rightClickableBlocks.add(Block.getBlockById(69));
        }
        return rightClickableBlocks;
    }
}
