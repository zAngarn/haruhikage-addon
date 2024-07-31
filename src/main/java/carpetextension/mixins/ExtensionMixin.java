package carpetextension.mixins;

import carpetextension.CarpetExtensionSettings;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class ExtensionMixin {
    @Inject(method = "init", at = @At("HEAD"))
    private void onInit(CallbackInfo ci) {
        // example mixin
        CarpetExtensionSettings.LOGGER.info("Hi from extension");
    }
}
