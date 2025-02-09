// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.type.types.minecraft;

import com.viaversion.viaversion.api.minecraft.Position;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.minecraft.GlobalPosition;
import com.viaversion.viaversion.api.type.Type;

public class OptionalGlobalPositionType extends Type<GlobalPosition>
{
    public OptionalGlobalPositionType() {
        super(GlobalPosition.class);
    }
    
    @Override
    public GlobalPosition read(final ByteBuf buffer) throws Exception {
        if (buffer.readBoolean()) {
            final String dimension = Type.STRING.read(buffer);
            return Type.POSITION1_14.read(buffer).withDimension(dimension);
        }
        return null;
    }
    
    @Override
    public void write(final ByteBuf buffer, final GlobalPosition object) throws Exception {
        buffer.writeBoolean(object != null);
        if (object != null) {
            Type.STRING.write(buffer, object.dimension());
            Type.POSITION1_14.write(buffer, object);
        }
    }
}
