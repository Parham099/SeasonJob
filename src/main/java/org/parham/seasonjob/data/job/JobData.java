package org.parham.seasonjob.data.job;

import static org.parham.seasonjob.data.job.JobManager.update;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JobData implements Job {
    String jobName;
    List<UUID> users = new ArrayList<>();
    String parent;
    String prefix;
    String suffix;
    int warn;
    int playtime;
    int memberSize;

    @Override
    public void delete() {
        JobManager.delete(this);
    }

    @Override
    public void save() {
        JobManager.save(this);
    }

    @Override
    public void setName(String name) {
        String oldName = getName();

        jobName = name;

        JobManager.rename(this, oldName, name);

        update(this);
    }

    @Override
    public String getName() {
        return jobName;
    }

    @Override
    public void setMembers(List<UUID> users) {
        this.users.addAll(users);
        update(this);
    }

    @Override
    public void addMember(UUID user) {
        this.users.add(user);
        update(this);
    }

    @Override
    public void removeMember(UUID user) {
        this.users.remove(user);
        update(this);
    }

    @Override
    public List<UUID> getMembers() {
        return users;
    }

    @Override
    public void setParent(String job) {
        parent = job;
        update(this);
    }

    @Override
    public void setParent(Job job) {
        parent = job.getName();
        update(this);
    }

    @Override
    public String getParent() {
        return parent;
    }

    @Override
    public void setMaxWarn(int maxWarn) {
        this.warn = maxWarn;
        update(this);
    }

    @Override
    public int getMaxWarn() {
        return warn;
    }

    @Override
    public void setPlaytime(int playtime) {
        this.playtime = playtime;
        update(this);
    }

    @Override
    public int getPlaytime() {
        return this.playtime;
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
        update(this);
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public void setSuffix(String suffix) {
        this.suffix = suffix;
        update(this);
    }

    @Override
    public String getSuffix() {
        return suffix;
    }

    @Override
    public void setMemberSize(int maxMember) {
        memberSize = maxMember;
    }

    @Override
    public int getMemberSize() {
        return memberSize;
    }
}