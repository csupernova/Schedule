package component.room

import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.useState
import ru.altmanea.webapp.data.Room
import web.html.InputType

external interface RoomAddProps : Props {
    var addRoom: (Room) -> Unit
}

val CRoomAdd = FC<RoomAddProps>("RoomNew") { props ->
    var name by useState("")
    div {
        input {
            pattern = "[0-9]*"
            type = InputType.text
            value = name
            onChange = { name = it.target.value }
        }
        button {
            +"✔"
            onClick = {
                props.addRoom(Room("id", name.toInt()))
            }
        }
    }
}