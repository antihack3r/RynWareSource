// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.entity.passive;

import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityMagmaCube;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityBlaze;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import com.google.common.collect.Sets;
import net.minecraft.init.Items;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockLeaves;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.potion.PotionEffect;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import java.util.List;
import net.minecraft.entity.EntityList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollow;
import net.minecraft.entity.ai.EntityAILandOnOwnersShoulder;
import net.minecraft.entity.ai.EntityAIWanderAvoidWaterFlying;
import net.minecraft.entity.ai.EntityAIFollowOwnerFlying;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISit;
import javax.annotation.Nullable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.entity.ai.EntityFlyHelper;
import net.minecraft.world.World;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.SoundEvent;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.Set;
import net.minecraft.item.Item;
import net.minecraft.entity.EntityLiving;
import com.google.common.base.Predicate;
import net.minecraft.network.datasync.DataParameter;

public class EntityParrot extends EntityShoulderRiding implements EntityFlying
{
    private static final DataParameter<Integer> field_192013_bG;
    private static final Predicate<EntityLiving> field_192014_bH;
    private static final Item field_192015_bI;
    private static final Set<Item> field_192016_bJ;
    private static final Int2ObjectMap<SoundEvent> field_192017_bK;
    public float field_192008_bB;
    public float field_192009_bC;
    public float field_192010_bD;
    public float field_192011_bE;
    public float field_192012_bF;
    private boolean field_192018_bL;
    private BlockPos field_192019_bM;
    
    public EntityParrot(final World p_i47411_1_) {
        super(p_i47411_1_);
        this.field_192012_bF = 1.0f;
        this.setSize(0.5f, 0.9f);
        this.moveHelper = new EntityFlyHelper(this);
    }
    
    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(final DifficultyInstance difficulty, @Nullable final IEntityLivingData livingdata) {
        this.func_191997_m(this.rand.nextInt(5));
        return super.onInitialSpawn(difficulty, livingdata);
    }
    
