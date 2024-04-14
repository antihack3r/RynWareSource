// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.hud;

import org.rynware.client.event.EventTarget;
import java.util.Iterator;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.Entity;
import net.minecraft.init.MobEffects;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.feature.Feature;

public class NoOverlay extends Feature
{
    public static BooleanSetting rain;
    public static BooleanSetting noHurtCam;
    public static BooleanSetting cameraClip;
    public static BooleanSetting antiTotem;
    public static BooleanSetting noFire;
    public static BooleanSetting noBossBar;
    public static BooleanSetting noArmorStand;
    public static BooleanSetting blindness;
    
    public NoOverlay() {
        super("NoOverlay", "\u0423\u0431\u0438\u0440\u0430\u0435\u0442 \u043b\u0438\u0448\u043d\u0438\u0435 \u0432\u0438\u0437\u0443\u0430\u043b\u044c\u043d\u044b\u0435 \u0447\u0430\u0441\u0442\u0438", FeatureCategory.Visuals);
        this.addSettings(NoOverlay.rain, NoOverlay.noArmorStand, NoOverlay.noHurtCam, NoOverlay.cameraClip, NoOverlay.antiTotem, NoOverlay.noFire, NoOverlay.blindness, NoOverlay.noBossBar);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        if (NoOverlay.rain.getBoolValue() && NoOverlay.mc.world.isRaining()) {
            NoOverlay.mc.world.setRainStrength(0.0f);
            NoOverlay.mc.world.setThunderStrength(0.0f);
        }
        if ((NoOverlay.blindness.getBoolValue() && NoOverlay.mc.player.isPotionActive(MobEffects.BLINDNESS)) || NoOverlay.mc.player.isPotionActive(MobEffects.NAUSEA)) {
            NoOverlay.mc.player.removePotionEffect(MobEffects.NAUSEA);
            NoOverlay.mc.player.removePotionEffect(MobEffects.BLINDNESS);
        }
        if (NoOverlay.noArmorStand.getBoolValue()) {
            if (NoOverlay.mc.player == null || NoOverlay.mc.world == null) {
                return;
            }
            for (final Entity entity : NoOverlay.mc.world.loadedEntityList) {
                if (entity != null) {
                    if (!(entity instanceof EntityArmorStand)) {
                        continue;
                    }
                    NoOverlay.mc.world.removeEntity(entity);
                }
            }
        }
    }
    
    static {
        NoOverlay.rain = new BooleanSetting("Rain", true, () -> true);
        NoOverlay.noHurtCam = new BooleanSetting("HurtCam", true, () -> true);
        NoOverlay.cameraClip = new BooleanSetting("Camera Clip", true, () -> true);
        NoOverlay.antiTotem = new BooleanSetting("AntiTotemAnimation", false, () -> true);
        NoOverlay.noFire = new BooleanSetting("NoFireOverlay", false, () -> true);
        NoOverlay.noBossBar = new BooleanSetting("NoBossBar", false, () -> true);
        NoOverlay.noArmorStand = new BooleanSetting("ArmorStand", false, () -> true);
        NoOverlay.blindness = new BooleanSetting("Blindness", true, () -> true);
    }
}
