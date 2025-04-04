package Libs.API.ir.parham.SeasonJobsAPI.DriverManager

import Libs.API.ir.parham.SeasonJobsAPI.Senders.Logger
import ir.parham.SeasonJobsAPI.DriverManager.Configs
import ir.parham.SeasonJobsAPI.LoadJobAPI
import ir.parham.SeasonJobsAPI.LoadJobAPI.Companion.PluginFolder
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class Config
{
    companion object
    {
        var configs = HashMap<String, FileConfiguration>()
    }

    fun load(name : Configs)
    {
        try
        {
            LoadJobAPI.Instance?.saveDefaultConfig()
            LoadJobAPI.Instance?.saveResource("${name.name.lowercase()}.yml", false)

            val file = File(PluginFolder, "${name.name.lowercase()}.yml")
            val fileConfig: FileConfiguration = YamlConfiguration.loadConfiguration(file)

            configs.put(name.name.lowercase(), fileConfig)
        } catch (ex : Exception)
        {
            Logger().log("Exception while loading config ($name)")
        }
    }

    fun save(name : Configs, FileConfiguration: FileConfiguration)
    {
        try
        {
            val file = File(PluginFolder, "${name.name.lowercase()}.yml")
            FileConfiguration.save(file)
        }
        catch (e : Exception)
        {
            Logger().log("Exception while saving config ($name)")
        }
    }

    fun get(name: Configs) : FileConfiguration?
    {
        return configs.get(name.name.lowercase());
    }

    fun recreate(name: Configs)
    {
        try
        {
            val file = File(PluginFolder, "${name.name.lowercase()}.yml")
            file.delete()
            file.createNewFile()
            load(name)
        }
        catch (e : Exception)
        {
            Logger().log("Exception while recreateing config ($name)")
        }
    }
}