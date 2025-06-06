package org.parham.seasonjob.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface SeasonCommand {

    boolean hasPermission();
    boolean isPlayerOnly();
    String getPermission();
    int getNeedArguments();
    String getUsage();
    void execute(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args);
    List<String> getCompletions(String[] args);
}
