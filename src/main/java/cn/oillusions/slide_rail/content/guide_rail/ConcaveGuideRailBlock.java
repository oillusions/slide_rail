package cn.oillusions.slide_rail.content.guide_rail;

import cn.oillusions.slide_rail.content.foundation.FullyOrientedBlock;
import cn.oillusions.slide_rail.content.util.DirectionalAxisVoxelShaper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.createmod.catnip.ghostblock.GhostBlockParams;
import net.createmod.catnip.ghostblock.GhostBlockRenderer;
import net.createmod.catnip.render.SuperRenderTypeBuffer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class ConcaveGuideRailBlock
    extends FullyOrientedBlock {
    public static final VoxelShape SHAPE_X = Shapes.or(
        Block.box(0, 0, 0, 16, 2, 16),
        Block.box(0, 2, 0, 2, 12, 16),
        Block.box(14, 2, 0, 16, 12, 16),
        Block.box(12, 9, 0, 14, 12, 16),
        Block.box(2, 9, 0, 4, 12, 16)
    );

    public static DirectionalAxisVoxelShaper SHAPER;


    public ConcaveGuideRailBlock(Properties properties) {
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
        boolean rotation = blockState.getValue(ROTATION);
        return SHAPER.get(facing, rotation);
    }
}
