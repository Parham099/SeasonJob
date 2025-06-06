package org.parham.seasonjob.commands.default_cmd;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.parham.seasonjob.commands.SeasonCommand;
import org.parham.seasonjob.data.job.JobManager;
import org.parham.seasonjob.data.member.Member;
import org.parham.seasonjob.data.member.MemberManager;
import org.parham.seasonjob.data.sender.Messages;

import java.util.List;

public class Warn implements SeasonCommand {
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
        return "seasonjob.warn";
    }

    @Override
    public int getNeedArguments() {
        return 2;
    }

    @Override
    public String getUsage() {
        return Messages.getMessage("usage-warn");
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);

        if (!MemberManager.contains(player.getUniqueId())) {
            sender.sendMessage(Messages.getMessage("unemployed-warn"));
        } else if (hasPermission() && !sender.hasPermission(getPermission() + "." + MemberManager.getMember(player.getUniqueId()).getJob())) {
            sender.sendMessage(Messages.getMessage("deny-permission-warn"));
        } else {
            Member member = MemberManager.getMember(player.getUniqueId());
            int warns = member.getWarn();

            if (args[0].equalsIgnoreCase("clear")) {
                member.setWarn(0);
                sender.sendMessage(Messages.getMessage("clear-warn").replace("{warn}", String.valueOf(member.getWarn())));
            } else if (args[0].equalsIgnoreCase("add")) {
                member.setWarn(warns + 1);
                if (member.getWarn() >= JobManager.getJob(member.getJob()).getMaxWarn()) {
                    member.delete();
                }
                sender.sendMessage(Messages.getMessage("add-warn").replace("{warn}", String.valueOf(member.getWarn())));
            } else if (args[0].equalsIgnoreCase("take")) {
                member.setWarn(warns - 1);
                sender.sendMessage(Messages.getMessage("take-warn").replace("{warn}", String.valueOf(member.getWarn())));
            } else {
                sender.sendMessage(getUsage());
            }
        }
    }

    @Override
    public List<String> getCompletions(String[] args) {
        if (args.length == 2) {
            return List.of("clear", "add", "take");
        } else {
            return null;
        }
    }
}
