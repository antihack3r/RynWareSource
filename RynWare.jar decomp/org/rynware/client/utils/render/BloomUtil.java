// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.utils.render;

import org.lwjgl.opengl.GL20;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL13;
import org.rynware.client.utils.math.MathematicHelper;
import org.lwjgl.BufferUtils;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.rynware.client.utils.Helper;

public class BloomUtil implements Helper
{
    public static ShaderUtil gaussianBloom;
    public static Framebuffer framebuffer;
    
    public static void renderBlur(final int sourceTexture, final int radius, final int offset) {
        BloomUtil.framebuffer = RenderUtils.createFrameBuffer(BloomUtil.framebuffer);
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(516, 0.0f);
        GlStateManager.enableBlend();
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        final FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);
        for (int i = 0; i <= radius; ++i) {
            weightBuffer.put(MathematicHelper.calculateGaussianValue((float)i, (float)radius));
        }
        weightBuffer.rewind();
        RenderUtils.setAlphaLimit(0.0f);
        BloomUtil.framebuffer.framebufferClear();
        BloomUtil.framebuffer.bindFramebuffer(true);
        BloomUtil.gaussianBloom.init();
        setupUniforms(radius, offset, 0, weightBuffer);
        RenderUtils.bindTexture(sourceTexture);
        ShaderUtil.drawQuads();
        BloomUtil.gaussianBloom.unload();
        BloomUtil.framebuffer.unbindFramebuffer();
        BloomUtil.mc.getFramebuffer().bindFramebuffer(true);
        BloomUtil.gaussianBloom.init();
        setupUniforms(radius, 0, offset, weightBuffer);
        GL13.glActiveTexture(34000);
        RenderUtils.bindTexture(sourceTexture);
        GL13.glActiveTexture(33984);
        RenderUtils.bindTexture(BloomUtil.framebuffer.framebufferTexture);
        ShaderUtil.drawQuads();
        BloomUtil.gaussianBloom.unload();
        GlStateManager.alphaFunc(516, 0.1f);
        GlStateManager.enableAlpha();
        GlStateManager.bindTexture(0);
    }
    
    public static void setupUniforms(final int radius, final int directionX, final int directionY, final FloatBuffer weights) {
        BloomUtil.gaussianBloom.setUniformi("inTexture", 0);
        BloomUtil.gaussianBloom.setUniformi("textureToCheck", 16);
        BloomUtil.gaussianBloom.setUniformf("radius", (float)radius);
        BloomUtil.gaussianBloom.setUniformf("texelSize", 1.0f / BloomUtil.mc.displayWidth, 1.0f / BloomUtil.mc.displayHeight);
        BloomUtil.gaussianBloom.setUniformf("direction", (float)directionX, (float)directionY);
        GL20.glUniform1(BloomUtil.gaussianBloom.getUniform("weights"), weights);
    }
    
    static {
        BloomUtil.gaussianBloom = new ShaderUtil("rynware/shaders/bloom.frag");
        BloomUtil.framebuffer = new Framebuffer(1, 1, false);
    }
}
