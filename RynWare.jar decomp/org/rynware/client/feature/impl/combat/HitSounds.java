// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.combat;

import org.rynware.client.utils.render.SoundUtils;
import org.rynware.client.event.events.impl.player.EventPostAttackSilent;
import org.rynware.client.event.EventTarget;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.feature.Feature;

public class HitSounds extends Feature
{
    private final ListSetting soundMode;
    private final NumberSetting volume;
    
    public HitSounds() {
        super("HitSounds", FeatureCategory.Combat);
        this.soundMode = new ListSetting("Sound Mode", "NeverLose", () -> true, new String[] { "NeverLose", "Pop", "UwU" });
        this.volume = new NumberSetting("Volume", 50.0f, 1.0f, 100.0f, 1.0f, () -> true);
        this.addSettings(this.soundMode, this.volume);
    }
    
    @EventTarget
    public void onSuffixUpdate(final EventUpdate event) {
        this.setSuffix(this.soundMode.getCurrentMode());
    }
    
    @EventTarget
    public void onPostAttack(final EventPostAttackSilent event) {
        final float volume = this.volume.getNumberValue() / 10.0f;
        if (KillAura.isBreaked) {
            return;
        }
        if (this.soundMode.currentMode.equals("NeverLose")) {
            SoundUtils.playSound("neverlose.wav", -30.0f + volume * 3.0f, false);
        }
        else if (this.soundMode.currentMode.equals("Pop")) {
            SoundUtils.playSound("pop.wav", -30.0f + volume * 3.0f, false);
        }
        else if (this.soundMode.currentMode.equals("UwU")) {
            SoundUtils.playSound("uwu.wav", -30.0f + volume * 3.0f, false);
        }
    }
}
