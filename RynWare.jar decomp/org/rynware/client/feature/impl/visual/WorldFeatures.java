// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.visual;

import java.awt.Color;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.event.EventTarget;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import org.rynware.client.event.events.impl.packet.EventReceivePacket;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.ui.settings.impl.ColorSetting;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.feature.Feature;

public class WorldFeatures extends Feature
{
    private long spinTime;
    public static BooleanSetting snow;
    public static ColorSetting weatherColor;
    public static BooleanSetting worldColor;
    public static ColorSetting worldColors;
    public BooleanSetting ambience;
    public ListSetting ambienceMode;
    public NumberSetting ambienceSpeed;
    
    public WorldFeatures() {
        super("WorldFeatures", FeatureCategory.Visuals);
        this.spinTime = 0L;
        this.ambience = new BooleanSetting("Ambience", false, () -> true);
        this.ambienceMode = new ListSetting("Ambience Mode", "Day", () -> this.ambience.getBoolValue(), new String[] { "Day", "Night", "Morning", "Sunset", "Spin" });
        this.ambienceSpeed = new NumberSetting("Ambience Speed", 20.0f, 0.1f, 1000.0f, 1.0f, () -> this.ambienceMode.currentMode.equals("Spin"));
        this.addSettings(WorldFeatures.snow, WorldFeatures.weatherColor, WorldFeatures.worldColor, WorldFeatures.worldColors, this.ambience, this.ambienceMode, this.ambienceSpeed);
    }
    
    @EventTarget
    public void onPacket(final EventReceivePacket event) {
        if (this.ambience.getBoolValue() && event.getPacket() instanceof SPacketTimeUpdate) {
            event.setCancelled(true);
        }
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        final String mode = this.ambienceMode.getOptions();
        if (this.ambience.getBoolValue()) {
            if (mode.equalsIgnoreCase("Spin")) {
                WorldFeatures.mc.world.setWorldTime(this.spinTime);
                this.spinTime += (long)this.ambienceSpeed.getNumberValue();
            }
            else if (mode.equalsIgnoreCase("Day")) {
                WorldFeatures.mc.world.setWorldTime(5000L);
            }
            else if (mode.equalsIgnoreCase("Night")) {
                WorldFeatures.mc.world.setWorldTime(17000L);
            }
            else if (mode.equalsIgnoreCase("Morning")) {
                WorldFeatures.mc.world.setWorldTime(0L);
            }
            else if (mode.equalsIgnoreCase("Sunset")) {
                WorldFeatures.mc.world.setWorldTime(13000L);
            }
        }
    }
    
    static {
        WorldFeatures.snow = new BooleanSetting("Snow", true, () -> true);
        WorldFeatures.weatherColor = new ColorSetting("Weather", new Color(16777215).getRGB(), () -> WorldFeatures.snow.getBoolValue());
        WorldFeatures.worldColor = new BooleanSetting("World Color", false, () -> true);
        WorldFeatures.worldColors = new ColorSetting("Color World", new Color(16777215).getRGB(), () -> WorldFeatures.worldColor.getBoolValue());
    }
}
