package haruhikage.mixins;

import carpet.CarpetServer;
import carpet.utils.Messenger;
import haruhikage.HaruhikageAddonSettings;
import haruhikage.command.ChunkDebugCommand;
import net.minecraft.server.world.chunk.ServerChunkCache;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerChunkCache.class)
public abstract class ServerChunkCacheMixin {

    @Unique
    private WorldChunk unloadedChunk;

    @Unique
    private World world;

    // Unload phase tracking

    @Inject(method = "tick()Z", at = @At("HEAD"))
    private void startUnloadTimer(CallbackInfoReturnable<Boolean> cir) {

        if (HaruhikageAddonSettings.logCertainTickPhases) {
            HaruhikageAddonSettings.LOGGER.info("Starting Unload Phase. Global timer: {}", System.nanoTime());
        }

    }

    @ModifyVariable(method = "tick()Z", at = @At(value = "STORE", ordinal = 0))
    private WorldChunk unloadedChunkCapture(WorldChunk chunk) {
        this.unloadedChunk = chunk;
        return chunk;
    }

    @Inject(method = "tick()Z", at = @At(value = "INVOKE", target = "Ljava/util/Iterator;remove()V", shift = At.Shift.AFTER))
    private void checkForUnloadChunk(CallbackInfoReturnable<Boolean> cir) {
        if (HaruhikageAddonSettings.logCertainTickPhases && this.unloadedChunk.chunkX == HaruhikageAddonSettings.unloadChunkX && this.unloadedChunk.chunkZ == HaruhikageAddonSettings.unloadChunkZ) {
            Messenger.print_server_message(CarpetServer.minecraftServer, String.format("Unload Chunk %d %d has been unloaded. Global timer: %d", this.unloadedChunk.chunkX, this.unloadedChunk.chunkZ, System.nanoTime()));
        }
    }

    @Inject(method = "tick()Z", at = @At("TAIL"))
    private void stopUnloadTimer(CallbackInfoReturnable<Boolean> cir) {
        if (HaruhikageAddonSettings.logCertainTickPhases) {
            HaruhikageAddonSettings.LOGGER.info("Unload phase exiting. Global timer: {}", System.nanoTime());
        }

        // Chunk Debug - Unloading Events
        /*if (ChunkDebugCommand.chunkDebugEnabled) {
            ChunkDebugCommand.onChunkUnloaded(unloadedChunk.chunkX, unloadedChunk.chunkZ, this.world, null); //TODO
        }*/
    }

    // Chunk Debug - World

    @ModifyArg(method = "loadChunk(II)Lnet/minecraft/world/chunk/WorldChunk;", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/storage/ChunkStorage;loadChunk(Lnet/minecraft/world/World;II)Lnet/minecraft/world/chunk/WorldChunk;"), index = 0)
    private World loadedChunkWorld(World world) {
        this.world = world;
        return world;
    }

    // Chunk Debug - Loading events

    @Inject(method = "loadChunk", at = @At("RETURN"))
    private void sniffLoadChunkEvents(int chunkX, int chunkZ, CallbackInfoReturnable<WorldChunk> cir) {
        if (ChunkDebugCommand.chunkDebugEnabled) {
            ChunkDebugCommand.onChunkLoaded(chunkX, chunkZ, this.world, null);
        }
    }

    // Chunk Debug - Generation events (Es seguro asumir que siempre que un chunk se genera se va a cargar, asi que usamos el mismo mundo para to')

    @Inject(method = "getChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/ChunkGenerator;getChunk(II)Lnet/minecraft/world/chunk/WorldChunk;", shift = At.Shift.AFTER))
    private void sniffGenerationChunkEvents(int chunkX, int chunkZ, CallbackInfoReturnable<WorldChunk> cir) {
        if (ChunkDebugCommand.chunkDebugEnabled) {
            ChunkDebugCommand.onChunkGenerated(chunkX, chunkZ, this.world, null);
        }
    }

    @Inject(method = "unloadChunk", at = @At(value = "INVOKE", target = "Ljava/util/Set;add(Ljava/lang/Object;)Z", shift = At.Shift.AFTER))
    private void sniffUnloadEvent(WorldChunk chunk, CallbackInfo ci) {
        if (ChunkDebugCommand.chunkDebugEnabled) {
            ChunkDebugCommand.onChunkUnloadScheduled(chunk.chunkX, chunk.chunkZ, this.world, null);
        }
    }
}

