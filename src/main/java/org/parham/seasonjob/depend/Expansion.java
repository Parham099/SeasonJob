package org.parham.seasonjob.depend;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
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
        return "3.0.0";
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
                    default:
                        return "Not Found";
                }
            } catch (Exception e) {
                switch (identifier) {
                    case "name":
                        return "";
                    case "suffix":
                        return "";
                    case "prefix":
                        return "";
                    case "playtime_p_hours", "playtime_p_hour":
                        return String.valueOf(0);
                    case "playtime_p_min":
                        return String.valueOf(0);
                    case "playtime_p_sec":
                        return String.valueOf(0);
                    case "warns":
                        return String.valueOf(0);
                    case "warns_max":
                        return "Infinity";
                    case "listmembers_max":
                        return "Infinity";
                    case "listmembers_size":
                        return "-1";
                    default:
                        return "Not Found";
                }
            }
        }
    }
    private static String c(String arg)
    {
        return ChatColor.translateAlternateColorCodes('&', arg);
    }
}
