// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_18_2to1_19.storage;

import java.util.HashMap;
import com.viaversion.viaversion.libs.opennbt.tag.builtin.CompoundTag;
import java.util.Map;
import com.viaversion.viaversion.api.connection.StorableObject;

public final class DimensionRegistryStorage implements StorableObject
{
    private Map<String, CompoundTag> dimensions;
    
    public DimensionRegistryStorage() {
        this.dimensions = new HashMap<String, CompoundTag>();
    }
    
    public CompoundTag dimension(final String dimensionKey) {
        final CompoundTag compoundTag = this.dimensions.get(dimensionKey);
        return (compoundTag != null) ? compoundTag.clone() : null;
    }
    
    public void setDimensions(final Map<String, CompoundTag> dimensions) {
        this.dimensions = dimensions;
    }
    
    @Override
    public boolean clearOnServerSwitch() {
        return false;
    }
}
