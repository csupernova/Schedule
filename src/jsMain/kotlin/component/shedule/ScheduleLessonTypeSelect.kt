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
import ru.altmanea.webapp.data.LessonType
import tanstack.query.core.QueryKey
import tanstack.react.query.useQuery
import tools.fetchText
import web.html.HTMLSelectElement


external interface LessonTypeSelectProps : Props {
    var onPick: (String) -> Unit
}

val CLessonTypeSelect = FC<LessonTypeSelectProps>("LessonTypeInput") { props->
    val selectQueryKey = arrayOf("LessonTypeInput").unsafeCast<QueryKey>()

    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = selectQueryKey,
        queryFn = {
            fetchText("${Config.lessonsPath}/types")
        }
    )
    val LessonTypeRef = useRef<HTMLSelectElement>()

    val lessontypes: List<String> =
        try {
            Json.decodeFromString(query.data ?: "")
        } catch (e: Throwable) {
            emptyList()
        }

    select {
        ref = LessonTypeRef
        option{
            +"Выберите тип занятия"
            hidden = true
            selected = true
            disabled = true
            value = ""
        }
        lessontypes.map {
            option {
                +it
                value = it
            }
            onClick = {
                props.onPick(LessonTypeRef.current!!.value)
            }
        }
    }
}