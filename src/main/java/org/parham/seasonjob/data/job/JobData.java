package org.parham.seasonjob.data.job;

import static org.parham.seasonjob.data.job.JobManager.update;

import java.util.*;
import java.util.stream.Collectors;

public class JobData implements Job {
    String jobName;
    Leader leader;
    HashSet<UUID> users = new HashSet<>();
    HashSet<String> wars = new HashSet<>();
    HashSet<String> peaces = new HashSet<>();
    String parent = "";
    String prefix = "";
    String suffix = "";
    int point = 0;
    int warn = 0;
    int playtime = 0;
    int memberSize = 0;

    public JobData() {
        this.leader = new LeaderData();

        for (LeaderAccess access : LeaderAccess.values()) {
            leader.setAccess(access, false);
        }
        leader.setEnable(false);
    }

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
        return users.stream().collect(Collectors.toList());
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

    @Override
    public void addPeace(Job job) {
        peaces.add(job.getName());
        update(this);
    }

    @Override
    public void addPeace(String jobName) {
        peaces.add(jobName);
        update(this);
    }

    @Override
    public void removePeace(Job job) {
        peaces.remove(job.getName());
        update(this);
    }

    @Override
    public void removePeace(String jobName) {
        peaces.remove(jobName);
        update(this);
    }

    @Override
    public List<String> getPeaces() {
        return peaces.stream().collect(Collectors.toList());
    }

    @Override
    public void setPeaces(List<String> peaceList) {
        this.peaces.addAll(peaceList);
        update(this);
    }

    @Override
    public void clearPeaces() {
        peaces.clear();
        update(this);
    }

    @Override
    public void addWar(Job job) {
        wars.add(job.getName());
        update(this);
    }

    @Override
    public void addWar(String jobName) {
        wars.add(jobName);
        update(this);
    }

    @Override
    public void removeWar(Job job) {
        wars.remove(job.getName());
        update(this);
    }

    @Override
    public void removeWar(String jobName) {
        wars.remove(jobName);
        update(this);
    }

    @Override
    public List<String> getWars() {
        return wars.stream().collect(Collectors.toList());
    }

    @Override
    public void setWars(List<String> warList) {
        wars.addAll(warList);
        update(this);
    }

    @Override
    public void clearWars() {
        wars.clear();
        update(this);
    }

    @Override
    public int getPoint() {
        return point;
    }

    @Override
    public void addPoint(int point) {
        this.point += point;
        update(this);
    }

    @Override
    public void takePoint(int point) {
        this.point -= point;
        update(this);
    }

    @Override
    public void setPoint(int point) {
        this.point = point;
        update(this);
    }

    @Override
    public void setLeader(UUID uuid) {
        leader.setUUID(uuid);
        this.setLeader(leader);
        update(this);
    }

    @Override
    public void setLeader(Leader leaderObject) {
        leader = leaderObject;
        update(this);
    }

    @Override
    public void removeLeader() {
        leader.setUUID(null);
        update(this);
    }

    @Override
    public Leader getLeader() {
        return leader;
    }

    @Override
    public void setLeaderPerm(LeaderAccess node, boolean isEnabled) {
        leader.setAccess(node, isEnabled);

        update(this);
    }
}