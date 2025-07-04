package org.parham.seasonjob.depend.Luckperms;

import java.util.UUID;

public interface JobPermissionManager {
    void updater();
    void createGroup(String groupName);
    void delete(String groupName);
    void add(UUID player, String groupName);
    void remove(UUID player, String groupName);
}
