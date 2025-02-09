// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.gui;

import com.google.common.collect.ComparisonChain;
import java.util.Comparator;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.world.GameType;
import net.minecraft.util.text.TextFormatting;
import org.rynware.client.feature.Feature;
import org.rynware.client.feature.impl.misc.NameProtect;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraft.util.math.MathHelper;
import org.rynware.client.Main;
import net.minecraft.scoreboard.IScoreCriteria;
import javax.annotation.Nullable;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.scoreboard.ScorePlayerTeam;
import java.util.Iterator;
import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import java.util.List;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import com.google.common.collect.Ordering;

public class GuiPlayerTabOverlay extends Gui
{
    public static Ordering<NetworkPlayerInfo> ENTRY_ORDERING;
    private static Minecraft mc;
    private final GuiIngame guiIngame;
    private ITextComponent footer;
    private ITextComponent header;
    public float posAnimationY;
    public float addition;
    private long lastTimeOpened;
    private boolean isBeingRendered;
    
    public GuiPlayerTabOverlay(final Minecraft mcIn, final GuiIngame guiIngameIn) {
        this.addition = 0.0f;
        GuiPlayerTabOverlay.mc = mcIn;
        this.guiIngame = guiIngameIn;
    }
    
    public static List<EntityPlayer> getPlayers() {
        final List<NetworkPlayerInfo> list = GuiPlayerTabOverlay.ENTRY_ORDERING.sortedCopy((Iterable)Minecraft.getMinecraft().player.connection.getPlayerInfoMap());
        final ArrayList<EntityPlayer> players = new ArrayList<EntityPlayer>();
        for (final NetworkPlayerInfo player : list) {
            if (player == null) {
                continue;
            }
            players.add(Minecraft.getMinecraft().world.getPlayerEntityByName(player.getGameProfile().getName()));
        }
        return players;
    }
    
