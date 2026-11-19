package cn.oillusions.slide_rail.content.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;

import java.util.function.Function;

public class Utils {
    public static BlockPos vec3ToBlockPos(final Vec3 vec3) {
        return new BlockPos((int) round(vec3.x()), (int) round(vec3.y()), (int) round(vec3.z()));
    }

    public static BlockPos vec3ToBlockPos(final Vec3 vec, @Nullable Function<Double, Double> func) {
        if (func != null) {
            return new BlockPos((int) func.apply(vec.x()).doubleValue(), (int) func.apply(vec.y()).doubleValue(), (int) func.apply(vec.z()).doubleValue());
        } else {
            return new BlockPos((int) vec.x(), (int) vec.y(), (int) vec.z());
        }
    }

    public static BlockPos vector3dToBlockPos(final Vector3d vector) {
        return vector3dToBlockPos(vector, null);
    }

    public static BlockPos vector3dToBlockPos(final Vector3d vector, @Nullable Function<Double, Double> func) {
        if (func != null) {
            return new BlockPos((int) func.apply(vector.x()).doubleValue(), (int) func.apply(vector.y()).doubleValue(), (int) func.apply(vector.z()).doubleValue());
        } else {
            return new BlockPos((int) vector.x(), (int) vector.y(), (int) vector.z());
        }
    }

    public static Vec3 vector3dToVec3(final Vector3d vector) {
        return new Vec3(vector.x(), vector.y(), vector.z());
    }

    public static Vector3d blockPosToVector3d(BlockPos blockPos) {
        return new Vector3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static Vector3d vec3ToVector3d(final Vec3 vec3) {
        return new Vector3d(vec3.x(), vec3.y(), vec3.z());
    }

    public static Vector3d vec3iToVector3d(final Vec3i vec3i) {
        return new Vector3d(vec3i.getX(), vec3i.getY(), vec3i.getZ());
    }

    public static Vector3d round(Vector3d vector) {
        return vector.set(round(vector.x()), round(vector.y()), round(vector.z()));
    }

    public static double round(double v) {
        return v >= 0 ? Math.round(v) : -Math.round(-v);
    }
}
