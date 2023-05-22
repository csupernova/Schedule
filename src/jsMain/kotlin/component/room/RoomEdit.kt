package component.room

import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.useState
import ru.altmanea.webapp.data.Room
import web.html.InputType

external interface RoomEditProps : Props {
    var room: Room
    var editRoom: (Room) -> Unit
}

val CRoomEdit = FC<RoomEditProps>("RoomEdit") { props ->
    var name by useState(props.room.number.toString())
    ReactHTML.span {
        ReactHTML.input {
            type = InputType.text
            value = name
            onChange = { name = it.target.value }
        }
    }
    ReactHTML.button {
        +"✓"
        onClick = {
            props.editRoom(Room(props.room.id, name.toInt()))
        }
    }
}