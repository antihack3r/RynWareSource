// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.misc;

import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.feature.Feature;

public class ModuleSoundAlert extends Feature
{
    public static ListSetting soundMode;
    public static NumberSetting volume;
    public static NumberSetting pitch;
    
    public ModuleSoundAlert() {
        super("ModuleSoundAlert", "\u0412\u043e\u0441\u043f\u0440\u043e\u0438\u0437\u0432\u043e\u0434\u0438\u0442 \u0437\u0432\u0443\u043a \u043f\u0440\u0438 \u0432\u043a\u043b\u044e\u0447\u0435\u043d\u0438\u0438 \u043c\u043e\u0434\u0443\u043b\u044f", FeatureCategory.Misc);
        ModuleSoundAlert.soundMode = new ListSetting("Sound Mode", "Wav", () -> true, new String[] { "Wav", "Button" });
        ModuleSoundAlert.volume = new NumberSetting("Volume", 50.0f, 1.0f, 100.0f, 1.0f, () -> true);
        ModuleSoundAlert.pitch = new NumberSetting("Pitch", 2.0f, 0.5f, 2.0f, 0.1f, () -> ModuleSoundAlert.soundMode.currentMode.equals("Button"));
        this.addSettings(ModuleSoundAlert.soundMode, ModuleSoundAlert.volume, ModuleSoundAlert.pitch);
    }
}
