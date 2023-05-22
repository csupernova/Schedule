package component.shedule

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.useRef
import ru.altmanea.webapp.config.Config
import tanstack.query.core.QueryKey
import tanstack.react.query.useQuery
import tools.fetchText
import web.html.HTMLSelectElement

external interface WeekTypeSelectProps : Props {
    var onPick: (String) -> Unit
}

val CWeekTypeSelect = FC<WeekTypeSelectProps>("WeekTypeInput") { props->
    val selectQueryKey = arrayOf("WeekTypeInput").unsafeCast<QueryKey>()

    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = selectQueryKey,
        queryFn = {
            fetchText("${Config.schedulePath}weektypes")
        }
    )
    val inputTimeLessonRef = useRef<HTMLSelectElement>()

    val types: List<String> =
        try {
            Json.decodeFromString(query.data ?: "")
        } catch (e: Throwable) {
            emptyList()
        }

    ReactHTML.select {
        ref = inputTimeLessonRef

        ReactHTML.option {
            +"Выберите тип недели"
            hidden = true
            selected = true
            disabled = true
        }
        types.map {
            ReactHTML.option {
                +it
                value = it
            }
        }
        onClick = {
            props.onPick(inputTimeLessonRef.current!!.value)
        }
    }

}