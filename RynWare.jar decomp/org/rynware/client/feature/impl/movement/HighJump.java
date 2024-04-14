// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.movement;

import java.util.concurrent.TimeUnit;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.feature.Feature;

public class HighJump extends Feature
{
    public HighJump() {
        super("HighJump", "\u041f\u043e\u0434\u043f\u0440\u044b\u0433\u0438\u0432\u0430\u0435\u0442 \u0432\u044b\u0441\u043e\u043a\u043e \u0432\u0432\u0435\u0440\u0445", FeatureCategory.Movement);
    }
    
    @Override
    public void onEnable() {
        if (HighJump.mc.player.onGround) {
            HighJump.mc.player.jump();
        }
        new Thread(() -> {
            HighJump.mc.player.motionY = 9.0;
            try {
                TimeUnit.MILLISECONDS.sleep(200L);
            }
            catch (final InterruptedException e) {
                e.printStackTrace();
            }
            HighJump.mc.player.motionY = 8.741999626159668;
            this.toggle();
        }).start();
    }
}
