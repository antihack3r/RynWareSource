// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.visual;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityExpBottle;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.passive.EntityAnimal;
import org.rynware.client.utils.math.MathematicHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.entity.EntityLivingBase;
import javax.vecmath.Vector4d;
import javax.vecmath.Vector3d;
import net.minecraft.util.math.AxisAlignedBB;
import org.rynware.client.Main;
import org.rynware.client.event.events.impl.render.EventRender2D;
import org.rynware.client.event.EventTarget;
import java.util.Iterator;
import org.rynware.client.utils.render.RenderUtils;
import net.minecraft.entity.player.EntityPlayer;
import org.rynware.client.utils.render.ClientHelper;
import org.rynware.client.event.events.impl.render.EventRender3D;
import org.rynware.client.ui.font.MCFontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import org.rynware.client.ui.settings.Setting;
import java.awt.Color;
import org.rynware.client.feature.impl.FeatureCategory;
import net.minecraft.entity.Entity;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.settings.impl.ColorSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.feature.Feature;

public class EntityESP extends Feature
{
    public static ListSetting espMode;
    private final int black;
    public static ColorSetting colorEsp;
    private final BooleanSetting friendHighlight;
    private final BooleanSetting fullBox;
    private final BooleanSetting armor;
    private final BooleanSetting border;
    public static BooleanSetting healRect;
    public BooleanSetting hpDetails;
    public static ListSetting healcolorMode;
    public static final ColorSetting healColor;
    public ListSetting csgoMode;
    public static ListSetting colorMode;
    public BooleanSetting mobESP;
    public static Entity clipEntity;
    
    public EntityESP() {
        super("EntityESP", FeatureCategory.Visuals);
        this.black = Color.BLACK.getRGB();
        this.hpDetails = new BooleanSetting("Health Details", true, () -> EntityESP.espMode.currentMode.equals("2D"));
        EntityESP.espMode = new ListSetting("ESP Mode", "2D", () -> true, new String[] { "2D", "Box" });
        this.csgoMode = new ListSetting("Border Mode", "Box", () -> EntityESP.espMode.currentMode.equals("2D"), new String[] { "Box", "Corner" });
        this.border = new BooleanSetting("Border Rect", true, () -> EntityESP.espMode.currentMode.equals("2D"));
        this.armor = new BooleanSetting("Render Armor", true, () -> EntityESP.espMode.currentMode.equals("2D"));
        EntityESP.colorEsp = new ColorSetting("ESP Color", new Color(16777215).getRGB(), () -> !EntityESP.colorMode.currentMode.equals("Client") && (EntityESP.espMode.currentMode.equals("2D") || EntityESP.espMode.currentMode.equals("Box")));
        this.friendHighlight = new BooleanSetting("Friend Highlight", false, () -> true);
        this.fullBox = new BooleanSetting("Full Box", false, () -> EntityESP.espMode.currentMode.equals("Box"));
        this.mobESP = new BooleanSetting("Mob ESP", false, () -> true);
        this.addSettings(EntityESP.espMode, this.csgoMode, EntityESP.colorMode, EntityESP.healcolorMode, EntityESP.healColor, this.border, this.fullBox, EntityESP.healRect, this.hpDetails, this.armor, EntityESP.colorEsp, this.friendHighlight, this.mobESP);
    }
    
