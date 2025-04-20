package ir.parham.seasonJob.Commands.Default

import Libs.API.ir.parham.SeasonJobsAPI.Commands
import Libs.API.ir.parham.SeasonJobsAPI.Senders.Message
import ir.parham.SeasonJobsAPI.Actions.Member
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.List

class Accept : Commands {
    override fun runner(sender: CommandSender, args: Array<out String>) {
        val message = Message()

        if (sender !is Player) {
            sender.sendMessage(message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "denyConsole"))
            // deny console
        } else if (!sender.hasPermission("seasonjobs.accept")) {
            sender.sendMessage(message.get(sender, "denyPerm"))
            // deny perm
        } else if (!Invite.invite.containsKey(sender.uniqueId)) {
            sender.sendMessage(message.get(sender, "emptyInvite"))
            // no invite
        } else {
            adder(sender)
            remover(sender)
            sender.sendMessage(message.get(sender, "jobAccepted"))
            // accepted
        }
    }

    override fun completer(sender: CommandSender, args: Array<out String>): List<String> {
        return listOf("")
    }

    override fun remover(target: OfflinePlayer) {
        Invite.invite.remove(target.uniqueId)
    }

    override fun adder(target: OfflinePlayer) {
        Member().remove(target.uniqueId)
        Member(Invite.inviters.get(target.uniqueId).toString()).add(target.uniqueId, Invite.invite.get(target.uniqueId)!!)
    }

    override fun setter(target: OfflinePlayer) {
        TODO("Not yet implemented")
    }
}