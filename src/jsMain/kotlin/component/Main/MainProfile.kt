package component.Main


import js.core.jso
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.button
import react.dom.html.ReactHTML.h1
import react.dom.html.ReactHTML.table
import react.dom.html.ReactHTML.tbody
import react.dom.html.ReactHTML.td
import react.dom.html.ReactHTML.tr
import react.useState
import ru.altmanea.webapp.common.FilterSchedule
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

external interface ScheduleListProps : Props {
    var scheduleitems: List<ScheduleItem>
    var lessons: List<Lesson>
    var teachers: List<Teacher>
    var groups: List<TypeGroup>
    var rooms: List<Room>
    var deleteItem: (ObjectId) -> Unit
}

val CScheduleList= FC<ScheduleListProps>("LessonList") { props ->
    h1{
        +"Расписание"
    }
    table{
        border = 1
        tbody {
            tr {
                td {
                    +"Номер пары"
                }
                td {
                    +"Время"
                }
                td {
                    +"День недели, тип"
                }
                td {
                    +"Название предмета"
                }
                td {
                    +"Преподаватель"
                }
                td {
                    +"Группа"
                }
                td {
                    +"Аудитория"
                }
                td{
                    +""
                }
            }

            props.scheduleitems.forEach { elem ->
                tr {
                    td {
                        +"${elem.time.numberLesson(elem.time.startTime)}"
                    }
                    td {
                        +"${elem.time.startTime}-${elem.time.endTime}"
                    }
                    td {
                        +"${elem.weekDay.day}, ${elem.weekType.type}"
                    }
                    td {
                        +"${props.lessons.findLast { it.id == elem.lesson }?.name}, ${elem.typeLesson.type}"
                    }
                    td {
                        +"${props.teachers.find { it.id == elem.teacher }?.fullname()}"
                    }
                    td {
                        if(elem.subGroup == null)
                        +"${props.groups.find { it.id == elem.group }?.name}"
                        else
                        +"${props.groups.find { it.id == elem.group }?.name} ${elem.subGroup}пг."
                    }
                    td {
                       +"${props.rooms.find { it.id == elem.room }?.number}"
                    }
                    td{
                        button{
                            +"Удалить"
                            onClick = {
                                props.deleteItem(elem.id)

                            }
                        }
                    }
                }

            }
        }

    }
}


val containerScheduleList = FC("containerLessons") { _: Props ->
    val lessonsQueryKey = arrayOf("LessonsGet").unsafeCast<QueryKey>()
    val teachersQueryKey = arrayOf("TeachersGet").unsafeCast<QueryKey>()
    val groupsQueryKey = arrayOf("GroupsGet").unsafeCast<QueryKey>()
    val roomsQueryKey = arrayOf("RoomsGet").unsafeCast<QueryKey>()
    val (filter, setFilter) = useState(FilterSchedule())
    val profileQueryKey = arrayOf("Schedule${filter.group}${filter.teacher}${filter.lesson}").unsafeCast<QueryKey>()

    val queryClient = useQueryClient()


    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = profileQueryKey,
        queryFn = {
            fetchText("schedule/filter/",
            jso{
                method = "POST"
                body = Json.encodeToString(filter)
            })
        }
    )

    val queryLessons = useQuery<String, QueryError, String, QueryKey>(
        queryKey = lessonsQueryKey,
        queryFn = {
            fetchText(Config.lessonsPath)
        }
    )

    val queryTeachers = useQuery<String, QueryError, String, QueryKey>(
        queryKey = teachersQueryKey,
        queryFn = {
            fetchText(Config.teachersPath)
        }
    )

    val queryGroups = useQuery<String, QueryError, String, QueryKey>(
        queryKey = groupsQueryKey,
        queryFn = {
            fetchText(Config.groupsPath)
        }
    )

    val queryRooms = useQuery<String, QueryError, String, QueryKey>(
        queryKey = roomsQueryKey,
        queryFn = {
            fetchText(Config.roomsPath)
        }
    )

    val deleteScheduleMutation = useMutation<HTTPResult, Any, ObjectId, Any>(
        mutationFn = { id ->
            fetch(
                "${Config.schedulePath}$id",
                jso {
                    method = "DELETE"
                    headers = json("Content-Type" to "application/json")
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(profileQueryKey)
            }
        }
    )

    if (query.isLoading) ReactHTML.div { +"Loading .." }
    else if (query.isError) ReactHTML.div { +"Error!" }
    else {
        val items =
            try {
                Json.decodeFromString<List<ScheduleItem>>(query.data ?: "")
            } catch (e: Throwable) {
                emptyList()
            }

        val lessons: List<Lesson> =
            try {
                Json.decodeFromString(queryLessons.data ?: "")
            } catch (e: Throwable) {
                emptyList()
            }

        val teachers: List<Teacher> =
            try {
                Json.decodeFromString(queryTeachers.data ?: "")
            } catch (e: Throwable) {
                emptyList()
            }

        val groups: List<TypeGroup> =
            try {
                Json.decodeFromString(queryGroups.data ?: "")
            } catch (e: Throwable) {
                emptyList()
            }

        val rooms: List<Room> =
            try {
                Json.decodeFromString(queryRooms.data ?: "")
            } catch (e: Throwable) {
                emptyList()
            }

        CScheduleFilter{
            this.teachers = teachers
            this.groups = groups
            this.lessons = lessons
            onPick = {
                setFilter(it)
            }
        }

        CScheduleList {
            scheduleitems = items
            this.lessons = lessons
            this.teachers = teachers
            this.groups = groups
            this.rooms = rooms
            deleteItem = {
                deleteScheduleMutation.mutateAsync(it, null)}
        }
    }
}