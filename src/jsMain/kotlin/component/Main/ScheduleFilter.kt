package component.Main

import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.useRef
import ru.altmanea.webapp.common.FilterSchedule
import ru.altmanea.webapp.data.Lesson
import ru.altmanea.webapp.data.Teacher
import ru.altmanea.webapp.data.TypeGroup
import web.html.HTMLSelectElement

external interface ScheduleFilterProps : Props {
    var teachers: List<Teacher>
    var groups: List<TypeGroup>
    var lessons: List<Lesson>
    var onPick: (FilterSchedule) -> Unit

}

val CScheduleFilter= FC<ScheduleFilterProps>("ScheduleFilter") { props ->
    val inputTeacherRef = useRef<HTMLSelectElement>()
    val inputGroupRef = useRef<HTMLSelectElement>()
    val inputLessonRef = useRef<HTMLSelectElement>()

    ReactHTML.div {
        ReactHTML.label {
            +"По преподавателю"
        }
        ReactHTML.select {
            ref = inputTeacherRef
            ReactHTML.option {
                +"Выберите преподавателя"
                hidden = true
                selected = true
                disabled = true

            }
            props.teachers.map {
                ReactHTML.option {
                    +it.fullname()
                    value = it.id
                }
            }
        }
        ReactHTML.button {
            +"Выбрать"
            onClick = {
                props.onPick(FilterSchedule(teacher = inputTeacherRef.current?.value!!))
            }
        }
    }
    ReactHTML.div {
        ReactHTML.label {
            +"По группе"
        }
        ReactHTML.select {
            ref = inputGroupRef
            ReactHTML.option {
                +"Выберите группу"
                hidden = true
                selected = true
                disabled = true

            }
            props.groups.map {
                ReactHTML.option {
                    +it.name
                    value = it.id
                }
            }
        }
        ReactHTML.button {
            +"Выбрать"
            onClick = {
                props.onPick(FilterSchedule(group = inputGroupRef.current?.value!!))
            }
        }
    }
    ReactHTML.div {
        ReactHTML.label {
            +"По предмету"
        }
        ReactHTML.select {
            ref = inputLessonRef
            ReactHTML.option {
                +"Выберите предмет"
                hidden = true
                selected = true
                disabled = true

            }
            props.lessons.map {
                ReactHTML.option {
                    +it.name
                    value = it.id
                }
            }
        }
        ReactHTML.button {
            +"Выбрать"
            onClick = {
                props.onPick(FilterSchedule(lesson = inputLessonRef.current!!.value))
            }
        }
    }
    ReactHTML.button {
        +"Сбросить"
        onClick = {
            props.onPick(FilterSchedule())
        }
    }
}

