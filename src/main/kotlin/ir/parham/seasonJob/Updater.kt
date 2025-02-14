package ir.parham.seasonJob

import Config
import Libs.API.ir.parham.SeasonJobsAPI.Senders.Logger
import ir.parham.SeasonJobsAPI.Actions.Member
import ir.parham.SeasonJobsAPI.DriverManager.Configs
import ir.parham.seasonJob.Commands.Default.Invite
import ir.parham.seasonJob.SeasonJob.Companion.instance
import org.bukkit.Bukkit

object Updater {
    var a = 0

    fun start() {
        Bukkit.getScheduler().runTaskTimer(instance!!, Runnable {
            for (p in Bukkit.getOnlinePlayers()) {
                val member = Member()
                member.addMinPlaytime(p.uniqueId, 1)
            }
            a++;
            Invite.data.clear()
            if (a == 10 && Config().get(Configs.CONFIG)!!.getBoolean("auto-save-data"))
            {
                Member().saveAll()
                Logger().log("&aMembers data auto saved - next: 10min")
                a = 0
            }
        }, 1, (20 * 60).toLong())
    }
}