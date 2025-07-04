package org.parham.seasonjob.commands;

import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.UUID;

public interface SeasonCommand {

    boolean hasPermission();
    boolean isPlayerOnly();
    String getPermission();
    int getNeedArguments();
    String getUsage();
    void execute(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args, boolean isLeader, boolean hasAccess);
    List<String> getCompletions(CommandSender sender, String[] args);
}
