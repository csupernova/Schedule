package component.shedule

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select
import react.useRef
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.Teacher
import tanstack.query.core.QueryKey
import tanstack.react.query.useQuery
import tools.fetchText
import web.html.HTMLSelectElement


external interface TeacherSelectProps : Props {
    var onPick: (String) -> Unit
}

val CTeacherSelect = FC<TeacherSelectProps>("TeacherInput") { props ->
    val selectQueryKey = arrayOf("TeacherInput").unsafeCast<QueryKey>()

    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = selectQueryKey,
        queryFn = {
            fetchText(Config.teachersPath)
        }
    )
    val selectTeacherRef = useRef<HTMLSelectElement>()
    val teachers: List<Teacher> =
        try {
            Json.decodeFromString(query.data ?: "")
        } catch (e: Throwable) {
            emptyList()
        }
    select{
        ref = selectTeacherRef
        option{
            +"Выберите преподавателя"
            hidden = true
            selected = true
            disabled = true
            value = ""

        }
        teachers.map { teacher ->
            option {
                +teacher.fullname()
                value = teacher.id
            }
        }
        onClick = {
            props.onPick(selectTeacherRef.current?.value!!)
        }
    }
}