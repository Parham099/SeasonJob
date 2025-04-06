package Libs.API.ir.parham.SeasonJobsAPI.Event.Member

import Libs.API.ir.parham.SeasonJobsAPI.Event.SeasonEvent
import ir.parham.SeasonJobsAPI.Actions.Member
import org.bukkit.OfflinePlayer

class MemberEvent(private var newJobName: String,
                  private var newWarn: Int,
                  private var newPlaytime: Int,
                  private var member: Member.Members,
                  private var admin: String,
                  private var eventType: MemberEventType) : SeasonEvent()
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
    fun getNewJob(newJob: String)
    {
        newJobName = newJob
    }
    fun getOldJob() : String
    {
        return member.JobName
    }
    fun getNewWarn() : Int
    {
        return newWarn
    }
    fun setNewWarn(newWarnNum: Int)
    {
        newWarn = newWarnNum
    }
    fun getOldWarn() : Int
    {
        return member.Warns
    }
    fun getNewPlaytime() : Int
    {
        return newPlaytime
    }
    fun setNewPlaytime(newPlaytimeNum: Int)
    {
        newPlaytime = newPlaytimeNum
    }
    fun getOldPlaytime() : Int
    {
        return member.PlayTime
    }
}