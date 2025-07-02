package haruhikage;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import haruhikage.command.ChunkDebugCommand;
import haruhikage.command.SearchCommand;
import haruhikage.command.TickCommand;
import haruhikage.utils.HaruhikageAddonTranslations;
import haruhikage.utils.pubsub.PubSubManager;
import haruhikage.utils.pubsub.PubSubMessenger;
import net.minecraft.server.command.handler.CommandRegistry;

import java.util.Map;

public class HaruhikageAddonServer implements CarpetExtension {
    @Override
    public String version() {
        return "carpet-extension";
    }
    public static final PubSubManager PUBSUB = new PubSubManager();
    public static final PubSubMessenger PUBSUB_MESSENGER = new PubSubMessenger(PUBSUB);

    public static void loadExtension() {
        // add to carpet's extension list
        CarpetServer.manageExtension(new HaruhikageAddonServer());
    }

    @Override
    public void onGameStarted() {
        // let carpet handle the settings
        CarpetServer.settingsManager.parseSettingsClass(HaruhikageAddonSettings.class);
    }

    @Override
    public void registerCommands(CommandRegistry registry) {
        // register commands here
        registry.register(new SearchCommand());
        registry.register(new TickCommand());
        registry.register(new ChunkDebugCommand());
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return HaruhikageAddonTranslations.getTranslationFromResourcePath(lang);
    }
}
