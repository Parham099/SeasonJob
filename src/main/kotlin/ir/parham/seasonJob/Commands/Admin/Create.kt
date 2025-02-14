package ir.parham.seasonJob.Commands.Admin

import Libs.API.ir.parham.SeasonJobsAPI.Senders.Message
import ir.parham.SeasonJobsAPI.Actions.Job
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import java.util.*

class Create {
    fun create(sender: CommandSender ,args : Array<out String>) : Boolean
    {
        // job admin create <name> <prefix> <suffix> <listmember> <playtime> <maxwarn>
        val job : Job = Job()
        val message = Message()

        if (!sender.hasPermission("seasonjobs.admin.create")) {
            // send deny perm message
            sender.sendMessage(message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "denyPerm"))
            return false
        } else if (args.size < 8) {
            // send create usage message
            sender.sendMessage(message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "createUsage"))
            return false
        } else if (job.contains(args[2])) {
            // send job is exits
            sender.sendMessage(message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "jobIsExits"))
            return false
        } else if (args[6].toInt() < 0 || args[5].toInt() < 0) {
            // send number is wrong
            sender.sendMessage(message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "wrongNumber"))
            return false
        } else {
            job.create(args[2], args[7].toInt(), args[5].toInt(), args[6].toInt(), args[3],  args[4])
            // send create succeed
            sender.sendMessage(message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "createSuccess"))
            return true
        }
    }
}