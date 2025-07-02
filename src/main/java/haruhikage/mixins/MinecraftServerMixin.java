package haruhikage.mixins;

import haruhikage.HaruhikageAddonSettings;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {

    @Inject(method = "tickWorlds", at = @At("TAIL"))
    private void populationLoggeStart(CallbackInfo ci) {
        if(HaruhikageAddonSettings.logCertainTickPhases) {
            HaruhikageAddonSettings.LOGGER.info("Player phase has just started. Global Timer: {}", System.nanoTime());
        }
    }

    @Inject(method = "tickWorlds", at = @At("TAIL"))
    private void populationLoggerEnd(CallbackInfo ci) {
        if(HaruhikageAddonSettings.logCertainTickPhases) {
            HaruhikageAddonSettings.LOGGER.info("Exiting player phase. Global timer: {}", System.nanoTime());
        }
    }

}
