package ir.parham.seasonJob

import Libs.API.ir.parham.SeasonJobsAPI.Event.SeasonEventManager
import ir.parham.SeasonJobsAPI.Actions.Job
import ir.parham.SeasonJobsAPI.Actions.Member
import ir.parham.SeasonJobsAPI.LoadJobAPI
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

        val load = LoadJobAPI()
        load.LoadJobAPI()

        Bukkit.getPluginManager().registerEvents(ir.parham.seasonJob.Commands.Default.List(), this)
        Bukkit.getPluginCommand("seasonjob")?.setExecutor(MainCommands())

        SeasonEventManager().addListener(listener())
    }

    override fun onDisable() {
        // Plugin shutdown logic
        Member().saveAll()
        Job().saveAll()
    }
}
