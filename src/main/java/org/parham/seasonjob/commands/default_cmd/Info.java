package org.parham.seasonjob.commands.default_cmd;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.parham.seasonjob.commands.SeasonCommand;
import org.parham.seasonjob.data.member.MemberManager;
import org.parham.seasonjob.data.sender.Messages;

import java.util.List;

public class Info implements SeasonCommand {
    @Override
    public boolean hasPermission() {
        return true;
    }

    @Override
    public boolean isPlayerOnly() {
        return false;
    }

    @Override
    public String getPermission() {
        return "seasonjob.info";
    }

    @Override
    public int getNeedArguments() {
        return 0;
    }

    @Override
    public String getUsage() {
        return Messages.getMessage("usage-info");
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args, boolean isLeader, boolean hasAccess) {
        if ((isLeader && !hasAccess) && (hasPermission() && !sender.hasPermission(getPermission()))) {
            sender.sendMessage(Messages.getMessage("deny-permission-info"));
        } else if (!(sender instanceof Player) && args.length == 0) {
            sender.sendMessage(getUsage());
        } else {
            OfflinePlayer target;
            if (args.length == 0) {
                target = Bukkit.getOfflinePlayer(((Player) sender).getUniqueId());
            } else {
                target = Bukkit.getOfflinePlayer(args[0]);
            }

            if (!MemberManager.contains(target.getUniqueId())) {
                sender.sendMessage(PlaceholderAPI.setPlaceholders(target, Messages.getMessage("unemployed-info")));
            } else {
                sender.sendMessage(PlaceholderAPI.setPlaceholders(target, Messages.getMessage("employed-info")));
            }
        }
    }

    @Override
    public List<String> getCompletions(CommandSender sender, String[] args) {
        return null;
    }
}
