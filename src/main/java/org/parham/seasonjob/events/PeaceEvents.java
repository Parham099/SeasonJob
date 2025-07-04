package org.parham.seasonjob.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.parham.seasonjob.data.Settings;
import org.parham.seasonjob.data.job.Job;
import org.parham.seasonjob.data.job.JobManager;
import org.parham.seasonjob.data.member.Member;
import org.parham.seasonjob.data.member.MemberManager;

public class PeaceEvents implements Listener {
    @EventHandler
    public void onDamagePeace(EntityDamageByEntityEvent event) {
        if (Settings.allowedPeaceDamage) {
            return;
        } else if (!(event.getDamager() instanceof Player)) {
            return;
        } else if (!(event.getEntity() instanceof Player)) {
            return;
        }

        doing(event);
    }

    private void doing(EntityDamageByEntityEvent event) {
        Player player = (Player) event.getDamager();
        Player target = (Player) event.getEntity();

        if (!MemberManager.contains(player.getUniqueId())) {
            return;
        } else if (!MemberManager.contains(target.getUniqueId())) {
            return;
        }

        Member playerMember = MemberManager.getMember(player.getUniqueId());
        Member targetMember = MemberManager.getMember(target.getUniqueId());
        Job playerJob = JobManager.getJob(playerMember.getJob());
        Job targetJob = JobManager.getJob(targetMember.getJob());

        if (playerJob.getPeaces().contains(targetJob.getName())) {
            event.setCancelled(true);
            String message = Settings.denyDamageMessage;
            if (message.length() > 0) {
                player.sendMessage(message);
            }
        }
    }
}
