// 
// Decompiled by Procyon v0.6.0
// 

package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Maps;
import optifine.Reflector;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.common.model.IModelPart;
import java.util.Optional;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Matrix4f;
import java.util.Map;
import net.minecraftforge.common.model.ITransformation;
import net.minecraftforge.common.model.IModelState;

public enum ModelRotation implements IModelState, ITransformation
{
    X0_Y0(0, 0), 
    X0_Y90(0, 90), 
    X0_Y180(0, 180), 
    X0_Y270(0, 270), 
    X90_Y0(90, 0), 
    X90_Y90(90, 90), 
    X90_Y180(90, 180), 
    X90_Y270(90, 270), 
    X180_Y0(180, 0), 
    X180_Y90(180, 90), 
    X180_Y180(180, 180), 
    X180_Y270(180, 270), 
    X270_Y0(270, 0), 
    X270_Y90(270, 90), 
    X270_Y180(270, 180), 
    X270_Y270(270, 270);
    
    private static final Map<Integer, ModelRotation> MAP_ROTATIONS;
    private final int combinedXY;
    private final Matrix4f matrix4d;
    private final int quartersX;
    private final int quartersY;
    
    private static int combineXY(final int p_177521_0_, final int p_177521_1_) {
        return p_177521_0_ * 360 + p_177521_1_;
    }
    
    private ModelRotation(final int x, final int y) {
        this.combinedXY = combineXY(x, y);
        this.matrix4d = new Matrix4f();
        final Matrix4f matrix4f = new Matrix4f();
        matrix4f.setIdentity();
        Matrix4f.rotate(-x * 0.017453292f, new Vector3f(1.0f, 0.0f, 0.0f), matrix4f, matrix4f);
        this.quartersX = MathHelper.abs(x / 90);
        final Matrix4f matrix4f2 = new Matrix4f();
        matrix4f2.setIdentity();
        Matrix4f.rotate(-y * 0.017453292f, new Vector3f(0.0f, 1.0f, 0.0f), matrix4f2, matrix4f2);
        this.quartersY = MathHelper.abs(y / 90);
        Matrix4f.mul(matrix4f2, matrix4f, this.matrix4d);
    }
    
    public Matrix4f getMatrix4d() {
        return this.matrix4d;
    }
    
    public EnumFacing rotateFace(final EnumFacing facing) {
        EnumFacing enumfacing = facing;
        for (int i = 0; i < this.quartersX; ++i) {
            enumfacing = enumfacing.rotateAround(EnumFacing.Axis.X);
        }
        if (enumfacing.getAxis() != EnumFacing.Axis.Y) {
            for (int j = 0; j < this.quartersY; ++j) {
                enumfacing = enumfacing.rotateAround(EnumFacing.Axis.Y);
            }
        }
        return enumfacing;
    }
    
    public int rotateVertex(final EnumFacing facing, final int vertexIndex) {
        int i = vertexIndex;
        if (facing.getAxis() == EnumFacing.Axis.X) {
            i = (vertexIndex + this.quartersX) % 4;
        }
        EnumFacing enumfacing = facing;
        for (int j = 0; j < this.quartersX; ++j) {
            enumfacing = enumfacing.rotateAround(EnumFacing.Axis.X);
        }
        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            i = (i + this.quartersY) % 4;
        }
        return i;
    }
    
    public static ModelRotation getModelRotation(final int x, final int y) {
        return ModelRotation.MAP_ROTATIONS.get(combineXY(MathHelper.normalizeAngle(x, 360), MathHelper.normalizeAngle(y, 360)));
    }
    
    @Override
    public Optional<TRSRTransformation> apply(final Optional<? extends IModelPart> p_apply_1_) {
        return (Optional)Reflector.call(Reflector.ForgeHooksClient_applyTransform, this.getMatrix(), p_apply_1_);
    }
    
    @Override
    public javax.vecmath.Matrix4f getMatrix() {
        return (javax.vecmath.Matrix4f)(Reflector.ForgeHooksClient_getMatrix.exists() ? Reflector.call(Reflector.ForgeHooksClient_getMatrix, this) : new javax.vecmath.Matrix4f());
    }
    
    @Override
    public EnumFacing rotate(final EnumFacing p_rotate_1_) {
        return this.rotateFace(p_rotate_1_);
    }
    
    @Override
    public int rotate(final EnumFacing p_rotate_1_, final int p_rotate_2_) {
        return this.rotateVertex(p_rotate_1_, p_rotate_2_);
    }
    
    static {
        MAP_ROTATIONS = Maps.newHashMap();
        for (final ModelRotation modelrotation : values()) {
            ModelRotation.MAP_ROTATIONS.put(modelrotation.combinedXY, modelrotation);
        }
    }
}
