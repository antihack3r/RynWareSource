// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature;

import java.util.Iterator;
import java.util.ArrayList;
import org.rynware.client.feature.impl.FeatureCategory;
import java.util.List;
import java.util.Comparator;
import net.minecraft.client.Minecraft;
import org.rynware.client.feature.impl.visual.XRay;
import org.rynware.client.feature.impl.visual.ViewModel;
import org.rynware.client.feature.impl.visual.Tracers;
import org.rynware.client.feature.impl.visual.BlockESP;
import org.rynware.client.feature.impl.visual.ItemESP;
import org.rynware.client.feature.impl.visual.Trails;
import org.rynware.client.feature.impl.visual.NameTags;
import org.rynware.client.feature.impl.visual.PearlESP;
import org.rynware.client.feature.impl.visual.ChinaHat;
import org.rynware.client.feature.impl.movement.TestFeature;
import org.rynware.client.feature.impl.movement.ClickTp;
import org.rynware.client.feature.impl.visual.FogColor;
import org.rynware.client.feature.impl.visual.TargetESP;
import org.rynware.client.feature.impl.visual.FullBright;
import org.rynware.client.feature.impl.visual.TrianglEsp;
import org.rynware.client.feature.impl.visual.Chams;
import org.rynware.client.feature.impl.visual.EntityESP;
import org.rynware.client.feature.impl.visual.JumpCircles;
import org.rynware.client.feature.impl.visual.WorldFeatures;
import org.rynware.client.feature.impl.visual.SwingAnimations;
import org.rynware.client.feature.impl.visual.ScoreboardFeatures;
import org.rynware.client.feature.impl.player.NoFall;
import org.rynware.client.feature.impl.player.NoClip;
import org.rynware.client.feature.impl.misc.VclipHelper;
import org.rynware.client.feature.impl.player.NoPush;
import org.rynware.client.feature.impl.player.GuiWalk;
import org.rynware.client.feature.impl.player.AutoTool;
import org.rynware.client.feature.impl.player.FreeCam;
import org.rynware.client.feature.impl.player.StaffAlert;
import org.rynware.client.feature.impl.player.NoSlowDown;
import org.rynware.client.feature.impl.player.NoInteract;
import org.rynware.client.feature.impl.player.NoJumpDelay;
import org.rynware.client.feature.impl.visual.GPS;
import org.rynware.client.feature.impl.player.AutoFarm;
import org.rynware.client.feature.impl.player.BedrockClip;
import org.rynware.client.feature.impl.player.ItemScroller;
import org.rynware.client.feature.impl.player.DeathCoordinates;
import org.rynware.client.feature.impl.player.MiddleClickPearl;
import org.rynware.client.feature.impl.player.NoServerRotations;
import org.rynware.client.feature.impl.player.FastPlace;
import org.rynware.client.feature.impl.movement.NoWeb;
import org.rynware.client.feature.impl.movement.Timer;
import org.rynware.client.feature.impl.movement.Strafe;
import org.rynware.client.feature.impl.movement.Jesus;
import org.rynware.client.feature.impl.movement.Speed;
import org.rynware.client.feature.impl.movement.Sprint;
import org.rynware.client.feature.impl.movement.Flight;
import org.rynware.client.feature.impl.movement.AirJump;
import org.rynware.client.feature.impl.movement.Spider;
import org.rynware.client.feature.impl.movement.HighJump;
import org.rynware.client.feature.impl.movement.WaterSpeed;
import org.rynware.client.feature.impl.movement.Phase;
import org.rynware.client.feature.impl.movement.DamageFly;
import org.rynware.client.feature.impl.combat.HitBox;
import org.rynware.client.feature.impl.combat.AntiBot;
import org.rynware.client.feature.impl.combat.Velocity;
import org.rynware.client.feature.impl.combat.KillAura;
import org.rynware.client.feature.impl.combat.AutoTotem;
import org.rynware.client.feature.impl.combat.AutoGapple;
import org.rynware.client.feature.impl.combat.SuperItems;
import org.rynware.client.feature.impl.combat.AutoPotion;
import org.rynware.client.feature.impl.misc.FakeLag;
import org.rynware.client.feature.impl.combat.TriggerBot;
import org.rynware.client.feature.impl.combat.NoFriendDamage;
import org.rynware.client.feature.impl.combat.AppleGoldenTimer;
import org.rynware.client.feature.impl.misc.Disabler;
import org.rynware.client.feature.impl.misc.BetterChat;
import org.rynware.client.feature.impl.misc.NameProtect;
import org.rynware.client.feature.impl.misc.ChatHistory;
import org.rynware.client.feature.impl.misc.PlayerTracker;
import org.rynware.client.feature.impl.misc.ModuleSoundAlert;
import org.rynware.client.feature.impl.misc.MiddleClickFriend;
import org.rynware.client.feature.impl.hud.Hud;
import org.rynware.client.feature.impl.hud.ClickGUI;
import org.rynware.client.feature.impl.hud.NoOverlay;
import org.rynware.client.feature.impl.hud.TargetHUD;
import org.rynware.client.feature.impl.hud.Notifications;
import org.rynware.client.feature.impl.hud.FeatureList;
import java.util.concurrent.CopyOnWriteArrayList;

