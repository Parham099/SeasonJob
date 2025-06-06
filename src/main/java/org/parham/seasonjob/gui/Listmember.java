package org.parham.seasonjob.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.parham.seasonjob.SeasonJob;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Listmember {
    private static Configuration config = SeasonJob.getInstance().getConfig();
    public static ConfigurationSection action_config = config.getConfigurationSection("listmembers-actions");
    public static ConfigurationSection items_config = config.getConfigurationSection("listmembers-items");

    public static List<Integer> back_slots = action_config.getIntegerList("back");
    public static List<Integer> next_slots = action_config.getIntegerList("next");

    public static ItemStack player;
    public static List<Integer> player_slots;

    public static Map<ItemStack, List<Integer>> items = new HashMap<>();

    static {
        loadMenuItems();
        loadPlayerItem();
    }

    private static void loadMenuItems() {
        for (String key : items_config.getKeys(false)) {
            ConfigurationSection item = items_config.getConfigurationSection(key);

            String name = cc(item.getString("name"));
            Material type = Material.getMaterial(item.getString("type"));
            List<Integer> slots = item.getIntegerList("slot");
            List<String> lores = item.getStringList("lore");

            for (int i = 0; i < lores.size(); i++) {
                lores.set(i, cc(lores.get(i)));
            }

            ItemStack i = new ItemStack(type);
            ItemMeta meta = i.getItemMeta();
            meta.setDisplayName(name);
            meta.setLore(lores);

            i.setItemMeta(meta);

            items.put(i, slots);
        }
    }
    private static void loadPlayerItem() {
        ConfigurationSection player_config = config.getConfigurationSection("listmembers-player");

        player = new ItemStack(Material.getMaterial(player_config.getString("type")));

        ItemMeta meta = player.getItemMeta();
        meta.setDisplayName(cc(player_config.getString("name")));
        List<String> lores = player_config.getStringList("lore");
        for (int i = 0; i < lores.size(); i++) {
            lores.set(i, cc(lores.get(i)));
        }
        meta.setLore(lores);
        player.setItemMeta(meta);

        player_slots = player_config.getIntegerList("slots");
        Collections.sort(player_slots);
    }

    private static String cc(String arg) {
        return ChatColor.translateAlternateColorCodes('&', arg);
    }
}
