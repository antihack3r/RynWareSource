// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.hud;

import org.rynware.client.draggable.component.impl.DraggableUserInfo;
import org.rynware.client.event.EventTarget;
import java.util.Iterator;
import java.util.List;
import com.mojang.realmsclient.gui.ChatFormatting;
import org.rynware.client.feature.impl.misc.NameProtect;
import org.rynware.client.draggable.component.impl.DraggableSessionInfo;
import org.rynware.client.utils.math.MathematicHelper;
import org.rynware.client.feature.impl.movement.Timer;
import org.rynware.client.draggable.component.impl.DraggableTimer;
import net.minecraft.client.resources.I18n;
import org.rynware.client.feature.impl.visual.FullBright;
import org.rynware.client.utils.render.RenderUtils;
import java.util.Comparator;
import java.util.Objects;
import optifine.CustomColors;
import net.minecraft.potion.Potion;
import org.rynware.client.utils.render.ClientHelper;
import java.util.Collection;
import net.minecraft.potion.PotionEffect;
import java.util.ArrayList;
import org.rynware.client.draggable.component.impl.DraggablePotionHUD;
import org.rynware.client.draggable.component.impl.DraggableCoordsInfo;
import org.rynware.client.utils.render.RoundedUtil;
import java.awt.Color;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import org.rynware.client.utils.render.GLUtils;
import org.rynware.client.draggable.component.DraggableComponent;
import org.rynware.client.Main;
import org.rynware.client.draggable.component.impl.DraggableWaterMark;
import org.rynware.client.event.events.impl.render.EventRender2D;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.feature.Feature;

public class Hud extends Feature
{
    public static BooleanSetting waterMark;
    public static ListSetting waterMarkMode;
    public static BooleanSetting coords;
    public static BooleanSetting coordsBlur;
    public static BooleanSetting sessionInfo;
    public static BooleanSetting indicators;
    public static BooleanSetting armor;
    public static BooleanSetting sessionShadow;
    public static BooleanSetting timerIndicator;
    public static BooleanSetting potions;
    private double cooldownBarWidth;
    private double hurttimeBarWidth;
    public float scale;
    public float xpos;
    
    public Hud() {
        super("Hud", "\u0412\u0438\u0437\u0443\u0430\u043b\u044c\u043d\u0430\u044f \u0447\u0430\u0441\u0442\u044c \u043a\u043b\u0438\u0435\u043d\u0442\u0430", FeatureCategory.Hud);
        this.scale = 2.0f;
        this.xpos = (float)(Hud.sr.getScaledWidth() + Hud.mc.rubik_30.getStringWidth("THIS VIDEO WAS UPLOADED TO VK.COM/RYNWARECC"));
        this.addSettings(Hud.waterMark, Hud.waterMarkMode, Hud.sessionInfo, Hud.indicators, Hud.potions, Hud.coords, Hud.coordsBlur, Hud.armor, Hud.sessionShadow, Hud.timerIndicator);
    }
    