public class FeatureManager
{
    public CopyOnWriteArrayList<Feature> features;
    
    public FeatureManager() {
        (this.features = new CopyOnWriteArrayList<Feature>()).add(new FeatureList());
        this.features.add(new Notifications());
        this.features.add(new TargetHUD());
        this.features.add(new NoOverlay());
        this.features.add(new ClickGUI());
        this.features.add(new Hud());
        this.features.add(new MiddleClickFriend());
        this.features.add(new ModuleSoundAlert());
        this.features.add(new PlayerTracker());
        this.features.add(new ChatHistory());
        this.features.add(new NameProtect());
        this.features.add(new BetterChat());
        this.features.add(new Disabler());
        this.features.add(new AppleGoldenTimer());
        this.features.add(new NoFriendDamage());
        this.features.add(new TriggerBot());
        this.features.add(new FakeLag());
        this.features.add(new AutoPotion());
        this.features.add(new SuperItems());
        this.features.add(new AutoGapple());
        this.features.add(new AutoTotem());
        this.features.add(new KillAura());
        this.features.add(new Velocity());
        this.features.add(new AntiBot());
        this.features.add(new HitBox());
        this.features.add(new DamageFly());
        this.features.add(new Phase());
        this.features.add(new WaterSpeed());
        this.features.add(new HighJump());
        this.features.add(new Spider());
        this.features.add(new AirJump());
        this.features.add(new Flight());
        this.features.add(new Sprint());
        this.features.add(new Speed());
        this.features.add(new Jesus());
        this.features.add(new Strafe());
        this.features.add(new Timer());
        this.features.add(new NoWeb());
        this.features.add(new FastPlace());
        this.features.add(new NoServerRotations());
        this.features.add(new MiddleClickPearl());
        this.features.add(new DeathCoordinates());
        this.features.add(new ItemScroller());
        this.features.add(new BedrockClip());
        this.features.add(new AutoFarm());
        this.features.add(new GPS());
        this.features.add(new NoJumpDelay());
        this.features.add(new NoInteract());
        this.features.add(new NoSlowDown());
        this.features.add(new StaffAlert());
        this.features.add(new FreeCam());
        this.features.add(new AutoTool());
        this.features.add(new GuiWalk());
        this.features.add(new NoPush());
        this.features.add(new VclipHelper());
        this.features.add(new NoClip());
        this.features.add(new NoFall());
        this.features.add(new ScoreboardFeatures());
        this.features.add(new SwingAnimations());
        this.features.add(new WorldFeatures());
        this.features.add(new JumpCircles());
        this.features.add(new EntityESP());
        this.features.add(new Chams());
        this.features.add(new TrianglEsp());
        this.features.add(new FullBright());
        this.features.add(new TargetESP());
        this.features.add(new FogColor());
        this.features.add(new ClickTp());
        this.features.add(new TestFeature());
        this.features.add(new ChinaHat());
        this.features.add(new PearlESP());
        this.features.add(new NameTags());
        this.features.add(new Trails());
        this.features.add(new ItemESP());
        this.features.add(new BlockESP());
        this.features.add(new Tracers());
        this.features.add(new ViewModel());
        this.features.add(new XRay());
        this.features.sort(Comparator.comparingInt(m -> Minecraft.getMinecraft().rubik_17.getStringWidth(((Feature)m).getLabel())).reversed());
    }
    
    public List<Feature> getAllFeatures() {
        return this.features;
    }
    
    public List<Feature> getFeaturesCategory(final FeatureCategory category) {
        final List<Feature> features = new ArrayList<Feature>();
        for (final Feature feature : this.getAllFeatures()) {
            if (feature.getCategory() == category) {
                features.add(feature);
            }
        }
        return features;
    }
    
    public Feature getFeature(final Class<? extends Feature> classFeature) {
        for (final Feature feature : this.getAllFeatures()) {
            if (feature != null && feature.getClass() == classFeature) {
                return feature;
            }
        }
        return null;
    }
    
    public Feature getFeature(final String name) {
        for (final Feature feature : this.getAllFeatures()) {
            if (feature.getLabel().equals(name)) {
                return feature;
            }
        }
        return null;
    }
    
    public List<Feature> getFeatureList() {
        return this.features;
    }
}
