package component.group

import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.label
import react.useState
import ru.altmanea.webapp.data.TypeGroup
import web.html.InputType

external interface GroupEditProps : Props {
    var group: TypeGroup
    var editGroup: (TypeGroup) -> Unit
}

val CGroupEdit = FC<GroupEditProps>("UserEdit") { props ->
    var name by useState(props.group.name)
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
            props.editGroup(
                if(props.group is TypeGroup.Group)
                    TypeGroup.Group(props.group.id, name)
                else
                    TypeGroup.Flow(props.group.id, name)
            )
        }
    }
    label{
        +name
    }
}
