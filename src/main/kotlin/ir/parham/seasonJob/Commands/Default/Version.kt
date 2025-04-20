package ir.parham.seasonJob.Commands.Default

import Libs.API.ir.parham.SeasonJobsAPI.Commands
import Libs.API.ir.parham.SeasonJobsAPI.Senders.Message
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import java.util.*
import kotlin.collections.List

class Version : Commands {
    override fun runner(sender: CommandSender, args: Array<out String>) {
        val message = Message()

        if (sender.hasPermission("seasonjobs.version")) {
            sender.sendMessage("SeasonJob running on 2.2.0v")
        } else {
            // deny perm
            sender.sendMessage(message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "denyPerm"))
        }
    }

    override fun completer(sender: CommandSender, args: Array<out String>): List<String> {
        TODO("Not yet implemented")
    }

    override fun remover(target: OfflinePlayer) {
    }

    override fun adder(target: OfflinePlayer) {
    }

    override fun setter(target: OfflinePlayer) {
    }
}