package org.parham.seasonjob.data.member;

import static org.parham.seasonjob.data.member.MemberManager.update;

import org.parham.seasonjob.data.job.Job;
import org.parham.seasonjob.data.job.JobManager;
import org.parham.seasonjob.data.job.Leader;
import org.parham.seasonjob.data.job.LeaderData;

import static org.parham.seasonjob.SeasonJob.luckperms;

import java.util.UUID;

public class MemberData implements Member {
    UUID uuid;
    int warn;
    int playtime;
    int point;

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
        if (job1.getLeader().getUUID() != null && job1.getLeader().getUUID().equals(uuid) && !job1.getName().equals(newJob)) {
            job1.removeLeader();
        }
        job1.removeMember(uuid);

        Job job2 = JobManager.getJob(newJob);
        job2.addMember(uuid);

        luckperms.remove(uuid, job1.getName());
        luckperms.add(uuid, job2.getName());

        setPoint(0);
        update(uuid, this);
    }

    @Override
    public void takeJob() {
        Job job = JobManager.getJob(getJob());
        if (job.getLeader().getUUID() != null && job.getLeader().getUUID().equals(uuid)) {
            job.removeLeader();
        }

        luckperms.remove(uuid, "group." + job.getName());

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

        if (job.getParent() != null && JobManager.getJobsList().contains(job.getParent())) {
            setJob(job.getParent());
            return true;
        } else {
            return false;
        }
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
    public int getPoint() {
        return point;
    }

    @Override
    public void addPoint(int point) {
        this.point += point;
        update(uuid, this);
    }

    @Override
    public void takePoint(int point) {
        this.point -= point;
        update(uuid, this);
    }

    @Override
    public void setPoint(int point) {
        this.point = point;
        update(uuid, this);
    }

    @Override
    public void delete() {
        MemberManager.delete(uuid);
    }

    @Override
    public void setAsLeader() {
        Job job = JobManager.getJob(getJob());

        job.setLeader(uuid);
    }
}
