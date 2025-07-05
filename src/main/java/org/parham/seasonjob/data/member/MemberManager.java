package org.parham.seasonjob.data.member;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.parham.seasonjob.data.job.Job;
import org.parham.seasonjob.data.job.JobManager;
import org.parham.seasonjob.SeasonJob;
import static org.parham.seasonjob.SeasonJob.luckperms;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemberManager {
    static Map<UUID, Member> members = new HashMap<UUID, Member>();

    public static Member getMember(UUID uuid) {
        return members.get(uuid);
    }

    public static void delete(UUID id) {
        Member member = getMember(id);
        Job job = JobManager.getJob(member.getJob());
        if (job.getLeader().getUUID() != null &&  job.getLeader().getUUID().equals(id)) {
            job.removeLeader();
        }
        luckperms.remove(id, member.getJob());

        JobManager.getJob(member.getJob()).removeMember(id);
        members.remove(id);
        update(id, member);
    }

    public static void update(UUID id, Member member) {
        if (members.containsKey(id)) {
            members.put(id, member);
        }

        if (member.getJob() == null) {
            try {
                File folder = new File(SeasonJob.getInstance().getDataFolder(), "data");
                File memFile = new File(folder, id + ".yml");
                if (memFile.exists()) {
                    memFile.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            members.remove(id);
        }
    }

    public static void saveAll() {
        try {
            for (UUID id : members.keySet()) {
                update(id, members.get(id));
                if (members.containsKey(id)) {
                    try {
                        save(getMember(id));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Field to save members.");
        }
    }
    public static boolean save(Member member) throws IOException {
        File folder = new File(SeasonJob.getInstance().getDataFolder(), "data");
        if (!folder.exists()) {
            folder.mkdir();
            folder.createNewFile();
        }

        File date = new File(folder, member.getUUID() + ".yml");
        if (date.exists()) {
            date.delete();
        }
        date.createNewFile();

        FileConfiguration config = YamlConfiguration.loadConfiguration(date);
        config.createSection("warn");
        config.createSection("playtime");
        config.createSection("point");
        config.set("warn", member.getWarn());
        config.set("playtime", member.getPlaytime());
        config.set("point", member.getPoint());

        config.save(date);

        return true;
    }

    public static void loadAll() {
        File folder = new File(SeasonJob.getInstance().getDataFolder(), "data");
        for (File file : folder.listFiles()) {
            if (file.getName().endsWith(".yml")) {
                load(UUID.fromString(file.getName().replace(".yml", "")));
            }
        }
    }

    public static void load(UUID id) {
        File folder = new File(SeasonJob.getInstance().getDataFolder(), "data");
        File date = new File(folder, id + ".yml");

        if (date.exists()) {
            FileConfiguration config = YamlConfiguration.loadConfiguration(date);

            try {
                Member member = new MemberData();
                member.setUUID(id);
                member.setJob(member.getJob());
                member.setWarn(config.getInt("warn"));
                member.setPlaytime(config.getInt("playtime"));
                member.setPoint(config.getInt("point"));

                members.put(id, member);
            } catch (Exception e) {
                date.delete();
            }
        }
    }

    public static void add(UUID id, String job) {
        Member member = new MemberData();

        member.setUUID(id);
        member.setWarn(0);
        member.setPlaytime(0);
        member.setPoint(0);

        members.put(id, member);
        JobManager.getJob(job).addMember(id);


        luckperms.add(id, member.getJob());
    }

    public static boolean contains(UUID id) {
        return members.containsKey(id);
    }

}
