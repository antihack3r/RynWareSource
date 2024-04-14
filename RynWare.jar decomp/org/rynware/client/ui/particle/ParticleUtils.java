// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.particle;

public final class ParticleUtils
{
    private static final ParticleGenerator particleGenerator;
    
    public static void drawParticles(final int mouseX, final int mouseY) {
        ParticleUtils.particleGenerator.draw(mouseX, mouseY);
    }
    
    static {
        particleGenerator = new ParticleGenerator(100);
    }
}
