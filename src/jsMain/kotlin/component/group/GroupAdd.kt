package component.group

import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.option
import react.useRef
import react.useState
import ru.altmanea.webapp.data.TypeGroup
import web.html.HTMLSelectElement
import web.html.InputType

external interface GroupAddProps : Props {
    var addGroup: (TypeGroup) -> Unit
}

val CGroupAdd = FC<GroupAddProps>("GroupAdd") { props ->
    var name by useState("")
    val selectGroupTypeRef = useRef<HTMLSelectElement>()
    ReactHTML.span {
        ReactHTML.input {
            type = InputType.text
            value = name
            onChange = { name = it.target.value }
        }
    }
    ReactHTML.span {
        ReactHTML.select {
            ref = selectGroupTypeRef
            ReactHTML.option {
                +"Выберите тип группы"
                hidden = true
                selected = true
                disabled = true

            }
            listOf("Группа", "Поток").mapIndexed{ index, elem ->
                option{
                    +elem
                    value = index
                }
            }
        }
    }
    ReactHTML.button {
        +"✓"
        onClick = {
            props.addGroup(
                if(selectGroupTypeRef.current?.value!! == "0")
                    TypeGroup.Group("id", name)
                else
                    TypeGroup.Flow("id", name)
            )
        }
    }
}