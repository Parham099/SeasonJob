package ir.parham.seasonJob.Commands.Default

import Libs.API.ir.parham.SeasonJobsAPI.Commands
import Libs.API.ir.parham.SeasonJobsAPI.Senders.Message
import ir.parham.SeasonJobsAPI.Actions.Member
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import java.util.ArrayList
import kotlin.collections.List

class Kick : Commands {
    override fun runner(sender: CommandSender, args: Array<out String>) {
        val message = Message()
        if (args.size < 2) {
            // usage
            sender.sendMessage(null, "kickUsage")
        } else if (Bukkit.getOfflinePlayer(args[1]) == null) {
            // player not found
            sender.sendMessage(message.get(Bukkit.getOfflinePlayer(args[1]), "playerNotExits"))
        } else if (!Member().contains(Bukkit.getOfflinePlayer(args[1]).uniqueId)) {
            // is employed
            val player = Bukkit.getOfflinePlayer(args[1])
            sender.sendMessage(message.get(player, "playerIsEmploy"))
        } else if (!sender.hasPermission("seasonjobs.kick." + Member().get(Bukkit.getOfflinePlayer(args[1]).getUniqueId()))) {
            // deny perm
            val player = Bukkit.getOfflinePlayer(args[1])
            sender.sendMessage(message.get(player, "denyPerm"))
        } else {
            val player = Bukkit.getOfflinePlayer(args[1])
            remover(player)
            sender.sendMessage(message.get(player, "kickSuccess"))
            // kicked
        }
    }

    override fun completer(sender: CommandSender, args: Array<out String>): List<String> {
        if (args.size == 2) {
            val players: ArrayList<String> = ArrayList()
            for (player in Bukkit.getOnlinePlayers()) {
                players.add(player.name)
            }
            return players
        }
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