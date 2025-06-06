package org.parham.seasonjob.commands.default_cmd;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.parham.seasonjob.commands.SeasonCommand;
import org.parham.seasonjob.data.job.JobManager;
import org.parham.seasonjob.data.member.MemberManager;
import org.parham.seasonjob.data.sender.Messages;

import java.util.List;

public class DePromote implements SeasonCommand {
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
        return "seasonjob.depromote";
    }

    @Override
    public int getNeedArguments() {
        return 1;
    }

    @Override
    public String getUsage() {
        return Messages.getMessage("usage-depromote");
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if (!MemberManager.contains(target.getUniqueId())) {
            sender.sendMessage(Messages.getMessage("unemployed-depromote"));
        } else if (hasPermission() && !sender.hasPermission(getPermission() + "." + MemberManager.getMember(target.getUniqueId()).getJob())) {
            sender.sendMessage(Messages.getMessage("deny-permission-depromote"));
        } else if (MemberManager.getMember(target.getUniqueId()).dePromote()) {
            sender.sendMessage(Messages.getMessage("success-depromote"));
        } else {
            sender.sendMessage(Messages.getMessage("error-depromote"));
        }
    }

    @Override
    public List<String> getCompletions(String[] args) {
        return null;
    }
}
