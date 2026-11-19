package cn.oillusions.slide_rail.content.belt_fixing_clip;

import cn.oillusions.slide_rail.AllBlockEntities;
import cn.oillusions.slide_rail.content.foundation.FullyOrientedBlock;
import cn.oillusions.slide_rail.content.util.DirectionalAxisVoxelShaper;
import com.simibubi.create.foundation.block.IBE;
import dev.ryanhcode.sable.api.block.BlockSubLevelCollisionShape;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class BeltFixingClipBlock
    extends FullyOrientedBlock
    implements IBE<BeltFixingClipBlockEntity>,
    BlockSubLevelCollisionShape {

    public static final VoxelShape SHAPE_X = Shapes.or(
        Block.box(0, 13, 0, 16, 16, 16),
        Block.box(3, 8, 3, 13, 11, 13),
        Block.box(6, 11, 6, 10, 13, 10)
    );

    public static final VoxelShape SUBLEVEL_SHAPE_X = Shapes.or(
        Block.box(0, 13, 0, 16, 16, 16)
    );

    public final DirectionalAxisVoxelShaper SHAPER;
    public final DirectionalAxisVoxelShaper SUBLEVEL_SHAPER;

    public BeltFixingClipBlock(Properties properties) {
        super(properties);
        SHAPER = new DirectionalAxisVoxelShaper(SHAPE_X);
        SUBLEVEL_SHAPER = new DirectionalAxisVoxelShaper(SUBLEVEL_SHAPE_X);
    }

    @Override
    public Class<BeltFixingClipBlockEntity> getBlockEntityClass() {
        return BeltFixingClipBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends BeltFixingClipBlockEntity> getBlockEntityType() {
        return AllBlockEntities.BELT_FIXING_CLIP_BLOCK_ENTITY.get();
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
        boolean rotation = blockState.getValue(ROTATION);
        return SHAPER.get(facing, rotation);
    }

    @Override
    @NotNull
    public VoxelShape getSubLevelCollisionShape(
        @NotNull BlockGetter blockGetter,
        @NotNull BlockState blockState
    ) {
        Direction facing = blockState.getValue(FACING);
        boolean rotation = blockState.getValue(ROTATION);
        return SUBLEVEL_SHAPER.get(facing, rotation);
    }
}
