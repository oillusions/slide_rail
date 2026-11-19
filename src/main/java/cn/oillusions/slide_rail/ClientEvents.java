package cn.oillusions.slide_rail;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Queue;

@EventBusSubscriber(modid = SlideRail.ID, value = Dist.CLIENT)
public class ClientEvents {
    private static final Minecraft minecraft = Minecraft.getInstance();
    public static Queue<BoxInfo> boxes = new ArrayDeque<>();

    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        BoxInfo box;
        while ((box = boxes.poll()) != null) {
            PoseStack poseStack =  event.getPoseStack();
            Camera camera = event.getCamera();
            poseStack.pushPose();


            double dx = -camera.getPosition().x();
            double dy = -camera.getPosition().y();
            double dz = -camera.getPosition().z();

            event.getPoseStack().translate(dx, dy ,dz);
            LevelRenderer.renderLineBox(
                event.getPoseStack(), minecraft.renderBuffers().bufferSource().getBuffer(RenderType.LINES),
                box.box, box.r, box.g, box.b, 1.0f
            );
            poseStack.popPose();
        }
    }

    public record BoxInfo(@NotNull AABB box, float r, float g, float b) {};
}
