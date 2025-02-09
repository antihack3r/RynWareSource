// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.misc;

import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.feature.Feature;

public class VclipHelper extends Feature
{
    public ListSetting vclipMode;
    public NumberSetting vclipPower;
    public BooleanSetting newTp;
    
    public VclipHelper() {
        super("VclipHelper", FeatureCategory.Misc);
        this.vclipMode = new ListSetting("Vclip Mode", "Default", () -> true, new String[] { "Default", "Minus", "UpClip", "DownClip" });
        this.vclipPower = new NumberSetting("Vclip Power", 75.0f, 1.0f, 110.0f, 1.0f, () -> this.vclipMode.currentMode.equalsIgnoreCase("Default") || this.vclipMode.currentMode.equalsIgnoreCase("Minus"));
        this.newTp = new BooleanSetting("Bypass", false, () -> true);
        this.addSettings(this.vclipMode, this.vclipPower, this.newTp);
    }
    
    @Override
    public void onEnable() {
        float y = 0.0f;
        if (this.vclipMode.currentMode.equalsIgnoreCase("DownClip")) {
            for (int i = 0; i < 255; ++i) {
                if (VclipHelper.mc.world.getBlockState(new BlockPos(VclipHelper.mc.player).add(0, -i, 0)) == Blocks.AIR.getDefaultState()) {
                    y = (float)(-i - 1);
                    break;
                }
                if (VclipHelper.mc.world.getBlockState(new BlockPos(VclipHelper.mc.player).add(0, -i, 0)) == Blocks.BEDROCK.getDefaultState()) {
                    break;
                }
            }
        }
        if (this.vclipMode.currentMode.equalsIgnoreCase("UpClip")) {
            for (int i = 3; i < 255; ++i) {
                if (VclipHelper.mc.world.getBlockState(new BlockPos(VclipHelper.mc.player).add(0, i, 0)) == Blocks.AIR.getDefaultState()) {
                    y = (float)(i + 1);
                    break;
                }
            }
        }
        if (this.vclipMode.currentMode.equalsIgnoreCase("Default")) {
            y = this.vclipPower.getNumberValue();
        }
        if (this.vclipMode.currentMode.equalsIgnoreCase("Minus")) {
            y = -this.vclipPower.getNumberValue();
        }
        this.clip(y);
        this.toggle();
    }
    
    private void clip(final float y) {
        if (!this.newTp.getBoolValue()) {
            for (int i = 0; i < 10; ++i) {
                VclipHelper.mc.player.connection.sendPacket(new CPacketPlayer.Position(VclipHelper.mc.player.posX, VclipHelper.mc.player.posY, VclipHelper.mc.player.posZ, VclipHelper.mc.player.onGround));
            }
            for (int i = 0; i < 10; ++i) {
                VclipHelper.mc.player.connection.sendPacket(new CPacketPlayer.Position(VclipHelper.mc.player.posX, VclipHelper.mc.player.posY + y, VclipHelper.mc.player.posZ, false));
            }
            VclipHelper.mc.player.setPosition(VclipHelper.mc.player.posX, VclipHelper.mc.player.posY + y, VclipHelper.mc.player.posZ);
        }
        else {
            Minecraft.getMinecraft().player.setSprinting(false);
            if (Minecraft.getMinecraft().player.onGround) {
                Minecraft.getMinecraft().player.jump();
            }
            for (int i = 0; i <= 3; ++i) {
                Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Position(VclipHelper.mc.player.posX, VclipHelper.mc.player.posY + y, VclipHelper.mc.player.posZ, false));
            }
            Minecraft.getMinecraft().player.connection.sendPacket(new CPacketPlayer.Position(VclipHelper.mc.player.posX, VclipHelper.mc.player.posY + y, VclipHelper.mc.player.posZ, true));
        }
    }
}
