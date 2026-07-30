package org.parham.seasonjob.commands.default_cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.parham.seasonjob.commands.SeasonCommand;
import org.parham.seasonjob.data.job.Job;
import org.parham.seasonjob.data.job.JobManager;
import org.parham.seasonjob.data.member.MemberManager;
import org.parham.seasonjob.data.sender.Messages;
import org.parham.seasonjob.gui.ListmembersGui;

import java.util.*;
import java.util.stream.Collectors;

public class Listmembers implements SeasonCommand {
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
            String jobName;
            if (args.length == 0) {
                jobName = MemberManager.getMember(player.getUniqueId()).getJob();
            } else {
                jobName = args[0];
            }
            Job job = JobManager.getJob(jobName);
            ListmembersGui.INSTANCE.openListMembers(
                    job,
                    player
            );
            player.sendMessage(Messages.getMessage("success-open-listmembers"));
        }
    }

    @Override
    public List<String> getCompletions(CommandSender sender, String[] args) {
        return JobManager.getJobsList().stream().collect(Collectors.toList());
    }
}
