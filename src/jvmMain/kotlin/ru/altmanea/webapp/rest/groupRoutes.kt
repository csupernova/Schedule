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
import ru.altmanea.webapp.data.TypeGroup
import ru.altmanea.webapp.repo.groupCollection
import java.util.*

fun Route.groupRoutes() {
    val serializer: KSerializer<TypeGroup> = serializer()
    route(Config.groupsPath){
        get{
            val groups = groupCollection.find().toList()
            if (groups.isEmpty()) {
                call.respondText("No groups found", status = HttpStatusCode.NotFound)
            }
            else call.respond(groups)
        }
        put{
            val groupJson = call.receive<String>()
            val group = Json.decodeFromString(serializer, groupJson)
            groupCollection.updateOne(TypeGroup::id eq group.id, setValue(TypeGroup::name, group.name))
            call.respondText(
                "Element updates correctly",
                status = HttpStatusCode.Created
            )

        }
        delete("{id}"){
            val id = call.parameters["id"]
                ?: return@delete call.respond(HttpStatusCode.BadRequest)
            groupCollection.deleteOne(TypeGroup.Group::id eq id)
            call.respondText(
                "Element removed correctly",
                status = HttpStatusCode.Accepted
            )
        }
        post{
            val groupJson = call.receive<String>()
            val group = Json.decodeFromString(serializer, groupJson).apply{
                id = UUID.randomUUID().toString()
            }
            groupCollection.insertOne(group)
            call.respondText(
                "Element stored correctly",
                status = HttpStatusCode.Created
            )

        }
    }
}