// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui.advancements;

public enum AdvancementState
{
    OBTAINED(0), 
    UNOBTAINED(1);
    
    private final int field_192671_d;
    
    private AdvancementState(final int p_i47384_3_) {
        this.field_192671_d = p_i47384_3_;
    }
    
    public int func_192667_a() {
        return this.field_192671_d;
    }
}
