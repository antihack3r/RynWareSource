// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.utils.math;

import org.rynware.client.event.EventTarget;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import org.rynware.client.event.events.impl.packet.EventReceivePacket;
import net.minecraft.util.math.MathHelper;
import java.util.Arrays;
import org.rynware.client.utils.Helper;

public class TPSUtils implements Helper
{
    private static float[] tickRates;
    private int nextIndex;
    private long timeLastTimeUpdate;
    
    public TPSUtils() {
        this.nextIndex = 0;
        this.nextIndex = 0;
        this.timeLastTimeUpdate = -1L;
        Arrays.fill(TPSUtils.tickRates, 0.0f);
    }
    
    public static float getTickRate() {
        float numTicks = 0.0f;
        float sumTickRates = 0.0f;
        for (final float tickRate : TPSUtils.tickRates) {
            if (tickRate > 0.0f) {
                sumTickRates += tickRate;
                ++numTicks;
            }
        }
        return MathHelper.clamp(sumTickRates / numTicks, 0.0f, 20.0f);
    }
    
    private void onTimeUpdate() {
        if (this.timeLastTimeUpdate != -1L) {
            final float timeElapsed = (System.currentTimeMillis() - this.timeLastTimeUpdate) / 1000.0f;
            TPSUtils.tickRates[this.nextIndex % TPSUtils.tickRates.length] = MathHelper.clamp(20.0f / timeElapsed, 0.0f, 20.0f);
            ++this.nextIndex;
        }
        this.timeLastTimeUpdate = System.currentTimeMillis();
    }
    
    @EventTarget
    public void onReceivePacket(final EventReceivePacket event) {
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            this.onTimeUpdate();
        }
    }
    
    static {
        TPSUtils.tickRates = new float[20];
    }
}
