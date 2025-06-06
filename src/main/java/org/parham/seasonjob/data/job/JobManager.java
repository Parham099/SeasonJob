package org.parham.seasonjob.data.job;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.parham.seasonjob.data.member.MemberManager;

import static org.parham.seasonjob.SeasonJob.getInstance;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class JobManager {
    static Map<String, Job> jobs = new HashMap<>();

    public static Job getJob(String jobName) {
        return jobs.get(jobName);
    }

    public static void delete(String name) {
        deleteInit(name);
    }
    public static void delete(Job job) {
        deleteInit(job.getName());
    }
    private static void deleteInit(String name) {
        Job job = jobs.get(name);

        for (UUID member : job.getMembers()) {
            MemberManager.delete(member);
        }

        File file = new File(getInstance().getDataFolder().getPath(), "/jobs/" + name + ".yml");
        file.delete();
        jobs.remove(name);
    }

    public static void saveAll() {
        for (String job : jobs.keySet()) {
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

        data.createSection("prefix");
        data.createSection("suffix");
        data.createSection("warn");
        data.createSection("parent");
        data.createSection("playtime");
        data.createSection("member-size");
        data.createSection("members");

        configUpdate(data, job).save(dataFiles);
    }

    public static FileConfiguration configUpdate(FileConfiguration data, Job job) {
        data.set("prefix", job.getPrefix());
        data.set("suffix", job.getSuffix());
        data.set("warn", job.getMaxWarn());
        data.set("parent", job.getParent());
        data.set("playtime", job.getPlaytime());
        data.set("member-size", job.getMemberSize());

        List<String> members = new ArrayList<>();
        for (UUID uuid : job.getMembers()) {
            members.add(uuid.toString());
        }
        data.set("members", members);

        return data;
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
        job.setPrefix(data.getString("prefix"));
        job.setSuffix(data.getString("suffix"));
        job.setMaxWarn(data.getInt("warn"));
        job.setParent(data.getString("parent"));
        job.setPlaytime(data.getInt("playtime"));
        job.setMemberSize(data.getInt("member-size"));

        List<UUID> users = new ArrayList<>();
        for (String key : data.getStringList("members")) {
            users.add(UUID.fromString(key));
        }
        job.setMembers(users);
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
}
