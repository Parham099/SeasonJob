package org.parham.seasonjob.data.sender;

import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.parham.seasonjob.SeasonJob;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Messages {
    private static String lang;
    private static Map<String, String> messages = new HashMap<String, String>();

    public static void loadAll() throws Exception {
        Configuration config = SeasonJob.getInstance().getConfig();
        lang = config.getString("lang");

        File langFolder = new File(SeasonJob.getInstance().getDataFolder(), "lang");
        if (!langFolder.exists()) {
            SeasonJob.getInstance().saveResource("lang/EN (US).yml", false);
            SeasonJob.getInstance().saveResource("lang/PR (IR).yml", false);
        }

        File langFile = new File(SeasonJob.getInstance().getDataFolder(), "lang/" + lang + ".yml");
        Configuration langConfig = YamlConfiguration.loadConfiguration(langFile);

        for (String key : langConfig.getKeys(false)) {
            messages.put(key, ChatColor.translateAlternateColorCodes('&', langConfig.getString(key)));
        }
    }

    public static String getMessage(String key) {
        return messages.get("prefix") + messages.get(key);
    }

    public static void clearMessages() {
        messages.clear();
    }
}
