// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.utils.other.font;

import java.io.IOException;
import java.awt.FontFormatException;
import java.awt.Font;
import java.util.Objects;
import java.io.InputStream;

public class Fonts
{
    public static CFontRenderer Regular35;
    public static CFontRenderer Regular30;
    public static CFontRenderer Regular20;
    public static CFontRenderer Regular25;
    public static CFontRenderer Monstserrat30;
    public static CFontRenderer Monstserrat20;
    public static CFontRenderer Monstserrat16;
    public static CFontRenderer Monstserrat17;
    public static CFontRenderer Monstserrat;
    public static CFontRenderer Esp;
    public static CFontRenderer Icon;
    
    public static void loadFonts() {
        try {
            Fonts.Regular35 = new CFontRenderer(Font.createFont(0, Objects.requireNonNull(Fonts.class.getResourceAsStream("/assets/minecraft/fonts/Raleway-Regular.ttf"))).deriveFont(35.0f), true, false);
            Fonts.Regular30 = new CFontRenderer(Font.createFont(0, Objects.requireNonNull(Fonts.class.getResourceAsStream("/assets/minecraft/fonts/Raleway-Regular.ttf"))).deriveFont(30.0f), true, false);
            Fonts.Regular25 = new CFontRenderer(Font.createFont(0, Objects.requireNonNull(Fonts.class.getResourceAsStream("/assets/minecraft/fonts/Raleway-Regular.ttf"))).deriveFont(25.0f), true, false);
            Fonts.Regular20 = new CFontRenderer(Font.createFont(0, Objects.requireNonNull(Fonts.class.getResourceAsStream("/assets/minecraft/fonts/Raleway-Regular.ttf"))).deriveFont(20.0f), true, false);
            Fonts.Monstserrat30 = new CFontRenderer(Font.createFont(0, Objects.requireNonNull(Fonts.class.getResourceAsStream("/assets/minecraft/fonts/mntsb.ttf"))).deriveFont(30.0f), true, false);
            Fonts.Monstserrat20 = new CFontRenderer(Font.createFont(0, Objects.requireNonNull(Fonts.class.getResourceAsStream("/assets/minecraft/fonts/mntsb.ttf"))).deriveFont(20.0f), true, false);
            Fonts.Monstserrat16 = new CFontRenderer(Font.createFont(0, Objects.requireNonNull(Fonts.class.getResourceAsStream("/assets/minecraft/fonts/mntsb.ttf"))).deriveFont(16.0f), true, false);
            Fonts.Monstserrat17 = new CFontRenderer(Font.createFont(0, Objects.requireNonNull(Fonts.class.getResourceAsStream("/assets/minecraft/fonts/mntsr.ttf"))).deriveFont(17.0f), true, false);
            Fonts.Monstserrat = new CFontRenderer(Font.createFont(0, Objects.requireNonNull(Fonts.class.getResourceAsStream("/assets/minecraft/fonts/mntsr.ttf"))).deriveFont(15.0f), true, false);
            Fonts.Esp = new CFontRenderer(Font.createFont(0, Objects.requireNonNull(Fonts.class.getResourceAsStream("/assets/minecraft/fonts/esp.ttf"))).deriveFont(27.0f), true, false);
            Fonts.Icon = new CFontRenderer(Font.createFont(0, Objects.requireNonNull(Fonts.class.getResourceAsStream("/assets/minecraft/fonts/stylesicons.ttf"))).deriveFont(25.0f), true, false);
        }
        catch (final FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }
}
