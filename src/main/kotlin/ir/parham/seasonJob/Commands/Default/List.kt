package ir.parham.seasonJob.Commands.Default

import Libs.API.ir.parham.SeasonJobsAPI.Commands
import Libs.API.ir.parham.SeasonJobsAPI.Senders.Message
import ir.parham.SeasonJobsAPI.Actions.Job
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.UUID
import kotlin.collections.List

class List : Commands, Listener {
    override fun runner(sender: CommandSender, args: Array<out String>) {
        val message = Message()
        if (sender.hasPermission("seasonjobs.list"))
        {
            if (sender !is Player)
            {
                sender.sendMessage(Job().list().toString())
            }
            else
            {
                val inv: Inventory = Bukkit.createInventory(null, 54, "Job Lists - 1.1.5")
                for (key: String in Job().list())
                {
                    val selectedJob = Job().get(key)
                    val i = ItemStack(Material.STONE, 1)
                    val meta = i.itemMeta
                    println(Job().getJobMembers(key))
                    println(selectedJob!!.MemberSize)
                    meta!!.lore = listOf(" ",
                        c("&ePrefix: &6${selectedJob!!.Prefix}"),
                        c("&eSuffix: &6${selectedJob.Suffix}"),
                        c("&ePlayTime: &6${selectedJob.PlayTime}"),
                        c("&eMaxWarn: &6${selectedJob.MaxWarn}"),
                        c("&eMemberSize: &a${Job().getJobMembersSize(key).toString()}&8/&4${selectedJob.MemberSize.toString()}"))

                    meta.setDisplayName(c("&6${key}"))
                    i.itemMeta = meta

                    inv.addItem(i)
                }
                sender.openInventory(inv)
            }
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

    private fun c(txt: String) : String {
        return ChatColor.translateAlternateColorCodes('&', txt)
    }

    @EventHandler
    fun event(event: InventoryClickEvent) {
        if (event.view.title.equals("Job Lists - 1.1.5"))
        {
            event.isCancelled = true
        }
    }
}