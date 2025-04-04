package ir.parham.seasonJob

import ir.parham.SeasonJobsAPI.Actions.Job
import ir.parham.SeasonJobsAPI.Actions.Member
import ir.parham.SeasonJobsAPI.LoadJobAPI
import ir.parham.seasonJob.Commands.Default.List
import ir.parham.seasonJob.Commands.Default.ListMember
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class SeasonJob : JavaPlugin() {
    companion object
    {
        var instance : Plugin? = null
    }


    override fun onEnable() {
        // Plugin startup logic
        instance = this
        saveDefaultConfig()

        // load plugin lib and api
        val load = LoadJobAPI()
        load.LoadJobAPI()

        Bukkit.getPluginManager().registerEvents(List(), this)
        Bukkit.getPluginManager().registerEvents(ListMember(), this)
        Bukkit.getPluginCommand("seasonjob")?.setExecutor(MainCommands())

//        if (Config().get(Configs.CONFIG)?.getBoolean("editor-gui") == true)
//        {
//            // load editor gui if exist
//            GUI()
//        }
    }

    override fun onDisable() {
        // Plugin shutdown logic
        Member().saveAll()
        Job().saveAll()
    }
}
