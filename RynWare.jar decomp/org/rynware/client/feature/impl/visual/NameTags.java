// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.visual;

import java.util.HashMap;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.PotionEffect;
import java.util.Iterator;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemArmor;
import net.minecraft.enchantment.EnchantmentHelper;
import java.util.Objects;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import net.minecraft.util.EnumHand;
import java.awt.Color;
import org.rynware.client.utils.render.RenderUtils;
import org.rynware.client.utils.render.ColorUtils;
import org.rynware.client.utils.math.MathematicHelper;
import com.mojang.realmsclient.gui.ChatFormatting;
import org.rynware.client.feature.impl.misc.NameProtect;
import org.rynware.client.Main;
import net.minecraft.entity.player.EntityPlayer;
import org.rynware.client.feature.impl.combat.AntiBot;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.ScaledResolution;
import org.rynware.client.event.events.impl.render.EventRender2D;
import org.rynware.client.event.EventTarget;
import org.rynware.client.event.events.impl.render.EventRender3D;
import net.minecraft.util.text.TextFormatting;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import net.minecraft.entity.EntityLivingBase;
import java.util.Map;
import org.rynware.client.feature.Feature;

public class NameTags extends Feature
{
    public static Map<EntityLivingBase, double[]> entityPositions;
    public BooleanSetting armor;
    public BooleanSetting potion;
    public BooleanSetting glowNameTags;
    public BooleanSetting backGround;
    public BooleanSetting offHand;
    public ListSetting backGroundMode;
    public NumberSetting opacity;
    public NumberSetting size;
    
    public NameTags() {
        super("NameTags", FeatureCategory.Visuals);
        this.armor = new BooleanSetting("Show Armor", true, () -> true);
        this.potion = new BooleanSetting("Show Potions", true, () -> true);
        this.glowNameTags = new BooleanSetting("Glow Potions", true, () -> true);
        this.backGround = new BooleanSetting("NameTags Background", true, () -> true);
        this.offHand = new BooleanSetting("OffHand Render", true, () -> true);
        this.backGroundMode = new ListSetting("Background Mode", "Default", () -> this.backGround.getBoolValue(), new String[] { "Default", "Shader" });
        this.opacity = new NumberSetting("Background Opacity", 150.0f, 0.0f, 255.0f, 5.0f, () -> this.backGround.getBoolValue());
        this.size = new NumberSetting("NameTags Size", 1.0f, 0.5f, 2.0f, 0.1f, () -> true);
        this.addSettings(this.size, this.backGround, this.backGroundMode, this.opacity, this.offHand, this.armor, this.potion, this.glowNameTags);
    }
    
    public static TextFormatting getHealthColor(final float health) {
        if (health <= 4.0f) {
            return TextFormatting.RED;
        }
        if (health <= 8.0f) {
            return TextFormatting.GOLD;
        }
        if (health <= 12.0f) {
            return TextFormatting.YELLOW;
        }
        if (health <= 16.0f) {
            return TextFormatting.DARK_GREEN;
        }
        return TextFormatting.GREEN;
    }
    
    @EventTarget
    public void onRender3D(final EventRender3D event) {
        this.updatePositions();
    }
    
