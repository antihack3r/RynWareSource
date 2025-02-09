// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.hud;

import org.rynware.client.event.EventTarget;
import java.util.Iterator;
import org.rynware.client.utils.render.RoundedUtil;
import net.minecraft.util.EnumHand;
import org.rynware.client.utils.math.MathematicHelper;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import org.rynware.client.utils.render.ClientHelper;
import org.rynware.client.utils.render.Colors;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.Entity;
import org.rynware.client.feature.impl.misc.NameProtect;
import org.rynware.client.utils.render.RenderUtils;
import java.awt.Color;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.client.gui.GuiChat;
import org.rynware.client.utils.math.AnimationHelper;
import org.rynware.client.feature.impl.combat.KillAura;
import org.rynware.client.draggable.component.DraggableComponent;
import org.rynware.client.Main;
import org.rynware.client.draggable.component.impl.DraggableTargetHUD;
import org.rynware.client.event.events.impl.render.EventRender2D;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.ColorSetting;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.utils.other.Particles;
import java.util.ArrayList;
import org.rynware.client.utils.math.TimerHelper;
import net.minecraft.entity.EntityLivingBase;
import org.rynware.client.feature.Feature;

public class TargetHUD extends Feature
{
    private double scale;
    private static EntityLivingBase curTarget;
    public static TimerHelper thudTimer;
    private float healthBarWidth;
    private ArrayList<Particles> particles;
    public ListSetting targetHudMode;
    public static ListSetting thudColorMode;
    public BooleanSetting particles2;
    public static ColorSetting targetHudColor;
    public BooleanSetting shadowThud;
    public BooleanSetting blurThud;
    
    public TargetHUD() {
        super("TargetHUD", FeatureCategory.Hud);
        this.scale = 0.0;
        this.particles = new ArrayList<Particles>();
        this.targetHudMode = new ListSetting("TargetHUD Mode", "RynWare", () -> true, new String[] { "RynWare" });
        this.particles2 = new BooleanSetting("Particles", TargetHUD.thudColorMode.currentMode.equals("Custom"), () -> this.targetHudMode.currentMode.equals("Style") && TargetHUD.thudColorMode.currentMode.equals("Custom"));
        this.shadowThud = new BooleanSetting("Shadow", true, () -> true);
        this.blurThud = new BooleanSetting("Blur", true, () -> true);
        this.addSettings(this.targetHudMode, TargetHUD.thudColorMode, this.particles2, TargetHUD.targetHudColor, this.shadowThud, this.blurThud);
    }
    
