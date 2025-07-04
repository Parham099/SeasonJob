package org.parham.seasonjob.commands.default_cmd;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.parham.seasonjob.commands.SeasonCommand;
import org.parham.seasonjob.data.job.JobManager;
import org.parham.seasonjob.data.member.Member;
import org.parham.seasonjob.data.member.MemberManager;
import org.parham.seasonjob.data.sender.Messages;

import java.util.List;

public class Promote implements SeasonCommand {
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
        return "seasonjob.promote";
    }

    @Override
    public int getNeedArguments() {
        return 1;
    }

    @Override
    public String getUsage() {
        return Messages.getMessage("usage-promote");
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args, boolean isLeader, boolean hasAccess) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if (!MemberManager.contains(target.getUniqueId())) {
            sender.sendMessage(Messages.getMessage("unemployed-promote"));
        } else if ((isLeader && !hasAccess) && (hasPermission() && !sender.hasPermission(getPermission() + "." + MemberManager.getMember(target.getUniqueId()).getJob()))) {
            sender.sendMessage(Messages.getMessage("deny-permission-promote"));
        } else if (!JobManager.getJobsList().contains(JobManager.getJob(MemberManager.getMember(target.getUniqueId()).getJob()).getParent())) {
            sender.sendMessage(Messages.getMessage("doesnt-have-parent-promote"));
        } else {
            Member member = MemberManager.getMember(target.getUniqueId());

            if (member.promote()) {
                sender.sendMessage(Messages.getMessage("success-promote"));
            } else {
                sender.sendMessage(Messages.getMessage("error-promote"));
            }
        }
    }

    @Override
    public List<String> getCompletions(CommandSender sender, String[] args) {
        return null;
    }
}
