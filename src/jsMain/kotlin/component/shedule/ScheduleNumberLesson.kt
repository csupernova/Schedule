package component.shedule

import react.FC
import react.Props
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select
import react.useRef
import web.html.HTMLSelectElement

external interface NumberLessonSelectProps : Props {
    var onPick: (String) -> Unit
}

val CNumberLessonSelect = FC<NumberLessonSelectProps>("StudentSelect") { props ->

    val selectNumberLessonRef = useRef<HTMLSelectElement>()
    val numbers = listOf(1,2,3,4,5)

    select{
        ref = selectNumberLessonRef

        option{
            +"Выберите номер пары"
            hidden = true
            selected = true
            disabled = true
            value = "1"

        }
        numbers.map{
            option{
                +"$it"
                value = it
            }
        }
            onClick = {
                props.onPick(selectNumberLessonRef.current!!.value)
            }
        }
    }