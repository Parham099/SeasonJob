package org.parham.seasonjob.depend;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeEqualityPredicate;
import org.bukkit.Bukkit;
import org.parham.seasonjob.SeasonJob;
import org.parham.seasonjob.data.job.Job;
import org.parham.seasonjob.data.job.JobManager;

import java.util.UUID;

public class Luckperms {
    private static int SYNC_INIT_TIME_SEC = 10;
    public static boolean isEnable = false;

    public static void checkEnabled() {
        if (SeasonJob.getInstance().getConfig().getBoolean("luck-perms")) {
            isEnable = Bukkit.getPluginManager().getPlugin("LuckPerms") != null;
        }
    }

    public static void updater() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(SeasonJob.getInstance(), () -> {
            LuckPerms luckPerms = LuckPermsProvider.get();

            for (String jobName : JobManager.getJobsList()) {
                Job job = JobManager.getJob(jobName);

                if (!luckPerms.getGroupManager().isLoaded(jobName)) {
                    luckPerms.getGroupManager().createAndLoadGroup(jobName);
                }
                for (UUID id : job.getMembers()) {
                    Node jobRank = Node.builder("group." + jobName).build();
                    User user = luckPerms.getUserManager().getUser(id);

                    if (luckPerms.getUserManager().isLoaded(id)) {
                        user.data().add(jobRank);
                        LuckPermsProvider.get().getUserManager().saveUser(user);
                    }
                }
            }
        }, 1, 20 * SYNC_INIT_TIME_SEC);
    }
}
