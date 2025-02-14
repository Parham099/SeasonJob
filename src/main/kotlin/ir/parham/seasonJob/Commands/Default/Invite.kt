package ir.parham.seasonJob.Commands.Default

import Config
import Libs.API.ir.parham.SeasonJobsAPI.Commands
import Libs.API.ir.parham.SeasonJobsAPI.Senders.Message
import ir.parham.SeasonJobsAPI.Actions.Job
import ir.parham.SeasonJobsAPI.DriverManager.Configs
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.List

class Invite : Commands {
    companion object {
        val data: HashMap<UUID, Int> = HashMap()
        val invite: HashMap<UUID, String> = HashMap()
    }
    override fun runner(sender: CommandSender, args: Array<out String>) {
        val message = Message()
        // job invite <player> <job>
        if (sender !is Player) {
            sender.sendMessage(message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "denyConsole"))
        } else if (args.size < 3) {
            sender.sendMessage(message.get(sender, "inviteUsage"))
            // invite usage
        } else if (!Job().contains(args[2])) {
            sender.sendMessage(message.get(sender, "jobIsNotExits"))
            // job not found
        } else if (!sender.hasPermission("seasonjobs.invite.${args[2]}")) {
            sender.sendMessage(message.get(sender, "denyPerm"))
            // deny perm
        } else if (!Bukkit.getOfflinePlayer(args[1]).isOnline) {
            sender.sendMessage(message.get(sender, "playerNotExits"))
            // player notfound
        } else if (sender is Player && data.containsKey(Bukkit.getPlayer(args[1])!!.uniqueId) && Config().get(Configs.CONFIG)!!.getBoolean("invite-cooldown")) {
            sender.sendMessage(message.get(sender, "cooldown"))
            // cooldown
        } else {
            invite.put(Bukkit.getPlayer(args[1])!!.uniqueId, args[2])
            if (sender is Player) {
                data.put(sender.uniqueId, 60)
            }
            sender.sendMessage(message.get(sender, "SInviteSucc"))
            sender.sendMessage(message.get(sender, "RInviteSucc"))
            // invite successes
        }
    }

    override fun completer(sender: CommandSender, args: Array<out String>): List<String> {
        when (args.size) {
            2 -> {
                val players: ArrayList<String> = ArrayList()
                for (player in Bukkit.getOnlinePlayers()) {
                    players.add(player.name)
                }
                return players
            }
            3 -> {
                return Job().list()
            }
            else -> {
                return listOf("")
            }
        }
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