// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature;

import java.util.Iterator;
import com.google.gson.JsonElement;
import org.rynware.client.ui.settings.impl.ColorSetting;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.settings.Setting;
import com.google.gson.JsonObject;
import org.rynware.client.event.EventManager;
import org.rynware.client.utils.other.ChatUtils;
import org.rynware.client.ui.notification.NotificationRenderer;
import org.rynware.client.ui.notification.NotificationMode;
import org.rynware.client.feature.impl.hud.Notifications;
import org.rynware.client.utils.render.SoundUtils;
import net.minecraft.init.SoundEvents;
import org.rynware.client.feature.impl.misc.ModuleSoundAlert;
import org.rynware.client.Main;
import net.minecraft.util.text.TextFormatting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.clickgui.ScreenHelper;
import org.rynware.client.utils.Helper;
import org.rynware.client.ui.settings.Configurable;

public class Feature extends Configurable implements Helper
{
    public ScreenHelper screenHelper;
    public FeatureCategory category;
    private boolean enabled;
    public float animYto;
    private String label;
    private String suffix;
    private int bind;
    private String desc;
    
    public Feature(final String label, final String desc, final FeatureCategory category) {
        this.label = label;
        this.desc = desc;
        this.category = category;
        this.bind = 0;
        this.enabled = false;
    }
    
    public Feature(final String label, final FeatureCategory category) {
        this.screenHelper = new ScreenHelper(0.0f, 0.0f);
        this.label = label;
        this.category = category;
        this.bind = 0;
        this.enabled = false;
    }
    
    public String getSuffix() {
        return (this.suffix == null) ? this.label : this.suffix;
    }
    
    public void setSuffix(final String suffix) {
        this.suffix = suffix;
        this.suffix = this.getLabel() + TextFormatting.GRAY + " " + suffix;
    }
    