    public String getPlayerName(final NetworkPlayerInfo networkPlayerInfoIn) {
        return (networkPlayerInfoIn.getDisplayName() != null) ? networkPlayerInfoIn.getDisplayName().getFormattedText() : ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName());
    }
    
    public void updatePlayerList(final boolean willBeRendered) {
        if (willBeRendered && !this.isBeingRendered) {
            this.lastTimeOpened = Minecraft.getSystemTime();
        }
        this.isBeingRendered = willBeRendered;
    }
    
    public void renderPlayerlist(final int width, final Scoreboard scoreboardIn, @Nullable final ScoreObjective scoreObjectiveIn) {
        final NetHandlerPlayClient nethandlerplayclient = GuiPlayerTabOverlay.mc.player.connection;
        List<NetworkPlayerInfo> list = GuiPlayerTabOverlay.ENTRY_ORDERING.sortedCopy((Iterable)nethandlerplayclient.getPlayerInfoMap());
        int i = 0;
        int j = 0;
        for (final NetworkPlayerInfo networkplayerinfo : list) {
            int k = GuiPlayerTabOverlay.mc.fontRendererObj.getStringWidth(this.getPlayerName(networkplayerinfo));
            i = Math.max(i, k);
            if (scoreObjectiveIn != null && scoreObjectiveIn.getRenderType() != IScoreCriteria.EnumRenderType.HEARTS) {
                k = GuiPlayerTabOverlay.mc.fontRendererObj.getStringWidth(" " + scoreboardIn.getOrCreateScore(networkplayerinfo.getGameProfile().getName(), scoreObjectiveIn).getScorePoints());
                j = Math.max(j, k);
            }
        }
        list = list.subList(0, Math.min(list.size(), 80));
        int i2;
        int l3;
        int j2;
        for (l3 = (i2 = list.size()), j2 = 1; i2 > 20; i2 = (l3 + j2 - 1) / j2) {
            ++j2;
        }
        final boolean flag = GuiPlayerTabOverlay.mc.isIntegratedServerRunning() || GuiPlayerTabOverlay.mc.getConnection().getNetworkManager().isEncrypted();
        int m;
        if (scoreObjectiveIn != null) {
            if (scoreObjectiveIn.getRenderType() == IScoreCriteria.EnumRenderType.HEARTS) {
                m = 90;
            }
            else {
                m = j;
            }
        }
        else {
            m = 0;
        }
        final int i3 = Math.min(j2 * ((flag ? 9 : 0) + i + m + 13), width - 50) / j2;
        final int j3 = width / 2 - (i3 * j2 + (j2 - 1) * 5) / 2;
        int k2 = 10;
        int l4 = i3 * j2 + (j2 - 1) * 5;
        List<String> list2 = null;
        final ScaledResolution sr = new ScaledResolution(GuiPlayerTabOverlay.mc);
        if (GuiPlayerTabOverlay.mc.gameSettings.keyBindPlayerList.isKeyDown()) {
            if (this.addition < 1.0f) {
                this.addition += (float)(1.5 * Main.deltaTime());
            }
        }
        else if (this.addition > 0.0f) {
            this.addition -= (float)(1.5 * Main.deltaTime());
        }
        this.addition = MathHelper.clamp(0.0f, 1.0f, this.addition);
        this.posAnimationY = MathHelper.EaseOutBack((float)(-sr.getScaledHeight()), (float)k2, this.addition);
        GL11.glPushMatrix();
        GlStateManager.translate(width / 2.0f, this.posAnimationY / 2.0f, 1.0f);
        GlStateManager.translate(-width / 2.0f, this.posAnimationY / 2.0f, 1.0f);
        if (this.header != null) {
            list2 = GuiPlayerTabOverlay.mc.fontRendererObj.listFormattedStringToWidth(this.header.getFormattedText(), width - 50);
            for (final String s : list2) {
                l4 = Math.max(l4, GuiPlayerTabOverlay.mc.fontRendererObj.getStringWidth(s));
            }
        }
        List<String> list3 = null;
        if (this.footer != null) {
            list3 = GuiPlayerTabOverlay.mc.fontRendererObj.listFormattedStringToWidth(this.footer.getFormattedText(), width - 50);
            for (final String s2 : list3) {
                l4 = Math.max(l4, GuiPlayerTabOverlay.mc.fontRendererObj.getStringWidth(s2));
            }
        }
        if (list2 != null) {
            Gui.drawRect(width / 2 - l4 / 2 - 1, k2 - 1, width / 2 + l4 / 2 + 1, k2 + list2.size() * GuiPlayerTabOverlay.mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);
            for (final String s3 : list2) {
                final int i4 = GuiPlayerTabOverlay.mc.fontRendererObj.getStringWidth(s3);
                GuiPlayerTabOverlay.mc.fontRendererObj.drawStringWithShadow(s3, (float)(width / 2 - i4 / 2), (float)k2, -1);
                k2 += GuiPlayerTabOverlay.mc.fontRendererObj.FONT_HEIGHT;
            }
            ++k2;
        }
        Gui.drawRect(width / 2 - l4 / 2 - 1, k2 - 1, width / 2 + l4 / 2 + 1, k2 + i2 * 9, Integer.MIN_VALUE);
        for (int k3 = 0; k3 < l3; ++k3) {
            final int l5 = k3 / i2;
            final int i5 = k3 % i2;
            int j4 = j3 + l5 * i3 + l5 * 5;
            final int k4 = k2 + i5 * 9;
            Gui.drawRect(j4, k4, j4 + i3, k4 + 8, 553648127);
            GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
            GlStateManager.enableAlpha();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            if (k3 < list.size()) {
                final NetworkPlayerInfo networkplayerinfo2 = list.get(k3);
                final GameProfile gameprofile = networkplayerinfo2.getGameProfile();
                if (flag) {
                    final EntityPlayer entityplayer = GuiPlayerTabOverlay.mc.world.getPlayerEntityByUUID(gameprofile.getId());
                    final boolean flag2 = entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.CAPE) && ("Dinnerbone".equals(gameprofile.getName()) || "Grumm".equals(gameprofile.getName()));
                    GuiPlayerTabOverlay.mc.getTextureManager().bindTexture(networkplayerinfo2.getLocationSkin());
                    final int l6 = 8 + (flag2 ? 8 : 0);
                    final int i6 = 8 * (flag2 ? -1 : 1);
                    Gui.drawScaledCustomSizeModalRect((float)j4, (float)k4, 8.0f, (float)l6, 8, i6, 8, 8, 64.0f, 64.0f);
                    if (entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.HAT)) {
                        final int j5 = 8 + (flag2 ? 8 : 0);
                        final int k5 = 8 * (flag2 ? -1 : 1);
                        Gui.drawScaledCustomSizeModalRect((float)j4, (float)k4, 40.0f, (float)j5, 8, k5, 8, 8, 64.0f, 64.0f);
                    }
                    j4 += 9;
                }
                String s4 = this.getPlayerName(networkplayerinfo2);
                if (Main.instance.featureManager.getFeature(NameProtect.class).isEnabled()) {
                    if (NameProtect.tabSpoof.getBoolValue() && !s4.contains(GuiPlayerTabOverlay.mc.player.getName())) {
                        s4 = "§aProtected";
                    }
                    if (NameProtect.myName.getBoolValue()) {
                        s4 = s4.replace(GuiPlayerTabOverlay.mc.player.getName(), TextFormatting.GREEN + "Protected");
                    }
                }
                if (networkplayerinfo2.getGameType() == GameType.SPECTATOR) {
                    GuiPlayerTabOverlay.mc.fontRendererObj.drawStringWithShadow(TextFormatting.ITALIC + s4, (float)j4, (float)k4, -1862270977);
                }
                else {
                    GuiPlayerTabOverlay.mc.fontRendererObj.drawStringWithShadow(s4, (float)j4, (float)k4, -1);
                }
                if (scoreObjectiveIn != null && networkplayerinfo2.getGameType() != GameType.SPECTATOR) {
                    final int k6 = j4 + i + 1;
                    final int l7 = k6 + m;
                    if (l7 - k6 > 5) {
                        this.drawScoreboardValues(scoreObjectiveIn, k4, gameprofile.getName(), k6, l7, networkplayerinfo2);
                    }
                }
                this.drawPing(i3, j4 - (flag ? 9 : 0), k4, networkplayerinfo2);
            }
        }
        if (list3 != null) {
            k2 = k2 + i2 * 9 + 1;
            Gui.drawRect(width / 2 - l4 / 2 - 1, k2 - 1, width / 2 + l4 / 2 + 1, k2 + list3.size() * GuiPlayerTabOverlay.mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);
            for (final String s5 : list3) {
                final int j6 = GuiPlayerTabOverlay.mc.fontRendererObj.getStringWidth(s5);
                GuiPlayerTabOverlay.mc.fontRendererObj.drawStringWithShadow(s5, (float)(width / 2 - j6 / 2), (float)k2, -1);
                k2 += GuiPlayerTabOverlay.mc.fontRendererObj.FONT_HEIGHT;
            }
        }
        GlStateManager.popMatrix();
    }
    
    protected void drawPing(final int p_175245_1_, final int p_175245_2_, final int p_175245_3_, final NetworkPlayerInfo networkPlayerInfoIn) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GuiPlayerTabOverlay.mc.getTextureManager().bindTexture(GuiPlayerTabOverlay.ICONS);
        final int i = 0;
        int j;
        if (networkPlayerInfoIn.getResponseTime() < 0) {
            j = 5;
        }
        else if (networkPlayerInfoIn.getResponseTime() < 150) {
            j = 0;
        }
        else if (networkPlayerInfoIn.getResponseTime() < 300) {
            j = 1;
        }
        else if (networkPlayerInfoIn.getResponseTime() < 600) {
            j = 2;
        }
        else if (networkPlayerInfoIn.getResponseTime() < 1000) {
            j = 3;
        }
        else {
            j = 4;
        }
        GuiPlayerTabOverlay.zLevel += 100.0f;
        this.drawTexturedModalRect(p_175245_2_ + p_175245_1_ - 11, p_175245_3_, 0, 176 + j * 8, 10, 8);
        GuiPlayerTabOverlay.zLevel -= 100.0f;
    }
    
    private void drawScoreboardValues(final ScoreObjective objective, final int p_175247_2_, final String name, final int p_175247_4_, final int p_175247_5_, final NetworkPlayerInfo info) {
        final int i = objective.getScoreboard().getOrCreateScore(name, objective).getScorePoints();
        if (objective.getRenderType() == IScoreCriteria.EnumRenderType.HEARTS) {
            GuiPlayerTabOverlay.mc.getTextureManager().bindTexture(GuiPlayerTabOverlay.ICONS);
            if (this.lastTimeOpened == info.getRenderVisibilityId()) {
                if (i < info.getLastHealth()) {
                    info.setLastHealthTime(Minecraft.getSystemTime());
                    info.setHealthBlinkTime(this.guiIngame.getUpdateCounter() + 20);
                }
                else if (i > info.getLastHealth()) {
                    info.setLastHealthTime(Minecraft.getSystemTime());
                    info.setHealthBlinkTime(this.guiIngame.getUpdateCounter() + 10);
                }
            }
            if (Minecraft.getSystemTime() - info.getLastHealthTime() > 1000L || this.lastTimeOpened != info.getRenderVisibilityId()) {
                info.setLastHealth(i);
                info.setDisplayHealth(i);
                info.setLastHealthTime(Minecraft.getSystemTime());
            }
            info.setRenderVisibilityId(this.lastTimeOpened);
            info.setLastHealth(i);
            final int j = MathHelper.ceil(Math.max(i, info.getDisplayHealth()) / 2.0f);
            final int k = Math.max(MathHelper.ceil((float)(i / 2)), Math.max(MathHelper.ceil((float)(info.getDisplayHealth() / 2)), 10));
            final boolean flag = info.getHealthBlinkTime() > this.guiIngame.getUpdateCounter() && (info.getHealthBlinkTime() - this.guiIngame.getUpdateCounter()) / 3L % 2L == 1L;
            if (j > 0) {
                final float f = Math.min((p_175247_5_ - p_175247_4_ - 4) / (float)k, 9.0f);
                if (f > 3.0f) {
                    for (int l = j; l < k; ++l) {
                        this.drawTexturedModalRect(p_175247_4_ + l * f, (float)p_175247_2_, flag ? 25 : 16, 0, 9, 9);
                    }
                    for (int j2 = 0; j2 < j; ++j2) {
                        this.drawTexturedModalRect(p_175247_4_ + j2 * f, (float)p_175247_2_, flag ? 25 : 16, 0, 9, 9);
                        if (flag) {
                            if (j2 * 2 + 1 < info.getDisplayHealth()) {
                                this.drawTexturedModalRect(p_175247_4_ + j2 * f, (float)p_175247_2_, 70, 0, 9, 9);
                            }
                            if (j2 * 2 + 1 == info.getDisplayHealth()) {
                                this.drawTexturedModalRect(p_175247_4_ + j2 * f, (float)p_175247_2_, 79, 0, 9, 9);
                            }
                        }
                        if (j2 * 2 + 1 < i) {
                            this.drawTexturedModalRect(p_175247_4_ + j2 * f, (float)p_175247_2_, (j2 >= 10) ? 160 : 52, 0, 9, 9);
                        }
                        if (j2 * 2 + 1 == i) {
                            this.drawTexturedModalRect(p_175247_4_ + j2 * f, (float)p_175247_2_, (j2 >= 10) ? 169 : 61, 0, 9, 9);
                        }
                    }
                }
                else {
                    final float f2 = MathHelper.clamp(i / 20.0f, 0.0f, 1.0f);
                    final int i2 = (int)((1.0f - f2) * 255.0f) << 16 | (int)(f2 * 255.0f) << 8;
                    String s = "" + i / 2.0f;
                    if (p_175247_5_ - GuiPlayerTabOverlay.mc.fontRendererObj.getStringWidth(s + "hp") >= p_175247_4_) {
                        s += "hp";
                    }
                    GuiPlayerTabOverlay.mc.fontRendererObj.drawStringWithShadow(s, (float)((p_175247_5_ + p_175247_4_) / 2 - GuiPlayerTabOverlay.mc.fontRendererObj.getStringWidth(s) / 2), (float)p_175247_2_, i2);
                }
            }
        }
        else {
            final String s2 = TextFormatting.YELLOW + "" + i;
            GuiPlayerTabOverlay.mc.fontRendererObj.drawStringWithShadow(s2, (float)(p_175247_5_ - GuiPlayerTabOverlay.mc.fontRendererObj.getStringWidth(s2)), (float)p_175247_2_, 16777215);
        }
    }
    
    public void setFooter(@Nullable final ITextComponent footerIn) {
        this.footer = footerIn;
    }
    
    public void setHeader(@Nullable final ITextComponent headerIn) {
        this.header = headerIn;
    }
    
    public void resetFooterHeader() {
        this.header = null;
        this.footer = null;
    }
    
    static {
        GuiPlayerTabOverlay.ENTRY_ORDERING = (Ordering<NetworkPlayerInfo>)Ordering.from((Comparator)new PlayerComparator());
        GuiPlayerTabOverlay.mc = Minecraft.getMinecraft();
    }
    
    static class PlayerComparator implements Comparator<NetworkPlayerInfo>
    {
        private PlayerComparator() {
        }
        
        @Override
        public int compare(final NetworkPlayerInfo p_compare_1_, final NetworkPlayerInfo p_compare_2_) {
            final ScorePlayerTeam scoreplayerteam = p_compare_1_.getPlayerTeam();
            final ScorePlayerTeam scoreplayerteam2 = p_compare_2_.getPlayerTeam();
            return ComparisonChain.start().compareTrueFirst(p_compare_1_.getGameType() != GameType.SPECTATOR, p_compare_2_.getGameType() != GameType.SPECTATOR).compare((Comparable)((scoreplayerteam != null) ? scoreplayerteam.getRegisteredName() : ""), (Comparable)((scoreplayerteam2 != null) ? scoreplayerteam2.getRegisteredName() : "")).compare((Comparable)p_compare_1_.getGameProfile().getName(), (Comparable)p_compare_2_.getGameProfile().getName()).result();
        }
    }
}
