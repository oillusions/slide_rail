package cn.oillusions.slide_rail.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class SimpleDisplayItemGenerator implements CreativeModeTab.DisplayItemsGenerator {
    private final Supplier<List<Supplier<Item>>> itemsSupplier;

    public SimpleDisplayItemGenerator(
        @NotNull Supplier<ResourceLocation> tabIDSupplier,
        Supplier<List<Supplier<Item>>> itemsSupplier
    ) {
        this.itemsSupplier = itemsSupplier;
    }

    @Override
    public void accept(
        @NotNull CreativeModeTab.ItemDisplayParameters params,
        @NotNull CreativeModeTab.Output output
    ) {
        if (itemsSupplier == null) return;
        List<Supplier<Item>> items = itemsSupplier.get();
        for (Supplier<Item> item : items) {
            if (item.get() == Items.AIR) continue;
            output.accept(item.get());
        }
    }
}