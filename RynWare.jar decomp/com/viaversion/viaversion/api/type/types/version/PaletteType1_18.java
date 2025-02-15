// 
// Decompiled by Procyon v0.6.0
// 

package com.viaversion.viaversion.api.type.types.version;

import com.viaversion.viaversion.util.MathUtil;
import com.viaversion.viaversion.util.CompactArrayUtil;
import com.viaversion.viaversion.api.minecraft.chunks.DataPaletteImpl;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.minecraft.chunks.PaletteType;
import com.viaversion.viaversion.api.minecraft.chunks.DataPalette;
import com.viaversion.viaversion.api.type.Type;

public final class PaletteType1_18 extends Type<DataPalette>
{
    private final int globalPaletteBits;
    private final PaletteType type;
    
    public PaletteType1_18(final PaletteType type, final int globalPaletteBits) {
        super(DataPalette.class);
        this.globalPaletteBits = globalPaletteBits;
        this.type = type;
    }
    
    @Override
    public DataPalette read(final ByteBuf buffer) throws Exception {
        final int originalBitsPerValue;
        int bitsPerValue = originalBitsPerValue = buffer.readByte();
        if (bitsPerValue > this.type.highestBitsPerValue()) {
            bitsPerValue = this.globalPaletteBits;
        }
        if (bitsPerValue == 0) {
            final DataPaletteImpl palette = new DataPaletteImpl(this.type.size(), 1);
            palette.addId(Type.VAR_INT.readPrimitive(buffer));
            Type.VAR_INT.readPrimitive(buffer);
            return palette;
        }
        DataPaletteImpl palette;
        if (bitsPerValue != this.globalPaletteBits) {
            final int paletteLength = Type.VAR_INT.readPrimitive(buffer);
            palette = new DataPaletteImpl(this.type.size(), paletteLength);
            for (int i = 0; i < paletteLength; ++i) {
                palette.addId(Type.VAR_INT.readPrimitive(buffer));
            }
        }
        else {
            palette = new DataPaletteImpl(this.type.size());
        }
        final long[] values = new long[Type.VAR_INT.readPrimitive(buffer)];
        if (values.length > 0) {
            final char valuesPerLong = (char)(64 / bitsPerValue);
            final int expectedLength = (this.type.size() + valuesPerLong - 1) / valuesPerLong;
            if (values.length != expectedLength) {
                throw new IllegalStateException("Palette data length (" + values.length + ") does not match expected length (" + expectedLength + ")! bitsPerValue=" + bitsPerValue + ", originalBitsPerValue=" + originalBitsPerValue);
            }
            for (int j = 0; j < values.length; ++j) {
                values[j] = buffer.readLong();
            }
            CompactArrayUtil.iterateCompactArrayWithPadding(bitsPerValue, this.type.size(), values, (bitsPerValue == this.globalPaletteBits) ? palette::setIdAt : palette::setPaletteIndexAt);
        }
        return palette;
    }
    
    @Override
    public void write(final ByteBuf buffer, final DataPalette palette) throws Exception {
        if (palette.size() == 1) {
            buffer.writeByte(0);
            Type.VAR_INT.writePrimitive(buffer, palette.idByIndex(0));
            Type.VAR_INT.writePrimitive(buffer, 0);
            return;
        }
        final int min = (this.type == PaletteType.BLOCKS) ? 4 : 1;
        int bitsPerValue = Math.max(min, MathUtil.ceilLog2(palette.size()));
        if (bitsPerValue > this.type.highestBitsPerValue()) {
            bitsPerValue = this.globalPaletteBits;
        }
        buffer.writeByte(bitsPerValue);
        if (bitsPerValue != this.globalPaletteBits) {
            Type.VAR_INT.writePrimitive(buffer, palette.size());
            for (int i = 0; i < palette.size(); ++i) {
                Type.VAR_INT.writePrimitive(buffer, palette.idByIndex(i));
            }
        }
        final long[] data = CompactArrayUtil.createCompactArrayWithPadding(bitsPerValue, this.type.size(), (bitsPerValue == this.globalPaletteBits) ? palette::idAt : palette::paletteIndexAt);
        Type.VAR_INT.writePrimitive(buffer, data.length);
        for (final long l : data) {
            buffer.writeLong(l);
        }
    }
}
