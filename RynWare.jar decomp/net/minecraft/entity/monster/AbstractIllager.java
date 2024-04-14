// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.monster;

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.world.World;
import net.minecraft.network.datasync.DataParameter;

public abstract class AbstractIllager extends EntityMob
{
    protected static final DataParameter<Byte> field_193080_a;
    
    public AbstractIllager(final World p_i47509_1_) {
        super(p_i47509_1_);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(AbstractIllager.field_193080_a, (Byte)0);
    }
    
    protected boolean func_193078_a(final int p_193078_1_) {
        final int i = this.dataManager.get(AbstractIllager.field_193080_a);
        return (i & p_193078_1_) != 0x0;
    }
    
    protected void func_193079_a(final int p_193079_1_, final boolean p_193079_2_) {
        int i = this.dataManager.get(AbstractIllager.field_193080_a);
        if (p_193079_2_) {
            i |= p_193079_1_;
        }
        else {
            i &= ~p_193079_1_;
        }
        this.dataManager.set(AbstractIllager.field_193080_a, (byte)(i & 0xFF));
    }
    
    @Override
    public EnumCreatureAttribute getCreatureAttribute() {
        return EnumCreatureAttribute.ILLAGER;
    }
    
    public IllagerArmPose func_193077_p() {
        return IllagerArmPose.CROSSED;
    }
    
    static {
        field_193080_a = EntityDataManager.createKey(AbstractIllager.class, DataSerializers.BYTE);
    }
    
    public enum IllagerArmPose
    {
        CROSSED, 
        ATTACKING, 
        SPELLCASTING, 
        BOW_AND_ARROW;
    }
}
