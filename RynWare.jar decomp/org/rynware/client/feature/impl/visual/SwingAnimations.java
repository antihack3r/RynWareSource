// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.visual;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumHandSide;
import org.rynware.client.event.events.impl.player.EventTransformSideFirstPerson;
import org.rynware.client.event.EventTarget;
import org.rynware.client.feature.impl.combat.KillAura;
import org.rynware.client.Main;
import org.rynware.client.event.events.impl.player.EventUpdate;
import org.rynware.client.ui.settings.Setting;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.ui.settings.impl.NumberSetting;
import org.rynware.client.ui.settings.impl.BooleanSetting;
import org.rynware.client.ui.settings.impl.ListSetting;
import org.rynware.client.feature.Feature;

public class SwingAnimations extends Feature
{
    public static boolean blocking;
    public static ListSetting swordAnim;
    public static BooleanSetting auraOnly;
    public static BooleanSetting item360;
    public static NumberSetting item360Speed;
    public static ListSetting item360Mode;
    public static ListSetting item360Hand;
    public static NumberSetting rightx1;
    public static NumberSetting righty2;
    public static NumberSetting rightz3;
    public static NumberSetting swingSpeed;
    public static NumberSetting spinSpeed;
    
    public SwingAnimations() {
        super("SwingAnimations", "\u041a\u0430\u0441\u0442\u043e\u043c\u043d\u044b\u0435 \u0430\u043d\u0438\u043c\u0430\u0446\u0438\u0438 \u043c\u0435\u0447\u0430", FeatureCategory.Visuals);
        SwingAnimations.swordAnim = new ListSetting("Animation Mode", "Main", () -> true, new String[] { "Main", "Default", "Drochka" });
        SwingAnimations.auraOnly = new BooleanSetting("Only Aura", false, () -> true);
        SwingAnimations.swingSpeed = new NumberSetting("Swing Speed", "\u0421\u043a\u043e\u0440\u043e\u0441\u0442\u044c \u0430\u043d\u0438\u043c\u0430\u0446\u0438\u0438", 8.0f, 1.0f, 20.0f, 1.0f, () -> true);
        SwingAnimations.rightx1 = new NumberSetting("RightX", 0.0f, -2.0f, 2.0f, 0.1f, () -> true);
        SwingAnimations.righty2 = new NumberSetting("RightY", 0.2f, -2.0f, 2.0f, 0.1f, () -> true);
        SwingAnimations.rightz3 = new NumberSetting("RightZ", -2.0f, -2.0f, -0.4f, 0.1f, () -> true);
        SwingAnimations.spinSpeed = new NumberSetting("Spin Speed", 4.0f, 1.0f, 10.0f, 1.0f, () -> SwingAnimations.swordAnim.currentMode.equals("Spin"));
        SwingAnimations.item360 = new BooleanSetting("Item360", false, () -> true);
        SwingAnimations.item360Mode = new ListSetting("Item360 Mode", "Horizontal", () -> SwingAnimations.item360.getBoolValue(), new String[] { "Horizontal", "Vertical", "Zoom" });
        SwingAnimations.item360Hand = new ListSetting("Item360 Hand", "All", () -> SwingAnimations.item360.getBoolValue(), new String[] { "All", "Left", "Right" });
        SwingAnimations.item360Speed = new NumberSetting("Item360 Speed", 8.0f, 1.0f, 15.0f, 1.0f, () -> SwingAnimations.item360.getBoolValue());
        this.addSettings(SwingAnimations.auraOnly, SwingAnimations.swordAnim, SwingAnimations.spinSpeed, SwingAnimations.swingSpeed, SwingAnimations.rightx1, SwingAnimations.righty2, SwingAnimations.rightz3, SwingAnimations.item360, SwingAnimations.item360Mode, SwingAnimations.item360Hand, SwingAnimations.item360Speed);
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        final String anim = SwingAnimations.swordAnim.getOptions();
        SwingAnimations.blocking = (Main.instance.featureManager.getFeature(KillAura.class).isEnabled() && KillAura.target != null);
        this.setSuffix(anim);
    }
    
    @EventTarget
    public void onSidePerson(final EventTransformSideFirstPerson event) {
        if (SwingAnimations.blocking && event.getEnumHandSide() == EnumHandSide.RIGHT) {
            GlStateManager.translate(0.29, 0.1, -0.31);
        }
    }
}
