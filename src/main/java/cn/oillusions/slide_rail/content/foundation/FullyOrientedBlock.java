package cn.oillusions.slide_rail.content.foundation;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.KineticBlock;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;

public class FullyOrientedBlock extends KineticBlock
    implements IWrenchable {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    public static final BooleanProperty ROTATION = BooleanProperty.create("rotation");

    public FullyOrientedBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any()
            .setValue(FACING, Direction.EAST)
            .setValue(ROTATION, false));
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ROTATION);
    }

    @Override
    @NotNull
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        Player player = context.getPlayer();
        if (player != null) {
            if (player.isShiftKeyDown()) {
                Direction facing = context.getClickedFace();

                boolean rotation;
                switch (facing) {
                    case UP, DOWN -> rotation = context.getHorizontalDirection().getAxis() == Direction.Axis.Z;
                    default -> rotation = context.getNearestLookingDirection().getAxis() == Direction.Axis.Y;
                }

                return defaultBlockState()
                    .setValue(FACING, facing)
                    .setValue(ROTATION, rotation);
            } else {
                Direction facing;
                facing = context.getClickedFace();

                boolean rotation;
                switch (facing) {
                    case UP, DOWN -> rotation = context.getHorizontalDirection().getAxis() == Direction.Axis.Z;
                    default -> rotation = context.getNearestLookingDirection().getAxis() == Direction.Axis.Y;
                }

                return defaultBlockState()
                    .setValue(FACING, facing)
                    .setValue(ROTATION, rotation);
            }
        }
        return defaultBlockState();
    }

    @Override
    @NotNull
    public Direction.Axis getRotationAxis(@NotNull BlockState blockState) {
        return blockState.getValue(FACING).getAxis();
    }

    @Override
    public InteractionResult onWrenched(BlockState blockState, UseOnContext context) {
        if (context.getClickedFace() == blockState.getValue(FACING)) {
            context.getLevel().setBlock(context.getClickedPos(), blockState.setValue(ROTATION, !blockState.getValue(ROTATION)), Block.UPDATE_ALL);
            return InteractionResult.SUCCESS;
        } else {
            return super.onWrenched(blockState, context);
        }
    }
}
