package org.parham.seasonjob.config

import annotations.Config
import annotations.ConfigField
import annotations.SubConfigObject
import models.FileYaml
import java.io.File

const val path = "plugins/SeasonJob/config.yml"

@Config(path)
object ListmemberConfig {
    private val config = FileYaml(File(path))

    // listmembers inventory size
    @ConfigField("listmembers")
    var size: Int = 54

    @SubConfigObject("listmembers-player")
    object Player {
        @ConfigField("type")
        var type: String = "PLAYER_HEAD"

        @ConfigField("name")
        var name: String = "&e&l%player_name%"

        @ConfigField("lore")
        var lore: List<String> = emptyList()

        @ConfigField("slots")
        var slots: List<Int> = emptyList()
    }

    @SubConfigObject("listmembers-actions")
    object Actions {
        @ConfigField("next")
        var next: List<Int> = emptyList()

        @ConfigField("back")
        var back: List<Int> = emptyList()
    }

    object Items {
        private var items: MutableMap<String, MutableMap<String, Any?>>? = config
            .get(
                "listmembers-items",
                mutableMapOf()
            )

        fun getKeys(): MutableSet<String> {
            return items?.keys ?: mutableSetOf<String>()
        }

        operator fun get(key: String): Item? {
            val itemInMap: MutableMap<String, Any?> = items?.get(key) ?: return null
            return Item(
                name = itemInMap["name"] as? String ?: return null,
                type = itemInMap["type"] as? String ?: return null,
                lore = itemInMap["lore"] as? List<String> ?: return null,
                slot = itemInMap["slot"] as? List<Int> ?: return null
            )
        }

        data class Item(
            var name: String,
            var type: String,
            var lore: List<String>,
            var slot: List<Int>
        )
    }
}