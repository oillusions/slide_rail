package cn.oillusions.slide_rail.data_gen;

import cn.oillusions.slide_rail.AllBlocks;
import cn.oillusions.slide_rail.SlideRail;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.stream.Collectors;

public class AllBlockLootSubProvider extends BlockLootSubProvider {
    public AllBlockLootSubProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(AllBlocks.CONCAVE_SLIDE_BLOCK.get());
        dropSelf(AllBlocks.CONVEX_SLIDE_BLOCK.get());
        dropSelf(AllBlocks.CONCAVE_GUIDE_RAIL_BLOCK.get());
        dropSelf(AllBlocks.CONVEX_GUIDE_RAIL_BLOCK.get());
    }

    @Override
    @NotNull
    protected Iterable<Block> getKnownBlocks() {
        return BuiltInRegistries.BLOCK.stream()
            .filter(block -> BuiltInRegistries.BLOCK.getKey(block).getNamespace().equals(SlideRail.ID))
            .collect(Collectors.toSet());
    }
}
