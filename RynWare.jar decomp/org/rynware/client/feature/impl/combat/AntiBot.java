// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.combat;

import java.util.ArrayList;
import org.rynware.client.event.EventTarget;
import net.minecraft.entity.player.EntityPlayer;
import java.util.Iterator;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import java.util.UUID;
import java.nio.charset.StandardCharsets;
import org.rynware.client.event.events.impl.player.EventPreMotion;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import net.minecraft.entity.Entity;
import java.util.List;
import org.rynware.client.feature.Feature;

public class AntiBot extends Feature
{
    public static List<Entity> isBotPlayer;
    public ListSetting antiBotMode;
    public BooleanSetting removeWorld;
    
    public AntiBot() {
        super("AntiBot", "\u0423\u0431\u0438\u0440\u0430\u0435\u0442 \u0431\u043e\u0442\u043e\u0432", FeatureCategory.Combat);
        this.antiBotMode = new ListSetting("AntiBot Mode", "Matrix", () -> true, new String[] { "Matrix", "Wellmore" });
        this.removeWorld = new BooleanSetting("Remove from World", true, () -> this.antiBotMode.currentMode.equalsIgnoreCase("Wellmore"));
        this.addSettings(this.antiBotMode, this.removeWorld);
    }
    
    @EventTarget
    public void onPreMotion(final EventPreMotion event) {
        for (final Entity entity : AntiBot.mc.world.playerEntities) {
            if (this.antiBotMode.currentMode.equalsIgnoreCase("Matrix")) {
                if (entity.getUniqueID().equals(UUID.nameUUIDFromBytes(("OfflinePlayer:" + entity.getName()).getBytes(StandardCharsets.UTF_8))) || !(entity instanceof EntityOtherPlayerMP)) {
                    continue;
                }
                AntiBot.isBotPlayer.add(entity);
            }
            else {
                if (!this.antiBotMode.currentMode.equalsIgnoreCase("Wellmore")) {
                    continue;
                }
                for (final Entity entity2 : AntiBot.mc.world.loadedEntityList) {
                    final String Test = entity2.getDisplayName().getFormattedText();
                    if (!entity2.isInvisible() && entity2 != AntiBot.mc.player && entity2.ticksExisted <= 30 && AntiBot.mc.player.getDistanceToEntity(entity2) <= 8.0f && !Test.startsWith("\u0412§7")) {
                        AntiBot.mc.world.removeEntity(entity2);
                    }
                }
            }
        }
    }
    
    static {
        AntiBot.isBotPlayer = new ArrayList<Entity>();
    }
}
