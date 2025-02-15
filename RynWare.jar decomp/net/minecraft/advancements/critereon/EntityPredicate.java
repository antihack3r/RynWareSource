// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.advancements.critereon;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.JsonUtils;
import com.google.gson.JsonElement;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import javax.annotation.Nullable;
import net.minecraft.util.ResourceLocation;

public class EntityPredicate
{
    public static final EntityPredicate field_192483_a;
    private final ResourceLocation field_192484_b;
    private final DistancePredicate field_192485_c;
    private final LocationPredicate field_193435_d;
    private final MobEffectsPredicate field_193436_e;
    private final NBTPredicate field_193437_f;
    
    public EntityPredicate(@Nullable final ResourceLocation p_i47541_1_, final DistancePredicate p_i47541_2_, final LocationPredicate p_i47541_3_, final MobEffectsPredicate p_i47541_4_, final NBTPredicate p_i47541_5_) {
        this.field_192484_b = p_i47541_1_;
        this.field_192485_c = p_i47541_2_;
        this.field_193435_d = p_i47541_3_;
        this.field_193436_e = p_i47541_4_;
        this.field_193437_f = p_i47541_5_;
    }
    
    public boolean func_192482_a(final EntityPlayerMP p_192482_1_, @Nullable final Entity p_192482_2_) {
        return this == EntityPredicate.field_192483_a || (p_192482_2_ != null && (this.field_192484_b == null || EntityList.isStringEntityName(p_192482_2_, this.field_192484_b)) && this.field_192485_c.func_193422_a(p_192482_1_.posX, p_192482_1_.posY, p_192482_1_.posZ, p_192482_2_.posX, p_192482_2_.posY, p_192482_2_.posZ) && this.field_193435_d.func_193452_a(p_192482_1_.getServerWorld(), p_192482_2_.posX, p_192482_2_.posY, p_192482_2_.posZ) && this.field_193436_e.func_193469_a(p_192482_2_) && this.field_193437_f.func_193475_a(p_192482_2_));
    }
    
    public static EntityPredicate func_192481_a(@Nullable final JsonElement p_192481_0_) {
        if (p_192481_0_ != null && !p_192481_0_.isJsonNull()) {
            final JsonObject jsonobject = JsonUtils.getJsonObject(p_192481_0_, "entity");
            ResourceLocation resourcelocation = null;
            if (jsonobject.has("type")) {
                resourcelocation = new ResourceLocation(JsonUtils.getString(jsonobject, "type"));
                if (!EntityList.isStringValidEntityName(resourcelocation)) {
                    throw new JsonSyntaxException("Unknown entity type '" + resourcelocation + "', valid types are: " + EntityList.func_192840_b());
                }
            }
            final DistancePredicate distancepredicate = DistancePredicate.func_193421_a(jsonobject.get("distance"));
            final LocationPredicate locationpredicate = LocationPredicate.func_193454_a(jsonobject.get("location"));
            final MobEffectsPredicate mobeffectspredicate = MobEffectsPredicate.func_193471_a(jsonobject.get("effects"));
            final NBTPredicate nbtpredicate = NBTPredicate.func_193476_a(jsonobject.get("nbt"));
            return new EntityPredicate(resourcelocation, distancepredicate, locationpredicate, mobeffectspredicate, nbtpredicate);
        }
        return EntityPredicate.field_192483_a;
    }
    
    static {
        field_192483_a = new EntityPredicate(null, DistancePredicate.field_193423_a, LocationPredicate.field_193455_a, MobEffectsPredicate.field_193473_a, NBTPredicate.field_193479_a);
    }
}
