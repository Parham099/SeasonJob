package org.parham.seasonjob;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.parham.seasonjob.commands.Commands;
import org.parham.seasonjob.commands.default_cmd.*;
import org.parham.seasonjob.data.AutoSave;
import org.parham.seasonjob.data.Playtime;
import org.parham.seasonjob.data.Settings;
import org.parham.seasonjob.data.job.JobManager;
import org.parham.seasonjob.data.member.MemberManager;
import org.parham.seasonjob.data.sender.Messages;
import org.parham.seasonjob.depend.Luckperms.JobPermissionManager;
import org.parham.seasonjob.depend.Luckperms.LuckpermsLoaded;
import org.parham.seasonjob.depend.Luckperms.LuckpermsNotFound;
import org.parham.seasonjob.depend.Placeholders;
import org.parham.seasonjob.events.PeaceEvents;
import org.parham.seasonjob.events.WarEvents;

import java.io.File;
import java.io.IOException;

public final class SeasonJob extends JavaPlugin {
    public static JobPermissionManager luckperms;
    private static volatile Plugin instance;
    public static int cooldown; // invite cooldown

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        saveDefaultConfig();

        if (getConfig().getBoolean("default-jobs")) {
            saveResource("jobs/taxi.yml", false);
            saveResource("jobs/police.yml", false);
            saveResource("jobs/fbi.yml", false);
            saveResource("jobs/mafia.yml", false);
        }

        try {
            saveResource("settings.yml", false);
            Settings.load();
            loader();
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage(color("&4An error occurred while loading the plugin."));
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        MemberManager.updateMembersDataFiles();
        JobManager.saveAll();
        MemberManager.saveAll();
    }

    private void loader() throws Exception {
        ConsoleCommandSender console = Bukkit.getConsoleSender();

        File jobFolder = new File(getDataFolder().getPath(), "jobs");
        File dataFolder = new File(getDataFolder().getPath(), "data");


        if (!jobFolder.exists()) {
            jobFolder.mkdir();
            jobFolder.createNewFile();
        }
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
            dataFolder.createNewFile();
        }

        console.sendMessage(color("&a------------------------ &2SeasonJob &a------------------------"));
        console.sendMessage("");
        console.sendMessage(color("&fDependency Manager:"));
        if (Bukkit.getPluginManager().getPlugin("LuckPerms") == null || !getConfig().getBoolean("luck-perms")) {
            luckperms = new LuckpermsNotFound();
            console.sendMessage(color("&fLuckPerms Not Loaded"));
            console.sendMessage(color("&7* Sync jobs with luckperms rank"));
        } else {
            luckperms = new LuckpermsLoaded();
            luckperms.updater();
            console.sendMessage(color("&6LuckPerms Loaded"));
            console.sendMessage(color("&e* Sync jobs with luckperms rank"));
        }
        console.sendMessage("");
        console.sendMessage(color("&2Loading jobs..."));

        try {
            JobManager.loadAll();
            console.sendMessage(color("&aLoaded all jobs.&e " + JobManager.getJobsList()));
        } catch (IOException e) {
            console.sendMessage(color("&4An error occurred while loading jobs. (&c" + e.getMessage() + "&4)"));
            return;
        } catch (InvalidConfigurationException e) {
            console.sendMessage(color("&4The jobs folder could not be loaded. (&c" + e.getMessage() + "&4)"));
            return;
        }

        console.sendMessage(color("&2Loading members..."));
        try {
            MemberManager.loadAll();
            console.sendMessage(color("&aLoaded members."));
        } catch (Exception e) {
            console.sendMessage(color("&4The data folder could not be loaded. (&c" + e.getMessage() + "&4)"));
        }

        console.sendMessage(color("&2Loading jobs auto saver..."));
        AutoSave.job();
        console.sendMessage(color("&aLoaded jobs auto saver."));

        console.sendMessage(color("&2Loading members auto saver."));
        AutoSave.member();
        console.sendMessage(color("&aLoaded members auto saver."));

        console.sendMessage(color("&2Loading invite cooldown time ..."));
        cooldown = getConfig().getInt("cooldown");
        Invite.cooldownManager();
        console.sendMessage(color("&aInvite cooldown time is " + cooldown + "s"));

        console.sendMessage(color("&6Loading Messages..."));
        Messages.loadAll();
        console.sendMessage(color("&eMessages loaded."));

        console.sendMessage(color("&aLoading Commands..."));
        Commands.addCommand("invite", new Invite());
        Commands.addCommand("accept", new Accept());
        Commands.addCommand("deny", new Deny());
        Commands.addCommand("leave", new Leave());
        Commands.addCommand("kick", new Kick());
        Commands.addCommand("promote", new Promote());
        Commands.addCommand("depromote", new DePromote());
        Commands.addCommand("warn", new Warn());
        Commands.addCommand("listmembers", new Listmembers());
        Commands.addCommand("info", new Info());
        Commands.addCommand("war", new War());
        Commands.addCommand("setleader", new SetLeader());
        Bukkit.getPluginCommand("job").setExecutor(new Commands());
        console.sendMessage(color("&2Commands loaded."));
        console.sendMessage("");
        console.sendMessage(color("&eLoadin listmembers ..."));
        Listmembers.listmembersSize = getInstance().getConfig().getInt("listmembers");
        Bukkit.getPluginManager().registerEvents(new Listmembers(), getInstance());
        console.sendMessage(color("&aListmembers Menu Loaded " + Listmembers.listmembersSize));
        console.sendMessage("");
        Playtime.updater();
        console.sendMessage(color("&2Playtime updater loading..."));
        if (getInstance().getConfig().getBoolean("placeholders", true)) {
            console.sendMessage(color("&eLoading placeholders..."));
            Placeholders.load();
        } else {
            console.sendMessage(color("&4Placeholders is deactivate."));
        }

        Bukkit.getPluginManager().registerEvents(new PeaceEvents(), this);
        Bukkit.getPluginManager().registerEvents(new WarEvents(), this);
        console.sendMessage("");
        console.sendMessage(color("&2SeasonJobs &dv&5" + getDescription().getVersion() + "&a-&2R"));
        console.sendMessage(color("&a------------------------ &2SeasonJob &a------------------------"));
    }

    private String color(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static Plugin getInstance() {
        return instance;
    }
}
