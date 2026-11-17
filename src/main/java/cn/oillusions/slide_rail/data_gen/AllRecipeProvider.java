package cn.oillusions.slide_rail.data_gen;

import cn.oillusions.slide_rail.AllItems;
import com.simibubi.create.AllBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class AllRecipeProvider extends RecipeProvider {
    public AllRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    /**
     * 木板 传动杆 空
     * 空 传动杆 木板
     * ↑ 螺杆
     * 空 平滑石头 空
     * 安山机壳 安山机壳 安山机壳
     * ↑ 安山凸形导轨[颠倒即为滑块]
     * 空 螺杆 空
     * 安山机壳 安山机壳 安山机壳
     * ↑ 安山凸形驱动导轨[与上面同理]
     * 空 螺杆 空
     * 黄铜机壳 精密构件 黄铜机壳
     * ↑ 黄铜智能凸形导轨[与上面同理]
     * 铁板
     * 传动杆
     * 铁板
     * ↑ 皮带固定卡
     */

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
    }
}
