package haruhikage.command;

import carpet.commands.CarpetAbstractCommand;
import carpet.utils.Messenger;
import haruhikage.HaruhikageAddonSettings;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.source.CommandSource;
import net.minecraft.util.math.ChunkPos;

import java.util.ArrayList;
import java.util.List;

public class ChunkTrackCommand extends CarpetAbstractCommand {

    public static List<ChunkPos> chunks = new ArrayList<>();

    @Override
    public String getName() {
        return "chunkTrack";
    }

    @Override
    public String getUsage(CommandSource source) {
        return "ri chunkTrack add/remove <cx> <cz>";
    }

    @Override
    public void run(MinecraftServer server, CommandSource source, String[] args) throws CommandException {

        if(HaruhikageAddonSettings.chunkTrackCommand) {

            if (args.length < 2) {

                Messenger.m(source, getUsage(source));

            } else {

                if (args[0].equals("add")) {

                    ChunkPos chunk = new ChunkPos(parseInt(args[1]), parseInt(args[2]));
                    chunks.add(chunk);
                    Messenger.m(source, String.format("y Added chunk %d %d to the watching list!", chunk.x, chunk.z));

                } else if (args[0].equals("remove")) {

                    ChunkPos chunk = new ChunkPos(parseInt(args[1]), parseInt(args[2]));

                    if (!chunks.contains(chunk)) {
                        Messenger.m(source, "r Chunk was not present on list already!");
                    } else {
                        chunks.remove(chunk);
                        Messenger.m(source, "y Chunk has been removed from chunk load tracking list!");
                    }

                }
            }
        } else {

            Messenger.m(source, "r Command not active! Enable it with /carpet chunkTrackCommand true");

        }
    }
}

