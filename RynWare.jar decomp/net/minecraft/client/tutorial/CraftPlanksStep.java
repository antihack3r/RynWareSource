// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.tutorial;

import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.item.Item;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Blocks;
import net.minecraft.world.GameType;
import net.minecraft.client.gui.toasts.TutorialToast;
import net.minecraft.util.text.ITextComponent;

public class CraftPlanksStep implements ITutorialStep
{
    private static final ITextComponent field_193286_a;
    private static final ITextComponent field_193287_b;
    private final Tutorial field_193288_c;
    private TutorialToast field_193289_d;
    private int field_193290_e;
    
    public CraftPlanksStep(final Tutorial p_i47583_1_) {
        this.field_193288_c = p_i47583_1_;
    }
    
    @Override
    public void func_193245_a() {
        ++this.field_193290_e;
        if (this.field_193288_c.func_194072_f() != GameType.SURVIVAL) {
            this.field_193288_c.func_193292_a(TutorialSteps.NONE);
        }
        else {
            if (this.field_193290_e == 1) {
                final EntityPlayerSP entityplayersp = this.field_193288_c.func_193295_e().player;
                if (entityplayersp != null) {
                    if (entityplayersp.inventory.hasItemStack(new ItemStack(Blocks.PLANKS))) {
                        this.field_193288_c.func_193292_a(TutorialSteps.NONE);
                        return;
                    }
                    if (func_194071_a(entityplayersp)) {
                        this.field_193288_c.func_193292_a(TutorialSteps.NONE);
                        return;
                    }
                }
            }
            if (this.field_193290_e >= 1200 && this.field_193289_d == null) {
                this.field_193289_d = new TutorialToast(TutorialToast.Icons.WOODEN_PLANKS, CraftPlanksStep.field_193286_a, CraftPlanksStep.field_193287_b, false);
                this.field_193288_c.func_193295_e().func_193033_an().func_192988_a(this.field_193289_d);
            }
        }
    }
    
    @Override
    public void func_193248_b() {
        if (this.field_193289_d != null) {
            this.field_193289_d.func_193670_a();
            this.field_193289_d = null;
        }
    }
    
    @Override
    public void func_193252_a(final ItemStack p_193252_1_) {
        if (p_193252_1_.getItem() == Item.getItemFromBlock(Blocks.PLANKS)) {
            this.field_193288_c.func_193292_a(TutorialSteps.NONE);
        }
    }
    
    public static boolean func_194071_a(final EntityPlayerSP p_194071_0_) {
        final StatBase statbase = StatList.getCraftStats(Item.getItemFromBlock(Blocks.PLANKS));
        return statbase != null && p_194071_0_.getStatFileWriter().readStat(statbase) > 0;
    }
    
    static {
        field_193286_a = new TextComponentTranslation("tutorial.craft_planks.title", new Object[0]);
        field_193287_b = new TextComponentTranslation("tutorial.craft_planks.description", new Object[0]);
    }
}
