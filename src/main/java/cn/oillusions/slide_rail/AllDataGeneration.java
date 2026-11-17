package cn.oillusions.slide_rail;

import cn.oillusions.slide_rail.data_gen.AllBlockLootSubProvider;
import cn.oillusions.slide_rail.data_gen.AllBlockTagsProvider;
import cn.oillusions.slide_rail.data_gen.AllRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = SlideRail.ID)
public class AllDataGeneration {
    @SubscribeEvent
    public static void gatherData(@NotNull GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        ExistingFileHelper sfh = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();

        gen.addProvider(event.includeServer(), new AllRecipeProvider(output, registries));
        gen.addProvider(event.includeServer(), new AllBlockTagsProvider(output, registries, SlideRail.ID, sfh));
        gen.addProvider(event.includeServer(), new LootTableProvider(
            output,
            Collections.emptySet(),
            List.of(new LootTableProvider.SubProviderEntry(
                AllBlockLootSubProvider::new,
                LootContextParamSets.BLOCK
            )),
            registries
        ));
    }
}
