package carpetextension.command;

import carpet.commands.CarpetAbstractCommand;
import carpet.utils.Messenger;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.exception.CommandException;
import net.minecraft.server.command.source.CommandSource;

public class ExtensionCommand extends CarpetAbstractCommand {
    // example command
    @Override
    public String getName() {
        return "extension";
    }

    @Override
    public String getUsage(CommandSource source) {
        return "extension";
    }

    @Override
    public void run(MinecraftServer server, CommandSource source, String[] args) throws CommandException {
        Messenger.m(source, "g Hi from extension");
    }
}
