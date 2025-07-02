package haruhikage.command;

import carpet.commands.CarpetAbstractCommand;
import haruhikage.utils.TickSpeed;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.source.CommandSource;

public class TickCommand extends CarpetAbstractCommand {
    @Override
    public String getName() {
        return "tick";
    }

    @Override
    public String getUsage(CommandSource source) {
        return "tick";
    }

    @Override
    public void run(MinecraftServer server, CommandSource sender, String[] args) throws CommandException {
        if (args.length == 0) {
            sendSuccess(sender, this, "no");
        }

        if ("rate".equalsIgnoreCase(args[0])) {
            if (args.length == 2) {
                float tickrate = (float) parseDouble(args[1], 0.01D);
                TickSpeed.tickRate(tickrate);
            }

            sendSuccess(sender, this, String.format("tick rate is %.1f", TickSpeed.tickrate));
            return;
        } else if ("warp".equalsIgnoreCase(args[0])) {
            long advance = args.length >= 2 ? parseLong(args[1], 0, Long.MAX_VALUE) : TickSpeed.time_bias > 0 ? 0 : Long.MAX_VALUE;
            PlayerEntity player = null;
            if (sender instanceof PlayerEntity) {
                player = (PlayerEntity) sender;
            }

            String s = null;
            CommandSource icommandsender = null;

            String message = TickSpeed.tickRateAdvance(player, advance, s, icommandsender);
            if (!message.isEmpty()) {
                sendSuccess(sender, this, message);
            }
            return;

        } else if ("freeze".equalsIgnoreCase(args[0])) {
            TickSpeed.is_paused = !TickSpeed.is_paused;
            if (TickSpeed.is_paused) {
                sendSuccess(sender, this, "Game is paused");
            } else {
                sendSuccess(sender, this, "Game runs normally");
            }
            return;
        } else if ("step".equalsIgnoreCase(args[0])) {
            int advance = 1;
            if (args.length > 1) {
                advance = parseInt(args[1], 1, 72000);
            }
            TickSpeed.addTicksToRunInPause(advance);
            return;
        } else if ("superHot".equalsIgnoreCase(args[0])) {
            if (args.length > 1) {
                if ("stop".equalsIgnoreCase(args[1]) && !TickSpeed.is_superHot) {
                    return;
                }
                if ("start".equalsIgnoreCase(args[1]) && TickSpeed.is_superHot) {
                    return;
                }
            }
            TickSpeed.is_superHot = !TickSpeed.is_superHot;
            if (TickSpeed.is_superHot) {
                sendSuccess(sender, this, "Superhot enabled");
            } else {
                sendSuccess(sender, this, "Superhot disabled");
            }
            return;
        }
    }
}
