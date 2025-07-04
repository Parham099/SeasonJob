package org.parham.seasonjob.events;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.parham.seasonjob.data.Settings;
import org.parham.seasonjob.data.job.Job;
import org.parham.seasonjob.data.job.JobManager;
import org.parham.seasonjob.data.member.Member;
import org.parham.seasonjob.data.member.MemberManager;

public class WarEvents implements Listener {

    @EventHandler
    public void onPlayerKillEvent(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        } else if (!(event.getEntity().getKiller() instanceof Player)) {
            return;
        } else if (!(event.getEntity().isDead())) {
            return;
        } else if (!MemberManager.contains(event.getEntity().getUniqueId())) {
            return;
        } else if (!MemberManager.contains(event.getEntity().getKiller().getUniqueId())) {
            return;
        }

        Player killerPlayer = (Player) event.getEntity().getKiller();
        Player killedPlayer = (Player) ((Player) event.getEntity()).getPlayer();

        eventInit(killerPlayer, killedPlayer);
    }

    private void eventInit(Player killerPlayer, Player killedPlayer) {
        Job killerJob = JobManager.getJob(MemberManager.getMember(killerPlayer.getUniqueId()).getJob());
        Job killedJob = JobManager.getJob(MemberManager.getMember(killedPlayer.getUniqueId()).getJob());

        if (killerJob.getWars().contains(killedJob.getName())) {
            setPoints(killerPlayer, killedPlayer);
            giveNerf(killedPlayer);
            giveRewards(killerPlayer);

            System.out.println(killedJob.getPoint());
            System.out.println(killerJob.getPoint());
            String message = Settings.warKillMessage;
            if (message.length() > 0) {
                message = PlaceholderAPI.setPlaceholders(killerPlayer, message);
                message = message.replace("{killed}", "");
                message = PlaceholderAPI.setPlaceholders(killedPlayer, message);

                Bukkit.broadcastMessage(message);
            }
        }
    }
    private void giveRewards(Player killer) {
        for (String cmd : Settings.rewardKiller) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("{player}", killer.getName()));
        }
    }
    private void giveNerf(Player killed) {
        for (String cmd : Settings.nerfKilled) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("{player}", killed.getName()));
        }
    }
    private void setPoints(Player killerPlayer, Player killedPlayer)  {
        Member killer = MemberManager.getMember(killerPlayer.getUniqueId());
        Member killed = MemberManager.getMember(killedPlayer.getUniqueId());
        Job killerJob = JobManager.getJob(killer.getJob());
        Job killedJob = JobManager.getJob(killed.getJob());

        int point = Settings.warPoint;
        killerJob.addPoint(point);
        killer.addPoint(point);

        if (Settings.lowestPoint < killedJob.getPoint()) {
            killedJob.takePoint(point);
        }
        if (Settings.lowestPoint < killed.getPoint()) {
            killed.takePoint(point);
        }
    }
}
