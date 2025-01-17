// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.color;

import net.minecraft.item.ItemMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.entity.EntityList;
import net.minecraft.potion.PotionUtils;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.item.ItemFireworkCharge;
import net.minecraft.init.Blocks;
import net.minecraft.block.Block;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ObjectIntIdentityMap;

public class ItemColors
{
    private final ObjectIntIdentityMap<IItemColor> mapItemColors;
    
    public ItemColors() {
        this.mapItemColors = new ObjectIntIdentityMap<IItemColor>(32);
    }
    
    public static ItemColors init(final BlockColors p_186729_0_) {
        final ItemColors itemcolors = new ItemColors();
        itemcolors.registerItemColorHandler(new IItemColor() {
            @Override
            public int getColorFromItemstack(final ItemStack stack, final int tintIndex) {
                return (tintIndex > 0) ? -1 : ((ItemArmor)stack.getItem()).getColor(stack);
            }
        }, Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS);
        itemcolors.registerItemColorHandler(new IItemColor() {
            @Override
            public int getColorFromItemstack(final ItemStack stack, final int tintIndex) {
                final BlockDoublePlant.EnumPlantType blockdoubleplant$enumplanttype = BlockDoublePlant.EnumPlantType.byMetadata(stack.getMetadata());
                return (blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.GRASS && blockdoubleplant$enumplanttype != BlockDoublePlant.EnumPlantType.FERN) ? -1 : ColorizerGrass.getGrassColor(0.5, 1.0);
            }
        }, Blocks.DOUBLE_PLANT);
        itemcolors.registerItemColorHandler(new IItemColor() {
            @Override
            public int getColorFromItemstack(final ItemStack stack, final int tintIndex) {
                if (tintIndex != 1) {
                    return -1;
                }
                final NBTBase nbtbase = ItemFireworkCharge.getExplosionTag(stack, "Colors");
                if (!(nbtbase instanceof NBTTagIntArray)) {
                    return 9079434;
                }
                final int[] aint = ((NBTTagIntArray)nbtbase).getIntArray();
                if (aint.length == 1) {
                    return aint[0];
                }
                int i = 0;
                int j = 0;
                int k = 0;
                for (final int l : aint) {
                    i += (l & 0xFF0000) >> 16;
                    j += (l & 0xFF00) >> 8;
                    k += (l & 0xFF) >> 0;
                }
                i /= aint.length;
                j /= aint.length;
                k /= aint.length;
                return i << 16 | j << 8 | k;
            }
        }, Items.FIREWORK_CHARGE);
        itemcolors.registerItemColorHandler(new IItemColor() {
            @Override
            public int getColorFromItemstack(final ItemStack stack, final int tintIndex) {
                return (tintIndex > 0) ? -1 : PotionUtils.func_190932_c(stack);
            }
        }, Items.POTIONITEM, Items.SPLASH_POTION, Items.LINGERING_POTION);
        itemcolors.registerItemColorHandler(new IItemColor() {
            @Override
            public int getColorFromItemstack(final ItemStack stack, final int tintIndex) {
                final EntityList.EntityEggInfo entitylist$entityegginfo = EntityList.ENTITY_EGGS.get(ItemMonsterPlacer.func_190908_h(stack));
                if (entitylist$entityegginfo == null) {
                    return -1;
                }
                return (tintIndex == 0) ? entitylist$entityegginfo.primaryColor : entitylist$entityegginfo.secondaryColor;
            }
        }, Items.SPAWN_EGG);
        itemcolors.registerItemColorHandler(new IItemColor() {
            @Override
            public int getColorFromItemstack(final ItemStack stack, final int tintIndex) {
                final IBlockState iblockstate = ((ItemBlock)stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata());
                return p_186729_0_.colorMultiplier(iblockstate, null, null, tintIndex);
            }
        }, Blocks.GRASS, Blocks.TALLGRASS, Blocks.VINE, Blocks.LEAVES, Blocks.LEAVES2, Blocks.WATERLILY);
        itemcolors.registerItemColorHandler(new IItemColor() {
            @Override
            public int getColorFromItemstack(final ItemStack stack, final int tintIndex) {
                return (tintIndex == 0) ? PotionUtils.func_190932_c(stack) : -1;
            }
        }, Items.TIPPED_ARROW);
        itemcolors.registerItemColorHandler(new IItemColor() {
            @Override
            public int getColorFromItemstack(final ItemStack stack, final int tintIndex) {
                return (tintIndex == 0) ? -1 : ItemMap.func_190907_h(stack);
            }
        }, Items.FILLED_MAP);
        return itemcolors;
    }
    
    public int getColorFromItemstack(final ItemStack stack, final int tintIndex) {
        final IItemColor iitemcolor = this.mapItemColors.getByValue(Item.REGISTRY.getIDForObject(stack.getItem()));
        return (iitemcolor == null) ? -1 : iitemcolor.getColorFromItemstack(stack, tintIndex);
    }
    
    public void registerItemColorHandler(final IItemColor itemColor, final Block... blocksIn) {
        for (final Block block : blocksIn) {
            this.mapItemColors.put(itemColor, Item.getIdFromItem(Item.getItemFromBlock(block)));
        }
    }
    
    public void registerItemColorHandler(final IItemColor itemColor, final Item... itemsIn) {
        for (final Item item : itemsIn) {
            this.mapItemColors.put(itemColor, Item.getIdFromItem(item));
        }
    }
}
