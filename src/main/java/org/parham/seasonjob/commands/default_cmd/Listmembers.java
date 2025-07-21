package org.parham.seasonjob.commands.default_cmd;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.parham.seasonjob.commands.SeasonCommand;
import org.parham.seasonjob.data.job.Job;
import org.parham.seasonjob.data.job.JobManager;
import org.parham.seasonjob.data.member.MemberManager;
import org.parham.seasonjob.data.sender.Messages;
import org.parham.seasonjob.gui.Listmember;

import java.util.*;
import java.util.stream.Collectors;

public class Listmembers implements SeasonCommand, Listener {
    public static int listmembersSize;
    private static Map<UUID, Integer> inListmembers = new HashMap<>();
    private static Map<UUID, String> inListmembersJob = new HashMap<>();

    @Override
    public boolean hasPermission() {
        return true;
    }

    @Override
    public boolean isPlayerOnly() {
        return true;
    }

    @Override
    public String getPermission() {
        return "seasonjob.listmembers";
    }

    @Override
    public int getNeedArguments() {
        return 0;
    }

    @Override
    public String getUsage() {
        return Messages.getMessage("usage-listmembers");
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args, boolean isLeader, boolean hasAccess) {
        Player player = (Player) sender;

        if ((isLeader && !hasAccess) || (!isLeader && hasPermission() && !player.hasPermission(getPermission()))) {
            player.sendMessage(Messages.getMessage("deny-permissiong-listmembers"));
        } else if (args.length == 0 && !MemberManager.contains(player.getUniqueId())) {
            player.sendMessage(getUsage());
        } else if (args.length >= 1 && !JobManager.getJobsList().contains(args[0])) {
            player.sendMessage(Messages.getMessage("job-not-found"));
        } else {
            String job;
            if (args.length == 0) {
                job = MemberManager.getMember(player.getUniqueId()).getJob();
            } else {
                job = args[0];
            }
            openInv(player, job, 0);
            player.sendMessage(Messages.getMessage("success-open-listmembers"));
        }
    }

    @Override
    public List<String> getCompletions(CommandSender sender, String[] args) {
        return JobManager.getJobsList().stream().collect(Collectors.toList());
    }

    private void openInv(Player player, String jobName, int page) {
        Job job = JobManager.getJob(jobName);
        Inventory inv = Bukkit.createInventory(null, listmembersSize);
        Map<ItemStack, List<Integer>> items = Listmember.items;

        for (ItemStack item : items.keySet()) {
            List<Integer> slots = items.get(item);
            for (int slot : slots) {
                inv.setItem(slot, item);
            }
        }

        int members = job.getMembers().size();
        int pageCount = Listmember.player_slots.size();
        int startMember = pageCount * page;
        int endMember = startMember + pageCount;

        int onSlot = 0;
        if (members > startMember) {
            for (int i = startMember; i < endMember; i++) {
                if (i >= members) {
                    break;
                }

                OfflinePlayer p = Bukkit.getOfflinePlayer(job.getMembers().get(i));
                int slot = Listmember.player_slots.get(onSlot);
                ItemStack item = loadPlayerItem(p);

                inv.setItem(slot, item);
                onSlot++;
            }
        }

        player.openInventory(inv);
        inListmembers.put(player.getUniqueId(), page);
        inListmembersJob.put(player.getUniqueId(), jobName);
    }
    private ItemStack loadPlayerItem(OfflinePlayer player) {
        ItemStack item = Listmember.player.clone();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(PlaceholderAPI.setPlaceholders(player, meta.getDisplayName()));

            List<String> newLore = new ArrayList<>();
            for (String lore : meta.getLore()) {
                newLore.add(PlaceholderAPI.setPlaceholders(player, lore));
            }
            meta.setLore(newLore);
            item.setItemMeta(meta);

            return item;
        }

        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        Player player = (Player) event.getWhoClicked();

        if (inListmembers.containsKey(player.getUniqueId())) {
            int slot = event.getSlot();
            int page = inListmembers.get(player.getUniqueId());

            event.setCancelled(true);
            if (Listmember.next_slots.contains(slot)) {
                if (inv.getItem(Listmember.player_slots.get(Listmember.player_slots.size() - 1)) != null) {
                    inListmembers.put(player.getUniqueId(), page + 1);
                }
                openInv(player, inListmembersJob.get(player.getUniqueId()), inListmembers.get(player.getUniqueId()));
            }
            if (Listmember.back_slots.contains(slot)) {
                if (page != 0) {
                    inListmembers.put(player.getUniqueId(), page - 1);
                }
                openInv(player, inListmembersJob.get(player.getUniqueId()), inListmembers.get(player.getUniqueId()));
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (inListmembers.containsKey(player.getUniqueId())) {
            inListmembers.remove(player.getUniqueId());
            inListmembersJob.remove(player.getUniqueId());
        }
    }
}