    private void drawScaledString(final String text, final double x, final double y, final double scale, final int color) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, x);
        GlStateManager.scale(scale, scale, scale);
        MCFontRenderer.drawStringWithOutline(EntityESP.mc.fontRendererObj, text, 0.0f, 0.0f, color);
        GlStateManager.popMatrix();
    }
    
    @EventTarget
    public void onRender3D(final EventRender3D event3D) {
        if (!this.isEnabled()) {
            return;
        }
        final Color color = ClientHelper.getESPColor();
        if (EntityESP.espMode.currentMode.equals("Box")) {
            GlStateManager.pushMatrix();
            for (final Entity entity : EntityESP.mc.world.loadedEntityList) {
                if (entity instanceof EntityPlayer) {
                    if (entity == EntityESP.mc.player && EntityESP.mc.gameSettings.thirdPersonView == 0) {
                        continue;
                    }
                    RenderUtils.drawEntityBox(entity, color, this.fullBox.getBoolValue(), this.fullBox.getBoolValue() ? 0.15f : 0.9f);
                }
            }
            GlStateManager.popMatrix();
        }
    }
    
    @EventTarget
    public void onRender2D(final EventRender2D event) {
        final float partialTicks = EntityESP.mc.timer.renderPartialTicks;
        final int scaleFactor = event.getResolution().getScaleFactor();
        final int black = this.black;
        for (final Entity entity : EntityESP.mc.world.loadedEntityList) {
            if (entity == null) {
                continue;
            }
            int color = ClientHelper.getESPColor().getRGB();
            if (Main.instance.friendManager.isFriend(entity.getName()) && this.friendHighlight.getBoolValue()) {
                color = Color.GREEN.getRGB();
            }
            if (!this.isValid(entity)) {
                continue;
            }
            if (!RenderUtils.isInViewFrustum(entity)) {
                continue;
            }
            final double x = RenderUtils.interpolate(entity.posX, entity.lastTickPosX, partialTicks);
            final double y = RenderUtils.interpolate(entity.posY, entity.lastTickPosY, partialTicks);
            final double z = RenderUtils.interpolate(entity.posZ, entity.lastTickPosZ, partialTicks);
            final double width = entity.width / 1.5;
            final double height = entity.height + ((entity.isSneaking() || (entity == EntityESP.mc.player && EntityESP.mc.player.isSneaking())) ? -0.3 : 0.2);
            final AxisAlignedBB axisAlignedBB = new AxisAlignedBB(x - width, y, z - width, x + width, y + height, z + width);
            final Vector3d[] vectors = { new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.minZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.minX, axisAlignedBB.maxY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.minY, axisAlignedBB.maxZ), new Vector3d(axisAlignedBB.maxX, axisAlignedBB.maxY, axisAlignedBB.maxZ) };
            EntityESP.mc.entityRenderer.setupCameraTransform(partialTicks, 0);
            Vector4d position = null;
            for (Vector3d vector : vectors) {
                vector = this.project2D(scaleFactor, vector.x - EntityESP.mc.getRenderManager().renderPosX, vector.y - EntityESP.mc.getRenderManager().renderPosY, vector.z - EntityESP.mc.getRenderManager().renderPosZ);
                if (vector != null && vector.z > 0.0) {
                    if (vector.z < 1.0) {
                        if (position == null) {
                            position = new Vector4d(vector.x, vector.y, vector.z, 0.0);
                        }
                        position.x = Math.min(vector.x, position.x);
                        position.y = Math.min(vector.y, position.y);
                        position.z = Math.max(vector.x, position.z);
                        position.w = Math.max(vector.y, position.w);
                    }
                }
            }
            if (position == null) {
                continue;
            }
            EntityESP.mc.entityRenderer.setupOverlayRendering();
            final double posX = position.x;
            final double posY = position.y;
            final double endPosX = position.z;
            final double endPosY = position.w;
            final double finalValue = 0.5;
            if (EntityESP.espMode.currentMode.equalsIgnoreCase("2D") && this.csgoMode.currentMode.equalsIgnoreCase("Box") && this.border.getBoolValue()) {
                RenderUtils.drawRect(posX - 1.0, posY, posX + finalValue, endPosY + finalValue, black);
                RenderUtils.drawRect(posX - 1.0, posY - finalValue, endPosX + finalValue, posY + finalValue + finalValue, black);
                RenderUtils.drawRect(endPosX - finalValue - finalValue, posY, endPosX + finalValue, endPosY + finalValue, black);
                RenderUtils.drawRect(posX - 1.0, endPosY - finalValue - finalValue, endPosX + finalValue, endPosY + finalValue, black);
                RenderUtils.drawRect(posX - finalValue, posY, posX + finalValue - finalValue, endPosY, color);
                RenderUtils.drawRect(posX, endPosY - finalValue, endPosX, endPosY, color);
                RenderUtils.drawRect(posX - finalValue, posY, endPosX, posY + finalValue, color);
                RenderUtils.drawRect(endPosX - finalValue, posY, endPosX, endPosY, color);
            }
            else if (EntityESP.espMode.currentMode.equalsIgnoreCase("2D") && this.csgoMode.currentMode.equalsIgnoreCase("Corner") && this.border.getBoolValue()) {
                RenderUtils.drawRect(posX + finalValue, posY, posX - 1.0, posY + (endPosY - posY) / 4.0 + finalValue, black);
                RenderUtils.drawRect(posX - 1.0, endPosY, posX + finalValue, endPosY - (endPosY - posY) / 4.0 - finalValue, black);
                RenderUtils.drawRect(posX - 1.0, posY - finalValue, posX + (endPosX - posX) / 3.0 + finalValue, posY + 1.0, black);
                RenderUtils.drawRect(endPosX - (endPosX - posX) / 3.0 - 0.0, posY - finalValue, endPosX, posY + 1.0, black);
                RenderUtils.drawRect(endPosX - 1.0, posY, endPosX + finalValue, posY + (endPosY - posY) / 4.0 + finalValue, black);
                RenderUtils.drawRect(endPosX - 1.0, endPosY, endPosX + finalValue, endPosY - (endPosY - posY) / 4.0 - finalValue, black);
                RenderUtils.drawRect(posX - 1.0, endPosY - 1.0, posX + (endPosX - posX) / 3.0 + finalValue, endPosY + finalValue, black);
                RenderUtils.drawRect(endPosX - (endPosX - posX) / 3.0 - finalValue, endPosY - 1.0, endPosX + finalValue, endPosY + finalValue, black);
                RenderUtils.drawRect(posX, posY, posX - finalValue, posY + (endPosY - posY) / 4.0, color);
                RenderUtils.drawRect(posX, endPosY, posX - finalValue, endPosY - (endPosY - posY) / 4.0, color);
                RenderUtils.drawRect(posX - finalValue, posY, posX + (endPosX - posX) / 3.0, posY + finalValue, color);
                RenderUtils.drawRect(endPosX - (endPosX - posX) / 3.0, posY, endPosX, posY + finalValue, color);
                RenderUtils.drawRect(endPosX - finalValue, posY, endPosX, posY + (endPosY - posY) / 4.0, color);
                RenderUtils.drawRect(endPosX - finalValue, endPosY, endPosX, endPosY - (endPosY - posY) / 4.0, color);
                RenderUtils.drawRect(posX, endPosY - finalValue, posX + (endPosX - posX) / 3.0, endPosY, color);
                RenderUtils.drawRect(endPosX - (endPosX - posX) / 3.0, endPosY - finalValue, endPosX - finalValue, endPosY, color);
            }
            final EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            float targetHP = entityLivingBase.getHealth();
            targetHP = MathHelper.clamp(targetHP, 0.0f, 24.0f);
            final float maxHealth = entityLivingBase.getMaxHealth();
            final double hpPercentage = targetHP / maxHealth;
            final double hpHeight2 = (endPosY - posY) * hpPercentage;
            final int colorHeal = ClientHelper.getHealthColor().getRGB();
            if (this.hpDetails.getBoolValue() && !EntityESP.espMode.currentMode.equals("Box")) {
                final String healthDisplay = String.valueOf(MathematicHelper.round(((EntityLivingBase)entity).getHealth(), 1));
                this.drawScaledString(healthDisplay, posX - 6.0 - EntityESP.mc.fontRendererObj.getStringWidth(healthDisplay) * 0.5f, endPosY - hpHeight2, 0.5, colorHeal);
            }
            if (!EntityESP.healRect.getBoolValue() || EntityESP.espMode.currentMode.equals("Box")) {
                continue;
            }
            RenderUtils.drawRect(posX - 4.0, posY - 0.5, posX - 1.5, endPosY + 0.5, new Color(0, 0, 0, 125).getRGB());
            RenderUtils.drawRect(posX - 3.5, endPosY, posX - 2.0, endPosY - hpHeight2, colorHeal);
        }
    }
    
    private boolean isValid(final Entity entity) {
        return entity != null && (EntityESP.mc.gameSettings.thirdPersonView != 0 || entity != EntityESP.mc.player) && !entity.isDead && (!(entity instanceof EntityAnimal) || this.mobESP.getBoolValue()) && (entity instanceof EntityPlayer || (!(entity instanceof EntityArmorStand) && (!(entity instanceof IAnimals) || this.mobESP.getBoolValue()) && !(entity instanceof EntityItemFrame) && !(entity instanceof EntityArrow) && !(entity instanceof EntitySpectralArrow) && !(entity instanceof EntityMinecart) && !(entity instanceof EntityBoat) && !(entity instanceof EntityDragonFireball) && !(entity instanceof EntityXPOrb) && !(entity instanceof EntityMinecartChest) && !(entity instanceof EntityTNTPrimed) && !(entity instanceof EntityMinecartTNT) && (!(entity instanceof EntityVillager) || this.mobESP.getBoolValue()) && !(entity instanceof EntityExpBottle) && !(entity instanceof EntityLightningBolt) && !(entity instanceof EntityPotion) && (!(entity instanceof Entity) || entity instanceof EntityMob || entity instanceof EntityWaterMob || entity instanceof IAnimals || entity instanceof EntityLiving || entity instanceof EntityItem) && ((!(entity instanceof EntityMob) && !(entity instanceof EntitySlime) && !(entity instanceof EntityDragon) && !(entity instanceof EntityGolem)) || this.mobESP.getBoolValue()) && entity != EntityESP.mc.player));
    }
    
    private Vector3d project2D(final int scaleFactor, final double x, final double y, final double z) {
        final float xPos = (float)x;
        final float yPos = (float)y;
        final float zPos = (float)z;
        final IntBuffer viewport = GLAllocation.createDirectIntBuffer(16);
        final FloatBuffer modelview = GLAllocation.createDirectFloatBuffer(16);
        final FloatBuffer projection = GLAllocation.createDirectFloatBuffer(16);
        final FloatBuffer vector = GLAllocation.createDirectFloatBuffer(4);
        GL11.glGetFloat(2982, modelview);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        if (GLU.gluProject(xPos, yPos, zPos, modelview, projection, viewport, vector)) {
            return new Vector3d(vector.get(0) / scaleFactor, (Display.getHeight() - vector.get(1)) / scaleFactor, vector.get(2));
        }
        return null;
    }
    
    static {
        EntityESP.healRect = new BooleanSetting("Health Rect", true, () -> EntityESP.espMode.currentMode.equals("2D"));
        EntityESP.healcolorMode = new ListSetting("Color Health Mode", "Client", () -> EntityESP.healRect.getBoolValue() && EntityESP.espMode.currentMode.equals("2D"), new String[] { "Astolfo", "Rainbow", "Client", "Custom" });
        healColor = new ColorSetting("Health Border Color", -1, () -> EntityESP.healcolorMode.currentMode.equals("Custom") && !EntityESP.espMode.currentMode.equals("Box"));
        EntityESP.colorMode = new ListSetting("Color Box Mode", "Client", () -> EntityESP.espMode.currentMode.equals("Box") || EntityESP.espMode.currentMode.equals("2D"), new String[] { "Astolfo", "Rainbow", "Client", "Custom" });
    }
}
