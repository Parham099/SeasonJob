package ir.parham.SeasonJobsAPI.Actions

import Config
import Libs.API.ir.parham.SeasonJobsAPI.Dependencies.Luckperms
import Libs.API.ir.parham.SeasonJobsAPI.Event.Job.JobCreateEvent
import Libs.API.ir.parham.SeasonJobsAPI.Event.Job.JobDeleteEvent
import Libs.API.ir.parham.SeasonJobsAPI.Event.Job.JobEditEvent
import Libs.API.ir.parham.SeasonJobsAPI.Event.Job.JobEvent
import Libs.API.ir.parham.SeasonJobsAPI.Event.SeasonEventManager
import ir.parham.SeasonJobsAPI.DriverManager.Configs
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration
import java.util.UUID

open class Job {
    class Jobs(val Name: String,val MaxWarn: Int, val MemberSize: Int, val PlayTime : Int, val Prefix : String, val Suffix : String)
    {
        init
        {
            val jobEvent = JobEvent(this)
            if (!jobEvent.isCancelled())
            {
                jobList.add(Name)
                jobs.put(Name, this)
            }
        }
    }
    companion object
    {
        var jobList = ArrayList<String>()
        var jobs : HashMap<String,Jobs> = HashMap()
    }

    fun getJobMembers(job: String) : List<UUID>
    {
        return Member().getJobMembers(job)
    }
    fun getJobMembersSize(job: String) : Int
    {
        return getJobMembers(job).size
    }
    fun create(name: String, maxWarn: Int, memberSize: Int, playTime: Int, prefix : String, suffix : String) : Boolean
    {
        if (!contains(name))
        {
            Jobs(name, maxWarn, memberSize, playTime, prefix, suffix)
            val jobCreateEvent = JobCreateEvent(get(name)!!)
            SeasonEventManager().fireEvent(jobCreateEvent)
            if (!jobCreateEvent.isCancelled()) {
                Luckperms().createGroup(name)
                return true
            }

            jobs.remove(name)
            jobList.remove(name)
            return false
        } else
        {
            return false
        }
    }
    fun set(name : String, maxWarn: Int, memberSize : Int, playTime : Int, prefix : String, suffix : String) : Boolean
    {
        if (contains(name))
        {
            val jobSetEvent = JobEditEvent(get(name)!!)
            SeasonEventManager().fireEvent(jobSetEvent)
            if (!jobSetEvent.isCancelled())
            {
                jobs.remove(name)
                jobList.remove(name)
                Jobs(name, maxWarn, memberSize, playTime, prefix, suffix)
                Luckperms().createGroup(name)
                return true
            }
        }
        return false
    }
    fun list() : List<String>
    {
        return jobList;
    }

    fun contains(name: String): Boolean
    {
        var jobs: String = jobList.toString();
        jobs = jobs.replace("[", "")
        jobs = jobs.replace("]", "")
        jobs = jobs.replace(",", " ")
        if (jobs.contains(name))
        {
            return true
        }

        return false
    }

    fun get(name: String) : Jobs?
    {
        return jobs.get(name)
    }

    fun remove(name: String) : Boolean
    {
        if (contains(name))
        {
            val jobDeleteEvent = JobDeleteEvent(get(name)!!)
            val jobEvent = JobEvent(get(name)!!)
            SeasonEventManager().fireEvent(jobEvent)
            SeasonEventManager().fireEvent(jobDeleteEvent)

            if (!jobDeleteEvent.isCancelled() && !jobEvent.isCancelled())
            {
                jobs.remove(name)
                jobList.remove(name)
                Luckperms().deleteGroup(name)

                return true
            }
        }
        return false
    }

    fun save(name: String) : Boolean
    {
        if (jobs.containsKey(name))
        {
            Config().recreate(Configs.JOBS)
            var jobsConfig : FileConfiguration = Config().get(Configs.JOBS)!!

            for (key in jobList)
            {
                jobsConfig.createSection(key)

                val section : ConfigurationSection = jobsConfig.getConfigurationSection(key)!!

                section.createSection("prefix")
                section.createSection("suffix")
                section.createSection("members")
                section.createSection("playtime")
                section.set("prefix", get(key)?.Prefix)
                section.set("suffix", get(key)?.Suffix)
                section.set("members", get(key)?.MemberSize)
                section.set("playtime", get(key)?.PlayTime)
            }

            Config().save(Configs.JOBS, jobsConfig)
            return true
        }
        return false
    }

    fun loadAll()
    {
        val jobsConfig : FileConfiguration = Config().get(Configs.JOBS)!!

        var a : Int = 0
        for (key in jobsConfig.getKeys(true))
        {
            if (a == 0) {
                val jobSection : ConfigurationSection? = jobsConfig.getConfigurationSection(key)

                if (jobSection != null) {
                    create(
                        jobSection.name,
                        jobSection.getInt("maxWarn"),
                        jobSection.getInt("members"),
                        jobSection.getInt("playtime"),
                        jobSection.getString("prefix").toString(),
                        jobSection.getString("suffix").toString()
                    )
                }
                a++
            } else {
                if (a == 4) {
                    a = 0
                    continue
                }
                a++
                continue
            }
        }
    }

    fun saveAll()
    {
        for (key in jobList)
        {
            save(key)
        }
    }
}