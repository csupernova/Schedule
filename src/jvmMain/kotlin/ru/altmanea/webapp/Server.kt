package ru.altmanea.webapp

import com.mongodb.client.MongoDatabase
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import kotlinx.coroutines.delay
import org.litote.kmongo.KMongo
import ru.altmanea.webapp.repo.createTestData
import ru.altmanea.webapp.rest.*


fun main() {
    embeddedServer(
        Netty,
        port = 8080,
        host = "127.0.0.1",
        watchPaths = listOf("classes")
    ) {
        main()
    }.start(wait = true)
}

val client = KMongo
    .createClient("mongodb://root:3nok3Pw3jULe@192.168.56.105:27017")
val mongoDatabase: MongoDatabase = client.getDatabase("test")

fun Application.main(isTest: Boolean = true) {
    config(isTest)
    static()
    rest()
    if (isTest) logRoute()
}

fun Application.config(isTest: Boolean) {
    install(ContentNegotiation) {
        json()
    }
    if (isTest) {
        createTestData()
        install(createApplicationPlugin("DelayEmulator") {
            onCall {
                delay(10L)
            }
        })
    }
}

fun Application.rest() {
    routing {
        teacherRoutes()
        groupRoutes()
        lessonRoutes()
        scheduleRoutes()
        roomsRoutes()
    }
}