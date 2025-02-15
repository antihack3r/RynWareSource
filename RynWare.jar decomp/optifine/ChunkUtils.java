// 
// Decompiled by Procyon v0.6.0
// 

package optifine;

import java.util.Iterator;
import java.util.List;
import java.lang.reflect.Field;
import java.util.ArrayList;
import net.minecraft.world.chunk.Chunk;

public class ChunkUtils
{
    private static ReflectorField fieldHasEntities;
    
    public static boolean hasEntities(final Chunk p_hasEntities_0_) {
        if (ChunkUtils.fieldHasEntities == null) {
            ChunkUtils.fieldHasEntities = findFieldHasEntities(p_hasEntities_0_);
        }
        if (!ChunkUtils.fieldHasEntities.exists()) {
            return true;
        }
        final Boolean obool = (Boolean)Reflector.getFieldValue(p_hasEntities_0_, ChunkUtils.fieldHasEntities);
        return obool == null || obool;
    }
    
    private static ReflectorField findFieldHasEntities(final Chunk p_findFieldHasEntities_0_) {
        try {
            final List list = new ArrayList();
            final List list2 = new ArrayList();
            final Field[] afield = Chunk.class.getDeclaredFields();
            for (int i = 0; i < afield.length; ++i) {
                final Field field = afield[i];
                if (field.getType() == Boolean.TYPE) {
                    field.setAccessible(true);
                    list.add(field);
                    list2.add(field.get(p_findFieldHasEntities_0_));
                }
            }
            p_findFieldHasEntities_0_.setHasEntities(false);
            final List list3 = new ArrayList();
            for (final Object field2 : list) {
                list3.add(((Field)field2).get(p_findFieldHasEntities_0_));
            }
            p_findFieldHasEntities_0_.setHasEntities(true);
            final List list4 = new ArrayList();
            for (final Object field3 : list) {
                list4.add(((Field)field3).get(p_findFieldHasEntities_0_));
            }
            final List list5 = new ArrayList();
            for (int j = 0; j < list.size(); ++j) {
                final Field field4 = list.get(j);
                final Boolean obool = list3.get(j);
                final Boolean obool2 = list4.get(j);
                if (!obool && obool2) {
                    list5.add(field4);
                    final Boolean obool3 = list2.get(j);
                    field4.set(p_findFieldHasEntities_0_, obool3);
                }
            }
            if (list5.size() == 1) {
                final Field field5 = list5.get(0);
                return new ReflectorField(field5);
            }
        }
        catch (final Exception exception) {
            Config.warn(exception.getClass().getName() + " " + exception.getMessage());
        }
        Config.warn("Error finding Chunk.hasEntities");
        return new ReflectorField(new ReflectorClass(Chunk.class), "hasEntities");
    }
    
    static {
        ChunkUtils.fieldHasEntities = null;
    }
}
