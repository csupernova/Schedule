package ru.altmanea.webapp.repo

import org.litote.kmongo.getCollection
import ru.altmanea.webapp.data.*
import ru.altmanea.webapp.mongoDatabase
import java.util.*



val scheduleItemCollection = mongoDatabase.getCollection<ScheduleItem>().apply { drop() }
val lessonCollection = mongoDatabase.getCollection<Lesson>().apply { drop() }
val teacherCollection = mongoDatabase.getCollection<Teacher>().apply{ drop() }
val groupCollection = mongoDatabase.getCollection<TypeGroup>().apply{ drop() }
val roomCollection =  mongoDatabase.getCollection<Room>().apply{ drop() }

fun createTestData(){
    listOf(
        Teacher(UUID.randomUUID().toString(),"Евгений", "Альтман", "Анатольевич"),
        Teacher(UUID.randomUUID().toString(),"Андрей", "Окишев", "Сергеевич"),
        Teacher(UUID.randomUUID().toString(),"Андрей", "Малютин","Геннадьевич"),
        Teacher(UUID.randomUUID().toString(),"Наталья", "Тихонова","Алексеева"),
        Teacher(UUID.randomUUID().toString(), "Дмитрий","Елизаров","Александрович"),
                Teacher(UUID.randomUUID().toString(), "Андрей","Лаврухин","Александрович")
    ).apply {
        map {
            teacherCollection.insertOne(it)
        }

        val teachers = teacherCollection.find().toList()
        val malutin = teachers.findLast { it.surname == "Малютин" }!!
        val altman = teachers.findLast { it.surname == "Альтман" }!!

        listOf(
            Lesson(UUID.randomUUID().toString(), "ИСИС"),
            Lesson(UUID.randomUUID().toString(), "ПП"),
            Lesson(UUID.randomUUID().toString(), "ПИСиБД"),
            Lesson(UUID.randomUUID().toString(), "ОТУ"),
            Lesson(UUID.randomUUID().toString(), "ККИС"),
            Lesson(UUID.randomUUID().toString(), "ТПП"),
            Lesson(UUID.randomUUID().toString(), "ООП"),

        ).apply {
            map {
                lessonCollection.insertOne(it)
            }
        }

        val lessons = lessonCollection.find().toList()
        val programming = lessons.findLast { it.name == "ПП" }!!
        val isis = lessons.findLast { it.name == "ИСИС" }!!

        listOf(
            TypeGroup.Flow(UUID.randomUUID().toString(),"20зм"),
            TypeGroup.Flow(UUID.randomUUID().toString(),"21зм"),
            TypeGroup.Group(UUID.randomUUID().toString(), "20м"),
            TypeGroup.Group(UUID.randomUUID().toString(), "20з"),
            TypeGroup.Group(UUID.randomUUID().toString(), "20и"),
            TypeGroup.Group(UUID.randomUUID().toString(), "21м"),
            TypeGroup.Group(UUID.randomUUID().toString(), "21з"),
            TypeGroup.Group(UUID.randomUUID().toString(), "21и"),
        ).apply {
            map {
                groupCollection.insertOne(it)
            }
        }
        val groups = groupCollection.find().toList()
        val group1 = groups[3]
        val group2 = groups[0]


        listOf(
            Room(UUID.randomUUID().toString(), 325),
            Room(UUID.randomUUID().toString(), 330),
            Room(UUID.randomUUID().toString(), 329),
            Room(UUID.randomUUID().toString(), 322),
        ).apply {
            map {
                roomCollection.insertOne(it)
            }
        }


        val rooms = roomCollection.find().toList()

        val room1 = rooms[0]
        val room2 = rooms[1]
        listOf(
            ScheduleItem(
                UUID.randomUUID().toString(),
                programming.id,
                LessonType.LAB,
                altman.id,
                group1.id,
                "2",
                Weekday.MONDEY,
                WeekType.EVEN,
                TimeLesson.FIRST,
                room1.id
            ),
            ScheduleItem(
                UUID.randomUUID().toString(),
                isis.id,
                LessonType.LECTURE,
                malutin.id,
                group2.id,
                null,
                Weekday.FRIDAY,
                WeekType.ODD,
                TimeLesson.FOURTH,
                room2.id
            ),
            ScheduleItem(
                    UUID.randomUUID().toString(),
            lessons.findLast { it.name == "ТПП" }!!.id,
                LessonType.LECTURE,
            teachers.findLast { it.surname == "Елизаров" }!!.id,
            groups.findLast{it.name == "20м"}!!.id,
                null,
            Weekday.MONDEY,
            WeekType.EVEN,
            TimeLesson.FIRST,
            rooms.findLast{it.number == 329}!!.id
        ),
            ScheduleItem(
                UUID.randomUUID().toString(),
                lessons.findLast { it.name == "ООП" }!!.id,
                LessonType.KSR,
                teachers.findLast { it.surname == "Альтман" }!!.id,
                groups.findLast{it.name == "21м"}!!.id,
                null,
                Weekday.THUESDAY,
                WeekType.EVEN,
                TimeLesson.SECOND,
                rooms.findLast{it.number == 325}!!.id
            ),
            ScheduleItem(
                UUID.randomUUID().toString(),
                lessons.findLast { it.name == "ПИСиБД" }!!.id,
                LessonType.KSR,
                teachers.findLast { it.surname == "Малютин" }!!.id,
                groups.findLast{it.name == "21м"}!!.id,
                null,
                Weekday.WEDNESDAY,
                WeekType.ODD,
                TimeLesson.THIRD,
                rooms.findLast{it.number == 329}!!.id
            ),
        ).apply {
            map {
                scheduleItemCollection.insertOne(it)
            }
        }
    }
}