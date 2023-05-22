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
import org.litote.kmongo.setValue
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.Lesson
import ru.altmanea.webapp.data.LessonType
import ru.altmanea.webapp.repo.lessonCollection
import java.util.*

fun Route.lessonRoutes() {
    val serializer: KSerializer<Lesson> = serializer()
    route(Config.lessonsPath){
        get{
            val lessons = lessonCollection.find().toList()
            if (lessons.isEmpty()) {
                call.respondText("No lessons found", status = HttpStatusCode.NotFound)
            }
            else call.respond(lessons)
        }
        put{
            val lessonJson = call.receive<String>()
            val lesson = Json.decodeFromString(serializer, lessonJson)
            lessonCollection.updateOne(Lesson::id eq lesson.id , setValue(Lesson::name, lesson.name))
            call.respondText(
                "Element updates correctly",
                status = HttpStatusCode.Created
            )
        }
        delete("{id}"){
            val id = call.parameters["id"]
                ?: return@delete call.respond(HttpStatusCode.BadRequest)
            lessonCollection.deleteOne(Lesson::id eq id)
            call.respondText(
                "Element removed correctly",
                status = HttpStatusCode.Accepted
            )
        }
        post{
            val lessonJson = call.receive<String>()
            val lesson = Json.decodeFromString(serializer, lessonJson).apply{
                id = UUID.randomUUID().toString()
            }
            lessonCollection.insertOne(lesson)
            call.respondText(
                "Element stored correctly",
                status = HttpStatusCode.Created
            )

        }
        get("types"){
            val types = LessonType.values().map{it.type}
            if (types.isEmpty()) {
                call.respondText("No lessons found", status = HttpStatusCode.NotFound)
            }
            else call.respond(types)
        }
    }
}