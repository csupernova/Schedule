package component.lesson

import component.shedule.*
import invalidateRepoKey
import js.core.jso
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML.br
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.div
import react.useContext
import react.useState
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.*
import tanstack.query.core.QueryKey
import tanstack.react.query.useMutation
import tanstack.react.query.useQuery
import tanstack.react.query.useQueryClient
import tools.HTTPResult
import tools.fetch
import tools.fetchText
import kotlin.js.json

external interface AddLessonSelectProps : Props {
    var onPick: (String) -> Unit
}

val containerAddSchedule = FC<AddLessonSelectProps>("AddSchedule") { _ ->

    val queryClient = useQueryClient()
    val invalidateRepoKey = useContext(invalidateRepoKey)

    var numberLesson by useState("1")
    var weekday by useState("")
    var weekType by useState("")
    var lesson by useState("")
    var lessonType by useState("")
    var teacher by useState("")
    var group by useState("")
    var subgroup by useState<String?>(null)
    var room by useState("")


    val selectQueryKey = arrayOf("TimeLessonInput", numberLesson).unsafeCast<QueryKey>()
    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = selectQueryKey,
        queryFn = {
            fetchText("schedule/time/${numberLesson}")
        }
    )

    val time: String =
        try {
            query.data ?: ""
        } catch (e: Throwable) {
            ""
        }

    val addMutation = useMutation<HTTPResult, Any, ScheduleItem, Any>(
        mutationFn = { element ->
            fetch(
                Config.schedulePath,
                jso {
                    method = "POST"
                    headers = json("Content-Type" to "application/json")
                    body = Json.encodeToString(element)
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(invalidateRepoKey)
            }
        }
    )
    div {
        CNumberLessonSelect {
            onPick = { numberLesson = it }
        }
    }
    br{}
    div {
        CWeekDaySelect {
            onPick = {
                weekday = it
            }
        }
    }
    br{}
    div {
        CWeekTypeSelect {
            onPick = {
                weekType = it
            }
        }
    }
    br{}
    div {
        CTeacherSelect {
            onPick = {
                teacher = it
            }
        }
    }
    br{}
    div {
        CLessonSelect {
            onPick = {
                lesson = it
            }
        }
    }
    br{}
    div {
        CLessonTypeSelect {
            onPick = {
                lessonType = it
            }
        }
    }
    br{}
    div {
        CGroupSelect {
            this.lessonType = lessonType
            onPick = {
                group = it
            }
            onPickSubGroup = {
                subgroup = it
            }
        }
    }
    br{}
    div {
        CRoomSelect {
            onPick = {
                room = it
            }
        }
    }
    br{}
    div {
        button {
            +"Добавить в расписание"
            onClick = {
                addMutation.mutateAsync(
                    ScheduleItem(
                        "id",
                        lesson,
                        LessonType.values().findLast { it.type == lessonType }!!,
                        teacher,
                        group,
                        subgroup,
                        Weekday.values().findLast { it.day == weekday }!!,
                        WeekType.values().findLast { it.type == weekType }!!,
                        TimeLesson.values().findLast { it.fulltime() == time }!!,
                        room
                    ), null
                )
            }
        }
    }
}
