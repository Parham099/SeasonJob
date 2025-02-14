package ir.parham.seasonJob.Commands.Default

import Libs.API.ir.parham.SeasonJobsAPI.Commands
import Libs.API.ir.parham.SeasonJobsAPI.Senders.Message
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.List

class Info: Commands {
    override fun runner(sender: CommandSender, args: Array<out String>) {
        if (args.size < 2)
        {
            sender.sendMessage(Message().get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "infoUsage"))
        }
        else if (sender.hasPermission("seasonjobs.info"))
        {
            sender.sendMessage(Message().get(Bukkit.getOfflinePlayer(args[1]), "info"))
        }
        else {
            sender.sendMessage(Message().get(Bukkit.getOfflinePlayer(args[1]), "denyPerm"))
        }
    }

    override fun completer(sender: CommandSender, args: Array<out String>): List<String> {
        return Bukkit.getOnlinePlayers().map { it.name };
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