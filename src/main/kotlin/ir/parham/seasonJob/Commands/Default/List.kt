package ir.parham.seasonJob.Commands.Default

import Libs.API.ir.parham.SeasonJobsAPI.Commands
import Libs.API.ir.parham.SeasonJobsAPI.Senders.Message
import ir.parham.SeasonJobsAPI.Actions.Job
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import java.util.UUID
import kotlin.collections.List

class List : Commands {
    override fun runner(sender: CommandSender, args: Array<out String>) {
        val message = Message()
        if (sender.hasPermission("seasonjobs.list"))
        {
            sender.sendMessage(Job().list().toString())
        }
        else
        {
            message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "denyPerm")
        }
    }

    override fun completer(sender: CommandSender, args: Array<out String>): List<String> {
        TODO("Not yet implemented")
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