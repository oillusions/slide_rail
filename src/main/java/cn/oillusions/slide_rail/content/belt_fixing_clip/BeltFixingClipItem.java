package cn.oillusions.slide_rail.content.belt_fixing_clip;

import cn.oillusions.slide_rail.AllBlocks;
import cn.oillusions.slide_rail.content.util.Utils;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.SubLevelAssemblyHelper;
import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.companion.math.Pose3d;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.SubLevel;
import dev.ryanhcode.sable.sublevel.plot.LevelPlot;
import dev.ryanhcode.sable.sublevel.storage.SubLevelRemovalReason;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3d;

public class BeltFixingClipItem extends BlockItem {
    public BeltFixingClipItem(Properties properties) {
        super(AllBlocks.BELT_FIXING_CLIP_BLOCK.get(), properties);
    }


    @Override
    @NotNull
    public InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        if (level.isClientSide()) return InteractionResult.FAIL;

        BlockPos blockPos = context.getClickedPos();

        Vector3d logicalBlockPosVector = Sable.HELPER.projectOutOfSubLevel(level, Utils.vec3ToVector3d(blockPos.getCenter()));

        BlockState targetBlockState = level.getBlockState(blockPos);
        if (targetBlockState.getBlock() instanceof BeltBlock beltBlock) {
            if (beltBlock.getBlockEntity(level, blockPos) instanceof BeltBlockEntity beltBlockEntity) {
                SubLevel subLevel = Sable.HELPER.getContaining(beltBlockEntity);

                ServerSubLevelContainer container = ServerSubLevelContainer.getContainer((ServerLevel) level);
                if (container == null) return InteractionResult.FAIL;
                Pose3d pose = new Pose3d();
                if (subLevel == null) {
                    pose.position().set(Utils.vec3ToVector3d(blockPos.getCenter()));
                } else {
                    Pose3d targetPose = subLevel.logicalPose();
                    pose.orientation().set(targetPose.orientation());
                    pose.position().set(logicalBlockPosVector);
                }

                ServerSubLevel newSubLevel = (ServerSubLevel) container.allocateNewSubLevel(pose);
                if (newSubLevel == null) return InteractionResult.FAIL;

                LevelPlot plot = newSubLevel.getPlot();
                if (plot == null) {
                    container.removeSubLevel(newSubLevel, SubLevelRemovalReason.REMOVED);
                    return InteractionResult.FAIL;
                }

                plot.newEmptyChunk(plot.getCenterChunk());
                if (!level.setBlock(
                    plot.getCenterBlock(),
                    AllBlocks.BELT_FIXING_CLIP_BLOCK.get().getStateForPlacement(new BlockPlaceContext(context)),
                    Block.UPDATE_ALL
                )) {
                    container.removeSubLevel(newSubLevel, SubLevelRemovalReason.REMOVED);
                    return InteractionResult.FAIL;
                }

                ItemStack itemStack = context.getItemInHand();
                itemStack.shrink(1);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.FAIL;
            }
        } else {
            return InteractionResult.FAIL;
        }
    }
}
