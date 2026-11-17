package cn.oillusions.slide_rail.content.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;

public class Utils {
    public static BlockPos vec3ToBlockPos(final Vec3 vec3) {
        return new BlockPos((int) round(vec3.x()), (int) round(vec3.y()), (int) round(vec3.z()));
    }

    public static BlockPos vector3dToBlockPos(final Vector3d vector3d) {
        return new BlockPos((int) round(vector3d.x()), (int) round(vector3d.y()), (int) round(vector3d.z()));
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

    private static double round(double v) {
        return v >= 0 ? Math.round(v) : -Math.round(-v);
    }
}
