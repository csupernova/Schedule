package component.room

import js.core.jso
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.useState
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.ObjectId
import ru.altmanea.webapp.data.Room
import tanstack.query.core.QueryKey
import tanstack.react.query.useMutation
import tanstack.react.query.useQuery
import tanstack.react.query.useQueryClient
import tools.HTTPResult
import tools.fetch
import tools.fetchText
import kotlin.js.json

external interface RoomListProps : Props {
    var rooms: List<Room>
    var deleteRoom: (ObjectId) -> Unit
    var editRoom: (Room) -> Unit
}

val CRoomList= FC<RoomListProps>("RoomList") { props ->
    var editedId by useState(-1)

    props.rooms.forEachIndexed { index, room ->
        if(index == editedId){
            CRoomEdit{
                this.room = room
                this.editRoom = {
                    props.editRoom(it)
                    editedId = -1
                }
            }

        }
        ReactHTML.div {
            +room.number.toString()
            ReactHTML.span {
                +"  ✎  "
                onClick = {
                    editedId = index
                }
            }

            ReactHTML.span {
                +" ✂ "
                onClick = {
                    props.deleteRoom(room.id)
                }
            }
        }
    }
}


val containerRoomList = FC("containerRoom") { _: Props ->
    val queryClient = useQueryClient()
    val roomQueryKey = arrayOf("Rooms").unsafeCast<QueryKey>()
    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = roomQueryKey,
        queryFn = {
            fetchText(Config.roomsPath)
        }
    )

    val deleteRoomMutation = useMutation<HTTPResult, Any, ObjectId, Any>(
        mutationFn = { id ->
            fetch(
                "${Config.roomsPath}$id",
                jso {
                    method = "DELETE"
                    headers = json("Content-Type" to "application/json")
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(roomQueryKey)
            }
        }
    )

    val addRoomMutation = useMutation<HTTPResult, Any, Room, Any>(
        mutationFn = { room ->
            fetch(
                Config.roomsPath,
                jso {
                    method = "POST"
                    headers = json(
                        "Content-Type" to "application/json")
                    body = Json.encodeToString(room)
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(roomQueryKey)
            }
        }
    )

    val editRoomMutation = useMutation<HTTPResult, Any, Room, Any>(
        mutationFn = { room ->
            fetch(
                Config.roomsPath,
                jso {
                    method = "PUT"
                    headers = json(
                        "Content-Type" to "application/json")
                    body = Json.encodeToString(room)
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(roomQueryKey)
            }
        }
    )


    if (query.isLoading) ReactHTML.div { +"Loading .." }
    else if (query.isError) ReactHTML.div { +"Error!" }
    else {
        val items: List<Room> =
            try {
                Json.decodeFromString(query.data ?: "")
            } catch (e: Throwable) {
                emptyList()
            }

        CRoomAdd{
            addRoom = {addRoomMutation.mutateAsync(it, null)}
        }

        CRoomList {
            rooms = items
            deleteRoom = {deleteRoomMutation.mutateAsync(it, null)}
            editRoom = {editRoomMutation.mutateAsync(it, null)}

        }
    }
}



