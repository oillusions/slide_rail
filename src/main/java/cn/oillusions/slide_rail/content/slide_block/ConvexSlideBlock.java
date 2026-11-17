package cn.oillusions.slide_rail.content.slide_block;

import cn.oillusions.slide_rail.content.foundation.DirectionAxisBlock;
import cn.oillusions.slide_rail.content.util.DirectionalAxisVoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class ConvexSlideBlock
    extends DirectionAxisBlock {
    public static final VoxelShape SHAPE_X = Shapes.or(
        Block.box(2, 2, 0, 14, 9, 16),
        Block.box(0, 12, 0, 16, 16, 16),
        Block.box(4, 9, 0, 12, 12, 16)
    );

    public static DirectionalAxisVoxelShaper SHAPER;

    public ConvexSlideBlock(Properties properties) {
        super(properties);
        SHAPER = new DirectionalAxisVoxelShaper(SHAPE_X);
    }

    @Override
    @NotNull
    protected VoxelShape getShape(
        @NotNull BlockState blockState,
        @NotNull BlockGetter blockGetter,
        @NotNull BlockPos blockPos,
        @NotNull CollisionContext context
    ) {
        Direction facing = blockState.getValue(FACING);
        Direction.Axis axis = blockState.getValue(AXIS);
        return SHAPER.get(facing, axis);
    }
}
