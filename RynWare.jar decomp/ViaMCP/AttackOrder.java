// 
// Decompiled by Procyon v0.6.0
// 

package ViaMCP;

import ViaMCP.protocols.ProtocolCollection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.client.Minecraft;

public class AttackOrder
{
    private static final Minecraft mc;
    private static final int VER_1_8_ID = 47;
    
    public static void sendConditionalSwing(final RayTraceResult ray, final EnumHand enumHand) {
        if (ray != null && ray.typeOfHit != RayTraceResult.Type.ENTITY) {
            AttackOrder.mc.player.swingArm(enumHand);
        }
    }
    
    public static void sendFixedAttack(final EntityPlayer entityIn, final Entity target, final EnumHand enumHand) {
        if (ViaMCP.getInstance().getVersion() <= ProtocolCollection.getProtocolById(47).getVersion()) {
            send1_8Attack(entityIn, target, enumHand);
        }
        else {
            send1_9Attack(entityIn, target, enumHand);
        }
    }
    
    private static void send1_8Attack(final EntityPlayer entityIn, final Entity target, final EnumHand enumHand) {
        AttackOrder.mc.player.swingArm(enumHand);
        AttackOrder.mc.playerController.attackEntity(entityIn, target);
    }
    
    private static void send1_9Attack(final EntityPlayer entityIn, final Entity target, final EnumHand enumHand) {
        AttackOrder.mc.playerController.attackEntity(entityIn, target);
        AttackOrder.mc.player.swingArm(enumHand);
    }
    
    static {
        mc = Minecraft.getMinecraft();
    }
}
