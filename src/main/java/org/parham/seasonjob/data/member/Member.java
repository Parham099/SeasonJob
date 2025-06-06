package org.parham.seasonjob.data.member;

import java.util.UUID;

public interface Member {
    UUID getUUID();
    void setUUID(UUID uuid);

    void setJob(String job);
    void takeJob();
    String getJob();

    boolean promote();
    boolean dePromote();

    void setWarn(int warn);
    int getWarn();

    void setPlaytime(int playtime);
    int getPlaytime();

    void delete();
}
