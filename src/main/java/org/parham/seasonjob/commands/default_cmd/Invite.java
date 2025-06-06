package org.parham.seasonjob.commands.default_cmd;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.parham.seasonjob.SeasonJob;
import org.parham.seasonjob.commands.SeasonCommand;
import org.parham.seasonjob.data.job.JobManager;
import org.parham.seasonjob.data.member.Member;
import org.parham.seasonjob.data.member.MemberManager;
import org.parham.seasonjob.data.sender.Messages;

import java.util.*;

public class Invite implements SeasonCommand {
    public static Map<UUID, String> invites = new HashMap<UUID, String>();
    public static Map<UUID, Integer> cooldowns = new HashMap<>();

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
        return "seasonjob.invite";
    }

    @Override
    public int getNeedArguments() {
        return 2;
    }

    @Override
    public String getUsage() {
        return Messages.getMessage("usage-invite");
    }

    @Override
    public void execute(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        String job = args[1];

        if (hasPermission() && !sender.hasPermission(getPermission() + "." + job)) {
            sender.sendMessage(Messages.getMessage("deny-permission-invite"));
        } else if (target == null || !target.isOnline()) {
            sender.sendMessage(Messages.getMessage("invalid-player-invite"));
        } else if ((sender instanceof Player) && !sender.hasPermission("seasonjob.invite.no-cooldown") && cooldowns.containsKey(((Player) sender).getUniqueId())) {
            sender.sendMessage(Messages.getMessage("cooldown-invite").replace("{cooldown}", cooldowns.get(((Player) sender).getUniqueId()).toString()));
        } else if (MemberManager.contains(target.getUniqueId())) {
            sender.sendMessage(Messages.getMessage("employed-invite"));
        } else if (!JobManager.getJobsList().contains(job)) {
            sender.sendMessage(Messages.getMessage("job-not-found-invite"));
        } else {
            sendInvite(sender, (Player) target, job);
        }
    }

    private void sendInvite(CommandSender sender, Player target, String job) {
        invites.put(target.getUniqueId(), job);

        if (sender instanceof Player) {
            cooldowns.put(((Player) sender).getUniqueId(), SeasonJob.cooldown);
        }

        sender.sendMessage(Messages.getMessage("sender-invite").replace("{job}", job));
        target.sendMessage(Messages.getMessage("getter-invite").replace("{job}", job));
    }

    @Override
    public List<String> getCompletions(String[] args) {
        if (args.length == 2) {
            return null;
        } else if (args.length == 3) {
            return JobManager.getJobsList().stream().toList();
        } else {
            return null;
        }
    }

    public static void cooldownManager() {
        Bukkit.getScheduler().runTaskTimer(SeasonJob.getInstance(), () -> {
            for (UUID uuid : cooldowns.keySet()) {
                cooldowns.put(uuid, cooldowns.get(uuid) - 1);
                if (cooldowns.get(uuid) <= 0) {
                    cooldowns.remove(uuid);
                }
            }
        }, 1, 20);
    }
}
