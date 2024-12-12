import ir.parham.seasonJobsNew.DriverManager.Configs
import ir.parham.seasonJobsNew.LoadJobAPI
import ir.parham.seasonJobsNew.LoadJobAPI.Companion.PluginFolder
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

class Config
{
    var configs = HashMap<String, FileConfiguration>()

    fun load(name : Configs)
    {
        LoadJobAPI.Instance?.saveDefaultConfig()
        LoadJobAPI.Instance?.saveResource("${name.name.lowercase()}.yml", false)

        val file = File(PluginFolder, "${name.name.lowercase()}.yml")
        val fileConfig: FileConfiguration = YamlConfiguration.loadConfiguration(file)

        configs.put(name.name.lowercase(), fileConfig)
    }

    fun save(name : Configs, FileConfiguration: FileConfiguration)
    {
        val file = File(PluginFolder, "${name.name.lowercase()}.yml")
        FileConfiguration.save(file)
    }

    fun get(name: Configs) : FileConfiguration?
    {
        return configs.get(name.name.lowercase());
    }

    fun recreate(name: Configs)
    {
        val file = File(PluginFolder, "${name.name.lowercase()}.yml")
        file.delete()
        file.createNewFile()
        load(name)
    }
}