package Libs.API.ir.parham.SeasonJobsAPI.Event.Member

import Libs.API.ir.parham.SeasonJobsAPI.Event.SeasonEvent
import ir.parham.SeasonJobsAPI.Actions.Member
import org.bukkit.OfflinePlayer

class MemberEvent(private val newJobName: String,
                  private val newWarn: Int,
                  private val newPlaytime: Int,
                  private val member: Member.Members,
                  private val admin: String,
                  private val eventType: MemberEventType) : SeasonEvent()
{

    fun getMember() : Member.Members
    {
        return member
    }
    fun getAdmin() : String
    {
        return admin
    }
    fun getEventType() : MemberEventType
    {
        return eventType
    }
    fun getNewJob() : String
    {
        return newJobName
    }
    fun getOldJob() : String
    {
        return member.JobName
    }
    fun getNewWarn() : Int
    {
        return newWarn
    }
    fun getOldWarn() : Int
    {
        return member.Warns
    }
    fun getNewPlaytime() : Int
    {
        return newPlaytime
    }
    fun getOldPlaytime() : Int
    {
        return member.PlayTime
    }
}