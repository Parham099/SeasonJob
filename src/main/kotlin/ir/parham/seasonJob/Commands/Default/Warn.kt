package ir.parham.seasonJob.Commands.Default

import Libs.API.ir.parham.SeasonJobsAPI.Commands
import Libs.API.ir.parham.SeasonJobsAPI.Senders.Message
import ir.parham.SeasonJobsAPI.Actions.Job
import ir.parham.SeasonJobsAPI.Actions.Member
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import java.util.*
import kotlin.collections.List

class Warn: Commands {
    override fun runner(sender: CommandSender, args: Array<out String>) {
        val message = Message()
        if (args.size < 3) {
            // usage
            sender.sendMessage(message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "warnUsage"))
        } else if (Bukkit.getOfflinePlayer(args[2]) == null || !Member().contains(Bukkit.getOfflinePlayer(args[2]).uniqueId)) {
            // player is employed
            sender.sendMessage(message.get(Bukkit.getOfflinePlayer(args[2]), "playerIsEmploy"))
        } else {
            val p = Bukkit.getOfflinePlayer(args[2])
            val member = Member().get(p.uniqueId)!!
            val job = Job().get(member.JobName)!!

            when (args[1]) {
                "add" -> {
                    val newWarn = member.Warns + 1

                    if (!sender.hasPermission("seasonjobs.warn.add.${member.JobName}")) {
                        // deny perm
                        sender.sendMessage(message.get(p, "denyPerm"))
                    } else if (newWarn == job.MaxWarn && sender.hasPermission("seasonjobs.kick.${member.JobName}")) {
                        Member().remove(member.UUID)
                        sender.sendMessage(message.get(p, "maxWarnAndKick"))
                        // max warn and kick
                    } else {
                        Member().set(member.UUID, newWarn, member.PlayTime, member.JobName)
                        sender.sendMessage(message.get(p, "warnAdded"))
                        // add warn
                    }
                }
                "take" -> {
                    val newWarn = member.Warns - 1

                    if (!sender.hasPermission("seasonjobs.warn.take.${member.JobName}")) {
                        // deny perm
                        sender.sendMessage(message.get(p, "denyPerm"))
                    } else if (newWarn < 0) {
                        // player warn is 0
                        sender.sendMessage(message.get(p, "warnIsLowest"))
                    } else {
                        Member().set(member.UUID, newWarn, member.PlayTime, member.JobName)
                        sender.sendMessage(message.get(p, "warnTaked"))
                        // take warn
                    }
                }
            }
        }
    }

    override fun completer(sender: CommandSender, args: Array<out String>): List<String> {
        if (args.size == 2) {
            return listOf("add", "take")
        } else if (args.size == 3) {
            val players: ArrayList<String> = ArrayList()
            for (player in Bukkit.getOnlinePlayers()) {
                players.add(player.name)
            }
            return players
        }
        return listOf("")
    }

    override fun remover(target: OfflinePlayer) {
        TODO("Not yet implemented")
    }

    override fun adder(target: OfflinePlayer) {
        TODO("Not yet implemented")
    }

    override fun setter(target: OfflinePlayer) {
        TODO("Not yet implemented")
    }
}