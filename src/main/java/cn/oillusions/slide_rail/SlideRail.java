package cn.oillusions.slide_rail;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(SlideRail.ID)
public class SlideRail {
    public static final String ID = "slide_rail";
    public static final Component NAME = Component.translatable("slide_rail.name");
    public static final Logger LOGGER = LogUtils.getLogger();

    public SlideRail(IEventBus modEventBus, ModContainer modContainer) {
        AllBlocks.register(modEventBus);
        AllItems.register(modEventBus);
        AllBlockEntities.register(modEventBus);
        AllCreativeModeTabs.register(modEventBus);
    }

    @NotNull
    public static ResourceLocation resourceAt(@NotNull String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }
}
