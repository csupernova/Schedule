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
import tanstack.query.core.QueryKey
import tanstack.react.query.useQuery
import tools.fetchText
import web.html.HTMLSelectElement


external interface WeekDaySelectProps : Props {
    var onPick: (String) -> Unit
}

val CWeekDaySelect = FC<WeekDaySelectProps>("WeekDayInput") { props->
    val selectQueryKey = arrayOf("WeekDayInput").unsafeCast<QueryKey>()

    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = selectQueryKey,
        queryFn = {
            fetchText("${Config.schedulePath}days")
        }
    )
    val inputTimeLessonRef = useRef<HTMLSelectElement>()

    val days: List<String> =
        try {
            Json.decodeFromString(query.data ?: "")
        } catch (e: Throwable) {
            emptyList()
        }

    select {
        ref = inputTimeLessonRef
        option{
            +"Выберите день недели"
            hidden = true
            selected = true
            disabled = true
            value = ""

        }
        days.map {
            option {
                +it
                value = it
            }
        }
            onClick = {
                props.onPick(inputTimeLessonRef.current!!.value)
            }

    }

}