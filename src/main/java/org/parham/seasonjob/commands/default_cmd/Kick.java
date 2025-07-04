package org.parham.seasonjob.commands.default_cmd;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.parham.seasonjob.commands.SeasonCommand;
import org.parham.seasonjob.data.job.Job;
import org.parham.seasonjob.data.job.JobManager;
import org.parham.seasonjob.data.member.Member;
import org.parham.seasonjob.data.member.MemberManager;
import org.parham.seasonjob.data.sender.Messages;

import java.util.List;

public class Kick implements SeasonCommand {
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
        return "seasonjob.kick";
    }

    @Override
    public int getNeedArguments() {
        return 1;
    }

    @Override
    public String getUsage() {
        return Messages.getMessage("usage-kick");
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args, boolean isLeader, boolean hasAccess) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);


        if (!MemberManager.contains(target.getUniqueId())) {
            sender.sendMessage(Messages.getMessage("unemployed-kick"));
        } else if ((isLeader && !hasAccess) && (hasPermission() && !sender.hasPermission(getPermission() + "." + MemberManager.getMember(target.getUniqueId()).getJob()))) {
            sender.sendMessage(Messages.getMessage("deny-permission-kick"));
        } else {
            if (sender instanceof Player && ((Player) sender).getUniqueId() == target.getUniqueId()) {
                sender.sendMessage(Messages.getMessage("self-kick"));
            } else {
                Member member = MemberManager.getMember(target.getUniqueId());
                Job job = JobManager.getJob(member.getJob());
                if (!job.getLeader().getUUID().equals(target.getUniqueId()) || sender.hasPermission(getPermission() + ".leader")) {
                    MemberManager.delete(target.getUniqueId());
                    sender.sendMessage(Messages.getMessage("success-kick"));
                } else {
                    sender.sendMessage(Messages.getMessage("deny-permission-kick"));
                }
            }
        }
    }

    @Override
    public List<String> getCompletions(CommandSender sender, String[] args) {
        return null;
    }
}
