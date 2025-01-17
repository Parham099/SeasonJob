package ir.parham.seasonJob.Commands.Admin

import Libs.API.ir.parham.SeasonJobsAPI.Senders.Message
import ir.parham.SeasonJobsAPI.Actions.Job
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import java.util.*

class Edit {
    fun edit(sender: CommandSender ,args : Array<out String>) : Boolean
    {
        // job admin edit <name> <prefix> <suffix> <size> <playtime> <maxwarn>
        val job : Job = Job()
        val message = Message()

        if (!sender.hasPermission("seasonjobs.admin.edit")) {
            // send deny perm message
            sender.sendMessage(message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "denyPerm"))
            return false
        } else if (args.size < 8) {
            // send edit usage message
            sender.sendMessage(message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "editUsage"))
            return false
        } else if (!job.contains(args[2])) {
            // send job is not exits
            sender.sendMessage(message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "jobIsNotExits"))
            return false
        } else if (args[6].toInt() < 0 || args[5].toInt() < 0) {
            // send number is wrong
            sender.sendMessage(message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "wrongNumber"))
            return false
        } else {
            job.set(args[3], args[7].toInt(), args[5].toInt(), args[6].toInt() , args[3], args[2])
            // send create succeed
            sender.sendMessage(message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "editSuccess"))
            return true
        }
    }
}