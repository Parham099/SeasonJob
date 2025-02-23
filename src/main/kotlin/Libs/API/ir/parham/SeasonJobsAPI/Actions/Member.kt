package ir.parham.SeasonJobsAPI.Actions

import Config
import Libs.API.ir.parham.SeasonJobsAPI.Dependencies.Luckperms
import ir.parham.SeasonJobsAPI.Actions.Member.Members.Companion.members
import ir.parham.SeasonJobsAPI.Actions.Member.Members.Companion.membersByJob

import ir.parham.SeasonJobsAPI.DriverManager.Configs
import net.luckperms.api.LuckPermsProvider
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Member {
    class Members(val UUID : UUID, val Warns : Int, val PlayTime : Int, val JobName : String)
    {
        companion object
        {
            val membersByJob : HashMap<String, List<UUID>> = HashMap()
            val members : HashMap<UUID,Members> = HashMap()
        }

        init
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

    fun list() : List<UUID>
    {
        return members.keys.toList();
    }

    fun add(uuid: UUID, jobName : String) : Boolean
    {
        if (!members.containsKey(uuid))
        {
            Members(uuid, 0, 0, jobName)
            Luckperms().addRank(uuid, jobName)
            return true;
        }
        return false;
    }

    fun remove(uuid: UUID) : Boolean
    {
        if (members.containsKey(uuid))
        {
            val playerJob : String = members.get(uuid)!!.JobName

            val listMembs = ArrayList<UUID>()
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
            Luckperms().removeRank(uuid, playerJob)
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

    fun set(uuid: UUID, warns: Int, playTime : Int, jobName : String) : Boolean
    {
        if (members.containsKey(uuid)) {
            remove(uuid)
        }
        Members(uuid, warns, playTime, jobName)
        Luckperms().addRank(uuid, jobName)
        return true;
    }

    fun contains(uuid: UUID) : Boolean
    {
        return list().contains(uuid)
    }

    fun loadAll()
    {
        val dataConfig : FileConfiguration = Config().get(Configs.DATA)!!

        var a : Int = 0
        for (key in dataConfig.getKeys(true))
        {
            if (a == 0)
            {
                val dataSection: ConfigurationSection = dataConfig.getConfigurationSection(key)!!
                if (dataSection != null) {
                    if (dataSection.contains("warn") && dataSection.contains("playtime")) {
                        if (Luckperms().isEnable())
                        {
                            LuckPermsProvider.get().userManager.loadUser(UUID.fromString(key))
                        }
                        Members(
                            UUID.fromString(key),
                            dataSection.getInt("warn"),
                            dataSection.getInt("playtime"),
                            dataSection.getString("job").toString()
                        )
                        a++
                    }
                }
            }
            else if (a == 3)
            {
                a = 0
            }
            else
            {
                a++
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
        if (membersByJob.contains(jobName))
        {
            return membersByJob.get(jobName)!!
        }
        return emptyList()
    }
    fun addMinPlaytime(uuid : UUID,pt : Int)
    {
        if (contains(uuid))
        {
            val items = get(uuid)
            val newpt: Int = 60 * pt
            set(uuid, items!!.Warns, items.PlayTime + newpt, items.JobName)
        }
    }
}