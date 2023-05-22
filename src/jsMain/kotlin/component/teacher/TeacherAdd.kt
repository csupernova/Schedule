package component.teacher

import react.FC
import react.Props
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.input
import react.dom.html.ReactHTML.span
import react.useState
import ru.altmanea.webapp.data.Teacher
import web.html.InputType

external interface TeacherAddProps : Props {
    var addTeacher: (Teacher) -> Unit
}


val CTeacherAdd = FC<TeacherAddProps>("TeacherAdd") { props ->
    var edited  by useState<Boolean>(false)
    var firstname by useState("Имя")
    var surname by useState("Фамилия")
    var patronymie by useState("Отчество")

    div{
        button{
            +"Добавить преподавателя"
        }
        onClick = {
            edited = true
        }
    }
    if(edited) {
        span {
            input {
                type = InputType.text
                value = firstname
                onChange = { firstname = it.target.value }
            }
            input {
                type = InputType.text
                value = surname
                onChange = { surname = it.target.value }
            }
            input {
                type = InputType.text
                value = patronymie
                onChange = { patronymie = it.target.value }
            }
        }
        button {
            +"✓"
            onClick = {
                props.addTeacher(Teacher("id", firstname, surname, patronymie))
                edited = false
            }
        }
    }
}