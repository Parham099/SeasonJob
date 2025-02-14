package ir.parham.seasonJob

import ir.parham.seasonJob.Commands.Admin.Create
import ir.parham.seasonJob.Commands.Admin.Delete
import ir.parham.seasonJob.Commands.Admin.Edit
import ir.parham.seasonJob.Commands.Admin.SetJob
import ir.parham.seasonJob.Commands.Default.*
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class MainCommands : CommandExecutor, TabCompleter
{
    override fun onCommand(sender: CommandSender, p1: Command, p2: String, args: Array<out String>?): Boolean {
        if (args == null)
        {
            // job usage
            return false
        }
        if (args.isEmpty())
        {
            return false
        }

        if (args[0].equals("admin", true))
        {
            if (args.size < 2)
            {
                return true
            }

            when (args[1])
            {
                "create" -> {
                    Create().create(sender, args)}
                "delete" -> {
                    Delete().delete(sender, args)}
                "setjob" -> {
                    SetJob().setJob(sender, args)}
                "edit" -> {
                    Edit().edit(sender, args)}
            }
            return true
        }

        when (args[0].lowercase())
        {
            "listmember" -> {sender.sendMessage("Coming soon!")}
            "list" -> {List().runner(sender, args)}
            "warn" -> {Warn().runner(sender, args)}
            "invite" -> {Invite().runner(sender, args)}
            "leave" -> {Leave().runner(sender, args)}
            "kick" -> {Kick().runner(sender, args)}
            "info" -> {Info().runner(sender, args)}
            "deny" -> {Deny().runner(sender, args)}
            "accept" -> {Accept().runner(sender, args)}
        }
        return false
    }

    override fun onTabComplete(p0: CommandSender,p1: Command, p2: String, args: Array<out String>?): List<String> {
        if (args!!.size == 1) {
            return listOf("admin", "listmember", "list", "warn", "invite", "leave", "kick", "deny", "accept", "info")
        }
        if (!args.isEmpty()) {
            if (args[0].equals("admin", true) && args.size == 2) {
                return listOf("create", "delete", "setjob", "edit")
            } else {
                when (args[0].lowercase()) {
                    "listmember" -> {}
                    "list" -> {}
                    "warn" -> {return Warn().completer(p0, args)}
                    "invite" -> {return Invite().completer(p0, args)}
                    "leave" -> {return Leave().completer(p0, args)}
                    "kick" -> {return Kick().completer(p0, args)}
                    "deny" -> {return Deny().completer(p0, args)}
                    "accept" -> {return Accept().completer(p0, args)}
                    "info" -> {return Info().completer(p0, args)}
                }
            }
        }
        return listOf("")
    }
}