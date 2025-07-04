package org.parham.seasonjob.commands.default_cmd;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.parham.seasonjob.commands.SeasonCommand;
import org.parham.seasonjob.data.job.Job;
import org.parham.seasonjob.data.job.JobManager;
import org.parham.seasonjob.data.member.Member;
import org.parham.seasonjob.data.member.MemberManager;
import org.parham.seasonjob.data.sender.Messages;

import java.util.List;

public class SetLeader implements SeasonCommand {
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
        return "seasonjob.setleader";
    }

    @Override
    public int getNeedArguments() {
        return 2;
    }

    @Override
    public String getUsage() {
        return Messages.getMessage("usage-setleader");
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args, boolean isLeader, boolean hasAccess) {
        if (args.length < getNeedArguments()) {
            sender.sendMessage(getUsage());
        } else if (!JobManager.getJobsList().contains(args[1])) {
            sender.sendMessage(Messages.getMessage("job-notfound-setleader"));
        } else if (hasPermission() && (!sender.hasPermission(getPermission() + "." + args[1]) && !sender.hasPermission(getPermission() + ".*"))) {
            sender.sendMessage(Messages.getMessage("deny-permission-setleader"));
        } else if (!Bukkit.getOfflinePlayer(args[0]).hasPlayedBefore()) {
            sender.sendMessage(Messages.getMessage("player-notfound-setleader"));
        } else {
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[0]);
            Job job = JobManager.getJob(args[1]);

            if (!MemberManager.contains(player.getUniqueId())) {
                MemberManager.add(player.getUniqueId(), job.getName());
            }

            Member member = MemberManager.getMember(player.getUniqueId());
            member.setJob(job.getName());
            member.setAsLeader();

            sender.sendMessage(Messages.getMessage("success-setleader"));
        }
    }

    @Override
    public List<String> getCompletions(CommandSender sender, String[] args) {
        if (args.length == 2) {
            return null;
        } else if (args.length == 3) {
            return JobManager.getJobsList().stream().toList();
        } else {
            return List.of("");
        }
    }
}
