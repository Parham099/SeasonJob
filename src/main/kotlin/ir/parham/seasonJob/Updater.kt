package ir.parham.seasonJob

import Libs.API.ir.parham.SeasonJobsAPI.DriverManager.Config
import Libs.API.ir.parham.SeasonJobsAPI.Senders.Logger
import ir.parham.SeasonJobsAPI.Actions.Member
import ir.parham.SeasonJobsAPI.DriverManager.Configs
import ir.parham.seasonJob.Commands.Default.Invite
import ir.parham.seasonJob.SeasonJob.Companion.instance
import org.bukkit.Bukkit
import kotlinx.coroutines.*
import kotlin.math.min
import ir.parham.seasonJob.Updater.run as run

object Updater {
    var minuteCalculator = 0


    fun start() {
        // run all as all one minute
        Bukkit.getScheduler().runTaskTimer(instance!!, Runnable
        {
            // run async task
            run()
        }, 1, (20 * 60).toLong() * 10)
    }

    @Synchronized // async running
    fun run()
    {
        // add player playtime
        for (p in Bukkit.getOnlinePlayers()) {
            val member = Member()
            member.addMinPlaytime(p.uniqueId, 1)
        }
        minuteCalculator++ // add one to calculated minute
        Invite.data.clear() // clear invite cooldown

        // check autosave
        if (minuteCalculator == 10 && Config().get(Configs.CONFIG)!!.getBoolean("auto-save-data"))
        {
            // if is enabled run this

            // save all members data
            Member().saveAll()
            // send log on console
            Logger().log("&aMembers data auto saved - next: 10min")

            // reset calculator to zero
            minuteCalculator = 0
        }
    }
}