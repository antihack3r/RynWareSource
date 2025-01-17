// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.monster;

import javax.annotation.Nullable;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIAttackRangedBow;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.world.World;
import net.minecraft.util.math.Vec3d;
import net.minecraft.entity.IRangedAttackMob;

public class EntityIllusionIllager extends EntitySpellcasterIllager implements IRangedAttackMob
{
    private int field_193099_c;
    private final Vec3d[][] field_193100_bx;
    
    public EntityIllusionIllager(final World p_i47507_1_) {
        super(p_i47507_1_);
        this.setSize(0.6f, 1.95f);
        this.experienceValue = 5;
        this.field_193100_bx = new Vec3d[2][4];
        for (int i = 0; i < 4; ++i) {
            this.field_193100_bx[0][i] = new Vec3d(0.0, 0.0, 0.0);
            this.field_193100_bx[1][i] = new Vec3d(0.0, 0.0, 0.0);
        }
    }
    
    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new AICastingApell());
        this.tasks.addTask(4, new AIMirriorSpell());
        this.tasks.addTask(5, new AIBlindnessSpell());
        this.tasks.addTask(6, new EntityAIAttackRangedBow<Object>(this, 0.5, 20, 15.0f));
        this.tasks.addTask(8, new EntityAIWander(this, 0.6));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 3.0f, 1.0f));
        this.tasks.addTask(10, new EntityAIWatchClosest(this, EntityLiving.class, 8.0f));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, (Class<?>[])new Class[] { EntityIllusionIllager.class }));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<Object>(this, EntityPlayer.class, true).func_190882_b(300));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<Object>(this, EntityVillager.class, false).func_190882_b(300));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<Object>(this, EntityIronGolem.class, false).func_190882_b(300));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(18.0);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(32.0);
    }
    
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, final IEntityLivingData livingdata) {
        this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
        return super.onInitialSpawn(difficulty, livingdata);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
    }
    
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.EMPTY;
    }
    
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return this.getEntityBoundingBox().expand(3.0, 0.0, 3.0);
    }
    
    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.world.isRemote && this.isInvisible()) {
            --this.field_193099_c;
            if (this.field_193099_c < 0) {
                this.field_193099_c = 0;
            }
            if (this.hurtTime != 1 && this.ticksExisted % 1200 != 0) {
                if (this.hurtTime == this.maxHurtTime - 1) {
                    this.field_193099_c = 3;
                    for (int k = 0; k < 4; ++k) {
                        this.field_193100_bx[0][k] = this.field_193100_bx[1][k];
                        this.field_193100_bx[1][k] = new Vec3d(0.0, 0.0, 0.0);
                    }
                }
            }
            else {
                this.field_193099_c = 3;
                final float f = -6.0f;
                final int i = 13;
                for (int j = 0; j < 4; ++j) {
                    this.field_193100_bx[0][j] = this.field_193100_bx[1][j];
                    this.field_193100_bx[1][j] = new Vec3d((-6.0f + this.rand.nextInt(13)) * 0.5, Math.max(0, this.rand.nextInt(6) - 4), (-6.0f + this.rand.nextInt(13)) * 0.5);
                }
                for (int l = 0; l < 16; ++l) {
                    this.world.spawnParticle(EnumParticleTypes.CLOUD, this.posX + (this.rand.nextDouble() - 0.5) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5) * this.width, 0.0, 0.0, 0.0, new int[0]);
                }
                this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.field_193788_dg, this.getSoundCategory(), 1.0f, 1.0f, false);
            }
        }
    }
    
    public Vec3d[] func_193098_a(final float p_193098_1_) {
        if (this.field_193099_c <= 0) {
            return this.field_193100_bx[1];
        }
        double d0 = (this.field_193099_c - p_193098_1_) / 3.0f;
        d0 = Math.pow(d0, 0.25);
        final Vec3d[] avec3d = new Vec3d[4];
        for (int i = 0; i < 4; ++i) {
            avec3d[i] = this.field_193100_bx[1][i].scale(1.0 - d0).add(this.field_193100_bx[0][i].scale(d0));
        }
        return avec3d;
    }
    
    @Override
    public boolean isOnSameTeam(final Entity entityIn) {
        return super.isOnSameTeam(entityIn) || (entityIn instanceof EntityLivingBase && ((EntityLivingBase)entityIn).getCreatureAttribute() == EnumCreatureAttribute.ILLAGER && this.getTeam() == null && entityIn.getTeam() == null);
    }
    
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.field_193783_dc;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.field_193786_de;
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource p_184601_1_) {
        return SoundEvents.field_193787_df;
    }
    
    @Override
    protected SoundEvent func_193086_dk() {
        return SoundEvents.field_193784_dd;
    }
    
    @Override
    public void attackEntityWithRangedAttack(final EntityLivingBase target, final float distanceFactor) {
        final EntityArrow entityarrow = this.func_193097_t(distanceFactor);
        final double d0 = target.posX - this.posX;
        final double d2 = target.getEntityBoundingBox().minY + target.height / 3.0f - entityarrow.posY;
        final double d3 = target.posZ - this.posZ;
        final double d4 = MathHelper.sqrt(d0 * d0 + d3 * d3);
        entityarrow.setThrowableHeading(d0, d2 + d4 * 0.20000000298023224, d3, 1.6f, (float)(14 - this.world.getDifficulty().getDifficultyId() * 4));
        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0f, 1.0f / (this.getRNG().nextFloat() * 0.4f + 0.8f));
        this.world.spawnEntityInWorld(entityarrow);
    }
    
    protected EntityArrow func_193097_t(final float p_193097_1_) {
        final EntityTippedArrow entitytippedarrow = new EntityTippedArrow(this.world, this);
        entitytippedarrow.func_190547_a(this, p_193097_1_);
        return entitytippedarrow;
    }
    
    public boolean func_193096_dj() {
        return this.func_193078_a(1);
    }
    
    @Override
    public void setSwingingArms(final boolean swingingArms) {
        this.func_193079_a(1, swingingArms);
    }
    
    @Override
    public IllagerArmPose func_193077_p() {
        if (this.func_193082_dl()) {
            return IllagerArmPose.SPELLCASTING;
        }
        return this.func_193096_dj() ? IllagerArmPose.BOW_AND_ARROW : IllagerArmPose.CROSSED;
    }
    
    class AIBlindnessSpell extends AIUseSpell
    {
        private int field_193325_b;
        
        private AIBlindnessSpell() {
        }
        
        @Override
        public boolean shouldExecute() {
            return super.shouldExecute() && EntityIllusionIllager.this.getAttackTarget() != null && EntityIllusionIllager.this.getAttackTarget().getEntityId() != this.field_193325_b && EntityIllusionIllager.this.world.getDifficultyForLocation(new BlockPos(EntityIllusionIllager.this)).func_193845_a((float)EnumDifficulty.NORMAL.ordinal());
        }
        
        @Override
        public void startExecuting() {
            super.startExecuting();
            this.field_193325_b = EntityIllusionIllager.this.getAttackTarget().getEntityId();
        }
        
        @Override
        protected int func_190869_f() {
            return 20;
        }
        
        @Override
        protected int func_190872_i() {
            return 180;
        }
        
        @Override
        protected void func_190868_j() {
            EntityIllusionIllager.this.getAttackTarget().addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 400));
        }
        
        @Override
        protected SoundEvent func_190871_k() {
            return SoundEvents.field_193789_dh;
        }
        
        @Override
        protected SpellType func_193320_l() {
            return SpellType.BLINDNESS;
        }
    }
    
    class AIMirriorSpell extends AIUseSpell
    {
        private AIMirriorSpell() {
        }
        
        @Override
        public boolean shouldExecute() {
            return super.shouldExecute() && !EntityIllusionIllager.this.isPotionActive(MobEffects.INVISIBILITY);
        }
        
        @Override
        protected int func_190869_f() {
            return 20;
        }
        
        @Override
        protected int func_190872_i() {
            return 340;
        }
        
        @Override
        protected void func_190868_j() {
            EntityIllusionIllager.this.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 1200));
        }
        
        @Nullable
        @Override
        protected SoundEvent func_190871_k() {
            return SoundEvents.field_193790_di;
        }
        
        @Override
        protected SpellType func_193320_l() {
            return SpellType.DISAPPEAR;
        }
    }
}
