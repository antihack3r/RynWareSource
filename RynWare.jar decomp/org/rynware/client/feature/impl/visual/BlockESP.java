// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.visual;

import org.rynware.client.event.EventTarget;
import net.minecraft.util.math.BlockPos;
import java.util.Iterator;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntityShulkerBox;
import net.minecraft.tileentity.TileEntityBed;
import net.minecraft.tileentity.TileEntityEnderChest;
import org.rynware.client.utils.render.RenderUtils;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntity;
import org.rynware.client.utils.render.ClientHelper;
import org.rynware.client.event.events.impl.render.EventRender3D;
import org.rynware.client.ui.settings.Setting;
import java.awt.Color;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.ColorSetting;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.feature.Feature;

public class BlockESP extends Feature
{
    private final BooleanSetting enderChest;
    private final BooleanSetting chest;
    private final BooleanSetting clientColor;
    private final ColorSetting spawnerColor;
    private final BooleanSetting espOutline;
    private final ColorSetting chestColor;
    private final ColorSetting enderChestColor;
    private final ColorSetting shulkerColor;
    private final ColorSetting bedColor;
    private final BooleanSetting bed;
    private final BooleanSetting shulker;
    private final BooleanSetting spawner;
    private final BooleanSetting f;
    
    public BlockESP() {
        super("BlockESP", "\u041f\u043e\u0434\u0441\u0432\u0435\u0447\u0438\u0432\u0430\u0435\u0442 \u0432\u044b\u0431\u0440\u0430\u043d\u043d\u044b\u0435 \u0431\u043b\u043e\u043a\u0438", FeatureCategory.Visuals);
        this.chest = new BooleanSetting("Chest", true, () -> true);
        this.enderChest = new BooleanSetting("Ender Chest", false, () -> true);
        this.f = new BooleanSetting("Minecard", false, () -> true);
        this.spawner = new BooleanSetting("Spawner", false, () -> true);
        this.shulker = new BooleanSetting("Shulker", false, () -> true);
        this.bed = new BooleanSetting("Bed", false, () -> true);
        this.chestColor = new ColorSetting("Chest Color", new Color(15609087).getRGB(), this.chest::getBoolValue);
        this.enderChestColor = new ColorSetting("EnderChest Color", new Color(15609087).getRGB(), this.enderChest::getBoolValue);
        this.shulkerColor = new ColorSetting("Shulker Color", new Color(15609087).getRGB(), this.shulker::getBoolValue);
        this.spawnerColor = new ColorSetting("Spawner Color", new Color(15609087).getRGB(), this.spawner::getBoolValue);
        this.bedColor = new ColorSetting("Bed Color", new Color(15609087).getRGB(), this.bed::getBoolValue);
        this.clientColor = new BooleanSetting("Client Colors", false, () -> true);
        this.espOutline = new BooleanSetting("ESP Outline", false, () -> true);
        this.addSettings(this.espOutline, this.chest, this.enderChest, this.spawner, this.shulker, this.bed, this.f, this.chestColor, this.enderChestColor, this.spawnerColor, this.shulkerColor, this.bedColor, this.clientColor);
    }
    
    @EventTarget
    public void onRender3D(final EventRender3D event) {
        final Color colorChest = this.clientColor.getBoolValue() ? ClientHelper.getClientColor() : new Color(this.chestColor.getColorValue());
        final Color enderColorChest = this.clientColor.getBoolValue() ? ClientHelper.getClientColor() : new Color(this.enderChestColor.getColorValue());
        final Color shulkColor = this.clientColor.getBoolValue() ? ClientHelper.getClientColor() : new Color(this.shulkerColor.getColorValue());
        final Color bedColoR = this.clientColor.getBoolValue() ? ClientHelper.getClientColor() : new Color(this.bedColor.getColorValue());
        final Color spawnerColoR = this.clientColor.getBoolValue() ? ClientHelper.getClientColor() : new Color(this.spawnerColor.getColorValue());
        if (BlockESP.mc.player != null || BlockESP.mc.world != null) {
            for (final TileEntity entity : BlockESP.mc.world.loadedTileEntityList) {
                final BlockPos pos = entity.getPos();
                if (entity instanceof TileEntityChest && this.chest.getBoolValue()) {
                    RenderUtils.blockEsp(pos, new Color(colorChest.getRGB()), this.espOutline.getBoolValue());
                }
                else if (entity instanceof TileEntityEnderChest && this.enderChest.getBoolValue()) {
                    RenderUtils.blockEsp(pos, new Color(enderColorChest.getRGB()), this.espOutline.getBoolValue());
                }
                else if (entity instanceof TileEntityBed && this.bed.getBoolValue()) {
                    RenderUtils.blockEsp(pos, new Color(bedColoR.getRGB()), this.espOutline.getBoolValue());
                }
                else if (entity instanceof TileEntityShulkerBox && this.shulker.getBoolValue()) {
                    RenderUtils.blockEsp(pos, new Color(shulkColor.getRGB()), this.espOutline.getBoolValue());
                }
                else {
                    if (!(entity instanceof TileEntityMobSpawner) || !this.spawner.getBoolValue()) {
                        continue;
                    }
                    RenderUtils.blockEsp(pos, new Color(spawnerColoR.getRGB()), this.espOutline.getBoolValue());
                }
            }
        }
    }
}
