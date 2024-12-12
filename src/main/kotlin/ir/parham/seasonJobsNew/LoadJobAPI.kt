package ir.parham.seasonJobsNew

import Config
import ir.parham.seasonJobsNew.Actions.Job
import ir.parham.seasonJobsNew.Actions.Member
import ir.parham.seasonJobsNew.DriverManager.Configs
import org.bukkit.plugin.Plugin
import java.io.File

class LoadJobAPI {
    companion object
    {
        var PluginFolder : File? = null
        var Instance : Plugin? = null
    }

    fun LoadJobAPI(pluginFile: File, instance: Plugin) : Boolean {
        PluginFolder = pluginFile
        Instance = instance

        if (PluginFolder != null || Instance != null)
        {
            Config().load(Configs.DATA)
            Config().load(Configs.JOBS)

            Job().loadAll()
            Member().loadAll()

            return true
        }
        else
        {
            return false
        }
    }
}