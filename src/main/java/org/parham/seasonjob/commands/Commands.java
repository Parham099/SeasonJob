package org.parham.seasonjob.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.parham.seasonjob.data.job.Job;
import org.parham.seasonjob.data.job.JobManager;
import org.parham.seasonjob.data.job.Leader;
import org.parham.seasonjob.data.job.LeaderAccess;
import org.parham.seasonjob.data.member.Member;
import org.parham.seasonjob.data.member.MemberManager;
import org.parham.seasonjob.data.sender.Messages;

import java.util.*;

public class Commands implements CommandExecutor, TabCompleter {
    private static Map<String, SeasonCommand> commands = new HashMap<String, SeasonCommand>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (args.length < 1 || !commands.containsKey(args[0])) {
            sender.sendMessage(Messages.getMessage("help-message"));
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

                // check player is leader or not
                try {
                    if (Arrays.stream(LeaderAccess.values()).anyMatch(var -> var.name().equals(args[0].toUpperCase()))) {
                        if (sender instanceof Player && MemberManager.contains(((Player) sender).getUniqueId())) {
                            Player p = (Player) sender;
                            Member member = MemberManager.getMember(p.getUniqueId());
                            Job job = JobManager.getJob(member.getJob());

                            if (job.getLeader().getUUID().equals(p.getUniqueId())) {
                                Leader leader = job.getLeader();
                                if (leader.isEnable()) {
                                    boolean hasAccess = leader.hasAccess(LeaderAccess.valueOf(args[0].toUpperCase()));

                                    seasonCommand.execute(sender, command, s, editedArgs.toArray(new String[editedArgs.size()]), true, hasAccess);
                                    return true;
                                }
                            }
                        }
                    }
                } catch (NullPointerException e) {}


                seasonCommand.execute(sender, command, s, editedArgs.toArray(new String[editedArgs.size()]), false, true);
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
            if (commands.containsKey(args[0])) {
                return commands.get(args[0]).getCompletions(commandSender, args);
            } else {
                return Collections.emptyList();
            }
        }
    }
}
