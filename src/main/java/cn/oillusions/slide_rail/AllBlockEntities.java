package cn.oillusions.slide_rail;

import cn.oillusions.slide_rail.content.belt_fixing_clip.BeltFixingClipBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AllBlockEntities {
    private static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, SlideRail.ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BeltFixingClipBlockEntity>> BELT_FIXING_CLIP_BLOCK_ENTITY = REGISTER
        .register("belt_fixing_clip", () -> BlockEntityType.Builder
            .of(BeltFixingClipBlockEntity::new, AllBlocks.BELT_FIXING_CLIP_BLOCK.get())
            .build(null)
        );

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }

    private AllBlockEntities() {}
}
