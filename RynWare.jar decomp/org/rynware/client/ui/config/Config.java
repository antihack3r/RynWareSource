// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.config;

import java.util.Iterator;
import com.google.gson.JsonElement;
import org.rynware.client.feature.Feature;
import org.rynware.client.Main;
import com.google.gson.JsonObject;
import java.io.File;

public final class Config implements ConfigUpdater
{
    private final String name;
    private final File file;
    
    public Config(final String name) {
        this.name = name;
        this.file = new File(ConfigManager.configDirectory, name + ".json");
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            }
            catch (final Exception ex) {}
        }
    }
    
    public File getFile() {
        return this.file;
    }
    
    public String getName() {
        return this.name;
    }
    
    @Override
    public JsonObject save() {
        final JsonObject jsonObject = new JsonObject();
        final JsonObject modulesObject = new JsonObject();
        final JsonObject panelObject = new JsonObject();
        for (final Feature module : Main.instance.featureManager.getAllFeatures()) {
            modulesObject.add(module.getLabel(), (JsonElement)module.save());
        }
        jsonObject.add("Features", (JsonElement)modulesObject);
        return jsonObject;
    }
    
    @Override
    public void load(final JsonObject object) {
        if (object.has("Features")) {
            final JsonObject modulesObject = object.getAsJsonObject("Features");
            for (final Feature module : Main.instance.featureManager.getAllFeatures()) {
                module.setEnabled(false);
                module.load(modulesObject.getAsJsonObject(module.getLabel()));
            }
        }
    }
}
