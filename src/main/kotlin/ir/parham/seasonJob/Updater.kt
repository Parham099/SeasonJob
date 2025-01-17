package ir.parham.seasonJob

import ir.parham.SeasonJobsAPI.Actions.Member
import ir.parham.seasonJob.SeasonJob.Companion.instance
import org.bukkit.Bukkit

object Updater {
    fun start() {
        Bukkit.getScheduler().runTaskTimer(instance!!, Runnable {
            for (p in Bukkit.getOnlinePlayers()) {
                val member = Member()
                member.addMinPlaytime(p.uniqueId, 1)
            }
        }, 1, (20 * 60).toLong())
    }
}