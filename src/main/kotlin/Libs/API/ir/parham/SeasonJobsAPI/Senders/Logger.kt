package Libs.API.ir.parham.SeasonJobsAPI.Senders

import org.bukkit.Bukkit
import org.bukkit.ChatColor

class Logger {
    fun log(arg : String)
    {
        Bukkit.getConsoleSender().sendMessage(colorize("&7[&6SeasonJobs&7] $arg"))
    }
    fun colorize(arg : String) : String
    {
        return ChatColor.translateAlternateColorCodes('&', arg)
    }
}