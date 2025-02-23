package Libs.API.ir.parham.SeasonJobsAPI.Event.Job

import Libs.API.ir.parham.SeasonJobsAPI.Event.SeasonEvent
import Libs.API.ir.parham.SeasonJobsAPI.Event.SeasonEventManager
import ir.parham.SeasonJobsAPI.Actions.Job

open class JobEvent(aJob: Job.Jobs) : SeasonEvent() {
    private var job: Job.Jobs

    init {
        job = aJob
    }

    fun getName(): String
    {
        return job.Name
    }
    fun setName(name: String)
    {
        Job().set(name, getMaxWarn(), getMemberSize(), getPlayTime(), getPrefix(), getSuffix())
    }
    fun getMaxWarn(): Int
    {
        return job.MaxWarn
    }
    fun setMaxWarn(maxWarn: Int)
    {
        Job().set(getName(), maxWarn, getMemberSize(), getPlayTime(), getPrefix(), getSuffix())
    }
    fun getMemberSize(): Int
    {
        return job.MemberSize
    }
    fun setMemberSize(memberSize: Int)
    {
        Job().set(getName(), getMaxWarn(), memberSize, getPlayTime(), getPrefix(), getSuffix())
    }
    fun getPlayTime(): Int
    {
        return job.PlayTime
    }
    fun setPlayTime(playTime: Int)
    {
        Job().set(getName(), getMaxWarn(), getMemberSize(), playTime, getPrefix(), getSuffix())
    }
    fun getPrefix(): String
    {
        return job.Prefix
    }
    fun setPrefix(prefix: String)
    {
        Job().set(getName(), getMaxWarn(), getMemberSize(), getPlayTime(), prefix, getSuffix())
    }
    fun getSuffix(): String
    {
        return job.Suffix
    }
    fun setSuffix(suffix: String)
    {
        Job().set(getName(), getMaxWarn(), getMemberSize(), getPlayTime(), getPrefix(), suffix)
    }
}