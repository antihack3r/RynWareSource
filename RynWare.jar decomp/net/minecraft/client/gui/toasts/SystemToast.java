// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui.toasts;

import net.minecraft.client.renderer.GlStateManager;
import javax.annotation.Nullable;
import net.minecraft.util.text.ITextComponent;

public class SystemToast implements IToast
{
    private final Type field_193659_c;
    private String field_193660_d;
    private String field_193661_e;
    private long field_193662_f;
    private boolean field_193663_g;
    
    public SystemToast(final Type p_i47488_1_, final ITextComponent p_i47488_2_, @Nullable final ITextComponent p_i47488_3_) {
        this.field_193659_c = p_i47488_1_;
        this.field_193660_d = p_i47488_2_.getUnformattedText();
        this.field_193661_e = ((p_i47488_3_ == null) ? null : p_i47488_3_.getUnformattedText());
    }
    
    @Override
    public Visibility func_193653_a(final GuiToast p_193653_1_, final long p_193653_2_) {
        if (this.field_193663_g) {
            this.field_193662_f = p_193653_2_;
            this.field_193663_g = false;
        }
        p_193653_1_.func_192989_b().getTextureManager().bindTexture(SystemToast.field_193654_a);
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        p_193653_1_.drawTexturedModalRect(0, 0, 0, 64, 160, 32);
        if (this.field_193661_e == null) {
            p_193653_1_.func_192989_b().fontRendererObj.drawString(this.field_193660_d, 18.0f, 12.0f, -256);
        }
        else {
            p_193653_1_.func_192989_b().fontRendererObj.drawString(this.field_193660_d, 18.0f, 7.0f, -256);
            p_193653_1_.func_192989_b().fontRendererObj.drawString(this.field_193661_e, 18.0f, 18.0f, -1);
        }
        return (p_193653_2_ - this.field_193662_f < 5000L) ? Visibility.SHOW : Visibility.HIDE;
    }
    
    public void func_193656_a(final ITextComponent p_193656_1_, @Nullable final ITextComponent p_193656_2_) {
        this.field_193660_d = p_193656_1_.getUnformattedText();
        this.field_193661_e = ((p_193656_2_ == null) ? null : p_193656_2_.getUnformattedText());
        this.field_193663_g = true;
    }
    
    @Override
    public Type func_193652_b() {
        return this.field_193659_c;
    }
    
    public static void func_193657_a(final GuiToast p_193657_0_, final Type p_193657_1_, final ITextComponent p_193657_2_, @Nullable final ITextComponent p_193657_3_) {
        final SystemToast systemtoast = p_193657_0_.func_192990_a((Class<? extends SystemToast>)SystemToast.class, (Object)p_193657_1_);
        if (systemtoast == null) {
            p_193657_0_.func_192988_a(new SystemToast(p_193657_1_, p_193657_2_, p_193657_3_));
        }
        else {
            systemtoast.func_193656_a(p_193657_2_, p_193657_3_);
        }
    }
    
    public enum Type
    {
        TUTORIAL_HINT, 
        NARRATOR_TOGGLE;
    }
}
