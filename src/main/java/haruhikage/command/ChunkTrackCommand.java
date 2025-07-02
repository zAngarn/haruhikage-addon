package haruhikage.command;

import carpet.commands.CarpetAbstractCommand;
import carpet.utils.Messenger;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.util.math.ChunkPos;

import java.util.List;

public class ChunkTrackCommand extends CarpetAbstractCommand {

    public static List<ChunkPos> chunks;

    @Override
    public String getName() {
        return "loadListener";
    }

    @Override
    public String getUsage(CommandSource source) {
        return "loadListener add/remove <cx> <cz>";
    }

    @Override
    public void run(MinecraftServer server, CommandSource source, String[] args) throws CommandException {

        if (args.length < 3) {

            getUsage(source);

        } else {

            if (args[1].equals("add")) {

                ChunkPos chunk = new ChunkPos(parseInt(args[2]), parseInt(args[3]));
                chunks.add(chunk);

            } else if (args[1].equals("remove")) {

                ChunkPos chunk = new ChunkPos(parseInt(args[2]), parseInt(args[3]));

                if (!chunks.contains(chunk)) {
                    Messenger.m(source, "r Chunk was not present on list already!");
                } else {
                    chunks.remove(chunk);
                    Messenger.m(source, "y Chunk has been removed from chunk load tracking list!");
                }

            }
        }
    }
}

