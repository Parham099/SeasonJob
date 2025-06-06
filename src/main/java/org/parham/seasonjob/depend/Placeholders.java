package org.parham.seasonjob.depend;

import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.parham.seasonjob.SeasonJob;

public class Placeholders {
    public static void load() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            try {
                new Expansion(SeasonJob.getInstance()).register();
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[SeasonJob] PlaceholderAPI failed to load.");
            }
        }
    }
}
