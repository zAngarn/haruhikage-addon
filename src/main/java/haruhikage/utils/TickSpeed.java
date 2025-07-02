package haruhikage.utils;

import carpet.CarpetServer;
import carpet.utils.Messenger;
import haruhikage.HaruhikageAddonServer;
import haruhikage.utils.pubsub.PubSubInfoProvider;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.handler.CommandHandler;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.util.math.MathHelper;

public class TickSpeed
{
    public static final int PLAYER_GRACE = 2;
    public static float tickrate = 20.0f;
    public static long mspt = 50L;
    public static long warp_temp_mspt = 1L;
    public static long time_bias = 0;
    public static long time_warp_start_time = 0;
    public static long time_warp_scheduled_ticks = 0;
    public static PlayerEntity time_advancerer = null;
    public static String tick_warp_callback = null;
    public static CommandSource tick_warp_sender = null;
    public static int player_active_timeout = 0;
    public static boolean process_entities = true;
    public static boolean is_paused = false;
    public static boolean is_superHot = false;

    private static PubSubInfoProvider<Float> PUBSUB_TICKRATE = new PubSubInfoProvider<>(HaruhikageAddonServer.PUBSUB, "carpet.tick.rate", 0, () -> tickrate);

    static {
        new PubSubInfoProvider<>(HaruhikageAddonServer.PUBSUB, "minecraft.performance.mspt", 20, TickSpeed::getMSPT);
        new PubSubInfoProvider<>(HaruhikageAddonServer.PUBSUB, "minecraft.performance.tps", 20, TickSpeed::getTPS);
    }

    public static void resetPlayerActiveTimeout()
    {
        if (player_active_timeout < PLAYER_GRACE)
        {
            player_active_timeout = PLAYER_GRACE;
        }
    }

    public static void addTicksToRunInPause(int ticks)
    {
        player_active_timeout = PLAYER_GRACE+ticks;
    }

    public static void tickRate(float rate)
    {
        tickrate = rate;
        mspt = (long)(1000.0/tickrate);
        if (mspt <=0)
        {
            mspt = 1L;
            tickrate = 1000.0f;
        }
        PUBSUB_TICKRATE.publish();
    }

    public static String tickRateAdvance(PlayerEntity player, long advance, String callback, CommandSource icommandsender)
    {
        if (0 == advance)
        {
            tick_warp_callback = null;
            tick_warp_sender = null;
            finishTimeWarp();
            return "Warp interrupted";
        }
        if (time_bias > 0)
        {
            return "Another player is already advancing time at the moment. Try later or talk to them";
        }
        time_advancerer = player;
        time_warp_start_time = System.nanoTime();
        time_warp_scheduled_ticks = advance;
        time_bias = advance;
        tick_warp_callback = callback;
        tick_warp_sender = icommandsender;
        return "Warp speed ....";
    }

    public static void finishTimeWarp()
    {

        long completed_ticks = time_warp_scheduled_ticks - time_bias;
        double milis_to_complete = System.nanoTime()-time_warp_start_time;
        if (milis_to_complete == 0.0)
        {
            milis_to_complete = 1.0;
        }
        milis_to_complete /= 1000000.0;
        int tps = (int) (1000.0D*completed_ticks/milis_to_complete);
        double mspt = (1.0*milis_to_complete)/completed_ticks;
        time_warp_scheduled_ticks = 0;
        time_warp_start_time = 0;
        if (tick_warp_callback != null)
        {
            CommandHandler icommandmanager = tick_warp_sender.getServer().getCommandHandler();
            try
            {
                int j = icommandmanager.run(tick_warp_sender, tick_warp_callback);

                if (j < 1)
                {
                    if (time_advancerer != null)
                    {
                        Messenger.m(time_advancerer, "r Command Callback failed: ", "rb /"+tick_warp_callback,"/"+tick_warp_callback);
                    }
                }
            }
            catch (Throwable var23)
            {
                if (time_advancerer != null)
                {
                    Messenger.m(time_advancerer, "r Command Callback failed - unknown error: ", "rb /"+tick_warp_callback,"/"+tick_warp_callback);
                }
            }
            tick_warp_callback = null;
            tick_warp_sender = null;
        }
        if (time_advancerer != null)
        {
            Messenger.m(time_advancerer, String.format("gi ... Time warp completed with %d tps, or %.2f mspt",tps, mspt ));
            time_advancerer = null;
        }
        time_bias = 0;

    }

    public static boolean continueWarp()
    {
        if (time_bias > 0)
        {
            if (time_bias == time_warp_scheduled_ticks) //first call after previous tick, adjust start time
            {
                time_warp_start_time = System.nanoTime();
            }
            time_bias -= 1;
            return true;
        }
        else
        {
            finishTimeWarp();
            return false;
        }
    }

    public static void tick(MinecraftServer server)
    {
        process_entities = true;
        if (player_active_timeout > 0)
        {
            player_active_timeout--;
        }
        if (is_paused)
        {
            if (player_active_timeout < PLAYER_GRACE)
            {
                process_entities = false;
            }
        }
        else if (is_superHot)
        {
            if (player_active_timeout <= 0)
            {
                process_entities = false;

            }
        }
    }

    public static double getMSPT() {
        return MathHelper.average(CarpetServer.minecraftServer.averageTickTimes) * 1.0E-6D;
    }

    public static double getTPS() {
        return 1000.0D / Math.max((time_warp_start_time != 0) ? 0.0 : TickSpeed.mspt, getMSPT());
    }
}
