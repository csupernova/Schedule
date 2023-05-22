package component.shedule

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML.option
import react.dom.html.ReactHTML.select
import react.useRef
import ru.altmanea.webapp.data.ObjectId
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.Lesson
import tanstack.query.core.QueryKey
import tanstack.react.query.useQuery
import tools.fetchText
import web.html.HTMLSelectElement


external interface LessonSelectProps : Props {
    var onPick: (String) -> Unit
}

val CLessonSelect = FC<LessonSelectProps>("LessonInput") { props ->
    val selectQueryKey = arrayOf("LessonInput").unsafeCast<QueryKey>()

    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = selectQueryKey,
        queryFn = {
            fetchText(Config.lessonsPath)
        }
    )
    val selectLessonRef = useRef<HTMLSelectElement>()
    val lessons: List<Lesson> =
        try {
            Json.decodeFromString(query.data ?: "")
        } catch (e: Throwable) {
            emptyList()
        }
    select {
        ref = selectLessonRef
        option{
            +"Выберите предмет"
            hidden = true
            selected = true
            disabled = true
            value = ""

        }
        lessons.map { les ->
            option {
                +les.name
                value = les.id
            }
        }
        onClick = {
            props.onPick(selectLessonRef.current?.value!!)
        }
    }

}