package Libs.API.ir.parham.SeasonJobsAPI.Event.Job

import Libs.API.ir.parham.SeasonJobsAPI.Event.SeasonEventManager
import ir.parham.SeasonJobsAPI.Actions.Job

class JobDeleteEvent(aJob: Job.Jobs) : JobEvent(aJob) {

    init {
    }
}