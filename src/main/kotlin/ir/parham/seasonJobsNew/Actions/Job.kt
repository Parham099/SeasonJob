package ir.parham.seasonJobsNew.Actions

import Config
import ir.parham.seasonJobsNew.DriverManager.Configs
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration

class Job {
    class Jobs(val Name: String, val MemberSize: Int, val PlayTime : Int, val Prefix : String, val Suffix : String)
    {
        fun Jobs()
        {
            jobList.add(Name)
            jobs.put(Name, this)
        }
    }
    companion object
    {
        var jobList = ArrayList<String>()
        var jobs : HashMap<String,Jobs> = HashMap()
    }


    fun create(name: String, memberSize: Int, playTime: Int, prefix : String, suffix : String) : Boolean
    {
        if (!contains(name))
        {
            Jobs(name, memberSize, playTime, prefix, suffix).Jobs()

            return true
        } else
        {
            return false
        }
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
        if (jobs.containsKey(name) && jobList.contains(name))
        {
            jobs.remove(name)
            jobList.remove(name)

            return true
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

                var section : ConfigurationSection = jobsConfig.getConfigurationSection(key)!!

                section.createSection("members")
                section.createSection("playtime")
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

        for (key in jobsConfig.getKeys(true))
        {
            val jobSection : ConfigurationSection = jobsConfig.getConfigurationSection(key)!!
            if (jobSection != null)
            {
                if (jobSection.contains("members") && jobSection.contains("playtime"))
                {
                    create(key, jobSection.getInt("members"), jobSection.getInt("playtime"),
                        jobSection.getString("prefix").toString(),
                        jobSection.getString("suffix").toString()
                    )
                }
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