    public ScreenHelper getTranslate() {
        return this.screenHelper;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public void setLabel(final String label) {
        this.label = label;
    }
    
    public int getBind() {
        return this.bind;
    }
    
    public void setBind(final int bind) {
        this.bind = bind;
    }
    
    public String getDesc() {
        return this.desc;
    }
    
    public void setDesc(final String desc) {
        this.desc = desc;
    }
    
    public FeatureCategory getCategory() {
        return this.category;
    }
    
    public void setCategory(final FeatureCategory category) {
        this.category = category;
    }
    
    public void onEnable() {
        if (Main.instance.featureManager.getFeature(ModuleSoundAlert.class).isEnabled()) {
            final float volume = ModuleSoundAlert.volume.getNumberValue() / 10.0f;
            if (ModuleSoundAlert.soundMode.currentMode.equals("Button")) {
                Feature.mc.player.playSound(SoundEvents.BLOCK_NOTE_PLING, ModuleSoundAlert.volume.getNumberValue() / 100.0f, ModuleSoundAlert.pitch.getNumberValue());
            }
            else if (ModuleSoundAlert.soundMode.currentMode.equalsIgnoreCase("Wav")) {
                SoundUtils.playSound("enable.wav", -30.0f + volume * 3.0f, false);
            }
        }
        if (!this.getLabel().contains("ClickGui") && !this.getLabel().contains("Client Font") && !this.getLabel().contains("Notifications") && Notifications.notifMode.currentMode.equalsIgnoreCase("Rect") && Notifications.state.getBoolValue()) {
            NotificationRenderer.queue("Feature", this.getLabel() + " was " + TextFormatting.GREEN + "Enabled!", 1, NotificationMode.INFO);
        }
        else if (!this.getLabel().contains("ClickGui") && !this.getLabel().contains("Client Font") && !this.getLabel().contains("Notifications") && Notifications.notifMode.currentMode.equalsIgnoreCase("Chat") && Notifications.state.getBoolValue()) {
            ChatUtils.addChatMessage(TextFormatting.GRAY + "[Notifications] " + TextFormatting.WHITE + this.getLabel() + " was" + TextFormatting.GREEN + " enabled!");
        }
        EventManager.register(this);
    }
    
    public void onDisable() {
        if (Main.instance.featureManager.getFeature(ModuleSoundAlert.class).isEnabled()) {
            final float volume = ModuleSoundAlert.volume.getNumberValue() / 10.0f;
            if (ModuleSoundAlert.soundMode.currentMode.equals("Button")) {
                Feature.mc.player.playSound(SoundEvents.BLOCK_NOTE_PLING, ModuleSoundAlert.volume.getNumberValue() / 100.0f, ModuleSoundAlert.pitch.getNumberValue());
            }
            else if (ModuleSoundAlert.soundMode.currentMode.equalsIgnoreCase("Wav")) {
                SoundUtils.playSound("disable.wav", -30.0f + volume * 3.0f, false);
            }
        }
        if (!this.getLabel().contains("ClickGui") && !this.getLabel().contains("Client Font") && !this.getLabel().contains("Notifications") && Notifications.notifMode.currentMode.equalsIgnoreCase("Rect") && Notifications.state.getBoolValue()) {
            NotificationRenderer.queue("Feature", this.getLabel() + " was " + TextFormatting.RED + "Disabled!", 1, NotificationMode.INFO);
        }
        else if (!this.getLabel().contains("ClickGui") && !this.getLabel().contains("Client Font") && !this.getLabel().contains("Notifications") && Notifications.notifMode.currentMode.equalsIgnoreCase("Chat") && Notifications.state.getBoolValue()) {
            ChatUtils.addChatMessage(TextFormatting.GRAY + "[Notifications] " + TextFormatting.WHITE + this.getLabel() + " was" + TextFormatting.RED + " disabled!");
        }
        EventManager.unregister(this);
    }
    
    public void toggle() {
        this.enabled = !this.enabled;
        if (this.enabled) {
            this.onEnable();
        }
        else {
            this.onDisable();
        }
    }
    
    public ScreenHelper getScreenHelper() {
        return this.screenHelper;
    }
    
    public boolean isEnabled() {
        return this.enabled;
    }
    
    public void setEnabled(final boolean enabled) {
        if (enabled) {
            EventManager.register(this);
        }
        else {
            EventManager.unregister(this);
        }
        this.enabled = enabled;
    }
    
    public JsonObject save() {
        final JsonObject object = new JsonObject();
        object.addProperty("state", Boolean.valueOf(this.isEnabled()));
        object.addProperty("keyIndex", (Number)this.getBind());
        final JsonObject propertiesObject = new JsonObject();
        for (final Setting set : this.getSettings()) {
            if (this.getSettings() != null) {
                if (set instanceof BooleanSetting) {
                    propertiesObject.addProperty(set.getName(), Boolean.valueOf(((BooleanSetting)set).getBoolValue()));
                }
                else if (set instanceof ListSetting) {
                    propertiesObject.addProperty(set.getName(), ((ListSetting)set).getCurrentMode());
                }
                else if (set instanceof NumberSetting) {
                    propertiesObject.addProperty(set.getName(), (Number)((NumberSetting)set).getNumberValue());
                }
                else if (set instanceof ColorSetting) {
                    propertiesObject.addProperty(set.getName(), (Number)((ColorSetting)set).getColorValue());
                }
            }
            object.add("Settings", (JsonElement)propertiesObject);
        }
        return object;
    }
    
    public void load(final JsonObject object) {
        if (object != null) {
            if (object.has("state")) {
                this.setEnabled(object.get("state").getAsBoolean());
            }
            if (object.has("keyIndex")) {
                this.setBind(object.get("keyIndex").getAsInt());
            }
            for (final Setting set : this.getSettings()) {
                final JsonObject propertiesObject = object.getAsJsonObject("Settings");
                if (set == null) {
                    continue;
                }
                if (propertiesObject == null) {
                    continue;
                }
                if (!propertiesObject.has(set.getName())) {
                    continue;
                }
                if (set instanceof BooleanSetting) {
                    ((BooleanSetting)set).setBoolValue(propertiesObject.get(set.getName()).getAsBoolean());
                }
                else if (set instanceof ListSetting) {
                    ((ListSetting)set).setListMode(propertiesObject.get(set.getName()).getAsString());
                }
                else if (set instanceof NumberSetting) {
                    ((NumberSetting)set).setValueNumber(propertiesObject.get(set.getName()).getAsFloat());
                }
                else {
                    if (!(set instanceof ColorSetting)) {
                        continue;
                    }
                    ((ColorSetting)set).setColorValue(propertiesObject.get(set.getName()).getAsInt());
                }
            }
        }
    }
}
