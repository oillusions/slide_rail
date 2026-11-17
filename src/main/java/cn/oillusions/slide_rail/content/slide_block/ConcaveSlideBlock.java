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

public class ConcaveSlideBlock
    extends DirectionAxisBlock {
    public static final VoxelShape SHAPE_X = Shapes.or(
        Block.box(0, 12, 0, 16, 16, 16),
        Block.box(0, 2, 0, 2, 12, 16),
        Block.box(14, 2, 0, 16, 12, 16),
        Block.box(12, 2, 0, 14, 5, 16),
        Block.box(2, 2, 0, 4, 5, 16)
    );

    public static DirectionalAxisVoxelShaper SHAPER;

    public ConcaveSlideBlock(Properties properties) {
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
