// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui.toasts;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import java.util.Arrays;
import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.gui.ScaledResolution;
import com.google.common.collect.Queues;
import java.util.Deque;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public class GuiToast extends Gui
{
    private final Minecraft field_191790_f;
    private final ToastInstance<?>[] field_191791_g;
    private final Deque<IToast> field_191792_h;
    
    public GuiToast(final Minecraft p_i47388_1_) {
        this.field_191791_g = new ToastInstance[5];
        this.field_191792_h = Queues.newArrayDeque();
        this.field_191790_f = p_i47388_1_;
    }
    
    public void func_191783_a(final ScaledResolution p_191783_1_) {
        if (!this.field_191790_f.gameSettings.hideGUI) {
            RenderHelper.disableStandardItemLighting();
            for (int i = 0; i < this.field_191791_g.length; ++i) {
                final ToastInstance<?> toastinstance = this.field_191791_g[i];
                if (toastinstance != null && toastinstance.func_193684_a(p_191783_1_.getScaledWidth(), i)) {
                    this.field_191791_g[i] = null;
                }
                if (this.field_191791_g[i] == null && !this.field_191792_h.isEmpty()) {
                    this.field_191791_g[i] = new ToastInstance<Object>((IToast)this.field_191792_h.removeFirst());
                }
            }
        }
    }
    
    @Nullable
    public <T extends IToast> T func_192990_a(final Class<? extends T> p_192990_1_, final Object p_192990_2_) {
        for (final ToastInstance<?> toastinstance : this.field_191791_g) {
            if (toastinstance != null && p_192990_1_.isAssignableFrom(toastinstance.func_193685_a().getClass()) && ((IToast)toastinstance.func_193685_a()).func_193652_b().equals(p_192990_2_)) {
                return (T)toastinstance.func_193685_a();
            }
        }
        for (final IToast itoast : this.field_191792_h) {
            if (p_192990_1_.isAssignableFrom(itoast.getClass()) && itoast.func_193652_b().equals(p_192990_2_)) {
                return (T)itoast;
            }
        }
        return null;
    }
    
    public void func_191788_b() {
        Arrays.fill(this.field_191791_g, null);
        this.field_191792_h.clear();
    }
    
    public void func_192988_a(final IToast p_192988_1_) {
        this.field_191792_h.add(p_192988_1_);
    }
    
    public Minecraft func_192989_b() {
        return this.field_191790_f;
    }
    
    class ToastInstance<T extends IToast>
    {
        private final T field_193688_b;
        private long field_193689_c;
        private long field_193690_d;
        private IToast.Visibility field_193691_e;
        
        private ToastInstance(final T p_i47483_2_) {
            this.field_193689_c = -1L;
            this.field_193690_d = -1L;
            this.field_193691_e = IToast.Visibility.SHOW;
            this.field_193688_b = p_i47483_2_;
        }
        
        public T func_193685_a() {
            return this.field_193688_b;
        }
        
        private float func_193686_a(final long p_193686_1_) {
            float f = MathHelper.clamp((p_193686_1_ - this.field_193689_c) / 600.0f, 0.0f, 1.0f);
            f *= f;
            return (this.field_193691_e == IToast.Visibility.HIDE) ? (1.0f - f) : f;
        }
        
        public boolean func_193684_a(final int p_193684_1_, final int p_193684_2_) {
            final long i = Minecraft.getSystemTime();
            if (this.field_193689_c == -1L) {
                this.field_193689_c = i;
                this.field_193691_e.func_194169_a(GuiToast.this.field_191790_f.getSoundHandler());
            }
            if (this.field_193691_e == IToast.Visibility.SHOW && i - this.field_193689_c <= 600L) {
                this.field_193690_d = i;
            }
            GlStateManager.pushMatrix();
            GlStateManager.translate(p_193684_1_ - 160.0f * this.func_193686_a(i), (float)(p_193684_2_ * 32), (float)(500 + p_193684_2_));
            final IToast.Visibility itoast$visibility = this.field_193688_b.func_193653_a(GuiToast.this, i - this.field_193690_d);
            GlStateManager.popMatrix();
            if (itoast$visibility != this.field_193691_e) {
                this.field_193689_c = i - (int)((1.0f - this.func_193686_a(i)) * 600.0f);
                (this.field_193691_e = itoast$visibility).func_194169_a(GuiToast.this.field_191790_f.getSoundHandler());
            }
            return this.field_193691_e == IToast.Visibility.HIDE && i - this.field_193689_c > 600L;
        }
    }
}
