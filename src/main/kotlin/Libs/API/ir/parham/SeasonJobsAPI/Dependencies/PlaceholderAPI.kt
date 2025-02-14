package Libs.API.ir.parham.SeasonJobsAPI.Dependencies

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer

class PlaceholderAPI
{
    fun isEnable() : Boolean
    {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
        {
            return true
        }
        return false
    }
    fun setPaPi(arg: String, p: OfflinePlayer) : String
    {
        if (isEnable()) {
            var args = arg
            args = PlaceholderAPI.setPlaceholders(p,arg)
            return args
        }
        return arg
    }
}