package Libs.API.ir.parham.SeasonJobsAPI.Event

import org.bukkit.event.EventPriority

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class SeasonHandler(val priority: EventPriority = EventPriority.NORMAL, val ignoreCancelled: Boolean = false)
