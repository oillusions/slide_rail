package cn.oillusions.slide_rail;

import cn.oillusions.slide_rail.content.belt_fixing_clip.BeltFixingClipBlock;
import cn.oillusions.slide_rail.content.guide_rail.ConcaveGuideRailBlock;
import cn.oillusions.slide_rail.content.guide_rail.ConvexGuideRailBlock;
import cn.oillusions.slide_rail.content.slide_block.ConcaveSlideBlock;
import cn.oillusions.slide_rail.content.slide_block.ConvexSlideBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class AllBlocks {
    private static final DeferredRegister.Blocks REGISTER = DeferredRegister.createBlocks(SlideRail.ID);

    public static final DeferredHolder<Block, ConvexSlideBlock> CONVEX_SLIDE_BLOCK =
        register("convex_slide_block", ConvexSlideBlock::new);

    public static final DeferredHolder<Block, ConcaveSlideBlock> CONCAVE_SLIDE_BLOCK =
        register("concave_slide_block", ConcaveSlideBlock::new);


    public static final DeferredHolder<Block, ConvexGuideRailBlock> CONVEX_GUIDE_RAIL_BLOCK =
        register("convex_guide_rail", ConvexGuideRailBlock::new);

    public static final DeferredHolder<Block, ConcaveGuideRailBlock> CONCAVE_GUIDE_RAIL_BLOCK =
        register("concave_guide_rail", ConcaveGuideRailBlock::new);

    public static final DeferredHolder<Block, BeltFixingClipBlock> BELT_FIXING_CLIP_BLOCK =
        register("belt_fixing_clip", BeltFixingClipBlock::new);



    @NotNull
    private static <B extends Block> DeferredHolder<Block, B> register(@NotNull String name, @NotNull Function<BlockBehaviour.Properties, B> blockSupplier) {
        return REGISTER.register(
            name, () -> blockSupplier.apply(BlockBehaviour.Properties.of()
                .strength(2.0f)
                .sound(SoundType.METAL)
                .noOcclusion())
        );
    }

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }

    private AllBlocks() {}
}
