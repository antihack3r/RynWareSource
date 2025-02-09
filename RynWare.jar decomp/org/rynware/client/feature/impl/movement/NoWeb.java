// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.movement;

import org.rynware.client.event.events.impl.player.EventMove;
import org.rynware.client.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import org.rynware.client.utils.movement.MovementUtils;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import org.rynware.client.event.events.impl.player.EventPreMotion;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.feature.Feature;

public class NoWeb extends Feature
{
    public ListSetting noWebMode;
    public NumberSetting webSpeed;
    
    public NoWeb() {
        super("NoWeb", "\u0423\u0431\u0438\u0440\u0430\u0435\u0442 \u0437\u0430\u043c\u0435\u0434\u043b\u0435\u043d\u0438\u0435 \u0432 \u043f\u0430\u0443\u0442\u0438\u043d\u0435", FeatureCategory.Player);
        this.noWebMode = new ListSetting("NoWeb Mode", "Matrix", () -> true, new String[] { "Matrix", "Matrix New", "NCP" });
        this.webSpeed = new NumberSetting("Web Speed", 0.8f, 0.1f, 2.0f, 0.1f, () -> this.noWebMode.currentMode.equals("Matrix New"));
        this.addSettings(this.noWebMode, this.webSpeed);
    }
    
    @EventTarget
    public void onPreMotion(final EventPreMotion event) {
        final String mode = this.noWebMode.getOptions();
        this.setSuffix(mode);
        if (mode.equalsIgnoreCase("Matrix New")) {
            final BlockPos blockPos = new BlockPos(NoWeb.mc.player.posX, NoWeb.mc.player.posY - 0.6, NoWeb.mc.player.posZ);
            final Block block = NoWeb.mc.world.getBlockState(blockPos).getBlock();
            if (NoWeb.mc.player.isInWeb) {
                final EntityPlayerSP player = NoWeb.mc.player;
                player.motionY += 2.0;
            }
            else if (Block.getIdFromBlock(block) == 30) {
                MovementUtils.setSpeed(this.webSpeed.getNumberValue());
                if (NoWeb.mc.gameSettings.keyBindJump.isKeyDown()) {
                    return;
                }
                NoWeb.mc.player.isInWeb = false;
                NoWeb.mc.gameSettings.keyBindJump.pressed = false;
            }
        }
    }
    
    @EventTarget
    public void onMove(final EventMove event) {
        final String mode = this.noWebMode.getOptions();
        this.setSuffix(mode);
        if (this.isEnabled()) {
            if (mode.equalsIgnoreCase("Matrix")) {
                if (NoWeb.mc.player.isInWeb) {
                    final EntityPlayerSP player = NoWeb.mc.player;
                    player.motionY += 2.0;
                }
                else {
                    if (NoWeb.mc.gameSettings.keyBindJump.isKeyDown()) {
                        return;
                    }
                    NoWeb.mc.player.isInWeb = false;
                }
                if (NoWeb.mc.gameSettings.keyBindJump.isKeyDown()) {
                    return;
                }
                if (NoWeb.mc.player.isInWeb && !NoWeb.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    MovementUtils.setEventSpeed(event, 0.483);
                }
            }
            else if (mode.equalsIgnoreCase("NCP")) {
                if (NoWeb.mc.player.onGround && NoWeb.mc.player.isInWeb) {
                    NoWeb.mc.player.isInWeb = true;
                }
                else {
                    if (NoWeb.mc.gameSettings.keyBindJump.isKeyDown()) {
                        return;
                    }
                    NoWeb.mc.player.isInWeb = false;
                }
                if (NoWeb.mc.player.isInWeb && !NoWeb.mc.gameSettings.keyBindSneak.isKeyDown()) {
                    MovementUtils.setSpeed(0.403);
                }
            }
        }
    }
}
