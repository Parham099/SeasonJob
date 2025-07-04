package org.parham.seasonjob.depend;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.parham.seasonjob.data.job.Job;
import org.parham.seasonjob.data.job.JobManager;
import org.parham.seasonjob.data.member.Member;
import org.parham.seasonjob.data.member.MemberManager;

public class Expansion extends PlaceholderExpansion {
    private final Plugin plugin;

    public Expansion(Plugin plugin) {
        this.plugin = plugin;
    }

    public String getIdentifier() {
        return "seasonjobs";
    }

    public String getAuthor() {
        return "clip";
    }

    public String getVersion() {
        return "3.0.1";
    }

    public String onRequest(OfflinePlayer player, String identifier) {
        if (player == null) {
            return "";
        } else {
            Member member = MemberManager.getMember(player.getUniqueId());

            try {
                Job job = JobManager.getJob(member.getJob());

                switch (identifier) {
                    case "name":
                        return member.getJob();
                    case "suffix":
                        return c(job.getSuffix());
                    case "prefix":
                        return c(job.getPrefix());
                    case "playtime_p_hours", "playtime_p_hour":
                        Float f = member.getPlaytime() / 60.0f / 60.0f;
                        return String.format("%.2f", f);
                    case "playtime_p_min":
                        return String.valueOf(member.getPlaytime() / 60f);
                    case "playtime_p_sec":
                        return String.valueOf(member.getPlaytime());
                    case "warns":
                        return String.valueOf(member.getWarn());
                    case "warns_max":
                        return String.valueOf(job.getMaxWarn());
                    case "listmembers_max":
                        return String.valueOf(job.getMemberSize());
                    case "listmembers_size":
                        return String.valueOf(job.getMembers().size());
                    case "point":
                        return String.valueOf(member.getPoint());
                    case "job_point":
                        return String.valueOf(job.getPoint());
                    case "cent_point":
                        return String.format("%.2f" ,member.getPoint() / job.getPoint() * 100f);
                    case "leader":
                        if (job.getLeader().getUUID() != null) {
                            return Bukkit.getOfflinePlayer(job.getLeader().getUUID()).getName();
                        } else {
                            return "No Leader";
                        }
                    default:
                        return "Not Found";
                }
            } catch (Exception e) {

                return "";
            }
        }
    }
    private static String c(String arg)
    {
        return ChatColor.translateAlternateColorCodes('&', arg);
    }
}
