package ir.parham.seasonJob.Commands.Admin

import Libs.API.ir.parham.SeasonJobsAPI.Senders.Message
import ir.parham.SeasonJobsAPI.Actions.Member
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import java.util.*

class SetJob {
    fun setJob(sender: CommandSender, args: Array<out String>) : Boolean {
        // job admin setjob <user> <new job>

        val message = Message()
        val member = Member()

        if (!sender.hasPermission("seasonjobs.admin.setjob")) {
            // deny perm message
            sender.sendMessage(message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "denyPerm"))
            return false;
        } else if (args.size < 4) {
            // send setjob usage
            sender.sendMessage(message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "setJobUsage"))
            return false;
        } else {
            val player : OfflinePlayer = Bukkit.getOfflinePlayer(args[2])
            if (!Member().contains(player.uniqueId))
            {
                Member().add(player.uniqueId, args[3])
                // set succeed message
                sender.sendMessage(message.get(player, "jobSetSuccess"))
            }
            else
            {
                val player : OfflinePlayer = Bukkit.getOfflinePlayer(args[2])
                val members : Member.Members = member.get(player.uniqueId)!!
                if (member.set(members.UUID, members.Warns, members.PlayTime, args[3])) {
                    // set succeed message
                    sender.sendMessage(message.get(player, "jobSetSuccess"))
                }  else {
                    sender.sendMessage(message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "jobIsNotExits"))
                }
            }
            return true
        }
    }
}