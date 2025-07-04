package org.parham.seasonjob.data;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.parham.seasonjob.SeasonJob;

import java.io.File;
import java.util.List;

public class Settings {
    public static boolean allowedPeaceDamage;
    public static String denyDamageMessage;
    public static int lowestPoint;

    public static int warPoint;
    public static List<String> nerfKilled;
    public static List<String> rewardKiller;
    public static String warKillMessage;

    public static void load() {
        File settings = new File(SeasonJob.getInstance().getDataFolder(), "settings.yml");
        FileConfiguration settingsConfig = YamlConfiguration.loadConfiguration(settings);

        ConfigurationSection peaceConfig = settingsConfig.getConfigurationSection("peace");
        ConfigurationSection warConfig = settingsConfig.getConfigurationSection("war");

        allowedPeaceDamage = peaceConfig.getBoolean("damage");
        denyDamageMessage = chatColor(peaceConfig.getString("deny-damage-message"));

        warPoint = warConfig.getInt("point");
        nerfKilled = warConfig.getStringList("nerf-killed");
        rewardKiller = warConfig.getStringList("reward-killer");
        warKillMessage = chatColor(warConfig.getString("message"));
        lowestPoint = warConfig.getInt("lowest-point");
    }

    private static String chatColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
