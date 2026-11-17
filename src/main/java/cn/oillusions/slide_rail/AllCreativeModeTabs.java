package cn.oillusions.slide_rail;

import cn.oillusions.slide_rail.util.SimpleDisplayItemGenerator;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AllCreativeModeTabs {
    public static List<Supplier<Item>> itemSuppliers = new ArrayList<>();
    private static final DeferredRegister<CreativeModeTab> REGISTER =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SlideRail.ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BASE = REGISTER.register(
        "base", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.slide_rail.base"))
            .icon(() -> new ItemStack(AllBlocks.CONCAVE_GUIDE_RAIL_BLOCK.get()))
            .displayItems(new SimpleDisplayItemGenerator(AllCreativeModeTabs.BASE::getId, AllCreativeModeTabs::itemSuppliers))
            .build()
    );

    public static void put(Supplier<Item> itemSupplier) {
        if (itemSuppliers.contains(itemSupplier)) return;
        itemSuppliers.add(itemSupplier);
    }

    private static List<Supplier<Item>> itemSuppliers() {
        return itemSuppliers;
    }

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }

    private AllCreativeModeTabs() {}
}
