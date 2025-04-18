package ir.parham.seasonJob

import Libs.API.ir.parham.SeasonJobsAPI.DriverManager.Config
import Libs.API.ir.parham.SeasonJobsAPI.Senders.Logger
import ir.parham.SeasonJobsAPI.Actions.Job
import ir.parham.SeasonJobsAPI.Actions.Member
import ir.parham.SeasonJobsAPI.DriverManager.Configs
import ir.parham.seasonJob.Commands.Default.Invite
import ir.parham.seasonJob.SeasonJob.Companion.instance
import org.bukkit.Bukkit
import kotlinx.coroutines.*

object Updater {
    val autosaveTime = 10
    var minuteCalculator = 0


    fun start() {
        // run all as all one minute
        Bukkit.getScheduler().runTaskTimer(instance!!, Runnable
        {
            // run update tasks task
            run()
        }, 1, (20 * 60).toLong())

        if (Config().get(Configs.CONFIG)?.getBoolean("luckperms-update-checker") == true)
        {
            FiveSecCheck()
        }
    }

    private fun run()
    {
        minuteCalculator++ // add one to calculated minute

        clearInvite()
        `addPlaytime's`()
        autoSave()
    }
    private fun clearInvite()
    {
        Invite.data.clear() // clear invite cooldown
    }

    // this code part is sync
    private fun `addPlaytime's`()
    {
        // add player playtime
        for (p in Bukkit.getOnlinePlayers()) {
            val member = Member()
            member.addMinPlaytime(p.uniqueId, 1)
        }
    }
    private fun autoSave()
    {
        // check autosave
        if (minuteCalculator == autosaveTime && Config().get(Configs.CONFIG)!!.getBoolean("auto-save-data"))
        {
            // if is enabled run this

            // save all members data
            Member().saveAll()
            // send log on console
            Logger().log("&aMembers data auto saved - next: $autosaveTime in")

            // reset calculator to zero
            minuteCalculator = 0
        }
    }

    private fun FiveSecCheck()
    {
        Bukkit.getScheduler().runTaskTimer(instance!!, Runnable {
            luckpermsChecker()
        }, 1, 20)
    }
    private fun luckpermsChecker()
    {
        for (p in Bukkit.getOnlinePlayers())
        {

            if (p.hasPermission("*") || p.isOp)
            {
                continue
            }
            try
            {
                val jobs = Job().list()
                val member = Member()

                for (job in jobs)
                {
                    if (p.hasPermission("group.$job"))
                    {
                        member.add(p.uniqueId, job)
                        break
                    }
                    else
                    {
                        member.remove(p.uniqueId, false)
                        continue
                    }
                }
            }
            catch (ex: Exception)
            {
                ex.printStackTrace()
            }
        }
    }
}