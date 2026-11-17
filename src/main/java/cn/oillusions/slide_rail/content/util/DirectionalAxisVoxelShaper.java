package cn.oillusions.slide_rail.content.util;

import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DirectionalAxisVoxelShaper {
    public final VoxelShaper SHAPER_UP_X;
    public final VoxelShaper SHAPER_UP_Z;

    public DirectionalAxisVoxelShaper(
        VoxelShape shapeX
    ) {
        VoxelShaper shaperX = VoxelShaper.forDirectional(shapeX, Direction.WEST);
        this.SHAPER_UP_X = VoxelShaper.forDirectional(shaperX.get(Direction.Axis.X), Direction.UP);
        this.SHAPER_UP_Z = VoxelShaper.forDirectional(shaperX.get(Direction.Axis.Z), Direction.UP);
    }

    public VoxelShape get(Direction facing, Direction.Axis axis) {
        switch (facing) {
            case UP, DOWN, SOUTH, NORTH -> {
                return axis == Direction.Axis.X
                    ? SHAPER_UP_Z.get(facing)
                    : SHAPER_UP_X.get(facing);
            }
            case EAST, WEST -> {
                return axis == Direction.Axis.X
                    ? SHAPER_UP_X.get(facing)
                    : SHAPER_UP_Z.get(facing);
            }
        }
        return SHAPER_UP_X.get(facing);
    }
}
