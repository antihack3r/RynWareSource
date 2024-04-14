// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viabackwards.protocol.protocol1_18_2to1_19.data;

import java.util.Iterator;
import com.viaversion.viaversion.libs.gson.JsonArray;
import com.viaversion.viaversion.libs.gson.JsonElement;
import com.viaversion.viaversion.libs.gson.JsonObject;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.protocols.protocol1_19to1_18_2.Protocol1_19To1_18_2;

public final class BackwardsMappings extends com.viaversion.viabackwards.api.data.BackwardsMappings
{
    private String[] argumentTypes;
    
    public BackwardsMappings() {
        super("1.19", "1.18", Protocol1_19To1_18_2.class, true);
    }
    
    @Override
    protected void loadExtras(final JsonObject oldMappings, final JsonObject newMappings, final JsonObject diffMappings) {
        super.loadExtras(oldMappings, newMappings, diffMappings);
        int i = 0;
        final JsonArray types = oldMappings.getAsJsonArray("argumenttypes");
        this.argumentTypes = new String[types.size()];
        for (final JsonElement element : types) {
            final String id = element.getAsString();
            this.argumentTypes[i++] = id;
        }
    }
    
    public String argumentType(final int argumentTypeId) {
        return (argumentTypeId >= 0 && argumentTypeId < this.argumentTypes.length) ? this.argumentTypes[argumentTypeId] : null;
    }
}
