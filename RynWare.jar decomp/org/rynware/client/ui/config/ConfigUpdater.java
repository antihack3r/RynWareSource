// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.config;

import com.google.gson.JsonObject;

public interface ConfigUpdater
{
    JsonObject save();
    
    void load(final JsonObject p0);
}
