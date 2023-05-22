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
import ru.altmanea.webapp.data.Room
import ru.altmanea.webapp.repo.roomCollection
import java.util.*

fun Route.roomsRoutes() {
    val serializer: KSerializer<Room> = serializer()
    route(Config.roomsPath){
        get{
            val rooms = roomCollection.find().toList()
            if (rooms.isEmpty()) {
                call.respondText("No rooms found", status = HttpStatusCode.NotFound)
            }
            else call.respond(rooms)
        }

        put{
            val roomJson = call.receive<String>()
            val room = Json.decodeFromString(serializer, roomJson)
            roomCollection.updateOne(Room::id eq room.id , setValue(Room::number, room.number))
            call.respondText(
                "Element updates correctly",
                status = HttpStatusCode.Created
            )
        }
        delete("{id}"){
            val id = call.parameters["id"]
                ?: return@delete call.respond(HttpStatusCode.BadRequest)
            roomCollection.deleteOne(Room::id eq id)
            call.respondText(
                "Element removed correctly",
                status = HttpStatusCode.Accepted
            )
        }
        post{
            val roomJson = call.receive<String>()
            val room = Json.decodeFromString(serializer, roomJson).apply{
                id = UUID.randomUUID().toString()
            }
            roomCollection.insertOne(room)
            call.respondText(
                "Element stored correctly",
                status = HttpStatusCode.Created
            )
        }
    }
}