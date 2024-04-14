// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.visual;

import org.rynware.client.event.EventTarget;
import net.minecraft.client.renderer.GlStateManager;
import org.rynware.client.event.events.impl.render.EventRenderScoreboard;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.feature.Feature;

public class ScoreboardFeatures extends Feature
{
    public static BooleanSetting noScore;
    public NumberSetting x;
    public NumberSetting y;
    
    public ScoreboardFeatures() {
        super("Scoreboard", "\u0418\u0437\u043c\u0435\u043d\u044f\u0435\u0442 \u0441\u043a\u043e\u0440\u0431\u043e\u0440\u0434", FeatureCategory.Visuals);
        ScoreboardFeatures.noScore = new BooleanSetting("No Scoreboard", false, () -> true);
        this.x = new NumberSetting("Scoreboard X", 0.0f, -1000.0f, 1000.0f, 1.0f, () -> !ScoreboardFeatures.noScore.getBoolValue());
        this.y = new NumberSetting("Scoreboard Y", 0.0f, -500.0f, 500.0f, 1.0f, () -> !ScoreboardFeatures.noScore.getBoolValue());
        this.addSettings(ScoreboardFeatures.noScore, this.x, this.y);
    }
    
    @EventTarget
    public void onRenderScoreboard(final EventRenderScoreboard event) {
        if (event.isPre()) {
            GlStateManager.translate(-this.x.getNumberValue(), this.y.getNumberValue(), 12.0f);
        }
        else {
            GlStateManager.translate(this.x.getNumberValue(), -this.y.getNumberValue(), 12.0f);
        }
    }
}
