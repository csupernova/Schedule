package ru.altmanea.webapp.common

import kotlinx.serialization.Serializable
import ru.altmanea.webapp.data.ObjectId
import ru.altmanea.webapp.data.WeekType
import ru.altmanea.webapp.data.Weekday

@Serializable
class FilterSchedule(
    var teacher: ObjectId? = null,
    var group: ObjectId? = null,
    var lesson: ObjectId? = null
    )