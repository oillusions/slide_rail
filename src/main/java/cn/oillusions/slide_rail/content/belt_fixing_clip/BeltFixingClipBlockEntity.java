package cn.oillusions.slide_rail.content.belt_fixing_clip;

import cn.oillusions.slide_rail.AllBlockEntities;
import cn.oillusions.slide_rail.content.util.Utils;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.physics.PhysicsPipeline;
import dev.ryanhcode.sable.api.physics.constraint.ConstraintJointAxis;
import dev.ryanhcode.sable.api.physics.constraint.GenericConstraintConfiguration;
import dev.ryanhcode.sable.api.physics.constraint.GenericConstraintHandle;
import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Quaterniond;
import org.joml.Vector3d;

import java.util.EnumSet;

public class BeltFixingClipBlockEntity
    extends KineticBlockEntity {
    private transient GenericConstraintHandle handle = null;
    private transient SubLevel subLevel = null;
    private transient Vector3d currentAnchor;

    public BeltFixingClipBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(AllBlockEntities.BELT_FIXING_CLIP_BLOCK_ENTITY.get(),blockPos, blockState);
    }

    @Override
    public void tick() {
        super.tick();
        if (level == null || level.isClientSide()) return;
        BlockState blockState = getBlockState();

        if (subLevel == null || subLevel.isRemoved()) {
            this.subLevel = Sable.HELPER.getContaining(this);
            currentAnchor = SableCompanion.INSTANCE.projectOutOfSubLevel(
                level, Utils.blockPosToVector3d(getBlockPos())
            );
        }

        if (blockState.getBlock() instanceof BeltFixingClipBlock) {
            BlockPos blockPos = getBlockPos();
            Vector3d logicalBlockPosVector3d = SableCompanion.INSTANCE.projectOutOfSubLevel(level, Utils.blockPosToVector3d(blockPos));
            BlockPos logicalBlockPos = Utils.vector3dToBlockPos(logicalBlockPosVector3d);
            BlockState overlapBlockState = level.getBlockState(logicalBlockPos);
            Block overlapBlock = overlapBlockState.getBlock();

            if (overlapBlock instanceof BeltBlock beltBlock) {
                BeltBlockEntity beltBlockEntity = beltBlock.getBlockEntity(level, logicalBlockPos);
                if (beltBlockEntity != null) {
                    double speed = beltBlockEntity.getDirectionAwareBeltMovementSpeed();

                    Vector3d attitude = Utils.vec3iToVector3d(overlapBlockState.getValue(BeltBlock.HORIZONTAL_FACING).getNormal()).absolute();
                    Vector3d acceleration = new Vector3d(attitude).mul(-speed);
                    Vector3d position = Utils.blockPosToVector3d(logicalBlockPos);
                    Vector3d prevAnchor = new Vector3d(currentAnchor);
                    Vector3d newAnchor = new Vector3d(currentAnchor);
                    newAnchor.set(position.sub(new Vector3d(position).mul(attitude))
                        .add(prevAnchor.mul(attitude))
                        .add(acceleration));

                    if (!level.getBlockState(Utils.vector3dToBlockPos(newAnchor)).isAir()) {
                        currentAnchor = newAnchor;
                        if (handle != null) {
                            handle.setFrame2(currentAnchor, new Quaterniond());

//                            if (subLevel != null && !subLevel.isRemoved() && speed != 0) {
                                PhysicsPipeline pipeline = ((ServerSubLevelContainer) SubLevelContainer.getContainer(level)).physicsSystem().getPipeline();
                                pipeline.wakeUp((ServerSubLevel) subLevel);
//                            }
                        }
                    }
                }
            }
        }

        if (level.isClientSide() || subLevel == null || subLevel.isRemoved() || handle != null) return;

        attachConstraint();
    }

    private void attachConstraint() {
        if (level == null || level.isClientSide() || subLevel == null || handle != null) return;

        Vector3d plotAnchor = Utils.blockPosToVector3d(getBlockPos());
        if (currentAnchor == null || plotAnchor.equals(currentAnchor)) return;
        final GenericConstraintConfiguration constraint = new GenericConstraintConfiguration(
            plotAnchor, currentAnchor,
            new Quaterniond(), new Quaterniond(),
            EnumSet.of(
//                ConstraintJointAxis.LINEAR_X,
//                ConstraintJointAxis.LINEAR_Y,
//                ConstraintJointAxis.LINEAR_Z,
                ConstraintJointAxis.ANGULAR_X,
                ConstraintJointAxis.ANGULAR_Y,
                ConstraintJointAxis.ANGULAR_Z
            )
        );

        ServerSubLevelContainer container = ServerSubLevelContainer.getContainer((ServerLevel) level);
        ServerSubLevel serverSubLevel = (ServerSubLevel) subLevel;
        PhysicsPipeline pipeline = container.physicsSystem().getPipeline();

        handle = pipeline.addConstraint(serverSubLevel, null, constraint);
        updateGain(1.0);
    }

    private void updateGain(double mul) {
        if (handle != null) {
            handle.setMotor(ConstraintJointAxis.LINEAR_X, 0, 1200.0 * mul, 100.0 * mul, false, 0);
            handle.setMotor(ConstraintJointAxis.LINEAR_Y, 0, 1200.0 * mul, 100.0 * mul, false, 0);
            handle.setMotor(ConstraintJointAxis.LINEAR_Z, 0, 1200.0 * mul, 100.0 * mul, false, 0);
        }
    }
}
