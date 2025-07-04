package org.parham.seasonjob.data.job;

import org.parham.seasonjob.data.member.Member;
import org.parham.seasonjob.data.member.MemberManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LeaderData implements Leader {
    UUID uuid = null;
    Boolean isEnable = false;
    Map<LeaderAccess, Boolean> accesses = new HashMap<LeaderAccess, Boolean>();

    @Override
    public void setUUID(UUID id) {
        this.uuid = id;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public boolean hasAccess(LeaderAccess access) {
        if (accesses.containsKey(access)) {
            return this.accesses.get(access);
        }

        return false;
    }

    @Override
    public void setAccess(LeaderAccess access, boolean value) {
        this.accesses.put(access, value);
    }

    @Override
    public void setAccesses(Map<LeaderAccess, Boolean> access) {
        this.accesses.putAll(access);
    }

    @Override
    public Map<LeaderAccess, Boolean> getAccesses() {
        return accesses;
    }

    @Override
    public boolean isEnable() {
        return this.isEnable;
    }

    @Override
    public void setEnable(boolean enable) {
        this.isEnable = enable;
    }

    @Override
    public Member getData() {
        return MemberManager.getMember(uuid);
    }
}
