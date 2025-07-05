package org.parham.seasonjob.commands.default_cmd;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
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
    private static int now = 0;
    private static final int EXPIRE_INVITE_TIME = 120;
    public static Map<UUID, ArrayList<String>> invites = new HashMap<UUID, ArrayList<String>>();
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
        return 1;
    }

    @Override
    public String getUsage() {
        return Messages.getMessage("usage-invite");
    }

    @Override
    public void execute(CommandSender sender, Command cmd, String label, String[] args, boolean isLeader, boolean hasAccess) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        String job;
        if (args.length >= 2) {
            job = args[1];
        } else if (sender instanceof Player && MemberManager.contains(((Player) sender).getUniqueId())) {
            Player p = (Player) sender;

            job = MemberManager.getMember(p.getUniqueId()).getJob();
        } else {
            sender.sendMessage(getUsage());
            return;
        }

        if ((isLeader && !hasAccess) && (hasPermission() && !sender.hasPermission(getPermission() + "." + job))) {
            sender.sendMessage(Messages.getMessage("deny-permission-invite"));
        } else if (!target.hasPlayedBefore() || !target.isOnline()) {
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

    private void sendInvite(CommandSender sender, OfflinePlayer target, String job) {
        ArrayList invitesList = new ArrayList();
        if (invites.containsKey(target.getUniqueId())) {
            invitesList.addAll(invites.get(target.getUniqueId()));
        }
        invitesList.add(job);
        invites.put(target.getUniqueId(), invitesList);

        if (sender instanceof Player) {
            cooldowns.put(((Player) sender).getUniqueId(), SeasonJob.cooldown);
        }

        sender.sendMessage(Messages.getMessage("sender-invite").replace("{job}", job));
        target.getPlayer().sendMessage(Messages.getMessage("getter-invite").replace("{job}", job));
    }

    @Override
    public List<String> getCompletions(CommandSender sender, String[] args) {
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
            now++;

            if (now >= EXPIRE_INVITE_TIME) {
                invites.clear();
                now = 0;
            }
        }, 1, 20);
    }
}
