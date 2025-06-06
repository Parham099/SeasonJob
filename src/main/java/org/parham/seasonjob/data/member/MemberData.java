package org.parham.seasonjob.data.member;

import static org.parham.seasonjob.data.member.MemberManager.delete;
import static org.parham.seasonjob.data.member.MemberManager.update;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.parham.seasonjob.data.job.Job;
import org.parham.seasonjob.data.job.JobManager;
import org.parham.seasonjob.depend.Luckperms;

import java.util.UUID;

public class MemberData implements Member {
    UUID uuid;
    int warn;
    int playtime;

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void setJob(String newJob) {
        Job job1 = JobManager.getJob(getJob());
        job1.removeMember(uuid);

        Job job2 = JobManager.getJob(newJob);
        job2.addMember(uuid);

        if (Luckperms.isEnable) {
            User user = LuckPermsProvider.get().getUserManager().getUser(uuid);

            user.data().remove(Node.builder("group." + job1.getName()).build());
            user.data().add(Node.builder("group." + job2.getName()).build());

            LuckPermsProvider.get().getUserManager().saveUser(user);
        }

        update(uuid, this);
    }

    @Override
    public void takeJob() {
        Job job = JobManager.getJob(getJob());

        if (Luckperms.isEnable) {
            User user = LuckPermsProvider.get().getUserManager().getUser(uuid);

            user.data().remove(Node.builder("group." + job.getName()).build());
            LuckPermsProvider.get().getUserManager().saveUser(user);
        }

        job.removeMember(uuid);

        MemberManager.delete(uuid);
    }

    @Override
    public String getJob() {
        String name = null;

        for (String key : JobManager.getJobsList()) {
            Job job = JobManager.getJob(key);

            if (job.getMembers().contains(uuid)) {
                name = job.getName();
            }
        }

        return name;
    }

    @Override
    public boolean promote() {
        Job job = JobManager.getJob(getJob());

        setJob(job.getParent());

        return false;
    }

    @Override
    public boolean dePromote() {
        String jobName = getJob();

        for (String jobs : JobManager.getJobsList()) {
            Job job = JobManager.getJob(jobs);
            if (job.getParent().equals(jobName)) {

                setJob(job.getName());
                return true;
            }
        }
        return false;
    }

    @Override
    public void setWarn(int warn) {
        this.warn = warn;

        update(uuid, this);
    }

    @Override
    public int getWarn() {
        return warn;
    }

    @Override
    public void setPlaytime(int playtime) {
        this.playtime = playtime;

        update(uuid, this);
    }

    @Override
    public int getPlaytime() {
        return playtime;
    }

    @Override
    public void delete() {
        MemberManager.delete(uuid);
    }
}
