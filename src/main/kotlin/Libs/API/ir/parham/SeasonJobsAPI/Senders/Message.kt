package Libs.API.ir.parham.SeasonJobsAPI.Senders

import Config
import Libs.API.ir.parham.SeasonJobsAPI.Dependencies.PlaceholderAPI
import ir.parham.SeasonJobsAPI.DriverManager.Configs
import org.bukkit.ChatColor
import org.bukkit.OfflinePlayer

class Message() {
    companion object
    {
        val messages : HashMap<String, String> = HashMap<String, String>()
    }

    fun loadAll()
    {
        val messageConfig = Config().get(Configs.MESSAGE)
        for (message in messageConfig!!.getKeys(true))
        {
            messages.put(message, messageConfig.getString(message).toString())
        }
    }
    fun get(player: OfflinePlayer, name : String) : String
    {
        if (messages.containsKey(name))
        {
            return colorize(messages.get("prefix").toString()) + PlaceholderAPI().setPaPi(colorize(messages.get(name).toString()), player);
        }
        else
        {
            val messageConfig = Config().get(Configs.MESSAGE)
            messageConfig!!.createSection(name)
            messageConfig.set(name, "Null")
            Config().save(Configs.MESSAGE, messageConfig)
            loadAll()
        }
        return ""
    }

    fun colorize(arg: String): String {
        return ChatColor.translateAlternateColorCodes('&', arg)
    }
}