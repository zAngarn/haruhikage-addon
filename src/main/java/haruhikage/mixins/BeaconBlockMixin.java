package haruhikage.mixins;

import haruhikage.HaruhikageAddonSettings;
import net.minecraft.block.BeaconBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.state.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.HttpUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BeaconBlock.class)
public abstract class BeaconBlockMixin {

    /**
     * @author soy gay
     * @reason soy muy gay
     */
    @Overwrite
    public static void updateBeam(final World world, final BlockPos pos) {
        final long[] t = new long[1];
        HttpUtil.DOWNLOAD_THREAD_FACTORY.submit(new Runnable() {

            public void run() {

                if(HaruhikageAddonSettings.logAsyncTimes) {
                    System.out.println("Starting Async Thread...");
                    t[0] = System.nanoTime();
                }

                WorldChunk worldChunk = world.getChunk(pos);

                for (int i = pos.getY() - 1; i >= 0; --i) {
                    final BlockPos blockPos = new BlockPos(pos.getX(), i, pos.getZ());
                    if (!worldChunk.hasSkyAccess(blockPos)) {
                        break;
                    }

                    BlockState blockState = world.getBlockState(blockPos);
                    if (blockState.getBlock() == Blocks.BEACON) {
                        ((ServerWorld) world).submit(() ->
                        {
                            BlockEntity blockEntity = world.getBlockEntity(blockPos);
                            if (blockEntity instanceof BeaconBlockEntity) {
                                ((BeaconBlockEntity) blockEntity).update();
                                world.addBlockEvent(blockPos, Blocks.BEACON, 1, 0);
                            }
                        });
                    }
                }

                if(HaruhikageAddonSettings.logAsyncTimes) System.out.println("Async thread exiting. Alive for " + (System.nanoTime() - t[0]) + "ns. Global timer: " + System.nanoTime() );

            }
        });
    }
}
