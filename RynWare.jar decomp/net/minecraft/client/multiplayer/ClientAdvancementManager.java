// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.multiplayer;

import org.apache.logging.log4j.LogManager;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketSeenAdvancements;
import java.util.Iterator;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.gui.toasts.AdvancementToast;
import net.minecraft.util.ResourceLocation;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import com.google.common.collect.Maps;
import javax.annotation.Nullable;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.Advancement;
import java.util.Map;
import net.minecraft.advancements.AdvancementList;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;

public class ClientAdvancementManager
{
    private static final Logger field_192800_a;
    private final Minecraft field_192801_b;
    private final AdvancementList field_192802_c;
    private final Map<Advancement, AdvancementProgress> field_192803_d;
    @Nullable
    private IListener field_192804_e;
    @Nullable
    private Advancement field_194231_f;
    
    public ClientAdvancementManager(final Minecraft p_i47380_1_) {
        this.field_192802_c = new AdvancementList();
        this.field_192803_d = Maps.newHashMap();
        this.field_192801_b = p_i47380_1_;
    }
    
    public void func_192799_a(final SPacketAdvancementInfo p_192799_1_) {
        if (p_192799_1_.func_192602_d()) {
            this.field_192802_c.func_192087_a();
            this.field_192803_d.clear();
        }
        this.field_192802_c.func_192085_a(p_192799_1_.func_192600_b());
        this.field_192802_c.func_192083_a(p_192799_1_.func_192603_a());
        for (final Map.Entry<ResourceLocation, AdvancementProgress> entry : p_192799_1_.func_192604_c().entrySet()) {
            final Advancement advancement = this.field_192802_c.func_192084_a(entry.getKey());
            if (advancement != null) {
                final AdvancementProgress advancementprogress = entry.getValue();
                advancementprogress.func_192099_a(advancement.func_192073_f(), advancement.func_192074_h());
                this.field_192803_d.put(advancement, advancementprogress);
                if (this.field_192804_e != null) {
                    this.field_192804_e.func_191933_a(advancement, advancementprogress);
                }
                if (p_192799_1_.func_192602_d() || !advancementprogress.func_192105_a() || advancement.func_192068_c() == null || !advancement.func_192068_c().func_193223_h()) {
                    continue;
                }
                this.field_192801_b.func_193033_an().func_192988_a(new AdvancementToast(advancement));
            }
            else {
                ClientAdvancementManager.field_192800_a.warn("Server informed client about progress for unknown advancement " + entry.getKey());
            }
        }
    }
    
    public AdvancementList func_194229_a() {
        return this.field_192802_c;
    }
    
    public void func_194230_a(@Nullable final Advancement p_194230_1_, final boolean p_194230_2_) {
        final NetHandlerPlayClient nethandlerplayclient = this.field_192801_b.getConnection();
        if (nethandlerplayclient != null && p_194230_1_ != null && p_194230_2_) {
            nethandlerplayclient.sendPacket(CPacketSeenAdvancements.func_194163_a(p_194230_1_));
        }
        if (this.field_194231_f != p_194230_1_) {
            this.field_194231_f = p_194230_1_;
            if (this.field_192804_e != null) {
                this.field_192804_e.func_193982_e(p_194230_1_);
            }
        }
    }
    
    public void func_192798_a(@Nullable final IListener p_192798_1_) {
        this.field_192804_e = p_192798_1_;
        this.field_192802_c.func_192086_a(p_192798_1_);
        if (p_192798_1_ != null) {
            for (final Map.Entry<Advancement, AdvancementProgress> entry : this.field_192803_d.entrySet()) {
                p_192798_1_.func_191933_a(entry.getKey(), entry.getValue());
            }
            p_192798_1_.func_193982_e(this.field_194231_f);
        }
    }
    
    static {
        field_192800_a = LogManager.getLogger();
    }
    
    public interface IListener extends AdvancementList.Listener
    {
        void func_191933_a(final Advancement p0, final AdvancementProgress p1);
        
        void func_193982_e(final Advancement p0);
    }
}
