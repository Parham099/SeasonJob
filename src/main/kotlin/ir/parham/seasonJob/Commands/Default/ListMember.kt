package ir.parham.seasonJob.Commands.Default

import Libs.API.ir.parham.SeasonJobsAPI.Commands
import Libs.API.ir.parham.SeasonJobsAPI.Dependencies.PlaceholderAPI
import Libs.API.ir.parham.SeasonJobsAPI.DriverManager.Config
import Libs.API.ir.parham.SeasonJobsAPI.Senders.Message
import ir.parham.SeasonJobsAPI.Actions.Job
import ir.parham.SeasonJobsAPI.Actions.Member
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.OfflinePlayer
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.collections.List

class ListMember : Commands, Listener
{
    companion object
    {
        val players = HashMap<UUID, Int>()
    }

    override fun runner(sender: CommandSender, args: Array<out String>)
    {
        val message = Message()
        // job listmembers <job>
        if (sender !is Player)
        {
            sender.sendMessage(message.get(Bukkit.getOfflinePlayer(UUID.randomUUID()), "denyConsole"))
        }
        else if (args.size < 2)
        {
            sender.sendMessage(message.get(sender, "list-members-usage"))
            // usage
        }
        else if (!Job().contains(args[1]))
        {
            sender.sendMessage(message.get(sender, "jobIsNotExits"))
            // job not found
        }
        else if (!sender.hasPermission("seasonjobs.listmembers"))
        {
            sender.sendMessage(message.get(sender, "denyPerm"))
            // deny perm
        }
        else
        {
            var job = Job().get(args[1])

            val inv = Bukkit.createInventory(null, 54, job!!.Name)

            inv.setItem(45, ItemStack(Material.RED_STAINED_GLASS_PANE))
            inv.setItem(53, ItemStack(Material.LIME_STAINED_GLASS_PANE))

            for (p in Member().getJobMembers(job.Name))
            {
                val member = Member().get(p)

                val i = ItemStack(Material.PLAYER_HEAD)
                val meta = i.itemMeta
                meta!!.setDisplayName(cc("&6${Bukkit.getOfflinePlayer(member!!.UUID).name}"))
                meta.lore = listOf(
                    "",
                    cc("&6Warns: &e${member.Warns}&6/&e${job.MaxWarn}"),
                    cc("&6Playtime: &e${PlaceholderAPI().setPaPi("%seasonjobs_playetime_p_hours%", Bukkit.getOfflinePlayer(member.UUID))}")
                )
                i.itemMeta = meta

                inv.addItem(i)

                players.put(p, 1)
            }

            sender.openInventory(inv)
        }
    }
    @EventHandler
    fun closeEvent(e: InventoryCloseEvent)
    {
        if (Job().list().contains(e.view.title) && players.containsKey(e.player.uniqueId))
        {
            players.remove(e.player.uniqueId)
        }
    }
    @EventHandler
    fun event(e: InventoryClickEvent)
    {
        try {
            if (Job().list().contains(e.view.title)) {
                e.isCancelled = true
                if (e.currentItem == null) return

                val members = Member().getJobMembers(e.view.title)
                var job = Job().get(e.view.title)

                if (e.currentItem!!.type == Material.RED_STAINED_GLASS_PANE) {
                    if (players.containsKey(e.view.player.uniqueId)) {
                        if (players.get(e.view.player.uniqueId)!! > 1) {
                            players.put(e.view.player.uniqueId, players.get(e.view.player.uniqueId)!!.minus(1))
                        }
                    } else {
                        players.put(e.view.player.uniqueId, 1)
                    }
                } else if (e.currentItem!!.type == Material.LIME_STAINED_GLASS_PANE) {
                    if (players.containsKey(e.view.player.uniqueId)) {
                        players.put(e.view.player.uniqueId, players.get(e.view.player.uniqueId)!!.plus(1))
                    } else {
                        players.put(e.view.player.uniqueId, 1)
                    }
                }
            }


            var job = Job().get(e.view.title)
            val inv = Bukkit.createInventory(null, 54, job!!.Name)

            inv.setItem(45, ItemStack(Material.RED_STAINED_GLASS_PANE))
            inv.setItem(53, ItemStack(Material.LIME_STAINED_GLASS_PANE))

            var count = 1
            var number = 1
            for (p in Member().getJobMembers(job.Name)) {
                if (number == 54) {
                    count++
                }
                if (count == players.get(e.view.player.uniqueId)) {
                    val member = Member().get(p)

                    val i = ItemStack(Material.PLAYER_HEAD)
                    val meta = i.itemMeta
                    meta!!.setDisplayName(cc("&6${Bukkit.getOfflinePlayer(member!!.UUID).name}"))
                    meta.lore = listOf(
                        "",
                        cc("&6Warns: &e${member.Warns}&6/&e${job.MaxWarn}"),
                        cc(
                            "&6Playtime: &e${
                                PlaceholderAPI().setPaPi(
                                    "%seasonjobs_playetime_p_hours%",
                                    Bukkit.getOfflinePlayer(member.UUID)
                                )
                            }"
                        )
                    )
                    i.itemMeta = meta

                    inv.addItem(i)
                }
                number++
            }

            e.view.player.openInventory(inv)
        }
        catch (ignored: Exception)
        {

        }
    }

    override fun completer(sender: CommandSender, args: Array<out String>): List<String> {
        return Job().list()
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
    fun cc(arg: String): String
    {
        return ChatColor.translateAlternateColorCodes('&', arg)
    }
}