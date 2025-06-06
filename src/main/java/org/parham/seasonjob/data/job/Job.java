package org.parham.seasonjob.data.job;

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
}