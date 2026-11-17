package cn.oillusions.slide_rail.data_gen;

import cn.oillusions.slide_rail.AllBlocks;
import dev.ryanhcode.sable.index.SableTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class AllBlockTagsProvider extends BlockTagsProvider {
    public static final TagKey<Block> SABLE_SLIPPERY = TagKey.create(
        Registries.BLOCK,
        ResourceLocation.fromNamespaceAndPath("sable", "slippery")
    );
    public AllBlockTagsProvider(
        PackOutput output,
        CompletableFuture<HolderLookup.Provider> lookupProvider,
        String modId,
        @Nullable ExistingFileHelper existingFileHelper
    ) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        tag(SABLE_SLIPPERY)
            .add(AllBlocks.CONVEX_SLIDE_BLOCK.get())
            .add(AllBlocks.CONVEX_GUIDE_RAIL_BLOCK.get());
    }
}
