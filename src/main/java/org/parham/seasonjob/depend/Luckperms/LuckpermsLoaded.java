package org.parham.seasonjob.depend.Luckperms;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeEqualityPredicate;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.parham.seasonjob.SeasonJob;
import org.parham.seasonjob.data.job.Job;
import org.parham.seasonjob.data.job.JobManager;
import org.parham.seasonjob.data.member.MemberManager;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class LuckpermsLoaded implements JobPermissionManager {
    private static final int SYNC_INIT_TIME_SEC = SeasonJob.getInstance().getConfig().getInt("luck-perms-sync-time", 10);
    private static final boolean UPDATE_JOBS_WITH_LUCK_PERMS = SeasonJob.getInstance().getConfig().getBoolean("luck-perms-async-users", true);

    public void updater() {
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

                updateJobs(luckPerms, jobName);
            }
        }, 1, 20 * SYNC_INIT_TIME_SEC);
    }

    private void updateJobs(LuckPerms luckPerms, String jobName) {
        if (UPDATE_JOBS_WITH_LUCK_PERMS) {
            Collection<User> loadedUsers = luckPerms.getUserManager().getLoadedUsers();
            List<User> usersInGroup = loadedUsers.stream()
                    .filter(user -> {
                        InheritanceNode node = InheritanceNode.builder(jobName).build();
                        return user.getNodes().contains(node);
                    })
                    .collect(Collectors.toList());

            for (User user : usersInGroup) {
                UUID uuid = user.getUniqueId();

                if (!MemberManager.contains(uuid)) {
                    MemberManager.add(uuid, jobName);
                }
                if (!MemberManager.getMember(uuid).getJob().equals(jobName)) {
                    MemberManager.getMember(uuid).setJob(jobName);
                }
            }
        }
    }

    public void createGroup(String groupName) {

        LuckPermsProvider.get().getGroupManager().createAndLoadGroup(groupName);
    }
    public void delete(String groupName) {
        LuckPermsProvider.get().getGroupManager().deleteGroup(LuckPermsProvider.get().getGroupManager().getGroup(groupName));
    }
    public void add(UUID player, String groupName) {
        User user = LuckPermsProvider.get().getUserManager().getUser(player);
        Node perm = Node.builder("group." + groupName).build();

        if (!user.data().contains(perm , NodeEqualityPredicate.ONLY_KEY).asBoolean()) {
            user.data().add(perm);
            LuckPermsProvider.get().getUserManager().saveUser(user);
        }
    }
    public void remove(UUID player, String groupName) {
        User user = LuckPermsProvider.get().getUserManager().getUser(player);
        Node perm = Node.builder("group." + groupName).build();

        if (user.data().contains(perm , NodeEqualityPredicate.ONLY_KEY).asBoolean()) {
            user.data().remove(perm);
            LuckPermsProvider.get().getUserManager().saveUser(user);
        }
    }
}
