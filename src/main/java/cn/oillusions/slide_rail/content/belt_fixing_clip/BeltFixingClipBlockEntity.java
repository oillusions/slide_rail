package cn.oillusions.slide_rail.content.belt_fixing_clip;

import cn.oillusions.slide_rail.AllBlockEntities;
import cn.oillusions.slide_rail.ClientEvents;
import cn.oillusions.slide_rail.content.util.Utils;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltBlock;
import com.simibubi.create.content.kinetics.belt.BeltBlockEntity;
import com.simibubi.create.content.kinetics.belt.BeltPart;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.physics.PhysicsPipeline;
import dev.ryanhcode.sable.api.physics.constraint.ConstraintJointAxis;
import dev.ryanhcode.sable.api.physics.constraint.GenericConstraintConfiguration;
import dev.ryanhcode.sable.api.physics.constraint.GenericConstraintHandle;
import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaterniond;
import org.joml.Vector3d;

import java.util.EnumSet;

public class BeltFixingClipBlockEntity
    extends KineticBlockEntity {
    private transient GenericConstraintHandle handle = null;
    private transient SubLevel selfBody = null;
    private transient SubLevel targetBody = null;
    private transient Vector3d currentAnchor;

    public BeltFixingClipBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(AllBlockEntities.BELT_FIXING_CLIP_BLOCK_ENTITY.get(),blockPos, blockState);
    }

    @Override
    public void tick() {
        super.tick();
        if (level == null || level.isClientSide()) return;

        if (selfBody == null || selfBody.isRemoved()) {
            this.selfBody = Sable.HELPER.getContaining(this);
            currentAnchor = null;
        }
        if (selfBody == null) return;

        BeltBlockEntity beltBlockEntity = getTargetBlockEntity(selfBody, JOMLConversion.toJOML(getBlockPos().getCenter()));
        if (beltBlockEntity != null) {

            BlockState targetBlockState = beltBlockEntity.getBlockState();
            SubLevel targetBody = Sable.HELPER.getContaining(beltBlockEntity);

            if (handle == null || !handle.isValid() || targetBody != this.targetBody) {
                this.targetBody = targetBody;
                reattachConstraint(beltBlockEntity);
            }

            double speed = beltBlockEntity.getDirectionAwareBeltMovementSpeed();

            Vector3d position = JOMLConversion.toJOML(beltBlockEntity.getBlockPos().getCenter());
            Vector3d beltAxialAttitude = Utils.vec3iToVector3d(targetBlockState.getValue(BeltBlock.HORIZONTAL_FACING).getNormal());
            Vector3d beltAxis = new Vector3d(beltAxialAttitude).absolute();
            Vector3d acceleration = new Vector3d(beltAxialAttitude).mul(speed);

            if (currentAnchor == null) {
                currentAnchor = position;
            }

            Vector3d newAnchor = new Vector3d(position);
            newAnchor.sub(new Vector3d(position).mul(beltAxis));
            newAnchor.add(new Vector3d(currentAnchor).mul(beltAxis));
            newAnchor.add(acceleration);

            BeltBlockEntity frontBlockEntity = getTargetBlockEntity(targetBody == null ? selfBody : targetBody, new Vector3d(newAnchor));
            if (frontBlockEntity != null) {
                BeltPart beltPart = frontBlockEntity.getBlockState().getValue(BeltBlock.PART);
                if (beltPart == BeltPart.MIDDLE) {
                    currentAnchor = newAnchor;
                }
            }

            if (handle != null) {
                Quaterniond posture;
                if (targetBody != null) {
                    posture = new Quaterniond(targetBody.logicalPose().orientation());
                } else {
                    posture = new Quaterniond();
                }

                handle.setFrame1(JOMLConversion.toJOML(getBlockPos().getCenter()), posture);
                handle.setFrame2(currentAnchor, posture);

                ServerSubLevelContainer container = ServerSubLevelContainer.getContainer((ServerLevel) level);
                if (container != null) {
                    PhysicsPipeline pipeline = container.physicsSystem().getPipeline();
                    pipeline.wakeUp((ServerSubLevel) selfBody);
                }
            }
        } else {
//            level.destroyBlock(getBlockPos(), true);
//            level.setBlock(getBlockPos(), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        }
    }

    @Nullable
    private BeltBlockEntity getTargetBlockEntity(@Nullable SubLevel subLevel, @NotNull Vector3d blockPos) {
        if (level == null
            || level.isClientSide()
            || selfBody == null
            || selfBody.isRemoved()
        ) return null;

        record Result(ServerSubLevel targetBody, BeltBlockEntity targetBlockEntity) {}

        Result result = Sable.HELPER.runIncludingSubLevels(
            level, Utils.vector3dToVec3(blockPos),
            true, subLevel,
            (sub, plotBlockPos) -> {
                if (level == null || level.isClientSide()) return null;
                if (sub == selfBody) return null;

                BlockPos pos = Utils.vector3dToBlockPos(Sable.HELPER.projectOutOfSubLevel(level, Utils.blockPosToVector3d(plotBlockPos)), Math::floor);
                ClientEvents.boxes.add(new ClientEvents.BoxInfo(new AABB(pos), 1.0f, 0.0f, 0.0f));
                if (level.getBlockEntity(plotBlockPos) instanceof BeltBlockEntity targetBlockEntity) {
                    return new Result((ServerSubLevel) Sable.HELPER.getContaining(targetBlockEntity), targetBlockEntity);
                }
            return null;
        });

        if (result != null) return result.targetBlockEntity();
        ClientEvents.boxes.add(new ClientEvents.BoxInfo(new AABB(Utils.vector3dToBlockPos(Sable.HELPER.projectOutOfSubLevel(level, new Vector3d(blockPos)), Math::floor)), 0.0f, 1.0f, 0.0f));
        if (level.getBlockEntity(Utils.vector3dToBlockPos(Sable.HELPER.projectOutOfSubLevel(level, new Vector3d(blockPos)), Math::floor))
            instanceof BeltBlockEntity targetBlockEntity) {
            return targetBlockEntity;
        }
        return null;
    }

    private void attachConstraint(BeltBlockEntity target) {
        if (level == null || level.isClientSide() || selfBody == null || handle != null && handle.isValid()) return;

        ServerSubLevel targetBody;

        if (target != null) {
            targetBody = (ServerSubLevel) Sable.HELPER.getContaining(target);
        } else {
            targetBody = null;
        }

        Quaterniond targetPosture;
        if (targetBody != null) {
            targetPosture = targetBody.logicalPose().orientation();
        } else {
            targetPosture = new Quaterniond();
        }

        Quaterniond selfPosture = selfBody.logicalPose().orientation();


        Vector3d selfAnchor = JOMLConversion.toJOML(getBlockPos().getCenter());
        Vector3d targetAnchor;
        if (target != null) {
            targetAnchor = JOMLConversion.toJOML(target.getBlockPos().getCenter());
        } else {
            targetAnchor = Sable.HELPER.projectOutOfSubLevel(level, JOMLConversion.toJOML(getBlockPos().getCenter()));
        }

        if (selfAnchor.equals(targetAnchor)) return;
        final GenericConstraintConfiguration constraint = new GenericConstraintConfiguration(
            selfAnchor, targetAnchor,
            selfPosture, targetPosture
            ,
            EnumSet.of(
//                ConstraintJointAxis.LINEAR_X,
//                ConstraintJointAxis.LINEAR_Y,
//                ConstraintJointAxis.LINEAR_Z,
                ConstraintJointAxis.ANGULAR_X,
                ConstraintJointAxis.ANGULAR_Y,
                ConstraintJointAxis.ANGULAR_Z
            )
        );

        ServerSubLevel serverSelfBody = (ServerSubLevel) selfBody;
        ServerSubLevelContainer container = ServerSubLevelContainer.getContainer((ServerLevel) level);
        if (container != null) {
            PhysicsPipeline pipeline = container.physicsSystem().getPipeline();
            handle = pipeline.addConstraint(serverSelfBody, targetBody, constraint);
        }

        if (handle != null && handle.isValid()) {
            handle.setMotor(ConstraintJointAxis.LINEAR_X, 0.0, 50000.0, 100.0, false, 0);
            handle.setMotor(ConstraintJointAxis.LINEAR_Y, 0.0, 50000.0, 100.0, false, 0);
            handle.setMotor(ConstraintJointAxis.LINEAR_Z, 0.0, 50000.0, 100.0, false, 0);
        }
    }

    private void detachConstraint() {
        currentAnchor = null;
        if (handle == null) return;
        if (handle.isValid()) {
            handle.remove();
        }
    }

    private void reattachConstraint(BeltBlockEntity target) {
        detachConstraint();
        attachConstraint(target);
    }

    @Override
    public void remove() {
        detachConstraint();
        super.remove();
    }
}