    @EventTarget
    public void onRender2D(final EventRender2D event) {
        final ScaledResolution sr = new ScaledResolution(NameTags.mc);
        GlStateManager.pushMatrix();
        for (final EntityLivingBase entity : NameTags.entityPositions.keySet()) {
            if (entity != null && entity != NameTags.mc.player) {
                if (AntiBot.isBotPlayer.contains(entity)) {
                    continue;
                }
                GlStateManager.pushMatrix();
                if (entity instanceof EntityPlayer) {
                    final double[] array = NameTags.entityPositions.get(entity);
                    if (array[3] < 0.0 || array[3] >= 1.0) {
                        GlStateManager.popMatrix();
                        continue;
                    }
                    final double scaleFactor = sr.getScaleFactor();
                    GlStateManager.translate(array[0] / scaleFactor, array[1] / scaleFactor, 0.0);
                    final String stringName = (Main.instance.featureManager.getFeature(NameProtect.class).isEnabled() && NameProtect.otherName.getBoolValue()) ? "Protected" : (Main.instance.friendManager.isFriend(entity.getName()) ? ((Main.instance.featureManager.getFeature(NameProtect.class).isEnabled() && NameProtect.friends.getBoolValue()) ? (TextFormatting.GREEN + "FProtected") : (ChatFormatting.GREEN + "[F] " + ChatFormatting.RESET + entity.getDisplayName().getUnformattedText())) : entity.getDisplayName().getUnformattedText());
                    final String stringHP = MathematicHelper.round(entity.getHealth(), 1) + " ";
                    final String stringHP2 = "" + stringHP;
                    final float width = (float)(NameTags.mc.rubik_18.getStringWidth(stringHP2 + " " + stringName) + 5);
                    GlStateManager.translate(0.0, -10.0, 0.0);
                    if (this.backGround.getBoolValue() && this.backGroundMode.currentMode.equals("Default")) {
                        RenderUtils.drawRect(-width / 2.0f, -10.0, width / 2.0f, 2.0, ColorUtils.getColor(0, (int)this.opacity.getNumberValue()));
                    }
                    else if (this.backGroundMode.currentMode.equals("Shader")) {
                        RenderUtils.drawBlurredShadow(-width / 2.0f - 2.0f, -10.0f, width + 3.0f, 11.0f, 20, new Color(0, 0, 0, (int)this.opacity.getNumberValue()));
                    }
                    NameTags.mc.rubik_18.drawStringWithShadow(stringName + " " + getHealthColor(entity.getHealth()) + stringHP2, -width / 2.0f + 2.0f, -7.5, -1);
                    final ItemStack heldItemStack = entity.getHeldItem(EnumHand.OFF_HAND);
                    final ArrayList<ItemStack> list = new ArrayList<ItemStack>();
                    for (int i = 0; i < 5; ++i) {
                        final ItemStack getEquipmentInSlot = ((EntityPlayer)entity).getEquipmentInSlot(i);
                        list.add(getEquipmentInSlot);
                    }
                    int n10 = -(list.size() * 9) - 8;
                    if (this.armor.getBoolValue()) {
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(0.0f, -2.0f, 0.0f);
                        NameTags.mc.getRenderItem().renderItemIntoGUI(heldItemStack, n10 + 86, -28);
                        NameTags.mc.getRenderItem().renderItemOverlays(NameTags.mc.rubik_18, heldItemStack, n10 + 86, -28);
                        for (final ItemStack itemStack : list) {
                            RenderHelper.enableGUIStandardItemLighting();
                            NameTags.mc.getRenderItem().renderItemIntoGUI(itemStack, n10 + 6, -28);
                            NameTags.mc.getRenderItem().renderItemOverlays(NameTags.mc.rubik_18, itemStack, n10 + 5, -28);
                            n10 += 3;
                            RenderHelper.disableStandardItemLighting();
                            int n11 = 7;
                            final int getEnchantmentLevel = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(16)), itemStack);
                            final int getEnchantmentLevel2 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(20)), itemStack);
                            final int getEnchantmentLevel3 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(19)), itemStack);
                            if (getEnchantmentLevel > 0) {
                                this.drawEnchantTag("S" + this.getColor(getEnchantmentLevel) + getEnchantmentLevel, n10, n11);
                                n11 += 8;
                            }
                            if (getEnchantmentLevel2 > 0) {
                                this.drawEnchantTag("F" + this.getColor(getEnchantmentLevel2) + getEnchantmentLevel2, n10, n11);
                                n11 += 8;
                            }
                            if (getEnchantmentLevel3 > 0) {
                                this.drawEnchantTag("Kb" + this.getColor(getEnchantmentLevel3) + getEnchantmentLevel3, n10, n11);
                            }
                            else if (itemStack.getItem() instanceof ItemArmor) {
                                final int getEnchantmentLevel4 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(0)), itemStack);
                                final int getEnchantmentLevel5 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(7)), itemStack);
                                final int getEnchantmentLevel6 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(34)), itemStack);
                                if (getEnchantmentLevel4 > 0) {
                                    this.drawEnchantTag("P" + this.getColor(getEnchantmentLevel4) + getEnchantmentLevel4, n10, n11);
                                    n11 += 8;
                                }
                                if (getEnchantmentLevel5 > 0) {
                                    this.drawEnchantTag("Th" + this.getColor(getEnchantmentLevel5) + getEnchantmentLevel5, n10, n11);
                                    n11 += 8;
                                }
                                if (getEnchantmentLevel6 > 0) {
                                    this.drawEnchantTag("U" + this.getColor(getEnchantmentLevel6) + getEnchantmentLevel6, n10, n11);
                                }
                            }
                            else if (itemStack.getItem() instanceof ItemBow) {
                                final int getEnchantmentLevel7 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(48)), itemStack);
                                final int getEnchantmentLevel8 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(49)), itemStack);
                                final int getEnchantmentLevel9 = EnchantmentHelper.getEnchantmentLevel(Objects.requireNonNull(Enchantment.getEnchantmentByID(50)), itemStack);
                                if (getEnchantmentLevel7 > 0) {
                                    this.drawEnchantTag("P" + this.getColor(getEnchantmentLevel7) + getEnchantmentLevel7, n10, n11);
                                    n11 += 8;
                                }
                                if (getEnchantmentLevel8 > 0) {
                                    this.drawEnchantTag("P" + this.getColor(getEnchantmentLevel8) + getEnchantmentLevel8, n10, n11);
                                    n11 += 8;
                                }
                                if (getEnchantmentLevel9 > 0) {
                                    this.drawEnchantTag("F" + this.getColor(getEnchantmentLevel9) + getEnchantmentLevel9, n10, n11);
                                }
                            }
                            n10 += (int)13.5;
                        }
                        GlStateManager.popMatrix();
                    }
                    if (this.potion.getBoolValue()) {
                        this.drawPotionEffect((EntityPlayer)entity);
                    }
                }
                GlStateManager.popMatrix();
            }
        }
        GlStateManager.popMatrix();
        GlStateManager.enableBlend();
    }
    
    private void drawPotionEffect(final EntityPlayer entity) {
        final int tagWidth = 0;
        int stringY = -25;
        if (entity.getTotalArmorValue() > 0 || (!entity.getHeldItemMainhand().func_190926_b() && !entity.getHeldItemOffhand().func_190926_b())) {
            stringY -= 37;
        }
        for (final PotionEffect potionEffect : entity.getActivePotionEffects()) {
            final Potion potion = potionEffect.getPotion();
            final boolean potRanOut = potionEffect.getDuration() != 0.0;
            String power = "";
            if (entity.isPotionActive(potion)) {
                if (!potRanOut) {
                    continue;
                }
                if (potionEffect.getAmplifier() == 1) {
                    power = "II";
                }
                else if (potionEffect.getAmplifier() == 2) {
                    power = "III";
                }
                else if (potionEffect.getAmplifier() == 3) {
                    power = "IV";
                }
                GlStateManager.pushMatrix();
                GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
                final float xValue = tagWidth - NameTags.mc.rubik_18.getStringWidth(I18n.format(potion.getName(), new Object[0]) + " " + power + TextFormatting.GRAY + " " + Potion.getDurationString(potionEffect)) / 2.0f;
                if (this.glowNameTags.getBoolValue()) {
                    NameTags.mc.rubik_18.drawBlurredStringWithShadow(I18n.format(potion.getName(), new Object[0]) + " " + power + TextFormatting.GRAY + " " + Potion.getDurationString(potionEffect), xValue, stringY, 20, new Color(255, 255, 255, 60), -1);
                }
                else {
                    NameTags.mc.rubik_18.drawStringWithShadow(I18n.format(potion.getName(), new Object[0]) + " " + power + TextFormatting.GRAY + " " + Potion.getDurationString(potionEffect), xValue, stringY, -1);
                }
                stringY -= 10;
                GlStateManager.popMatrix();
            }
        }
    }
    
    private void drawEnchantTag(final String text, final int n, int n2) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        n2 -= 7;
        NameTags.mc.rubik_18.drawStringWithShadow(text, n + 6, -35 - n2, -1);
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }
    
    private String getColor(final int n) {
        if (n != 1) {
            if (n == 2) {
                return "";
            }
            if (n == 3) {
                return "";
            }
            if (n == 4) {
                return "";
            }
            if (n >= 5) {
                return "";
            }
        }
        return "";
    }
    
    private void updatePositions() {
        NameTags.entityPositions.clear();
        final float pTicks = NameTags.mc.timer.renderPartialTicks;
        for (final Entity o : NameTags.mc.world.loadedEntityList) {
            if (o != NameTags.mc.player) {
                if (!(o instanceof EntityPlayer)) {
                    continue;
                }
                final double x = o.lastTickPosX + (o.posX - o.lastTickPosX) * pTicks - NameTags.mc.getRenderManager().viewerPosX;
                double y = o.lastTickPosY + (o.posY - o.lastTickPosY) * pTicks - NameTags.mc.getRenderManager().viewerPosY;
                final double z = o.lastTickPosZ + (o.posZ - o.lastTickPosZ) * pTicks - NameTags.mc.getRenderManager().viewerPosZ;
                if (Objects.requireNonNull(this.convertTo2D(x, y += o.height + 0.2, z))[2] < 0.0) {
                    continue;
                }
                if (Objects.requireNonNull(this.convertTo2D(x, y, z))[2] >= 2.0) {
                    continue;
                }
                NameTags.entityPositions.put((EntityPlayer)o, new double[] { Objects.requireNonNull(this.convertTo2D(x, y, z))[0], Objects.requireNonNull(this.convertTo2D(x, y, z))[1], Math.abs(Objects.requireNonNull(this.convertTo2D(x, y + 1.0, z))[1] - Objects.requireNonNull(this.convertTo2D(x, y, z))[1]), Objects.requireNonNull(this.convertTo2D(x, y, z))[2] });
            }
        }
    }
    
    private double[] convertTo2D(final double x, final double y, final double z) {
        final FloatBuffer screenCords = BufferUtils.createFloatBuffer(3);
        final IntBuffer viewport = BufferUtils.createIntBuffer(16);
        final FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
        final FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        final boolean result = GLU.gluProject((float)x, (float)y, (float)z, modelView, projection, viewport, screenCords);
        if (result) {
            return new double[] { screenCords.get(0), Display.getHeight() - screenCords.get(1), screenCords.get(2) };
        }
        return null;
    }
    
    private void scale() {
        final float n = NameTags.mc.gameSettings.smoothCamera ? 2.0f : this.size.getNumberValue();
        GlStateManager.scale(n, n, n);
    }
    
    static {
        NameTags.entityPositions = new HashMap<EntityLivingBase, double[]>();
    }
}
