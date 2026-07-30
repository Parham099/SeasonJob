package org.parham.seasonjob

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage

private val miniMessage = MiniMessage.miniMessage()

fun String.colorize(): Component {
    val wrappedText = colorWrapper(this)

    return MiniMessage
        .miniMessage()
        .deserialize(wrappedText)
        .decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
}

private fun colorWrapper(arg: String): String {
    return arg
        .replace("&1", "<dark_blue>")
        .replace("&2", "<dark_green>")
        .replace("&3", "<dark_aqua>")
        .replace("&4", "<dark_red>")
        .replace("&5", "<dark_purple>")
        .replace("&6", "<gold>")
        .replace("&7", "<gray>")
        .replace("&8", "<dark_gray>")
        .replace("&9", "<blue>")
        .replace("&0", "<black>")
        .replace("&d", "<light_purple>")
        .replace("&l", "<bold>")
        .replace("&a", "<green>")
        .replace("&f", "<white>")
        .replace("&e", "<yellow>")
        .replace("&b", "<aqua>")
        .replace("&n", "<underline>")
        .replace("&m", "<strikethrough>")
        .replace("&c", "<red>")
        .replace("&o", "<italic>")
        .replace("&r", "<reset>")
}