    @EventTarget
    public void onRender(final EventRender2D eventRender2D) {
        if (Hud.waterMark.getBoolValue()) {
            if (Hud.waterMarkMode.currentMode.equals("Simple")) {
                final DraggableWaterMark dwm = (DraggableWaterMark)Main.instance.draggableHUD.getDraggableComponentByClass(DraggableWaterMark.class);
                dwm.setWidth(180);
                dwm.setHeight(25);
                GLUtils.INSTANCE.rescale(this.scale);
                Minecraft.getMinecraft().rubik_30.drawStringWithFade("RynWare Client", dwm.getX() + 3, dwm.getY() + 3);
                Minecraft.getMinecraft().rubik_18.drawStringWithFade("Version " + Main.instance.version, dwm.getX() + 27, dwm.getY() + 19);
                GLUtils.INSTANCE.rescaleMC();
            }
            else if (!Hud.waterMarkMode.currentMode.equals("Modern")) {
                if (Hud.waterMarkMode.currentMode.equals("Modern2")) {
                    final DraggableWaterMark dwm = (DraggableWaterMark)Main.instance.draggableHUD.getDraggableComponentByClass(DraggableWaterMark.class);
                    dwm.setWidth(200);
                    dwm.setHeight(15);
                    GLUtils.INSTANCE.rescale(this.scale);
                    GlStateManager.popMatrix();
                    final String server = Hud.mc.isSingleplayer() ? "local" : ((Hud.mc.getCurrentServerData() != null) ? Hud.mc.getCurrentServerData().serverIP.toLowerCase() : "null");
                    final String scam = "rynware " + TextFormatting.GRAY + " | " + TextFormatting.RESET + Hud.mc.getSession().getUsername() + TextFormatting.GRAY + " | " + TextFormatting.RESET + server;
                    final String text = TextFormatting.GRAY + " | " + TextFormatting.RESET + Hud.mc.getSession().getUsername() + TextFormatting.GRAY + " | " + TextFormatting.RESET + server;
                    RoundedUtil.drawGradientRound((float)(dwm.getX() + 5), (float)(dwm.getY() + 4), (float)(Minecraft.getMinecraft().rubik_17.getStringWidth(scam) + 9), 15.5f, 3.0f, new Color(2, 1, 1, 255), new Color(2, 1, 1, 255), new Color(2, 1, 1, 255), new Color(2, 1, 1, 255));
                    Minecraft.getMinecraft().rubik_18.drawStringWithShadow("rynware", dwm.getX() + 7, dwm.getY() + 9.0f, -1);
                    Minecraft.getMinecraft().rubik_18.drawStringWithShadow(text, dwm.getX() + 7 + Minecraft.getMinecraft().rubik_17.getStringWidth("rynware "), dwm.getY() + 9, -1);
                    GlStateManager.pushMatrix();
                    GLUtils.INSTANCE.rescaleMC();
                }
                else if (Hud.waterMarkMode.currentMode.equalsIgnoreCase("Xvideos")) {
                    final String text2 = "THIS VIDEO WAS UPLOADED TO VK.COM/RYNWARECC";
                    this.xpos -= (float)1.2;
                    if (this.xpos < -Hud.mc.rubik_30.getStringWidth(text2)) {
                        this.xpos = (float)(Hud.sr.getScaledWidth() + Hud.mc.rubik_30.getStringWidth(text2));
                    }
                    Hud.mc.rubik_30.drawString(text2, this.xpos + 2.0f, Hud.sr.getScaledHeight() / 2 - 103.0f, new Color(2, 2, 2, 87).getRGB());
                    Hud.mc.rubik_30.drawString(text2, this.xpos, Hud.sr.getScaledHeight() / 2 - 105.0f, new Color(240, 248, 240, 87).getRGB());
                }
            }
        }
        if (Hud.coords.getBoolValue()) {
            final DraggableCoordsInfo dci = (DraggableCoordsInfo)Main.instance.draggableHUD.getDraggableComponentByClass(DraggableCoordsInfo.class);
            dci.setWidth(90);
            dci.setHeight(25);
            GLUtils.INSTANCE.rescale(this.scale);
            final String xCoord = "" + Math.round(Hud.mc.player.posX);
            final String yCoord = "" + Math.round(Hud.mc.player.posY);
            final String zCoord = "" + Math.round(Hud.mc.player.posZ);
            final String fps = "" + Math.round((float)Minecraft.getDebugFPS());
            try {
                Hud.mc.rubik_16.drawStringWithShadow("BPS:", this.getX(), this.getY() - 10.0f, new Color(148, 124, 124).getRGB());
                Hud.mc.rubik_16.drawStringWithShadow("" + this.calculateBPS(), this.getX() + 21.0f, this.getY() - 10.0f, -1);
                Hud.mc.rubik_16.drawStringWithShadow("X: ", this.getX(), this.getY(), new Color(148, 124, 124).getRGB());
                Hud.mc.rubik_16.drawStringWithShadow(xCoord, this.getX() + 8.0f, this.getY(), -1);
                Hud.mc.rubik_16.drawStringWithShadow("Y: ", this.getX() + 26.0f + Hud.mc.rubik_18.getStringWidth(xCoord) - 17.0f, this.getY(), new Color(148, 124, 124).getRGB());
                Hud.mc.rubik_16.drawStringWithShadow(yCoord, this.getX() + 36.0f + Hud.mc.rubik_18.getStringWidth(xCoord) - 17.0f, this.getY(), -1);
                Hud.mc.rubik_16.drawStringWithShadow("Z: ", this.getX() + 62.0f + Hud.mc.rubik_18.getStringWidth(xCoord) - 23.0f + Hud.mc.rubik_18.getStringWidth(yCoord) - 17.0f, this.getY(), new Color(148, 124, 124).getRGB());
                Hud.mc.rubik_16.drawStringWithShadow(zCoord, this.getX() + 72.0f + Hud.mc.rubik_18.getStringWidth(xCoord) - 23.0f + Hud.mc.rubik_18.getStringWidth(yCoord) - 17.0f, this.getY(), -1);
                GLUtils.INSTANCE.rescaleMC();
            }
            catch (final Exception ex) {}
        }
        if (Hud.potions.getBoolValue()) {
            final Collection<PotionEffect> collection = Hud.mc.player.getActivePotionEffects();
            final DraggablePotionHUD dph = (DraggablePotionHUD)Main.instance.draggableHUD.getDraggableComponentByClass(DraggablePotionHUD.class);
            dph.setWidth(100);
            dph.setHeight(56);
            GLUtils.INSTANCE.rescale(this.scale);
            final int xOff = 21;
            final int yOff = 9;
            int counter = 10;
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.disableLighting();
            final int listOffset = 10;
            final List<PotionEffect> potions = new ArrayList<PotionEffect>(Hud.mc.player.getActivePotionEffects());
            potions.sort(Comparator.comparingDouble(potionEffect -> ClientHelper.getFontRender().getStringWidth(Objects.requireNonNull(Potion.getPotionById(CustomColors.getPotionId(potionEffect.getEffectName()))).getName())));
            RoundedUtil.drawRoundOutline((float)dph.getX(), (float)dph.getY(), Math.max(100.0f, (float)(Hud.mc.mntsb_18.getStringWidth(potions.get(Minecraft.getMinecraft().player.getActivePotionEffects().size() - 1).getEffectName()) + 6)), (float)(7 + Minecraft.getMinecraft().player.getActivePotionEffects().size() * 12), 5.0f, 1.0f, new Color(1, 1, 1, 187), ClientHelper.getClientColor());
            RenderUtils.drawRect(dph.getX() + 6, dph.getY() + 15, dph.getX() + Math.max(94.0f, (float)(Hud.mc.mntsb_18.getStringWidth(potions.get(Minecraft.getMinecraft().player.getActivePotionEffects().size() - 1).getEffectName()) + 2)), dph.getY() + 16, RenderUtils.injectAlpha(Color.GRAY, 105).getRGB());
            Hud.mc.mntsb_18.drawCenteredString("Potions", dph.getX() + Math.max(100.0f, (float)(Hud.mc.mntsb_18.getStringWidth(potions.get(Minecraft.getMinecraft().player.getActivePotionEffects().size() - 1).getEffectName()) + 2)) / 2.0f, dph.getY() + 5.0f, -1);
            for (final PotionEffect potion : potions) {
                final Potion effect = Potion.getPotionById(CustomColors.getPotionId(potion.getEffectName()));
                assert effect != null;
                if (effect == Potion.getPotionById(16) && FullBright.brightMode.currentMode.equalsIgnoreCase("Potion") && Main.instance.featureManager.getFeature(FullBright.class).isEnabled()) {
                    continue;
                }
                final String durationString = Potion.getDurationString(potion);
                String level = I18n.format(potion.getEffectName(), new Object[0]);
                if (potion.getAmplifier() == 1) {
                    level = level + " " + I18n.format("enchantment.level.2", new Object[0]);
                }
                else if (potion.getAmplifier() == 2) {
                    level = level + " " + I18n.format("enchantment.level.3", new Object[0]);
                }
                else if (potion.getAmplifier() == 3) {
                    level = level + " " + I18n.format("enchantment.level.4", new Object[0]);
                }
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                int getPotionColor = -1;
                if (potion.getDuration() < 200) {
                    getPotionColor = new Color(255, 77, 120).getRGB();
                }
                else if (potion.getDuration() < 400) {
                    getPotionColor = new Color(252, 228, 110).getRGB();
                }
                else if (potion.getDuration() > 400) {
                    getPotionColor = new Color(118, 248, 118).getRGB();
                }
                Hud.mc.mntsb_16.drawStringWithShadow(level, dph.getX() + 6.5f, dph.getY() + counter + yOff, -1);
                Hud.mc.mntsb_16.drawStringWithShadow(durationString, dph.getX() + Hud.mc.mntsb_16.getStringWidth(level) + 8.5f, dph.getY() + counter + yOff, getPotionColor);
                counter += listOffset;
            }
            GLUtils.INSTANCE.rescaleMC();
        }
        if (Hud.timerIndicator.getBoolValue()) {
            final DraggableTimer dt = (DraggableTimer)Main.instance.draggableHUD.getDraggableComponentByClass(DraggableTimer.class);
            dt.setWidth(150);
            dt.setHeight(25);
            RenderUtils.drawBlurredShadow((float)(dt.getX() - 52), (float)(dt.getY() - 14), 105.0f, 23.0f, 8, new Color(17, 17, 17, 170));
            Hud.mc.sfui18.drawCenteredString(MathematicHelper.round(100.0f - Timer.ticks * 2.0f, 1) + "%", (float)dt.getX(), (float)(dt.getY() - 10), -1);
            RenderUtils.drawRect2(dt.getX() - 50, dt.getY(), 100.0f - Timer.ticks * 2.0f, 5.0, ClientHelper.getClientColor().getRGB());
        }
        if (Hud.sessionInfo.getBoolValue()) {
            final DraggableSessionInfo dsi = (DraggableSessionInfo)Main.instance.draggableHUD.getDraggableComponentByClass(DraggableSessionInfo.class);
            dsi.setWidth(155);
            dsi.setHeight(56);
            GLUtils.INSTANCE.rescale(this.scale);
            final String server = Hud.mc.isSingleplayer() ? "local" : ((Hud.mc.getCurrentServerData() != null) ? Hud.mc.getCurrentServerData().serverIP.toLowerCase() : "null");
            final String name = (Main.instance.featureManager.getFeature(NameProtect.class).isEnabled() && NameProtect.myName.getBoolValue()) ? "RynWare" : Hud.mc.player.getName();
            String time;
            if (Minecraft.getMinecraft().isSingleplayer()) {
                time = "SinglePlayer";
            }
            else {
                final long durationInMillis = System.currentTimeMillis() - Main.playTimeStart;
                final long second = durationInMillis / 1000L % 60L;
                final long minute = durationInMillis / 60000L % 60L;
                final long hour = durationInMillis / 3600000L % 24L;
                time = String.format("%02dh %02dm %02ds", hour, minute, second);
            }
            RoundedUtil.drawRoundOutline((float)dsi.getX(), (float)(dsi.getY() + 3), 155.0f, 56.0f, 5.0f, 1.0f, new Color(20, 20, 20, 187), ClientHelper.getClientColor());
            RenderUtils.drawRect(dsi.getX() + 2.8f, dsi.getY() + 19, dsi.getX() + 152, dsi.getY() + 20.0f, new Color(65, 65, 65).getRGB());
            Hud.mc.mntsb_18.drawString("Statistics", dsi.getX() + 55.5f, dsi.getY() + 9.3f, -1);
            Hud.mc.mntsb_18.drawString(ChatFormatting.LIGHT_PURPLE + "Server: " + ChatFormatting.RESET + server, (float)(dsi.getX() + 5), (float)(dsi.getY() + 25), -1);
            Hud.mc.mntsb_17.drawString(ChatFormatting.LIGHT_PURPLE + "Name: " + ChatFormatting.RESET + name, (float)(dsi.getX() + 5), dsi.getY() + 34.5f, -1);
            Hud.mc.mntsb_17.drawString(ChatFormatting.LIGHT_PURPLE + "InGame Time: " + ChatFormatting.RESET + time, (float)(dsi.getX() + 5), (float)(dsi.getY() + 44), -1);
            GLUtils.INSTANCE.rescaleMC();
        }
    }
    
