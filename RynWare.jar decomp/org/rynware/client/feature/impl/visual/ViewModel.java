// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.visual;

import org.rynware.client.event.EventTarget;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import org.rynware.client.event.events.impl.player.EventTransformSideFirstPerson;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.feature.Feature;

public class ViewModel extends Feature
{
    public static NumberSetting rightx;
    public static NumberSetting righty;
    public static NumberSetting rightz;
    public static NumberSetting leftx;
    public static NumberSetting lefty;
    public static NumberSetting leftz;
    
    public ViewModel() {
        super("ViewModel", "\u0418\u0437\u043c\u0435\u043d\u044f\u0435\u0442 \u043e\u0431\u0437\u043e\u0440 \u0440\u0443\u043a", FeatureCategory.Visuals);
        this.addSettings(ViewModel.rightx, ViewModel.righty, ViewModel.rightz, ViewModel.leftx, ViewModel.lefty, ViewModel.leftz);
    }
    
    @EventTarget
    public void onSidePerson(final EventTransformSideFirstPerson event) {
        if (event.getEnumHandSide() == EnumHandSide.RIGHT) {
            GlStateManager.translate(ViewModel.rightx.getNumberValue(), ViewModel.righty.getNumberValue(), ViewModel.rightz.getNumberValue());
        }
        if (event.getEnumHandSide() == EnumHandSide.LEFT) {
            GlStateManager.translate(-ViewModel.leftx.getNumberValue(), ViewModel.lefty.getNumberValue(), ViewModel.leftz.getNumberValue());
        }
    }
    
    static {
        ViewModel.rightx = new NumberSetting("RightX", 0.0f, -2.0f, 2.0f, 0.1f, () -> true);
        ViewModel.righty = new NumberSetting("RightY", 0.2f, -2.0f, 2.0f, 0.1f, () -> true);
        ViewModel.rightz = new NumberSetting("RightZ", 0.2f, -2.0f, 2.0f, 0.1f, () -> true);
        ViewModel.leftx = new NumberSetting("LeftX", 0.0f, -2.0f, 2.0f, 0.1f, () -> true);
        ViewModel.lefty = new NumberSetting("LeftY", 0.2f, -2.0f, 2.0f, 0.1f, () -> true);
        ViewModel.leftz = new NumberSetting("LeftZ", 0.2f, -2.0f, 2.0f, 0.1f, () -> true);
    }
}
