package org.parham.seasonjob.commands.default_cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.parham.seasonjob.commands.SeasonCommand;
import org.parham.seasonjob.data.member.MemberManager;
import org.parham.seasonjob.data.sender.Messages;

import java.util.ArrayList;
import java.util.List;

public class Leave implements SeasonCommand {

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
        return "seasonjob.leave";
    }

    @Override
    public int getNeedArguments() {
        return 0;
    }

    @Override
    public String getUsage() {
        return "";
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args, boolean isLeader, boolean hasAccess) {
        Player player = (Player) sender;

        if ((isLeader && !hasAccess) || (!isLeader && hasPermission() && !player.hasPermission(getPermission()))) {
            player.sendMessage(Messages.getMessage("deny-permission-leave"));
        } else if (!MemberManager.contains(player.getUniqueId())) {
            player.sendMessage(Messages.getMessage("unemployed-leave"));
        } else if (isLeader) {
            player.sendMessage(Messages.getMessage("leave-leader"));
        } else {
            MemberManager.delete(player.getUniqueId());
            player.sendMessage(Messages.getMessage("success-leave"));
        }
    }

    @Override
    public List<String> getCompletions(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
