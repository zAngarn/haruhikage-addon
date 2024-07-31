package carpetextension;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpetextension.command.ExtensionCommand;
import carpetextension.utils.CarpetExtensionTranslations;
import net.minecraft.server.command.handler.CommandRegistry;

import java.util.Map;

public class CarpetExtensionServer implements CarpetExtension {
    @Override
    public String version() {
        return "carpet-extension";
    }

    public static void loadExtension() {
        // add to carpet's extension list
        CarpetServer.manageExtension(new CarpetExtensionServer());
    }

    @Override
    public void onGameStarted() {
        // let carpet handle the settings
        CarpetServer.settingsManager.parseSettingsClass(CarpetExtensionSettings.class);
    }

    @Override
    public void registerCommands(CommandRegistry registry) {
        // register commands here
        registry.register(new ExtensionCommand());
    }

    @Override
    public Map<String, String> canHasTranslations(String lang) {
        return CarpetExtensionTranslations.getTranslationFromResourcePath(lang);
    }
}
