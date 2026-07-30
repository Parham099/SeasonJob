package org.parham.seasonjob.gui

import ir.parham099.seasonMenuBuilder.builder.template.paginated.PaginatedMenuBuilder
import ir.parham099.seasonMenuBuilder.dsl.paginatedMenu
import ir.parham099.seasonMenuBuilder.dsl.staticPage
import ir.parham099.seasonMenuBuilder.runtime.MenuManager.openGui
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.parham.seasonjob.SeasonJob
import org.parham.seasonjob.colorize
import org.parham.seasonjob.data.job.Job
import org.parham.seasonjob.data.job.JobManager
import org.parham.seasonjob.config.ListmemberConfig
import kotlin.collections.chunked
import kotlin.system.measureNanoTime

object ListmembersGui {
    private val menuConfig = ListmemberConfig
    private val playerConfig = ListmemberConfig.Player
    private val actionsConfig = ListmemberConfig.Actions
    private val itemsConfig = ListmemberConfig.Items
    private val jobsMenus = HashMap<String, PaginatedMenuBuilder>()

    fun Job.openListMembers(player: Player) {
        val menu = jobsMenus[this.name] ?: load(this)
        player.openGui(menu)
    }

    private fun load(job: Job): PaginatedMenuBuilder {
        val perPagePlayers = job.members.chunked(playerConfig.slots.size)
        val menu = paginatedMenu {
            for ((index, pagesMembers) in perPagePlayers.withIndex()) {
                staticPage(
                    title = "${job.name} ${index + 1}/${perPagePlayers.size}".colorize(),
                    size = menuConfig.size
                ) {
                    // Load players
                    for ((index, uuid) in pagesMembers.withIndex()) {
                        playerConfig.let {
                            item(
                                material = Material.getMaterial(it.type) ?: Material.PLAYER_HEAD,
                                slots = listOf(it.slots[index])
                            ) {
                                val offPlayer = Bukkit.getOfflinePlayer(uuid)
                                displayName = PlaceholderAPI.setPlaceholders(offPlayer, it.name).colorize()
                                lore = buildList {
                                    for (line in it.lore) {
                                        add(PlaceholderAPI.setPlaceholders(offPlayer, line).colorize())
                                    }
                                }
                            }
                        }
                    }
                    // Load other items
                    for (itemKey in itemsConfig.getKeys()) {
                        val item = itemsConfig[itemKey] ?: continue
                        item(
                            material = Material.getMaterial(item.type) ?: continue,
                            slots = item.slot
                        ) {
                            displayName = item.name.colorize()
                            lore = buildList {
                                for (line in item.lore) {
                                    add(line.colorize())
                                }
                            }
                        }
                    }
                    onClick {
                        val player = it.whoClicked
                        if (actionsConfig.next.contains(it.slot) && hasNextPage(player)) {
                            next(player)
                        } else if (actionsConfig.back.contains(it.slot) && hasPreviousPage(player)) {
                            previous(player)
                        }
                        it.isCancelled = true
                    }
                }
            }
        }
        Bukkit.getScheduler().runTaskLater(
            SeasonJob.getInstance(),
            Runnable { load(JobManager.getJob(job.name)) },
            20L * 60 * 10
        )
        jobsMenus[job.name] = menu
        return menu
    }
}