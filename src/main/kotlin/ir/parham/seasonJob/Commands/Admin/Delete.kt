package ir.parham.seasonJob.Commands.Admin

import Libs.API.ir.parham.SeasonJobsAPI.Senders.Message
import ir.parham.SeasonJobsAPI.Actions.Job
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import java.util.*

class Delete {
    fun delete(sender: CommandSender ,args : Array<out String>) : Boolean
    {
        // job admin delete <name>
        val job : Job = Job()
        val message = Message()

        if (!sender.hasPermission("seasonjobs.admin.delete")) {
            // send deny perm message
            sender.sendMessage(message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "denyPerm"))
            return false
        } else if (args.size < 3) {
            // send delete usage message
            sender.sendMessage(message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "deleteUsage"))
            return false
        } else if (!job.contains(args[2])) {
            // send job is not exits
            sender.sendMessage(message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "jobIsNotExits"))
            return false
        } else {
            if (job.remove(args[2])) {
                // send delete succeed
                sender.sendMessage(message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "deleteSuccess"))
            }
            return true
        }
    }
}