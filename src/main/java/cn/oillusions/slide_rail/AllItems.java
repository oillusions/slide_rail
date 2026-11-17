package cn.oillusions.slide_rail;

import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class AllItems {
    private static final DeferredRegister.Items REGISTER = DeferredRegister.createItems(SlideRail.ID);

    public static final DeferredHolder<Item, BlockItem> CONVEX_SLIDE_BLOCK_ITEM = REGISTER.registerSimpleBlockItem(
        "convex_slide_block", AllBlocks.CONVEX_SLIDE_BLOCK
    );

    public static final DeferredHolder<Item, BlockItem> CONCAVE_SLIDE_BLOCK_ITEM = REGISTER.registerSimpleBlockItem(
        "concave_slide_block", AllBlocks.CONCAVE_SLIDE_BLOCK
    );

    public static final DeferredHolder<Item, BlockItem> CONVEX_GUIDE_RAIL_ITEM = REGISTER.registerSimpleBlockItem(
        "convex_guide_rail", AllBlocks.CONVEX_GUIDE_RAIL_BLOCK
    );

    public static final DeferredHolder<Item, BlockItem> CONCAVE_GUIDE_RAIL_ITEM = REGISTER.registerSimpleBlockItem(
        "concave_guide_rail", AllBlocks.CONCAVE_GUIDE_RAIL_BLOCK
    );

    public static final DeferredHolder<Item, BlockItem> BELT_FIXING_CLIP_ITEM = REGISTER.registerSimpleBlockItem(
        "belt_fixing_clip", AllBlocks.BELT_FIXING_CLIP_BLOCK
    );

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
        registerDisplayItems();
    }

    private static void registerDisplayItem(@NotNull DeferredHolder<Item, ?> item) {
        AllCreativeModeTabs.put(item::get);
    }

    private static void registerDisplayItems() {
        registerDisplayItem(CONVEX_SLIDE_BLOCK_ITEM);
        registerDisplayItem(CONCAVE_SLIDE_BLOCK_ITEM);
        registerDisplayItem(CONVEX_GUIDE_RAIL_ITEM);
        registerDisplayItem(CONCAVE_GUIDE_RAIL_ITEM);
        registerDisplayItem(BELT_FIXING_CLIP_ITEM);
    }

    private AllItems() {}
}
