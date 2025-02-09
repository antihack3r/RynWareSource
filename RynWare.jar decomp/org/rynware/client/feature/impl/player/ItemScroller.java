// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.player;

import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.feature.Feature;

public class ItemScroller extends Feature
{
    public static NumberSetting scrollerDelay;
    
    public ItemScroller() {
        super("ItemScroller", "\u0411\u044b\u0441\u0442\u0440\u043e\u0435 \u043f\u0435\u0440\u0435\u0434\u0432\u0438\u0436\u0435\u043d\u0438\u0435 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432 \u0432 \u0441\u0443\u043d\u0434\u0443\u043a\u0435", FeatureCategory.Player);
        ItemScroller.scrollerDelay = new NumberSetting("Scroller Delay", 80.0f, 0.0f, 1000.0f, 1.0f, () -> true);
        this.addSettings(ItemScroller.scrollerDelay);
    }
}
