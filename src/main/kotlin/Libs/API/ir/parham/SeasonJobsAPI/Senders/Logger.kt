package Libs.API.ir.parham.SeasonJobsAPI.Senders

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import java.util.logging.Level

class Logger {
    fun log(arg : String)
    {
        Bukkit.getLogger().log(Level.INFO, colorize("&7[&6SeasonJobs&7] $arg"))
    }
    fun colorize(arg : String) : String
    {
        return ChatColor.translateAlternateColorCodes('&', arg)
    }
}