    @EventTarget
    public void onRender2D(final EventRender2D e) {
        if (this.targetHudMode.currentMode.equals("Style")) {
            final DraggableTargetHUD dth = (DraggableTargetHUD)Main.instance.draggableHUD.getDraggableComponentByClass(DraggableTargetHUD.class);
            final float x = (float)dth.getX();
            final float y = (float)dth.getY();
            dth.setWidth(130);
            dth.setHeight(37);
            if (KillAura.target != null) {
                TargetHUD.curTarget = KillAura.target;
                this.scale = AnimationHelper.animation((float)this.scale, 1.0f, (float)(6.0 * Main.deltaTime()));
            }
            else if (TargetHUD.mc.currentScreen instanceof GuiChat) {
                TargetHUD.curTarget = TargetHUD.mc.player;
                this.scale = AnimationHelper.animation((float)this.scale, 1.0f, (float)(6.0 * Main.deltaTime()));
            }
            else {
                this.scale = AnimationHelper.animation((float)this.scale, 0.0f, (float)(6.0 * Main.deltaTime()));
            }
            final EntityLivingBase target = KillAura.target;
            if (TargetHUD.curTarget != null && TargetHUD.curTarget instanceof EntityPlayer) {
                try {
                    GlStateManager.pushMatrix();
                    GlStateManager.resetColor();
                    GL11.glTranslated((double)(x + 36.0f), (double)(y + 26.0f), 0.0);
                    GL11.glScaled(this.scale, this.scale, 0.0);
                    GL11.glTranslated((double)(-(x + 36.0f)), (double)(-(y + 26.0f)), 0.0);
                    if (this.blurThud.getBoolValue()) {
                        RenderUtils.drawBlur(8.0f, () -> RenderUtils.drawSmoothRect(x, y, x + dth.getWidth(), y + dth.getHeight(), new Color(0, 0, 0, 100).getRGB()));
                    }
                    if (this.shadowThud.getBoolValue()) {
                        RenderUtils.drawShadow(8.0f, 1.0f, () -> RenderUtils.drawSmoothRect(x, y, x + dth.getWidth(), y + dth.getHeight(), new Color(0, 0, 0, 100).getRGB()));
                    }
                    RenderUtils.drawSmoothRect(x, y, x + dth.getWidth(), y + dth.getHeight(), new Color(0, 0, 0, 100).getRGB());
                    TargetHUD.mc.rubik_18.drawString((Main.instance.featureManager.getFeature(NameProtect.class).isEnabled() && NameProtect.otherName.getBoolValue()) ? "Protected" : TargetHUD.curTarget.getName(), x + 35.0f, y + 5.0f, -1);
                    TargetHUD.mc.rubik_17.drawString((int)TargetHUD.curTarget.getHealth() + " HP - " + (int)TargetHUD.mc.player.getDistanceToEntity(TargetHUD.curTarget) + "m", x + 35.0f, y + 5.0f + 10.0f, -1);
                    double healthWid = TargetHUD.curTarget.getHealth() / TargetHUD.curTarget.getMaxHealth() * 90.0f;
                    healthWid = MathHelper.clamp(healthWid, 0.0, 90.0);
                    this.healthBarWidth = AnimationHelper.animation(this.healthBarWidth, (float)healthWid, (float)(10.0 * Main.deltaTime()));
                    RenderUtils.drawRect2(x + 36.0f, y + 25.0f, 90.0, 5.0, new Colors(Color.decode("#191f13")).setAlpha(180).getColor().getRGB());
                    RenderUtils.drawGradientRected(x + 36.0f, y + 25.0f, this.healthBarWidth, 5.0, ClientHelper.getTargetHudColor().darker().getRGB(), ClientHelper.getTargetHudColor().brighter().brighter().getRGB());
                    RenderUtils.drawBlurredShadow(x + 36.0f, y + 25.0f, this.healthBarWidth + 2.0f, 5.0f, 8, ClientHelper.getTargetHudColor().darker());
                    if (this.particles2.getBoolValue() && TargetHUD.thudColorMode.currentMode.equals("Custom")) {
                        for (final Particles p : this.particles) {
                            if (p.opacity > 4.0) {
                                p.render2D();
                            }
                        }
                        if (TargetHUD.thudTimer.hasReached(15.0)) {
                            for (final Particles p : this.particles) {
                                p.updatePosition();
                                if (p.opacity < 1.0) {
                                    this.particles.remove(p);
                                }
                            }
                            TargetHUD.thudTimer.reset();
                        }
                        if (TargetHUD.curTarget.hurtTime == 8) {
                            for (int i = 0; i < 1; ++i) {
                                final Particles p = new Particles();
                                p.init(x + 15.0f, y + 15.0f, (Math.random() - 0.5) * 2.0 * 1.9, (Math.random() - 0.5) * 2.0 * 1.4, (float)Math.random() * 1.0f, ClientHelper.getTargetHudColor());
                                this.particles.add(p);
                            }
                        }
                    }
                    for (final NetworkPlayerInfo targetHead : TargetHUD.mc.player.connection.getPlayerInfoMap()) {
                        try {
                            if (TargetHUD.mc.world.getPlayerEntityByUUID(targetHead.getGameProfile().getId()) != TargetHUD.curTarget) {
                                continue;
                            }
                            TargetHUD.mc.getTextureManager().bindTexture(targetHead.getLocationSkin());
                            final int scaleOffset = (int)(TargetHUD.curTarget.hurtTime * 0.55f);
                            final float hurtPercent = getHurtPercent(TargetHUD.curTarget);
                            GL11.glPushMatrix();
                            GL11.glColor4f(1.0f, 1.0f - hurtPercent, 1.0f - hurtPercent, 1.0f);
                            Gui.drawScaledCustomSizeModalRect((float)((int)x + 3), y + 3.0f, 8.0f, 8.0f, 8, 8, 30, 30, 64.0f, 64.0f);
                            GL11.glPopMatrix();
                            GlStateManager.bindTexture(0);
                        }
                        catch (final Exception ex) {}
                    }
                }
                catch (final Exception ex2) {}
                finally {
                    GlStateManager.popMatrix();
                }
            }
        }
        else if (this.targetHudMode.currentMode.equals("New")) {
            final DraggableTargetHUD dth = (DraggableTargetHUD)Main.instance.draggableHUD.getDraggableComponentByClass(DraggableTargetHUD.class);
            final float x = (float)dth.getX();
            final float y = (float)dth.getY();
            dth.setWidth(153);
            dth.setHeight(42);
            if (KillAura.target != null) {
                TargetHUD.curTarget = KillAura.target;
                this.scale = AnimationHelper.animation((float)this.scale, 1.0f, (float)(6.0 * Main.deltaTime()));
            }
            else if (TargetHUD.mc.currentScreen instanceof GuiChat) {
                TargetHUD.curTarget = TargetHUD.mc.player;
                this.scale = AnimationHelper.animation((float)this.scale, 1.0f, (float)(6.0 * Main.deltaTime()));
            }
            else {
                this.scale = AnimationHelper.animation((float)this.scale, 0.0f, (float)(6.0 * Main.deltaTime()));
            }
            final EntityLivingBase target = KillAura.target;
            if (TargetHUD.curTarget != null && TargetHUD.curTarget instanceof EntityPlayer) {
                try {
                    GlStateManager.pushMatrix();
                    GlStateManager.resetColor();
                    GL11.glTranslated((double)(x + 50.0f), (double)(y + 31.0f), 0.0);
                    GL11.glScaled(this.scale, this.scale, 0.0);
                    GL11.glTranslated((double)(-(x + 50.0f)), (double)(-(y + 31.0f)), 0.0);
                    if (this.blurThud.getBoolValue()) {
                        RenderUtils.drawBlur(7.0f, () -> RenderUtils.drawSmoothRect(x, y, x + dth.getWidth(), y + dth.getHeight(), new Color(17, 17, 17, 200).getRGB()));
                    }
                    if (this.shadowThud.getBoolValue()) {
                        RenderUtils.drawShadow(5.0f, 1.0f, () -> RenderUtils.drawSmoothRect(x, y, x + dth.getWidth(), y + dth.getHeight(), new Color(17, 17, 17, 200).getRGB()));
                    }
                    RenderUtils.drawSmoothRect(x, y, x + dth.getWidth(), y + dth.getHeight(), new Color(17, 17, 17, 200).getRGB());
                    double healthWid = TargetHUD.curTarget.getHealth() / TargetHUD.curTarget.getMaxHealth() * 110.0f;
                    healthWid = MathHelper.clamp(healthWid, 0.0, 110.0);
                    this.healthBarWidth = AnimationHelper.animation(this.healthBarWidth, (float)healthWid, (float)(10.0 * Main.deltaTime()));
                    final String health = "" + MathematicHelper.round(TargetHUD.curTarget.getHealth(), 1);
                    final String distance = "" + MathematicHelper.round(TargetHUD.mc.player.getDistanceToEntity(TargetHUD.curTarget), 1);
                    TargetHUD.mc.rubik_15.drawString("Name: " + TargetHUD.curTarget.getName(), x + 42.0f, y + 6.0f, -1);
                    TargetHUD.mc.rubik_15.drawString("Distance: " + distance, x + 42.0f, y + 15.0f, -1);
                    TargetHUD.mc.rubik_14.drawString((TargetHUD.curTarget.getHealth() >= 3.0f) ? health : "", x + 24.0f + this.healthBarWidth, y + 26.5f, new Color(200, 200, 200).getRGB());
                    RenderUtils.drawSmoothRect(x + 38.0f, y + 33.0f, x + 38.0f + this.healthBarWidth, y + 33.0f + 5.0f, ClientHelper.getTargetHudColor().darker().getRGB());
                    RenderUtils.drawBlurredShadow(x + 38.0f, y + 33.0f, this.healthBarWidth, 5.0f, 12, RenderUtils.injectAlpha(ClientHelper.getTargetHudColor(), 100));
                    TargetHUD.mc.getRenderItem().renderItemOverlays(TargetHUD.mc.rubik_13, TargetHUD.curTarget.getHeldItem(EnumHand.OFF_HAND), (int)x + 132, (int)y + 7);
                    TargetHUD.mc.getRenderItem().renderItemIntoGUI(TargetHUD.curTarget.getHeldItem(EnumHand.OFF_HAND), (int)x + 135, (int)y + 1);
                    for (final NetworkPlayerInfo targetHead2 : TargetHUD.mc.player.connection.getPlayerInfoMap()) {
                        try {
                            if (TargetHUD.mc.world.getPlayerEntityByUUID(targetHead2.getGameProfile().getId()) != TargetHUD.curTarget) {
                                continue;
                            }
                            TargetHUD.mc.getTextureManager().bindTexture(targetHead2.getLocationSkin());
                            final int scaleOffset2 = (int)(TargetHUD.curTarget.hurtTime * 0.55f);
                            final float hurtPercent2 = getHurtPercent(TargetHUD.curTarget);
                            GL11.glPushMatrix();
                            GL11.glColor4f(1.0f, 1.0f - hurtPercent2, 1.0f - hurtPercent2, 1.0f);
                            Gui.drawScaledCustomSizeModalRect((float)((int)x + 3), y + 4.0f, 8.0f, 8.0f, 8, 8, 32, 35, 64.0f, 64.0f);
                            GL11.glPopMatrix();
                            GlStateManager.bindTexture(0);
                        }
                        catch (final Exception ex3) {}
                    }
                }
                catch (final Exception ex4) {}
                finally {
                    GlStateManager.popMatrix();
                }
            }
        }
        else if (this.targetHudMode.currentMode.equalsIgnoreCase("Recode")) {
            final DraggableTargetHUD dth = (DraggableTargetHUD)Main.instance.draggableHUD.getDraggableComponentByClass(DraggableTargetHUD.class);
            final float x = (float)dth.getX();
            final float y = (float)dth.getY();
            dth.setWidth(120);
            dth.setHeight(42);
            if (KillAura.target != null) {
                TargetHUD.curTarget = KillAura.target;
                this.scale = AnimationHelper.animation((float)this.scale, 1.0f, (float)(6.0 * Main.deltaTime()));
            }
            else if (TargetHUD.mc.currentScreen instanceof GuiChat) {
                TargetHUD.curTarget = TargetHUD.mc.player;
                this.scale = AnimationHelper.animation((float)this.scale, 1.0f, (float)(6.0 * Main.deltaTime()));
            }
            else {
                this.scale = AnimationHelper.animation((float)this.scale, 0.0f, (float)(6.0 * Main.deltaTime()));
            }
            if (TargetHUD.curTarget != null && TargetHUD.curTarget instanceof EntityPlayer) {
                try {
                    GlStateManager.pushMatrix();
                    GlStateManager.resetColor();
                    GL11.glTranslated((double)(x + 50.0f), (double)(y + 31.0f), 0.0);
                    GL11.glScaled(this.scale, this.scale, 0.0);
                    GL11.glTranslated((double)(-(x + 50.0f)), (double)(-(y + 31.0f)), 0.0);
                    RenderUtils.drawSmoothRect(x, y, x + dth.getWidth(), y + dth.getHeight(), new Color(17, 17, 17, 255).getRGB());
                    if (!TargetHUD.curTarget.getHeldItem(EnumHand.MAIN_HAND).isEmpty()) {
                        RenderUtils.drawSmoothRect(x - 24.5, y + dth.getHeight() / 2 - 9.75f, x - 5.0f, y + dth.getHeight() / 2 + 9.75f, new Color(17, 17, 17, 255).getRGB());
                        RenderUtils.renderItem(TargetHUD.curTarget.getHeldItem(EnumHand.MAIN_HAND), (int)x - 24, (int)y + dth.getHeight() / 2 - 8);
                    }
                    double healthWid2 = TargetHUD.curTarget.getHealth() / TargetHUD.curTarget.getMaxHealth() * 110.0f;
                    healthWid2 = MathHelper.clamp(healthWid2, 0.0, 75.0);
                    this.healthBarWidth = AnimationHelper.animation(this.healthBarWidth, (float)healthWid2, (float)(10.0 * Main.deltaTime()));
                    final String health2 = "" + MathematicHelper.round(TargetHUD.curTarget.getHealth() * 5.0f, 1);
                    final String distance2 = "" + MathematicHelper.round(TargetHUD.mc.player.getDistanceToEntity(TargetHUD.curTarget), 1);
                    RenderUtils.drawSmoothRect(x + 38.0f, y + 16.0f, x + 38.0f + this.healthBarWidth, y + 16.0f + 8.0f, ClientHelper.getTargetHudColor().darker().getRGB());
                    TargetHUD.mc.rubik_15.drawString("" + TargetHUD.curTarget.getName(), x + 42.0f, y + 6.0f, -1);
                    TargetHUD.mc.rubik_15.drawString(health2 + "% hp", x + 42.5f, y + 18.5f, new Color(200, 200, 200).getRGB());
                    RenderUtils.renderItem(TargetHUD.curTarget.getHeldItem(EnumHand.OFF_HAND), (int)(x + 100.0f), (int)(y + 24.0f));
                    for (final NetworkPlayerInfo targetHead3 : TargetHUD.mc.player.connection.getPlayerInfoMap()) {
                        try {
                            if (TargetHUD.mc.world.getPlayerEntityByUUID(targetHead3.getGameProfile().getId()) != TargetHUD.curTarget) {
                                continue;
                            }
                            TargetHUD.mc.getTextureManager().bindTexture(targetHead3.getLocationSkin());
                            final int scaleOffset3 = (int)(TargetHUD.curTarget.hurtTime * 0.55f);
                            final float hurtPercent3 = getHurtPercent(TargetHUD.curTarget);
                            GL11.glPushMatrix();
                            GL11.glColor4f(1.0f, 1.0f - hurtPercent3, 1.0f - hurtPercent3, 1.0f);
                            Gui.drawScaledCustomSizeModalRect((float)((int)x + 3), y + 4.0f, 8.0f, 8.0f, 8, 8, 32, 35, 64.0f, 64.0f);
                            GL11.glPopMatrix();
                            GlStateManager.bindTexture(0);
                        }
                        catch (final Exception ex5) {}
                    }
                }
                catch (final Exception ex6) {}
                finally {
                    GlStateManager.popMatrix();
                }
            }
        }
        else if (this.targetHudMode.currentMode.equalsIgnoreCase("RynWare")) {
            final DraggableTargetHUD dth = (DraggableTargetHUD)Main.instance.draggableHUD.getDraggableComponentByClass(DraggableTargetHUD.class);
            final float x = (float)dth.getX();
            final float y = (float)dth.getY();
            dth.setWidth(120);
            dth.setHeight(42);
            if (KillAura.target != null) {
                TargetHUD.curTarget = KillAura.target;
                this.scale = AnimationHelper.animation((float)this.scale, 1.0f, (float)(6.0 * Main.deltaTime()));
            }
            else if (TargetHUD.mc.currentScreen instanceof GuiChat) {
                TargetHUD.curTarget = TargetHUD.mc.player;
                this.scale = AnimationHelper.animation((float)this.scale, 1.0f, (float)(6.0 * Main.deltaTime()));
            }
            else {
                this.scale = AnimationHelper.animation((float)this.scale, 0.0f, (float)(6.0 * Main.deltaTime()));
            }
            if (TargetHUD.curTarget != null && TargetHUD.curTarget instanceof EntityPlayer) {
                try {
                    GlStateManager.pushMatrix();
                    GlStateManager.resetColor();
                    GL11.glTranslated((double)(x + 50.0f), (double)(y + 31.0f), 0.0);
                    GL11.glScaled(this.scale, this.scale, 0.0);
                    GL11.glTranslated((double)(-(x + 50.0f)), (double)(-(y + 31.0f)), 0.0);
                    RoundedUtil.drawRoundOutline(x, y, (float)dth.getWidth(), (float)dth.getHeight(), 5.0f, 1.0f, new Color(17, 17, 17, 187), ClientHelper.getTargetHudColor());
                    if (!TargetHUD.curTarget.getHeldItem(EnumHand.OFF_HAND).isEmpty()) {
                        RoundedUtil.drawRoundOutline(x - 22.5f, y + dth.getHeight() / 2 - 10.75f, 20.5f, 20.5f, 10.0f, 1.0f, new Color(17, 17, 17, 187), ClientHelper.getTargetHudColor());
                        RenderUtils.renderItem(TargetHUD.curTarget.getHeldItem(EnumHand.OFF_HAND), (int)x - 21, (int)y + dth.getHeight() / 2 - 8);
                    }
                    double healthWid2 = TargetHUD.curTarget.getHealth() / TargetHUD.curTarget.getMaxHealth() * 110.0f;
                    healthWid2 = MathHelper.clamp(healthWid2, 0.0, 75.0);
                    this.healthBarWidth = AnimationHelper.animation(this.healthBarWidth, (float)healthWid2, (float)(10.0 * Main.deltaTime()));
                    final String health2 = "" + MathematicHelper.round(TargetHUD.curTarget.getHealth() * 5.0f, 1);
                    final String distance2 = "" + MathematicHelper.round(TargetHUD.mc.player.getDistanceToEntity(TargetHUD.curTarget), 1);
                    RoundedUtil.drawRound(x + 38.0f, y + 16.0f, this.healthBarWidth, 8.0f, 3.0f, ClientHelper.getTargetHudColor().darker());
                    TargetHUD.mc.mntsb_15.drawString("" + TargetHUD.curTarget.getName(), x + 42.0f, y + 6.0f, -1);
                    TargetHUD.mc.mntsb_15.drawString(health2 + "% hp", x + 42.5f, y + 18.5f, new Color(200, 200, 200).getRGB());
                    for (final NetworkPlayerInfo targetHead3 : TargetHUD.mc.player.connection.getPlayerInfoMap()) {
                        try {
                            if (TargetHUD.mc.world.getPlayerEntityByUUID(targetHead3.getGameProfile().getId()) != TargetHUD.curTarget) {
                                continue;
                            }
                            TargetHUD.mc.getTextureManager().bindTexture(targetHead3.getLocationSkin());
                            final int scaleOffset3 = (int)(TargetHUD.curTarget.hurtTime * 0.55f);
                            final float hurtPercent3 = getHurtPercent(TargetHUD.curTarget);
                            GL11.glPushMatrix();
                            GL11.glColor4f(1.0f, 1.0f - hurtPercent3, 1.0f - hurtPercent3, 1.0f);
                            Gui.drawScaledCustomSizeModalRect((float)((int)x + 3), y + 4.0f, 8.0f, 8.0f, 8, 8, 32, 35, 64.0f, 64.0f);
                            GL11.glPopMatrix();
                            GlStateManager.bindTexture(0);
                        }
                        catch (final Exception ex7) {}
                    }
                }
                catch (final Exception ex8) {}
                finally {
                    GlStateManager.popMatrix();
                }
            }
        }
    }
    
    public static float getRenderHurtTime(final EntityLivingBase hurt) {
        return hurt.hurtTime - ((hurt.hurtTime != 0) ? TargetHUD.mc.timer.renderPartialTicks : 0.0f);
    }
    
    public static float getHurtPercent(final EntityLivingBase hurt) {
        return getRenderHurtTime(hurt) / 10.0f;
    }
    
    @Override
    public void onEnable() {
        if (TargetHUD.mc.gameSettings.ofFastRender) {
            TargetHUD.mc.gameSettings.ofFastRender = false;
        }
        super.onEnable();
    }
    
    static {
        TargetHUD.curTarget = null;
        TargetHUD.thudTimer = new TimerHelper();
        TargetHUD.thudColorMode = new ListSetting("TargetHUD Color", "Astolfo", () -> true, new String[] { "Astolfo", "Rainbow", "Client", "Custom" });
        TargetHUD.targetHudColor = new ColorSetting("THUD Color", Color.PINK.getRGB(), () -> TargetHUD.thudColorMode.currentMode.equals("Custom"));
    }
}
