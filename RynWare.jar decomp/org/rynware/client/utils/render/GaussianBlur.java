// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.utils.render;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.GlStateManager;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL20;
import org.rynware.client.utils.math.MathematicHelper;
import org.lwjgl.BufferUtils;
import net.minecraft.client.shader.Framebuffer;
import org.rynware.client.utils.Helper;

public class GaussianBlur implements Helper
{
    public static ShaderUtil blurShader;
    public static Framebuffer framebuffer;
    
    public static void setupUniforms(final float dir1, final float dir2, final float radius) {
        GaussianBlur.blurShader.setUniformi("textureIn", 0);
        GaussianBlur.blurShader.setUniformf("texelSize", 1.0f / GaussianBlur.mc.displayWidth, 1.0f / GaussianBlur.mc.displayHeight);
        GaussianBlur.blurShader.setUniformf("direction", dir1, dir2);
        GaussianBlur.blurShader.setUniformf("radius", radius);
        final FloatBuffer weightBuffer = BufferUtils.createFloatBuffer(256);
        for (int i = 0; i <= radius; ++i) {
            weightBuffer.put(MathematicHelper.calculateGaussianValue((float)i, radius / 2.0f));
        }
        weightBuffer.rewind();
        GL20.glUniform1(GaussianBlur.blurShader.getUniform("weights"), weightBuffer);
    }
    
    public static void renderBlur(final float radius) {
        GlStateManager.enableBlend();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        (GaussianBlur.framebuffer = RenderUtils.createFrameBuffer(GaussianBlur.framebuffer)).framebufferClear();
        GaussianBlur.framebuffer.bindFramebuffer(true);
        GaussianBlur.blurShader.init();
        setupUniforms(1.0f, 0.0f, radius);
        RenderUtils.bindTexture(GaussianBlur.mc.getFramebuffer().framebufferTexture);
        ShaderUtil.drawQuads();
        GaussianBlur.framebuffer.unbindFramebuffer();
        GaussianBlur.blurShader.unload();
        GaussianBlur.mc.getFramebuffer().bindFramebuffer(true);
        GaussianBlur.blurShader.init();
        setupUniforms(0.0f, 1.0f, radius);
        RenderUtils.bindTexture(GaussianBlur.framebuffer.framebufferTexture);
        ShaderUtil.drawQuads();
        GaussianBlur.blurShader.unload();
        GlStateManager.resetColor();
        GlStateManager.bindTexture(0);
    }
    
    static {
        GaussianBlur.blurShader = new ShaderUtil("rynware/shaders/gaussian.frag");
        GaussianBlur.framebuffer = new Framebuffer(1, 1, false);
    }
}
