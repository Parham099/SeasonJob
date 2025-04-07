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

        val manager = Bukkit.getPluginManager()

        manager.registerEvents(List(), this)
        manager.registerEvents(ListMember(), this)
        Bukkit.getPluginCommand("seasonjob")?.setExecutor(MainCommands())
    }

    override fun onDisable() {
        // Plugin shutdown logic
        Member().saveAll()
        Job().saveAll()
    }
}
