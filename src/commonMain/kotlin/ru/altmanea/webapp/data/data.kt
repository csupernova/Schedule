package ru.altmanea.webapp.data

import kotlinx.serialization.Serializable

typealias ObjectId = String

@Serializable
data class Lesson(
    var id: ObjectId,
    val name: String,
)


@Serializable
data class Teacher(
    var id : ObjectId,
    val firstname: String,
    val surname: String,
    val patronymie: String
){
    fun fullname() =
        "$surname $firstname $patronymie"
}

@Serializable
data class ScheduleItem(
    var id: ObjectId,
    val lesson: ObjectId,
    val typeLesson: LessonType,
    val teacher: ObjectId,
    val group: ObjectId,
    val subGroup: String? = null,
    val weekDay: Weekday,
    val weekType: WeekType,
    val time: TimeLesson,
    val room: ObjectId
)

@Serializable
sealed class TypeGroup {
    abstract var id: ObjectId
    abstract val name: String

    @Serializable
    data class Flow(
        override var id: ObjectId,
        override val name: String
    ): TypeGroup()

    @Serializable
    data class Group(
        override var id: ObjectId,
        override val name: String,
    ): TypeGroup()
}

@Serializable
enum class TimeLesson(val startTime: String, val endTime: String) {
    FIRST("08:00", "09:30"),
    SECOND("09:45", "11:15"),
    THIRD("11:30", "13:00"),
    FOURTH("13:55", "15:25"),
    FIFTH("15:40", "17:10");

    fun numberLesson(time: String): Int {
        val lessonTime = values().find { it.startTime == time }
        val lessonNumber = lessonTime?.ordinal?.plus(1)
        return lessonNumber!!
    }
    fun getTimeLesson(lessonNumber: Int): String {
        val lesson = TimeLesson.values().find { it.ordinal == lessonNumber - 1 }
        return lesson.let { "${it!!.startTime}-${it.endTime}" }
    }
    fun fulltime() = "${startTime}-${endTime}"
}
@Serializable
enum class Weekday(val day: String){
    MONDEY("Понедельник"),
    THUESDAY("Вторник"),
    WEDNESDAY("Среда"),
    THURSDAY("Четверг"),
    FRIDAY("Пятница"),
    SATURDAY("Суббота"),
    SUNDAY("Воскресенье")
}
@Serializable
enum class WeekType(val type: String){
    ODD("Нечетная"),
    EVEN("Четная")
}
@Serializable
enum class LessonType(val type: String){
    LECTURE("Лекция"),
    LAB("Лабораторная"),
    KSR("КСР"),
    KRB("КРБ")
}

@Serializable
data class Room(
    var id: ObjectId,
    val number: Int
)