package component.teacher

import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.span
import react.useState
import ru.altmanea.webapp.data.Teacher
import web.html.InputType

external interface TeacherEditProps : Props {
    var teacher: Teacher
    var editTeacher: (Teacher) -> Unit
}

val CTeacherEdit = FC<TeacherEditProps>("TeacherEdit") { props ->
    var firstname by useState(props.teacher.firstname)
    var surname by useState(props.teacher.surname)
    var patronymie by useState(props.teacher.patronymie)
    span {
        input {
            type = InputType.text
            value = firstname
            onChange = { firstname = it.target.value }
        }
        input {
            type = InputType.text
            value = surname
            onChange = {surname = it.target.value }
        }
        input {
            type = InputType.text
            value = patronymie
            onChange = {patronymie = it.target.value }
        }
    }
    button {
        +"✓"
        onClick = {
            props.editTeacher(Teacher(props.teacher.id, firstname, surname, patronymie))
        }
    }
}