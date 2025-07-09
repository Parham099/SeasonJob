package org.parham.seasonjob.data.job;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.parham.seasonjob.data.member.Member;
import org.parham.seasonjob.data.member.MemberManager;

import static org.parham.seasonjob.SeasonJob.getInstance;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class JobManager {
    public static Map<String, Job> jobs = new HashMap<>();

    public static Job getJob(String jobName) {
        return jobs.containsKey(jobName) ? jobs.get(jobName) : null;
    }

    public static void delete(String name) {
        deleteInit(name);
    }
    public static void delete(Job job) {
        deleteInit(job.getName());
    }
    private static void deleteInit(String name) {
        try {
            File file = new File(getInstance().getDataFolder().getPath(), "/jobs/" + name + ".yml");
            file.delete();
        } catch (Exception e) {

        }

        jobs.remove(name);
    }

    public static void saveAll() {
        for (String job : getJobsList()) {
            save(job);
        }
    }

    public static void save(String name) {
        try {
            saveInit(getJob(name));
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void save(Job job) {
        try {
            saveInit(job);
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }
    private static void saveInit(Job job) throws RuntimeException, IOException, InvalidConfigurationException {
        File dataFiles = new File(getInstance().getDataFolder().getPath(), "jobs/" + job.getName() + ".yml");

        if (dataFiles.exists()) {
            dataFiles.delete();
        }
        dataFiles.createNewFile();

        FileConfiguration data = new YamlConfiguration();
        data.load(dataFiles);

        data.createSection("leader");
        data.createSection("leader.enable");
        data.createSection("leader.uuid");

        data.createSection("leader.accesses");
        data.createSection("prefix");
        data.createSection("suffix");
        data.createSection("warn");
        data.createSection("point");
        data.createSection("parent");
        data.createSection("playtime");
        data.createSection("member-size");
        data.createSection("members");
        data.createSection("wars");
        data.createSection("peaces");
        configUpdate(data, job).save(dataFiles);
    }

    public static FileConfiguration configUpdate(FileConfiguration data, Job job) {

        data.set("leader.enable", job.getLeader().isEnable());
        try {
            if (data.contains("leader.uuid")) {
                data.set("leader.uuid", job.getLeader().getUUID().toString());
            } else {
                data.set("leader.uuid", null);
            }
        } catch (NullPointerException ignored) {
            data.set("leader.uuid", null);
        }

        List<LeaderAccess> leaderAccesses = job.getLeader().getAccesses().keySet().stream().toList();
        String leaderAccessesSection = "leader.accesses.";

        for (LeaderAccess key : leaderAccesses) {
            String accessName = key.name().toLowerCase();

            data.createSection(leaderAccessesSection + accessName);
            data.set(leaderAccessesSection + accessName, job.getLeader().hasAccess(key));
        }

        data.set("prefix", job.getPrefix());
        data.set("suffix", job.getSuffix());
        data.set("warn", job.getMaxWarn());
        data.set("parent", job.getParent());
        data.set("playtime", job.getPlaytime());
        data.set("member-size", job.getMemberSize());
        data.set("point", job.getPoint());

        listsConfigUpdate(data, job);

        return data;
    }

    private static void listsConfigUpdate(FileConfiguration data, Job job) {
        List<String> members = new ArrayList<>();
        for (UUID uuid : job.getMembers()) {
            members.add(uuid.toString());
        }
        data.set("members", members);

        List<String> peaces = new ArrayList<>();
        for (String peace : job.getPeaces()) {
            peaces.add(peace);
        }
        data.set("peaces", peaces);

        List<String> wars = new ArrayList<>();
        for (String war : job.getWars()) {
            wars.add(war);
        }
        data.set("wars", wars);
    }

    public static void loadAll() throws RuntimeException, IOException, InvalidConfigurationException {
        File file = new File(getInstance().getDataFolder().getPath(), "jobs");
        if (!file.exists()) {
            file.mkdir();
            file.createNewFile();
        }

        for (File jobs : file.listFiles()) {
            try {
                load(jobs.getName().replace(".yml", ""));
            } catch (RuntimeException | InvalidConfigurationException | IOException e) {
                Bukkit.getConsoleSender().sendMessage(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public static void load(String name) throws RuntimeException, IOException, InvalidConfigurationException {
        File file = new File(getInstance().getDataFolder().getPath(), "jobs/" + name + ".yml");
        FileConfiguration data = YamlConfiguration.loadConfiguration(file);

        Job job = new JobData();
        job.setName(name);
        loadLeader(job, data);
        job.setPrefix(data.getString("prefix"));
        job.setSuffix(data.getString("suffix"));
        job.setMaxWarn(data.getInt("warn"));
        job.setParent(data.getString("parent"));
        job.setPlaytime(data.getInt("playtime"));
        job.setMemberSize(data.getInt("member-size"));
        job.setPoint(data.getInt("point"));

        loadLists(job, data);
    }

    private static void loadLeader(Job job, ConfigurationSection data) {
        Leader leader = new LeaderData();
        ConfigurationSection leaderSection = data.getConfigurationSection("leader");

        leader.setEnable(leaderSection.getBoolean("enable"));

        if (leaderSection.contains("uuid")) {
            leader.setUUID(UUID.fromString(leaderSection.getString("uuid")));
        }

        ConfigurationSection accessesSection = leaderSection.getConfigurationSection("accesses");
        Map<LeaderAccess, Boolean> accesses = new HashMap<>();
        for (String key : accessesSection.getKeys(false)) {
            LeaderAccess node = LeaderAccess.valueOf(key.toUpperCase());

            accesses.put(node, accessesSection.getBoolean(key));
        }
        leader.setAccesses(accesses);

        job.setLeader(leader);
    }

    private static void loadLists(Job job, Configuration data) throws RuntimeException, IOException, InvalidConfigurationException {
        List<UUID> users = new ArrayList<>();
        for (String key : data.getStringList("members")) {
            users.add(UUID.fromString(key));
        }
        job.setMembers(users);

        List<String> peaces = new ArrayList<>();
        for (String key : data.getStringList("peaces")) {
            peaces.add(key);
        }
        job.setPeaces(peaces);

        List<String> wars = new ArrayList<>();
        for (String key : data.getStringList("wars")) {
            wars.add(key);
        }
        job.setWars(wars);
    }

    public static void rename(Job job, String nowName, String newName) {
        File dataFiles = new File(getInstance().getDataFolder().getPath(), "jobs/" + nowName + ".yml");

        dataFiles.renameTo(new File(dataFiles, newName));

        jobs.remove(nowName);
        jobs.put(newName, job);
    }

    public static Set<String> getJobsList() {
        return jobs.keySet();
    }

    public static void update(Job job) {
        jobs.put(job.getName(), job);
    }

    public static void add(Job job) {
        jobs.put(job.getName(), job);
    }
}
