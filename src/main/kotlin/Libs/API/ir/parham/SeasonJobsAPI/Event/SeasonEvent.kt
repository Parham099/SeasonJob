package Libs.API.ir.parham.SeasonJobsAPI.Event

open class SeasonEvent : Cancellable {
    private var cancel = false

    override fun isCancelled(): Boolean {
        return cancel
    }
    override fun setCancelled(var1: Boolean) {
        cancel = true
    }

}