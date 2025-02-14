package Libs.API.ir.parham.SeasonJobsAPI

import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender

interface Commands {
    fun runner(sender: CommandSender, args: Array<out String>)
    fun completer(sender: CommandSender, args: Array<out String>): List<String>
    fun remover(target: OfflinePlayer)
    fun adder(target: OfflinePlayer)
    fun setter(target: OfflinePlayer)
}