    @Override
    protected void initEntityAI() {
        this.aiSit = new EntityAISit(this);
        this.tasks.addTask(0, new EntityAIPanic(this, 1.25));
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0f));
        this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(2, new EntityAIFollowOwnerFlying(this, 1.0, 5.0f, 1.0f));
        this.tasks.addTask(2, new EntityAIWanderAvoidWaterFlying(this, 1.0));
        this.tasks.addTask(3, new EntityAILandOnOwnersShoulder(this));
        this.tasks.addTask(3, new EntityAIFollow(this, 1.0, 3.0f, 7.0f));
    }
    
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.field_193334_e);
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(6.0);
        this.getEntityAttribute(SharedMonsterAttributes.field_193334_e).setBaseValue(0.4000000059604645);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.20000000298023224);
    }
    
    @Override
    protected PathNavigate getNewNavigator(final World worldIn) {
        final PathNavigateFlying pathnavigateflying = new PathNavigateFlying(this, worldIn);
        pathnavigateflying.func_192879_a(false);
        pathnavigateflying.func_192877_c(true);
        pathnavigateflying.func_192878_b(true);
        return pathnavigateflying;
    }
    
    @Override
    public float getEyeHeight() {
        return this.height * 0.6f;
    }
    
    @Override
    public void onLivingUpdate() {
        func_192006_b(this.world, this);
        if (this.field_192019_bM == null || this.field_192019_bM.distanceSq(this.posX, this.posY, this.posZ) > 12.0 || this.world.getBlockState(this.field_192019_bM).getBlock() != Blocks.JUKEBOX) {
            this.field_192018_bL = false;
            this.field_192019_bM = null;
        }
        super.onLivingUpdate();
        this.func_192001_dv();
    }
    
    @Override
    public void func_191987_a(final BlockPos p_191987_1_, final boolean p_191987_2_) {
        this.field_192019_bM = p_191987_1_;
        this.field_192018_bL = p_191987_2_;
    }
    
    public boolean func_192004_dr() {
        return this.field_192018_bL;
    }
    
    private void func_192001_dv() {
        this.field_192011_bE = this.field_192008_bB;
        this.field_192010_bD = this.field_192009_bC;
        this.field_192009_bC += (float)((this.onGround ? -1 : 4) * 0.3);
        this.field_192009_bC = MathHelper.clamp(this.field_192009_bC, 0.0f, 1.0f);
        if (!this.onGround && this.field_192012_bF < 1.0f) {
            this.field_192012_bF = 1.0f;
        }
        this.field_192012_bF *= (float)0.9;
        if (!this.onGround && this.motionY < 0.0) {
            this.motionY *= 0.6;
        }
        this.field_192008_bB += this.field_192012_bF * 2.0f;
    }
    
    private static boolean func_192006_b(final World p_192006_0_, final Entity p_192006_1_) {
        if (!p_192006_1_.isSilent() && p_192006_0_.rand.nextInt(50) == 0) {
            final List<EntityLiving> list = p_192006_0_.getEntitiesWithinAABB((Class<? extends EntityLiving>)EntityLiving.class, p_192006_1_.getEntityBoundingBox().expandXyz(20.0), (com.google.common.base.Predicate<? super EntityLiving>)EntityParrot.field_192014_bH);
            if (!list.isEmpty()) {
                final EntityLiving entityliving = list.get(p_192006_0_.rand.nextInt(list.size()));
                if (!entityliving.isSilent()) {
                    final SoundEvent soundevent = func_191999_g(EntityList.field_191308_b.getIDForObject(entityliving.getClass()));
                    p_192006_0_.playSound(null, p_192006_1_.posX, p_192006_1_.posY, p_192006_1_.posZ, soundevent, p_192006_1_.getSoundCategory(), 0.7f, func_192000_b(p_192006_0_.rand));
                    return true;
                }
            }
            return false;
        }
        return false;
    }
    
    @Override
    public boolean processInteract(final EntityPlayer player, final EnumHand hand) {
        final ItemStack itemstack = player.getHeldItem(hand);
        if (!this.isTamed() && EntityParrot.field_192016_bJ.contains(itemstack.getItem())) {
            if (!player.capabilities.isCreativeMode) {
                itemstack.func_190918_g(1);
            }
            if (!this.isSilent()) {
                this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.field_192797_eu, this.getSoundCategory(), 1.0f, 1.0f + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f);
            }
            if (!this.world.isRemote) {
                if (this.rand.nextInt(10) == 0) {
                    this.func_193101_c(player);
                    this.playTameEffect(true);
                    this.world.setEntityState(this, (byte)7);
                }
                else {
                    this.playTameEffect(false);
                    this.world.setEntityState(this, (byte)6);
                }
            }
            return true;
        }
        if (itemstack.getItem() == EntityParrot.field_192015_bI) {
            if (!player.capabilities.isCreativeMode) {
                itemstack.func_190918_g(1);
            }
            this.addPotionEffect(new PotionEffect(MobEffects.POISON, 900));
            if (player.isCreative() || !this.func_190530_aW()) {
                this.attackEntityFrom(DamageSource.causePlayerDamage(player), Float.MAX_VALUE);
            }
            return true;
        }
        if (!this.world.isRemote && !this.func_192002_a() && this.isTamed() && this.isOwner(player)) {
            this.aiSit.setSitting(!this.isSitting());
        }
        return super.processInteract(player, hand);
    }
    
    @Override
    public boolean isBreedingItem(final ItemStack stack) {
        return false;
    }
    
    @Override
    public boolean getCanSpawnHere() {
        final int i = MathHelper.floor(this.posX);
        final int j = MathHelper.floor(this.getEntityBoundingBox().minY);
        final int k = MathHelper.floor(this.posZ);
        final BlockPos blockpos = new BlockPos(i, j, k);
        final Block block = this.world.getBlockState(blockpos.down()).getBlock();
        return block instanceof BlockLeaves || block == Blocks.GRASS || block instanceof BlockLog || (block == Blocks.AIR && this.world.getLight(blockpos) > 8 && super.getCanSpawnHere());
    }
    
    @Override
    public void fall(final float distance, final float damageMultiplier) {
    }
    
    @Override
    protected void updateFallState(final double y, final boolean onGroundIn, final IBlockState state, final BlockPos pos) {
    }
    
    @Override
    public boolean canMateWith(final EntityAnimal otherAnimal) {
        return false;
    }
    
    @Nullable
    @Override
    public EntityAgeable createChild(final EntityAgeable ageable) {
        return null;
    }
    
    public static void func_192005_a(final World p_192005_0_, final Entity p_192005_1_) {
        if (!p_192005_1_.isSilent() && !func_192006_b(p_192005_0_, p_192005_1_) && p_192005_0_.rand.nextInt(200) == 0) {
            p_192005_0_.playSound(null, p_192005_1_.posX, p_192005_1_.posY, p_192005_1_.posZ, func_192003_a(p_192005_0_.rand), p_192005_1_.getSoundCategory(), 1.0f, func_192000_b(p_192005_0_.rand));
        }
    }
    
    @Override
    public boolean attackEntityAsMob(final Entity entityIn) {
        return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0f);
    }
    
    @Nullable
    public SoundEvent getAmbientSound() {
        return func_192003_a(this.rand);
    }
    
    private static SoundEvent func_192003_a(final Random p_192003_0_) {
        if (p_192003_0_.nextInt(1000) == 0) {
            final List<Integer> list = new ArrayList<Integer>((Collection<? extends Integer>)EntityParrot.field_192017_bK.keySet());
            return func_191999_g(list.get(p_192003_0_.nextInt(list.size())));
        }
        return SoundEvents.field_192792_ep;
    }
    
    public static SoundEvent func_191999_g(final int p_191999_0_) {
        return (SoundEvent)(EntityParrot.field_192017_bK.containsKey(p_191999_0_) ? EntityParrot.field_192017_bK.get(p_191999_0_) : SoundEvents.field_192792_ep);
    }
    
    @Override
    protected SoundEvent getHurtSound(final DamageSource p_184601_1_) {
        return SoundEvents.field_192794_er;
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.field_192793_eq;
    }
    
    @Override
    protected void playStepSound(final BlockPos pos, final Block blockIn) {
        this.playSound(SoundEvents.field_192795_es, 0.15f, 1.0f);
    }
    
    @Override
    protected float func_191954_d(final float p_191954_1_) {
        this.playSound(SoundEvents.field_192796_et, 0.15f, 1.0f);
        return p_191954_1_ + this.field_192009_bC / 2.0f;
    }
    
    @Override
    protected boolean func_191957_ae() {
        return true;
    }
    
    @Override
    protected float getSoundPitch() {
        return func_192000_b(this.rand);
    }
    
    private static float func_192000_b(final Random p_192000_0_) {
        return (p_192000_0_.nextFloat() - p_192000_0_.nextFloat()) * 0.2f + 1.0f;
    }
    
    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.NEUTRAL;
    }
    
    @Override
    public boolean canBePushed() {
        return true;
    }
    
    @Override
    protected void collideWithEntity(final Entity entityIn) {
        if (!(entityIn instanceof EntityPlayer)) {
            super.collideWithEntity(entityIn);
        }
    }
    
    @Override
    public boolean attackEntityFrom(final DamageSource source, final float amount) {
        if (this.isEntityInvulnerable(source)) {
            return false;
        }
        if (this.aiSit != null) {
            this.aiSit.setSitting(false);
        }
        return super.attackEntityFrom(source, amount);
    }
    
    public int func_191998_ds() {
        return MathHelper.clamp(this.dataManager.get(EntityParrot.field_192013_bG), 0, 4);
    }
    
    public void func_191997_m(final int p_191997_1_) {
        this.dataManager.set(EntityParrot.field_192013_bG, p_191997_1_);
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityParrot.field_192013_bG, 0);
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger("Variant", this.func_191998_ds());
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        this.func_191997_m(compound.getInteger("Variant"));
    }
    
    @Nullable
    @Override
    protected ResourceLocation getLootTable() {
        return LootTableList.field_192561_ax;
    }
    
    public boolean func_192002_a() {
        return !this.onGround;
    }
    
    static {
        field_192013_bG = EntityDataManager.createKey(EntityParrot.class, DataSerializers.VARINT);
        field_192014_bH = (Predicate)new Predicate<EntityLiving>() {
            public boolean apply(@Nullable final EntityLiving p_apply_1_) {
                return p_apply_1_ != null && EntityParrot.field_192017_bK.containsKey(EntityList.field_191308_b.getIDForObject(p_apply_1_.getClass()));
            }
        };
        field_192015_bI = Items.COOKIE;
        field_192016_bJ = Sets.newHashSet((Object[])new Item[] { Items.WHEAT_SEEDS, Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, Items.BEETROOT_SEEDS });
        (field_192017_bK = (Int2ObjectMap)new Int2ObjectOpenHashMap(32)).put(EntityList.field_191308_b.getIDForObject(EntityBlaze.class), (Object)SoundEvents.field_193791_eM);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntityCaveSpider.class), (Object)SoundEvents.field_193813_fc);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntityCreeper.class), (Object)SoundEvents.field_193792_eN);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntityElderGuardian.class), (Object)SoundEvents.field_193793_eO);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntityDragon.class), (Object)SoundEvents.field_193794_eP);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntityEnderman.class), (Object)SoundEvents.field_193795_eQ);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntityEndermite.class), (Object)SoundEvents.field_193796_eR);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntityEvoker.class), (Object)SoundEvents.field_193797_eS);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntityGhast.class), (Object)SoundEvents.field_193798_eT);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntityHusk.class), (Object)SoundEvents.field_193799_eU);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntityIllusionIllager.class), (Object)SoundEvents.field_193800_eV);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntityMagmaCube.class), (Object)SoundEvents.field_193801_eW);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntityPigZombie.class), (Object)SoundEvents.field_193822_fl);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntityPolarBear.class), (Object)SoundEvents.field_193802_eX);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntityShulker.class), (Object)SoundEvents.field_193803_eY);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntitySilverfish.class), (Object)SoundEvents.field_193804_eZ);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntitySkeleton.class), (Object)SoundEvents.field_193811_fa);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntitySlime.class), (Object)SoundEvents.field_193812_fb);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntitySpider.class), (Object)SoundEvents.field_193813_fc);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntityStray.class), (Object)SoundEvents.field_193814_fd);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntityVex.class), (Object)SoundEvents.field_193815_fe);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntityVindicator.class), (Object)SoundEvents.field_193816_ff);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntityWitch.class), (Object)SoundEvents.field_193817_fg);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntityWither.class), (Object)SoundEvents.field_193818_fh);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntityWitherSkeleton.class), (Object)SoundEvents.field_193819_fi);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntityWolf.class), (Object)SoundEvents.field_193820_fj);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntityZombie.class), (Object)SoundEvents.field_193821_fk);
        EntityParrot.field_192017_bK.put(EntityList.field_191308_b.getIDForObject(EntityZombieVillager.class), (Object)SoundEvents.field_193823_fm);
    }
}
