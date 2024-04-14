// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.ui.particle;

import java.util.Random;
import java.util.Iterator;
import org.rynware.client.utils.render.RenderUtils;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import java.util.ArrayList;
import java.util.List;

public class ParticleGenerator
{
    private final List<Particle> particles;
    private final int amount;
    private int prevWidth;
    private int prevHeight;
    
    public ParticleGenerator(final int amount) {
        this.particles = new ArrayList<Particle>();
        this.amount = amount;
    }
    
    public void draw(final int mouseX, final int mouseY) {
        if (this.particles.isEmpty() || this.prevWidth != Minecraft.getMinecraft().displayWidth || this.prevHeight != Minecraft.getMinecraft().displayHeight) {
            this.particles.clear();
            this.create();
        }
        this.prevWidth = Minecraft.getMinecraft().displayWidth;
        this.prevHeight = Minecraft.getMinecraft().displayHeight;
        for (final Particle particle : this.particles) {
            particle.fall();
            particle.interpolation();
            final int range = 110;
            final boolean mouseOver = mouseX >= particle.x - range && mouseY >= particle.y - range && mouseX <= particle.x + range && mouseY <= particle.y + range;
            if (mouseOver) {
                this.particles.stream().filter(part -> part.getX() > particle.getX() && part.getX() - particle.getX() < range && particle.getX() - part.getX() < range && ((part.getY() > particle.getY() && part.getY() - particle.getY() < range) || (particle.getY() > part.getY() && particle.getY() - part.getY() < range))).forEach(connectable -> particle.connect(connectable.getX(), connectable.getY()));
            }
            RenderUtils.drawBlurredShadow(particle.getX(), particle.getY(), particle.size, 4.0f, 6, new Color(255, 255, 255));
        }
    }
    
    private void create() {
        final Random random = new Random();
        for (int i = 0; i < this.amount; ++i) {
            this.particles.add(new Particle(random.nextInt(Minecraft.getMinecraft().displayWidth), random.nextInt(Minecraft.getMinecraft().displayHeight)));
        }
    }
}
