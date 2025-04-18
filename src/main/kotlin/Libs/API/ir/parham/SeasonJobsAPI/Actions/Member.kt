package ir.parham.SeasonJobsAPI.Actions

import Libs.API.ir.parham.SeasonJobsAPI.DriverManager.Config
import Libs.API.ir.parham.SeasonJobsAPI.Dependencies.Luckperms
import Libs.API.ir.parham.SeasonJobsAPI.Event.Member.MemberEvent
import Libs.API.ir.parham.SeasonJobsAPI.Event.Member.MemberEventType
import Libs.API.ir.parham.SeasonJobsAPI.Event.SeasonEventManager
import Libs.API.ir.parham.SeasonJobsAPI.Senders.Logger

import ir.parham.SeasonJobsAPI.DriverManager.Configs
import net.luckperms.api.LuckPermsProvider
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Member(val admin: String = "No Information") {
    val luckperms = Luckperms()
    val logger = Logger()
    companion object
    {
        val membersByJob : HashMap<String, List<UUID>> = HashMap()
        val members : HashMap<UUID,Members> = HashMap()
    }

    class Members(val UUID : UUID, val Warns : Int, val PlayTime : Int, val JobName : String)
    {
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
        if (!members.containsKey(uuid)) {
            val mem = Members(uuid, 0, 0, jobName)

            val memberEvent = MemberEvent(jobName, 0, 0, mem, admin, MemberEventType.ADD)
            SeasonEventManager().fireEvent(memberEvent)

            if (!memberEvent.isCancelled())
            {
                luckperms.addRank(uuid, jobName)
            }
            else
            {
                remove(uuid, false)
            }
            return true;
        }
        return false;
    }

    fun remove(uuid: UUID, sendEvent: Boolean = true) : Boolean
    {
        if (members.containsKey(uuid))
        {
            val playerJob : Members = members.get(uuid)!!

            val listMembs = ArrayList<UUID>()

            val memberEvent = MemberEvent(
                playerJob.JobName,
                playerJob.Warns,
                playerJob.PlayTime,
                playerJob,
                admin,
                MemberEventType.REMOVE
            )
            if (sendEvent)
            {
                SeasonEventManager().fireEvent(memberEvent)
            }

            if (!sendEvent || !memberEvent.isCancelled())
            {
                for (member in membersByJob.get(playerJob.JobName)!!)
                {
                    if (!member.equals(uuid))
                    {
                        listMembs.add(member)
                    }
                }

                if (listMembs.isEmpty()) {
                    membersByJob.remove(playerJob.JobName)
                }
                else
                {
                    membersByJob.put(playerJob.JobName, listMembs)
                }
                members.remove(uuid)
                luckperms.removeRank(uuid, playerJob.JobName)
                luckperms.addRank(uuid, "default")
                return true;
            }
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
        if (members.containsKey(uuid))
        {
            val memberEvent = MemberEvent(
                jobName,
                warns,
                playTime,
                members.get(uuid)!!,
                admin,
                MemberEventType.SET
            )
            SeasonEventManager().fireEvent(memberEvent)

            if (!memberEvent.isCancelled())
            {
                remove(uuid, false)

                Members(uuid, warns, playTime, jobName)
                Luckperms().addRank(uuid, jobName)
            }
            return true;
        }
        return false
    }

    fun contains(uuid: UUID) : Boolean
    {
        return list().contains(uuid)
    }

    // run load all synchronized
    @Synchronized
    fun loadAll()
    {
        val dataConfig : FileConfiguration = Config().get(Configs.DATA)!!

        for (key in dataConfig.getKeys(false))
        {
            load(UUID.fromString(key))
        }
    }
    fun load(uuid: UUID)
    {
        try
        {
            val dataConfig: FileConfiguration = Config().get(Configs.DATA)!!
            val dataSection: ConfigurationSection = dataConfig.getConfigurationSection(uuid.toString())!!


            if (dataSection.contains("warn") && dataSection.contains("playtime"))
            {
                if (luckperms.isEnable())
                {
                    LuckPermsProvider.get().userManager.loadUser(uuid)
                }
                Members(
                    uuid,
                    dataSection.getInt("warn"),
                    dataSection.getInt("playtime"),
                    dataSection.getString("job").toString()
                )
            }
        }
        catch (e : Exception)
        {
            logger.log("Exception loading members ($uuid)")
        }
    }

    fun save(uuid: UUID) : Boolean
    {
        if (members.keys.contains(uuid))
        {
            try
            {
                Config().recreate(Configs.DATA)
                var dataConfig: FileConfiguration = Config().get(Configs.DATA)!!

                for (key in members.keys)
                {
                    dataConfig.createSection(key.toString())

                    var section: ConfigurationSection = dataConfig.getConfigurationSection(key.toString())!!

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
            catch (e : Exception)
            {
                logger.log("Exception while loading player ($uuid)")
            }
        }
        return false
    }

    // async save all
    fun saveAll()
    {
        val tst = Thread(Runnable {
            for (key in members.keys)
            {
                save(key)
            }
        }, UUID.randomUUID().toString())

        tst.start()
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