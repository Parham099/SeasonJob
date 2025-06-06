package org.parham.seasonjob.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.parham.seasonjob.data.sender.Messages;

import java.util.*;

public class Commands implements CommandExecutor, TabCompleter {
    private static Map<String, SeasonCommand> commands = new HashMap<String, SeasonCommand>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length < 1 || !commands.containsKey(args[0])) {
            // help message
        } else {
            SeasonCommand seasonCommand = commands.get(args[0]);

            if (seasonCommand.isPlayerOnly() && !(sender instanceof Player)) {
                sender.sendMessage(Messages.getMessage("deny-console"));
                return true;
            }
            List<String> editedArgs = new ArrayList<String>();
            editedArgs.addAll(Arrays.stream(args).toList());
            editedArgs.remove(0);
            if (editedArgs.size() < seasonCommand.getNeedArguments()) {
                sender.sendMessage(seasonCommand.getUsage());
            } else {
                seasonCommand.execute(sender, command, s, editedArgs.toArray(new String[editedArgs.size()]));
            }
        }
        return false;
    }

    public static void addCommand(String command, SeasonCommand seasonCommand) {
        commands.put(command, seasonCommand);
    }

    public static void removeCommand(String command) {
        commands.remove(command);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length <= 1) {
            return Commands.commands.keySet().stream().toList();
        } else {
            return commands.get(args[0]).getCompletions(args);
        }
    }
}
