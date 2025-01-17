// 
// Decompiled by Procyon v0.6.0
// 

package ViaMCP;

import java.util.concurrent.Future;
import com.viaversion.viaversion.api.platform.PlatformTask;

public class FutureTaskId implements PlatformTask<Future<?>>
{
    private final Future<?> object;
    
    public FutureTaskId(final Future<?> object) {
        this.object = object;
    }
    
    @Override
    public Future<?> getObject() {
        return this.object;
    }
    
    @Override
    public void cancel() {
        this.object.cancel(false);
    }
}
