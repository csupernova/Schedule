package component.lesson

import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.useState
import ru.altmanea.webapp.data.Lesson
import web.html.InputType

external interface LessonEditProps : Props {
    var lesson: Lesson
    var editLesson: (Lesson) -> Unit
}

val CLessonEdit = FC<LessonEditProps>("LessonEdit") { props ->
    var name by useState(props.lesson.name)
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
            props.editLesson(Lesson(props.lesson.id, name))
        }
    }
}

