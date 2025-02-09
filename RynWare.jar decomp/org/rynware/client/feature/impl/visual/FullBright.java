// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.visual;

import org.rynware.client.event.EventTarget;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.Potion;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.feature.Feature;

public class FullBright extends Feature
{
    public static final ListSetting brightMode;
    
    public FullBright() {
        super("FullBright", "\u0414\u0435\u043b\u0430\u0435\u0442 \u043c\u0438\u0440 \u044f\u0440\u0447\u0435", FeatureCategory.Visuals);
        this.addSettings(FullBright.brightMode);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (this.isEnabled()) {
            final String mode = FullBright.brightMode.getOptions();
            if (mode.equalsIgnoreCase("Gamma")) {
                FullBright.mc.gameSettings.gammaSetting = 10.0f;
            }
            if (mode.equalsIgnoreCase("Potion")) {
                FullBright.mc.player.addPotionEffect(new PotionEffect(Potion.getPotionById(16), 817, 1));
            }
            else {
                FullBright.mc.player.removePotionEffect(Potion.getPotionById(16));
            }
        }
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        FullBright.mc.gameSettings.gammaSetting = 0.1f;
        FullBright.mc.player.removePotionEffect(Potion.getPotionById(16));
    }
    
    static {
        brightMode = new ListSetting("FullBright Mode", "Gamma", () -> true, new String[] { "Gamma", "Potion" });
    }
}
