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
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.Teacher
import ru.altmanea.webapp.repo.teacherCollection
import java.util.*

fun Route.teacherRoutes() {
    val serializer: KSerializer<Teacher> = serializer()
    route(Config.teachersPath){
        get{
            val teachers = teacherCollection.find().toList()
            if (teachers.isEmpty()) {
                call.respondText("No teachers found", status = HttpStatusCode.NotFound)
            } else {
                call.respond(teachers)
            }
        }
        put{
            val teacherJson = call.receive<String>()
            val teacher = Json.decodeFromString(serializer, teacherJson)
            teacherCollection.replaceOne(Teacher::id eq teacher.id , teacher)
            call.respondText(
                "Element updates correctly",
                status = HttpStatusCode.Created
            )
        }
        delete("{id}"){
            val id = call.parameters["id"]
                ?: return@delete call.respond(HttpStatusCode.BadRequest)
            teacherCollection.deleteOne(Teacher::id eq id)
            call.respondText(
                "Element removed correctly",
                status = HttpStatusCode.Accepted
            )
        }
        post{
            val teacherJson = call.receive<String>()
            val teacher = Json.decodeFromString(serializer, teacherJson).apply{
                id = UUID.randomUUID().toString()
            }
            teacherCollection.insertOne(teacher)
            call.respondText(
                "Element stored correctly",
                status = HttpStatusCode.Created
            )
        }
    }
}