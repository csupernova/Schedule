package component.shedule

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.useRef
import ru.altmanea.webapp.data.ObjectId
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.Room
import tanstack.query.core.QueryKey
import tanstack.react.query.useQuery
import tools.fetchText
import web.html.HTMLSelectElement


external interface RoomSelectProps : Props {
    var onPick: (ObjectId) -> Unit
}

val CRoomSelect = FC<RoomSelectProps>("RoomInput") { props->
    val selectQueryKey = arrayOf("RoomInput").unsafeCast<QueryKey>()

    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = selectQueryKey,
        queryFn = {
            fetchText(Config.roomsPath)
        }
    )
    val selectRoomRef = useRef<HTMLSelectElement>()

    val rooms: List<Room> =
        try {
            Json.decodeFromString(query.data ?: "")
        } catch (e: Throwable) {
            emptyList()
        }
    ReactHTML.select {
        ref = selectRoomRef
        ReactHTML.option {
            +"Выберите аудиторию"
            hidden = true
            selected = true
            disabled = true
            value = ""

        }
        rooms.map {
            ReactHTML.option {
                +it.number.toString()
                value = it.id
            }
            onClick = {
                props.onPick(selectRoomRef.current!!.value)
            }
        }
    }
}