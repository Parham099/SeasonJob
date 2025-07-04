package org.parham.seasonjob.data.job;

import org.parham.seasonjob.data.member.Member;

import java.util.Map;
import java.util.UUID;

public interface Leader {

    void setUUID(UUID uuid);
    UUID getUUID();
    boolean hasAccess(LeaderAccess access);
    void setAccess(LeaderAccess access, boolean value);
    void setAccesses(Map<LeaderAccess, Boolean> access);
    Map<LeaderAccess, Boolean> getAccesses();
    boolean isEnable();
    void setEnable(boolean enable);
    Member getData();
}
