package org.parham.seasonjob.data.member;

import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.parham.seasonjob.data.job.JobManager;
import org.parham.seasonjob.SeasonJob;
import org.parham.seasonjob.depend.Luckperms;

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

        if (Luckperms.isEnable) {
            User user = LuckPermsProvider.get().getUserManager().getUser(id);

            user.data().remove(Node.builder("group." + member.getJob()).build());
            LuckPermsProvider.get().getUserManager().saveUser(user);
        }

        JobManager.getJob(members.get(id).getJob()).removeMember(id);
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
        } else {
            // lp update
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
        config.set("warn", member.getWarn());
        config.set("playtime", member.getPlaytime());

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

        members.put(id, member);
        JobManager.getJob(job).addMember(id);

        if (Luckperms.isEnable) {
            User user = LuckPermsProvider.get().getUserManager().getUser(id);

            user.data().add(Node.builder("group." + member.getJob()).build());
            LuckPermsProvider.get().getUserManager().saveUser(user);
        }
    }

    public static boolean contains(UUID id) {
        return members.containsKey(id);
    }

}
