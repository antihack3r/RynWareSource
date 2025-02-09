// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.util.text;

public enum ChatType
{
    CHAT((byte)0), 
    SYSTEM((byte)1), 
    GAME_INFO((byte)2);
    
    private final byte field_192588_d;
    
    private ChatType(final byte p_i47429_3_) {
        this.field_192588_d = p_i47429_3_;
    }
    
    public byte func_192583_a() {
        return this.field_192588_d;
    }
    
    public static ChatType func_192582_a(final byte p_192582_0_) {
        for (final ChatType chattype : values()) {
            if (p_192582_0_ == chattype.field_192588_d) {
                return chattype;
            }
        }
        return ChatType.CHAT;
    }
}
