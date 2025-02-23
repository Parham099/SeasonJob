package Libs.API.ir.parham.SeasonJobsAPI.Event

interface Cancellable {
    fun isCancelled(): Boolean

    fun setCancelled(var1: Boolean)
}