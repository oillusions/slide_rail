package cn.oillusions.slide_rail.mixin;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.Variant;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Type;

@Mixin(Variant.Deserializer.class)
public class BlockStateModelLoaderMixin {

    @Inject(
        method = "deserialize(Lcom/google/gson/JsonElement;Ljava/lang/reflect/Type;Lcom/google/gson/JsonDeserializationContext;)Lnet/minecraft/client/renderer/block/model/Variant;",
        at = @At("RETURN"),
        cancellable = true
    )
    private void getBlockRotation(
        JsonElement p_111893_,
        Type p_111894_,
        JsonDeserializationContext p_111895_,
        CallbackInfoReturnable<Variant> cir,
        @Local JsonObject jsonobject,
        @Local ResourceLocation resourcelocation,
        @Local BlockModelRotation blockmodelrotation,
        @Local boolean flag,
        @Local int i
    ) {
        if (!jsonobject.has("z")) return;
        int z = GsonHelper.getAsInt(jsonobject, "z", 0);
        cir.setReturnValue(new Variant(resourcelocation, blockmodelrotation.getRotation().compose(
            new Transformation(null, new Quaternionf().rotateZ((float)(-z) * ((float)Math.PI / 180F)), null, null)
        ), flag, i));
    }
}
