// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.font;

import net.minecraft.client.Minecraft;
import java.awt.Font;
import net.minecraft.util.ResourceLocation;

public class FontUtil
{
    public static Font getFontFromTTF(final ResourceLocation loc, final float fontSize, final int fontType) {
        try {
            Font output = Font.createFont(fontType, Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream());
            output = output.deriveFont(fontSize);
            return output;
        }
        catch (final Exception e) {
            return null;
        }
    }
}
