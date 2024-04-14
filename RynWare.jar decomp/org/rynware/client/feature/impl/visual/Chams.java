// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.visual;

import org.lwjgl.opengl.ARBShaderObjects;
import net.minecraft.entity.Entity;
import org.rynware.client.ui.settings.Setting;
import java.awt.Color;
import org.rynware.client.feature.impl.FeatureCategory;
import net.minecraft.util.ResourceLocation;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.settings.impl.ColorSetting;
import org.rynware.client.feature.Feature;

public class Chams extends Feature
{
    public static ColorSetting colorChams;
    public static BooleanSetting clientColor;
    public static ListSetting chamsMode;
    public static NumberSetting cosmoSpeed;
    private static ResourceLocation space;
    private static int shaderProgram;
    public static NumberSetting chamsAlpha;
    
    public Chams() {
        super("Chams", "\u041f\u0440\u043e\u0441\u0432\u0435\u0447\u0438\u0432\u0430\u0435\u0442 \u0438\u0433\u0440\u043e\u043a\u043e\u0432", FeatureCategory.Visuals);
        Chams.chamsMode = new ListSetting("Chams Mode", "Fill", () -> true, new String[] { "Fill", "Walls", "Cosmo" });
        Chams.clientColor = new BooleanSetting("Client Colored", false, () -> !Chams.chamsMode.currentMode.equals("Walls"));
        Chams.chamsAlpha = new NumberSetting("Chams Aplha", 0.5f, 0.2f, 1.0f, 0.1f, () -> Chams.chamsMode.currentMode.equals("Fill"));
        Chams.colorChams = new ColorSetting("Chams Color", new Color(16777215).getRGB(), () -> !Chams.chamsMode.currentMode.equals("Walls") && !Chams.clientColor.getBoolValue());
        Chams.cosmoSpeed = new NumberSetting("Cosmo Speed", 1.0f, 1.0f, 10.0f, 1.0f, () -> Chams.chamsMode.currentMode.equals("Cosmo"));
        this.addSettings(Chams.chamsMode, Chams.colorChams, Chams.clientColor, Chams.chamsAlpha, Chams.cosmoSpeed);
    }
    
    public static void shaderAttach(final Entity entity) {
        if (Chams.shaderProgram == 0) {
            return;
        }
        ARBShaderObjects.glUseProgramObjectARB(Chams.shaderProgram);
        ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(Chams.shaderProgram, (CharSequence)"time"), entity.ticksExisted / -100.0f * Chams.cosmoSpeed.getNumberValue());
        ARBShaderObjects.glUniform1iARB(ARBShaderObjects.glGetUniformLocationARB(Chams.shaderProgram, (CharSequence)"image"), 0);
        Chams.mc.getTextureManager().bindTexture(Chams.space);
    }
    
    public static void shaderDetach() {
        ARBShaderObjects.glUseProgramObjectARB(0);
    }
    
    private static void parseShaderFromBytes(final byte[] str) {
        Chams.shaderProgram = ARBShaderObjects.glCreateProgramObjectARB();
        if (Chams.shaderProgram == 0) {
            System.out.println("PC Issued");
            return;
        }
        final int shader = ARBShaderObjects.glCreateShaderObjectARB(35632);
        ARBShaderObjects.glShaderSourceARB(shader, (CharSequence)new String(str));
        ARBShaderObjects.glCompileShaderARB(shader);
        ARBShaderObjects.glAttachObjectARB(Chams.shaderProgram, shader);
        ARBShaderObjects.glLinkProgramARB(Chams.shaderProgram);
        ARBShaderObjects.glValidateProgramARB(Chams.shaderProgram);
    }
    
    static {
        Chams.space = new ResourceLocation("rynware/space.jpg");
        parseShaderFromBytes(new byte[] { 35, 118, 101, 114, 115, 105, 111, 110, 32, 49, 51, 48, 10, 10, 117, 110, 105, 102, 111, 114, 109, 32, 115, 97, 109, 112, 108, 101, 114, 50, 68, 32, 105, 109, 97, 103, 101, 59, 10, 117, 110, 105, 102, 111, 114, 109, 32, 102, 108, 111, 97, 116, 32, 116, 105, 109, 101, 59, 10, 10, 118, 111, 105, 100, 32, 109, 97, 105, 110, 40, 41, 10, 123, 10, 118, 101, 99, 52, 32, 114, 101, 115, 117, 108, 116, 32, 61, 32, 116, 101, 120, 116, 117, 114, 101, 50, 68, 40, 105, 109, 97, 103, 101, 44, 32, 118, 101, 99, 50, 40, 40, 45, 103, 108, 95, 70, 114, 97, 103, 67, 111, 111, 114, 100, 32, 47, 32, 49, 53, 48, 48, 41, 32, 43, 32, 116, 105, 109, 101, 32, 47, 32, 53, 41, 41, 59, 10, 114, 101, 115, 117, 108, 116, 46, 97, 32, 61, 32, 49, 59, 10, 103, 108, 95, 70, 114, 97, 103, 67, 111, 108, 111, 114, 32, 61, 32, 114, 101, 115, 117, 108, 116, 59, 10, 125 });
    }
}
