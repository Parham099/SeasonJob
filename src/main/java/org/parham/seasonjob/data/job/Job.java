package org.parham.seasonjob.data.job;

import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.UUID;

public interface Job {
    void delete();
    void save();

    void setName(String name);
    String getName();

    void setMembers(List<UUID> users);
    void addMember(UUID user);
    void removeMember(UUID user);
    List<UUID> getMembers();

    void setParent(String job);
    void setParent(Job job);
    String getParent();

    void setMaxWarn(int maxWarn);
    int getMaxWarn();

    void setPlaytime(int playtime);
    int getPlaytime();

    void setPrefix(String suffix);
    String getPrefix();

    void setSuffix(String suffix);
    String getSuffix();

    void setMemberSize(int maxMember);
    int getMemberSize();

    void addPeace(Job job);
    void addPeace(String jobName);
    void removePeace(Job job);
    void removePeace(String jobName);
    List<String> getPeaces();
    void setPeaces(List<String> peaces);
    void clearPeaces();

    void addWar(Job job);
    void addWar(String jobName);
    void removeWar(Job job);
    void removeWar(String jobName);
    List<String> getWars();
    void setWars(List<String> wars);
    void clearWars();

    int getPoint();
    void addPoint(int point);
    void takePoint(int point);
    void setPoint(int point);

    void setLeader(UUID uuid);
    void setLeader(Leader leaderObject);
    void removeLeader();
    Leader getLeader();
    void setLeaderPerm(LeaderAccess node, boolean isEnabled);
}