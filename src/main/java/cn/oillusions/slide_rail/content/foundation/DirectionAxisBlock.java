package cn.oillusions.slide_rail.content.foundation;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;

public class DirectionAxisBlock extends KineticBlock
    implements IWrenchable {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;

    public DirectionAxisBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any()
            .setValue(FACING, Direction.EAST)
            .setValue(AXIS, Direction.Axis.Y));
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, AXIS);
    }

    @Override
    @NotNull
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        Player player = context.getPlayer();
        if (player != null) {
            if (player.isShiftKeyDown()) {
                Direction facing = context.getClickedFace();
                Direction.Axis axis = context.getNearestLookingDirection().getAxis();
                if (facing == Direction.DOWN || facing == Direction.UP) {
                    axis = context.getHorizontalDirection().getAxis();
                }
                return defaultBlockState()
                    .setValue(FACING, facing)
                    .setValue(AXIS, axis);
            } else {
                Direction facing = context.getClickedFace() == Direction.DOWN
                    ? Direction.DOWN
                    : Direction.UP;
                Direction.Axis axis = context.getHorizontalDirection().getAxis();
                return defaultBlockState()
                    .setValue(FACING, facing)
                    .setValue(AXIS, axis);
            }
        }
        return defaultBlockState()
            .setValue(FACING, Direction.UP)
            .setValue(AXIS, Direction.Axis.X);
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState blockState) {
        return blockState.getValue(AXIS);
    }
}
