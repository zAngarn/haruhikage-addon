package haruhikage.mixins;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.chunk.ServerChunkCache;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerChunkCache.class)
public interface ServerChunkCacheAccessor {

    @Accessor("chunks")
    Long2ObjectMap<WorldChunk> getChunks();

    @Accessor("world")
    ServerWorld getWorld();

}
