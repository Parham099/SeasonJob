package org.parham.seasonjob.data;

import org.bukkit.Bukkit;
import org.parham.seasonjob.data.job.JobManager;
import org.parham.seasonjob.data.member.MemberManager;
import org.parham.seasonjob.SeasonJob;

public class AutoSave {
    private static final long AUTO_SAVE_TIME = SeasonJob.getInstance().getConfig().getLong("auto-save");

    public static void job() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(SeasonJob.getInstance(), JobManager::saveAll, 1, 20 * AUTO_SAVE_TIME);
    }

    public static void member() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(SeasonJob.getInstance(), MemberManager::saveAll, 1, 20 * AUTO_SAVE_TIME);
    }
}
