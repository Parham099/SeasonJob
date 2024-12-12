package ir.parham.seasonJobsNew.Actions

import Config

import ir.parham.seasonJobsNew.DriverManager.Configs
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Member {
    class Members(val UUID : UUID, val Warns : Int, val PlayTime : Int, val JobName : String)
    {
        fun Members()
        {
            members.put(UUID, this)

            if (membersByJob.containsKey(JobName))
            {
                var listMembs = ArrayList<UUID>()
                listMembs.addAll(membersByJob.get(JobName)!!)
                membersByJob.put(JobName, listMembs)
            }
            else
            {
                val list = listOf(UUID)
                membersByJob.put(JobName, list);
            }
        }
    }
    companion object
    {
        var membersByJob : HashMap<String, List<UUID>> = HashMap()
        var members : HashMap<UUID,Members> = HashMap()
    }

    fun list() : List<UUID>
    {
        return members.keys.toList();
    }

    fun add(uuid: UUID, jobName : String) : Boolean
    {
        if (!members.containsKey(uuid))
        {
            Members(uuid, 0, 0, jobName).Members()
            return true;
        }
        return false;
    }

    fun remove(uuid: UUID) : Boolean
    {
        if (members.containsKey(uuid))
        {
            val playerJob : String = members.get(uuid)!!.JobName

            var listMembs = ArrayList<UUID>()
            for (member in membersByJob.get(playerJob)!!)
            {
                if (!member.equals(uuid))
                {
                    listMembs.add(member)
                }
            }

            if (listMembs.isEmpty()) {
                membersByJob.remove(playerJob)
            }
            else
            {
                membersByJob.put(playerJob, listMembs)
            }
            members.remove(uuid)
            return true;
        }
        return false;
    }

    fun get(uuid: UUID) : Members?
    {
        if (members.containsKey(uuid))
        {
            return members.get(uuid)
        }
        return null
    }

    fun set(uuid: UUID, warns: Int, playTime : Int, jobName : String)
    {
        remove(uuid)
        Members(uuid, warns, playTime, jobName)
    }

    fun contains(uuid: UUID) : Boolean
    {
        return list().contains(uuid)
    }

    fun loadAll()
    {
        val dataConfig : FileConfiguration = Config().get(Configs.DATA)!!

        for (key in dataConfig.getKeys(true))
        {
            val dataSection : ConfigurationSection = dataConfig.getConfigurationSection(key)!!
            if (dataSection != null)
            {
                if (dataSection.contains("warn") && dataSection.contains("playtime"))
                {
                    Members(UUID.fromString(key), dataSection.getInt("warn"), dataSection.getInt("playtime"), dataSection.getString("job").toString()).Members()
                }
            }
        }
    }

    fun save(uuid: UUID) : Boolean
    {
        if (members.keys.contains(uuid))
        {
            Config().recreate(Configs.DATA)
            var dataConfig : FileConfiguration = Config().get(Configs.DATA)!!

            for (key in members.keys)
            {
                dataConfig.createSection(key.toString())

                var section : ConfigurationSection = dataConfig.getConfigurationSection(key.toString())!!

                section.createSection("playtime")
                section.createSection("warn")
                section.createSection("job")
                section.set("playtime", get(key)?.PlayTime)
                section.set("warn", get(key)?.Warns)
                section.set("job", get(key)?.JobName)
            }

            Config().save(Configs.DATA, dataConfig)
            return true
        }
        return false
    }

    fun saveAll()
    {
        for (key in members.keys) {
            save(key)
        }
    }

    fun getJobMembers(jobName: String) : List<UUID>
    {
        return membersByJob.get(jobName)!!
    }
}