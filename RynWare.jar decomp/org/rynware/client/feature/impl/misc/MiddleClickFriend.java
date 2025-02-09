// 
// Decompiled by Procyon v0.6.0
// 

package org.rynware.client.feature.impl.misc;

import org.rynware.client.event.EventTarget;
import org.rynware.client.files.impl.FriendConfig;
import org.rynware.client.friend.Friend;
import org.rynware.client.utils.other.ChatUtils;
import com.mojang.realmsclient.gui.ChatFormatting;
import org.rynware.client.Main;
import net.minecraft.entity.EntityLivingBase;
import org.rynware.client.event.events.impl.input.EventMouse;
import org.rynware.client.feature.impl.FeatureCategory;
import org.rynware.client.feature.Feature;

public class MiddleClickFriend extends Feature
{
    public MiddleClickFriend() {
        super("MiddleClickFriend", FeatureCategory.Misc);
    }
    
    @EventTarget
    public void onMouseEvent(final EventMouse event) {
        if (event.getKey() == 2 && MiddleClickFriend.mc.pointedEntity instanceof EntityLivingBase) {
            if (Main.instance.friendManager.getFriends().stream().anyMatch(friend -> friend.getName().equals(MiddleClickFriend.mc.pointedEntity.getName()))) {
                Main.instance.friendManager.getFriends().remove(Main.instance.friendManager.getFriend(MiddleClickFriend.mc.pointedEntity.getName()));
                ChatUtils.addChatMessage(ChatFormatting.RED + "Removed " + ChatFormatting.RESET + "'" + MiddleClickFriend.mc.pointedEntity.getName() + "' as Friend!");
            }
            else {
                Main.instance.friendManager.addFriend(new Friend(MiddleClickFriend.mc.pointedEntity.getName()));
                try {
                    Main.instance.fileManager.getFile(FriendConfig.class).saveFile();
                }
                catch (final Exception exception) {
                    exception.printStackTrace();
                }
                ChatUtils.addChatMessage(ChatFormatting.GREEN + "Added " + ChatFormatting.RESET + "'" + MiddleClickFriend.mc.pointedEntity.getName() + "' as Friend!");
            }
        }
    }
}
