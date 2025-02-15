// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.util;

import com.viaversion.viaversion.api.Via;
import java.util.List;
import java.util.Comparator;
import java.util.ArrayList;
import com.viaversion.viaversion.api.minecraft.entities.EntityType;

public class EntityTypeUtil
{
    public static EntityType[] toOrderedArray(final EntityType[] values) {
        final List<EntityType> types = new ArrayList<EntityType>();
        for (final EntityType type : values) {
            if (type.getId() != -1) {
                types.add(type);
            }
        }
        types.sort(Comparator.comparingInt(EntityType::getId));
        return types.toArray(new EntityType[0]);
    }
    
    public static EntityType[] createSizedArray(final EntityType[] values) {
        int count = 0;
        for (final EntityType type : values) {
            if (!type.isAbstractType()) {
                ++count;
            }
        }
        return new EntityType[count];
    }
    
    public static void fill(final EntityType[] values, final EntityType[] toFill) {
        for (final EntityType type : values) {
            if (!type.isAbstractType()) {
                toFill[type.getId()] = type;
            }
        }
    }
    
    public static EntityType getTypeFromId(final EntityType[] values, final int typeId, final EntityType fallback) {
        final EntityType type;
        if (typeId < 0 || typeId >= values.length || (type = values[typeId]) == null) {
            Via.getPlatform().getLogger().severe("Could not find " + fallback.getClass().getSimpleName() + " type id " + typeId);
            return fallback;
        }
        return type;
    }
}