    private double calculateBPS() {
        final double bps = Math.hypot(Hud.mc.player.posX - Hud.mc.player.prevPosX, Hud.mc.player.posZ - Hud.mc.player.prevPosZ) * Hud.mc.timer.timerSpeed * 20.0;
        return Math.round(bps * 100.0) / 100.0;
    }
    
    public float getX2() {
        final DraggableUserInfo dci = (DraggableUserInfo)Main.instance.draggableHUD.getDraggableComponentByClass(DraggableUserInfo.class);
        return (float)dci.getX();
    }
    
    public float getY2() {
        final DraggableUserInfo dci = (DraggableUserInfo)Main.instance.draggableHUD.getDraggableComponentByClass(DraggableUserInfo.class);
        return (float)(dci.getY() + 10);
    }
    
    public float getX() {
        final DraggableCoordsInfo dci = (DraggableCoordsInfo)Main.instance.draggableHUD.getDraggableComponentByClass(DraggableCoordsInfo.class);
        return (float)dci.getX();
    }
    
    public float getY() {
        final DraggableCoordsInfo dci = (DraggableCoordsInfo)Main.instance.draggableHUD.getDraggableComponentByClass(DraggableCoordsInfo.class);
        return (float)(dci.getY() + 15);
    }
    
    private int getYOff(final int rowCount, final int scaledHeight, final int height) {
        return scaledHeight / 2 - rowCount * height / 2;
    }
    
    @Override
    public void onEnable() {
        super.onEnable();
    }
    
    static {
        Hud.waterMark = new BooleanSetting("WaterMark", true, () -> true);
        Hud.waterMarkMode = new ListSetting("WaterMark Mode", "Modern", () -> Hud.waterMark.getBoolValue(), new String[] { "Simple", "Modern", "Modern2", "Xvideos" });
        Hud.coords = new BooleanSetting("Coordinates", true, () -> true);
        Hud.coordsBlur = new BooleanSetting("CoordsBlur", true, () -> Hud.coords.getBoolValue());
        Hud.sessionInfo = new BooleanSetting("Statistics", true, () -> true);
        Hud.indicators = new BooleanSetting("Indicators", true, () -> true);
        Hud.armor = new BooleanSetting("Armor Status", true, () -> true);
        Hud.sessionShadow = new BooleanSetting("Statistics Shadow", false, () -> Hud.sessionInfo.getBoolValue());
        Hud.timerIndicator = new BooleanSetting("Timer Indicator", false, () -> true);
        Hud.potions = new BooleanSetting("Potion Info", true, () -> true);
    }
}
