// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.mainmenu;

import java.io.IOException;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.io.InputStream;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;

public class Shader
{
    private final int programId;
    Minecraft mc;
    private final ScaledResolution sr;
    public static final String VERTEX_SORUCE;
    private final long initTime;
    
    public Shader(final String fragmentShaderName) {
        this.mc = Minecraft.getMinecraft();
        this.sr = new ScaledResolution(this.mc);
        final int programId = GL20.glCreateProgram();
        try {
            final int vertexShader = GL20.glCreateShader(35633);
            GL20.glShaderSource(vertexShader, (CharSequence)Shader.VERTEX_SORUCE);
            GL20.glCompileShader(vertexShader);
            final int isVertexCompiled = GL20.glGetShaderi(vertexShader, 35713);
            if (isVertexCompiled == 0) {
                GL20.glDeleteShader(vertexShader);
                System.err.println("Vertex shader couldn't compile. It has been deleted.");
            }
            final int fragmentShader = GL20.glCreateShader(35632);
            GL20.glShaderSource(fragmentShader, (CharSequence)getShaderSource(fragmentShaderName));
            GL20.glCompileShader(fragmentShader);
            final int isFragmentCompiled = GL20.glGetShaderi(fragmentShader, 35713);
            if (isFragmentCompiled == 0) {
                GL20.glDeleteShader(fragmentShader);
                System.err.println("Fragment shader couldn't compile. It has been deleted.");
            }
            GL20.glAttachShader(programId, vertexShader);
            GL20.glAttachShader(programId, fragmentShader);
            GL20.glLinkProgram(programId);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        this.programId = programId;
        this.initTime = System.currentTimeMillis();
    }
    
    public void draw(final float mouseX, final float mouseY) {
        GL20.glUseProgram(this.programId);
        GL20.glUniform2f(GL20.glGetUniformLocation(this.programId, (CharSequence)"resolution"), (float)this.sr.getScaledWidth(), (float)this.sr.getScaledHeight());
        GL20.glUniform2f(GL20.glGetUniformLocation(this.programId, (CharSequence)"mouse"), mouseX, mouseY);
        GL20.glUniform1f(GL20.glGetUniformLocation(this.programId, (CharSequence)"time"), (System.currentTimeMillis() - this.initTime) / 1000.0f);
        GL11.glBegin(7);
        GL11.glVertex2d(0.0, 0.0);
        GL11.glVertex2d(0.0, (double)this.sr.getScaledHeight());
        GL11.glVertex2d((double)this.sr.getScaledWidth(), (double)this.sr.getScaledHeight());
        GL11.glVertex2d((double)this.sr.getScaledWidth(), 0.0);
        GL11.glEnd();
        GL20.glUseProgram(0);
    }
    
    public static String getShaderSource(final String fileName) {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Shader.class.getResourceAsStream("/assets/minecraft/rynware/shaders/" + fileName))));
        final String source = bufferedReader.lines().filter(str -> !str.isEmpty()).map(str -> {
            str = str.replace("\t", "");
            return;
        }).collect((Collector<? super Object, ?, String>)Collectors.joining("\n"));
        try {
            bufferedReader.close();
        }
        catch (final IOException ex) {}
        return source;
    }
    
    static {
        VERTEX_SORUCE = getShaderSource("vertex.vsh");
    }
}
