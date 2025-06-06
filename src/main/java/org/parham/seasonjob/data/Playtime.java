package org.parham.seasonjob.data;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.parham.seasonjob.SeasonJob;
import org.parham.seasonjob.data.member.Member;
import org.parham.seasonjob.data.member.MemberManager;

public class Playtime {
    public static void updater() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(SeasonJob.getInstance(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!MemberManager.contains(player.getUniqueId())) {
                    continue;
                }
                Member member = MemberManager.getMember(player.getUniqueId());
                member.setPlaytime(member.getPlaytime() + 1);
            }
        }, 1, 20);
    }
}
