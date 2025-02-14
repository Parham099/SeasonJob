package ir.parham.seasonJob.Commands.Default

import Libs.API.ir.parham.SeasonJobsAPI.Commands
import Libs.API.ir.parham.SeasonJobsAPI.Senders.Message
import ir.parham.SeasonJobsAPI.Actions.Member
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.UUID
import kotlin.collections.List

class Leave : Commands {
    override fun runner(sender: CommandSender, args: Array<out String>) {
        val message = Message()
        if (sender !is Player) {
            // deny console
            sender.sendMessage(message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "denyConsole"))
        } else if (!sender.hasPermission("seasonjobs.leave")) {
            // deny perm
            sender.sendMessage(message.get(sender, "denyPerm"))
        } else if (!Member().contains(sender.uniqueId)) {
            // player is employed
            sender.sendMessage(message.get(sender, "playerIsEmploy"))
        } else {
            remover(sender)
            sender.sendMessage(message.get(sender, "leaveSuccess"))
            // leaved
        }
    }

    override fun completer(sender: CommandSender, args: Array<out String>): List<String> {
        return listOf("")
    }

    override fun remover(target: OfflinePlayer) {
        Member().remove(target.uniqueId)
    }

    override fun adder(target: OfflinePlayer) {
        TODO("Not yet implemented")
    }

    override fun setter(target: OfflinePlayer) {
        TODO("Not yet implemented")
    }
}