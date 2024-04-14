// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.utils.math;

import org.rynware.client.utils.render.RenderUtils;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.shader.Shader;
import net.minecraft.util.ResourceLocation;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.shader.ShaderGroup;

public class BlurUtil
{
    private static ShaderGroup blurShader;
    private static Minecraft mc;
    private static Framebuffer buffer;
    private static int lastScale;
    private static int lastScaleWidth;
    private static int lastScaleHeight;
    private static final ResourceLocation shader;
    
    public static void initFboAndShader() {
        try {
            (BlurUtil.blurShader = new ShaderGroup(BlurUtil.mc.getTextureManager(), BlurUtil.mc.getResourceManager(), BlurUtil.mc.getFramebuffer(), BlurUtil.shader)).createBindFramebuffers(BlurUtil.mc.displayWidth, BlurUtil.mc.displayHeight);
            BlurUtil.buffer = BlurUtil.blurShader.mainFramebuffer;
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void setShaderConfigs(final float intensity) {
        BlurUtil.blurShader.getShaders().get(0).getShaderManager().getShaderUniform("Radius").set(intensity);
        BlurUtil.blurShader.getShaders().get(1).getShaderManager().getShaderUniform("Radius").set(intensity);
    }
    
    public static void blurRect(final float x, final float y, final float width, final float height, final float intensity) {
        final ScaledResolution scale = new ScaledResolution(BlurUtil.mc);
        final int factor = scale.getScaleFactor();
        final int factor2 = scale.getScaledWidth();
        final int factor3 = scale.getScaledHeight();
        if (BlurUtil.lastScale != factor || BlurUtil.lastScaleWidth != factor2 || BlurUtil.lastScaleHeight != factor3 || BlurUtil.buffer == null || BlurUtil.blurShader == null) {
            initFboAndShader();
        }
        GL11.glPushMatrix();
        GL11.glEnable(3089);
        RenderUtils.scissorRect(x, y, width, height);
        BlurUtil.buffer.bindFramebuffer(true);
        BlurUtil.blurShader.loadShaderGroup(BlurUtil.mc.timer.renderPartialTicks);
        BlurUtil.blurShader.getShaders().get(0).getShaderManager().getShaderUniform("Radius").set(intensity);
        BlurUtil.blurShader.getShaders().get(1).getShaderManager().getShaderUniform("Radius").set(intensity);
        BlurUtil.mc.getFramebuffer().bindFramebuffer(false);
        GL11.glDisable(3089);
        GL11.glPopMatrix();
    }
    
    public static void blurAll(final float intensity) {
        final ScaledResolution scale = new ScaledResolution(BlurUtil.mc);
        final int factor = scale.getScaleFactor();
        final int factor2 = scale.getScaledWidth();
        final int factor3 = scale.getScaledHeight();
        if (BlurUtil.lastScale != factor || BlurUtil.lastScaleWidth != factor2 || BlurUtil.lastScaleHeight != factor3 || BlurUtil.buffer == null || BlurUtil.blurShader == null) {
            initFboAndShader();
        }
        BlurUtil.lastScale = factor;
        BlurUtil.lastScaleWidth = factor2;
        BlurUtil.lastScaleHeight = factor3;
        setShaderConfigs(intensity);
        BlurUtil.buffer.bindFramebuffer(true);
        BlurUtil.blurShader.loadShaderGroup(BlurUtil.mc.timer.renderPartialTicks);
        BlurUtil.mc.getFramebuffer().bindFramebuffer(true);
    }
    
    static {
        BlurUtil.mc = Minecraft.getMinecraft();
        shader = new ResourceLocation("shaders/post/blur.json");
    }
}
