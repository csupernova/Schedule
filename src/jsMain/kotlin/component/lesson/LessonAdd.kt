package component.lesson

import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.useState
import ru.altmanea.webapp.data.Lesson
import web.html.InputType

external interface LessonAddProps : Props {
    var addLesson: (Lesson) -> Unit
}

val CLessonAdd = FC<LessonAddProps>("LessonNew") { props ->
    var name by useState("")
    div {
        input {
            type = InputType.text
            value = name
            onChange = { name = it.target.value }
        }
        button {
            +"✔"
            onClick = {
                props.addLesson(Lesson("id", name))
            }
        }
    }
}