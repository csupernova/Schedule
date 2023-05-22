package ru.altmanea.webapp.rest

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import org.litote.kmongo.eq
import ru.altmanea.webapp.common.FilterSchedule
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.ScheduleItem
import ru.altmanea.webapp.data.TimeLesson
import ru.altmanea.webapp.data.WeekType
import ru.altmanea.webapp.data.Weekday
import ru.altmanea.webapp.repo.scheduleItemCollection
import java.util.*

fun Route.scheduleRoutes(){
    val serializer: KSerializer<ScheduleItem> = serializer()
    val serializerFilter: KSerializer<FilterSchedule> = serializer()
    route(Config.schedulePath){
        get{
            val items = scheduleItemCollection.find().toList()
            if (items.isEmpty()) {
                call.respondText("No schedule items found", status = HttpStatusCode.NotFound)
            } else {
                call.respond(items)
            }
        }
        post{
            val elemJson = call.receive<String>()
            val elem = Json.decodeFromString(serializer, elemJson)
            elem.id = UUID.randomUUID().toString()
            scheduleItemCollection.insertOne(elem)
            call.respondText(
                "Element stored correctly",
                status = HttpStatusCode.Created
            )
        }
        delete("{id}"){
            val id =
                call.parameters["id"] ?: return@delete call.respondText(
                    "Missing or malformed id",
                    status = HttpStatusCode.BadRequest
                )
            scheduleItemCollection.deleteOne(ScheduleItem::id eq id)
            call.respondText(
                "Element removed correctly",
                status = HttpStatusCode.Accepted
            )
        }
        get("time/{selected}") {
            val numberLesson =
                call.parameters["selected"] ?: return@get call.respondText(
                    "Missing or malformed number lesson",
                    status = HttpStatusCode.BadRequest
                )
            val timeLesson = TimeLesson.FIRST.getTimeLesson(numberLesson.toInt())
            if (timeLesson.isEmpty()) {
                call.respondText("No students found", status = HttpStatusCode.NotFound)
            } else {
                call.respond(timeLesson)
            }
        }
        get("days"){
            val weekdays = Weekday.values().map{it.day}
            call.respond(weekdays)
        }
        get("weektypes"){
            val weektypes = WeekType.values().map{it.type}
            call.respond(weektypes)
        }
        post("filter/") {
            val filterScheduleJson = call.receive<String>()
            val filterSchedule = Json.decodeFromString(serializerFilter, filterScheduleJson)

            var scheduleitems = scheduleItemCollection.find().toList()

//            for (prop in FilterSchedule::class.memberProperties){
//                if(prop.get(filterSchedule)!=null)
//                    scheduleitems = scheduleitems.filter{it.prop.name == prop.get(filterSchedule)}
//            }
            if(filterSchedule.group != null)  scheduleitems = scheduleitems.filter{it.group == filterSchedule.group}
            if(filterSchedule.teacher != null) scheduleitems = scheduleitems.filter{it.teacher == filterSchedule.teacher}
            if(filterSchedule.lesson != null) scheduleitems = scheduleitems.filter{it.lesson == filterSchedule.lesson}

            if (scheduleitems.isEmpty()) {
                call.respondText("No students found", status = HttpStatusCode.NotFound)
            } else {
                call.respond(scheduleitems)
            }
        }
    }
}
