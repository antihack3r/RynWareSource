// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.utils.math;

import org.rynware.client.utils.Helper;

public class TimerHelper implements Helper
{
    private long ms;
    private long previousTime;
    
    public TimerHelper() {
        this.ms = this.getCurrentMS();
        this.previousTime = -1L;
    }
    
    private long getCurrentMS() {
        return System.currentTimeMillis();
    }
    
    public boolean hasReached(final double milliseconds) {
        return this.getCurrentMS() - this.ms > milliseconds;
    }
    
    public boolean check(final float milliseconds) {
        return this.getCurrentMS() - this.previousTime >= milliseconds;
    }
    
    public void reset() {
        this.ms = this.getCurrentMS();
    }
    
    public long getTime() {
        return this.getCurrentMS() - this.ms;
    }
}
