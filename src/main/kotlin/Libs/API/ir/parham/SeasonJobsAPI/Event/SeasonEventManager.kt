package Libs.API.ir.parham.SeasonJobsAPI.Event

import Libs.API.ir.parham.SeasonJobsAPI.Event.Job.JobCreateEvent
import Libs.API.ir.parham.SeasonJobsAPI.Event.Job.JobDeleteEvent
import Libs.API.ir.parham.SeasonJobsAPI.Event.Job.JobEvent

class SeasonEventManager
{
    companion object {
        val listeners: ArrayList<SeasonListener> = ArrayList()
    }

    fun addListener(event: SeasonListener)
    {
        listeners.add(event)
    }

    @Synchronized
    fun fireEvent(event: Any)
    {
        for (listener in listeners)
        {
            val _class = listener.javaClass


            val functions = _class.methods

            for (function in functions)
            {
                if (function.isAnnotationPresent(SeasonHandler::class.java))
                {
                    if (function.parameters.isEmpty())
                    {
                        return
                    }
                    else if (event is JobCreateEvent && function.parameters[0].type == JobCreateEvent::class.java)
                    {
                        function.invoke(listener, event)
                    }
                    else if (event is JobDeleteEvent && function.parameters[0].type == JobDeleteEvent::class.java)
                    {
                        function.invoke(listener, event)
                    }
                    else if (event is JobEvent && function.parameters[0].type == JobEvent::class.java)
                    {
                        function.invoke(listener, event)
                    }
                    else if (event is JobCreateEvent && function.parameters[0].type == JobCreateEvent::class.java)
                    {
                        function.invoke(listener, event)
                    }
                }
            }
        }
    }
}