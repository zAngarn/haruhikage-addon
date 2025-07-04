package haruhikage.mixins;

import carpet.CarpetServer;
import carpet.utils.Messenger;
import haruhikage.HaruhikageAddonSettings;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin {

    @Unique
    private int cX;

    @Unique
    private int cZ;

    @ModifyArg(method = "populate(Lnet/minecraft/world/chunk/ChunkGenerator;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/ChunkGenerator;populateChunk(II)V"), index = 0)
    private int populationChunkX(int x) {
        this.cX = x;
        return x;
    }

    @ModifyArg(method = "populate(Lnet/minecraft/world/chunk/ChunkGenerator;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/ChunkGenerator;populateChunk(II)V"), index = 1)
    private int populationChunkZ(int z) {
        this.cZ = z;
        return z;
    }

    @Inject(method = "populate(Lnet/minecraft/world/chunk/ChunkGenerator;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/ChunkGenerator;populateChunk(II)V", shift = At.Shift.BEFORE))
    private void populationLoggeStart(CallbackInfo ci) {
        if (HaruhikageAddonSettings.logChunkPopulation) {
            Messenger.print_server_message(CarpetServer.minecraftServer, "Started populating a chunk...");
        }
    }

    @Inject(method = "populate(Lnet/minecraft/world/chunk/ChunkGenerator;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/ChunkGenerator;populateChunk(II)V", shift = At.Shift.AFTER))
    private void populationLoggerEnd(CallbackInfo ci) {
        if (HaruhikageAddonSettings.logChunkPopulation) {
            Messenger.print_server_message(CarpetServer.minecraftServer, String.format("Finished populating chunk %d %d!", cX, cZ));
        }
    }
}
