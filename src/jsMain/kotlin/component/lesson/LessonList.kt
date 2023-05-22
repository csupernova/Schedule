package component.lesson

import js.core.jso
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.useState
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.Lesson
import ru.altmanea.webapp.data.ObjectId
import tanstack.query.core.QueryKey
import tanstack.react.query.useMutation
import tanstack.react.query.useQuery
import tanstack.react.query.useQueryClient
import tools.HTTPResult
import tools.fetch
import tools.fetchText
import kotlin.js.json

external interface LessonListProps : Props {
    var lessons: List<Lesson>
    var deleteLesson: (ObjectId) -> Unit
    var editLesson: (Lesson) -> Unit
}

val CLessonList= FC<LessonListProps>("LessonList") { props ->
    var editedId by useState(-1)

    props.lessons.forEachIndexed { index, lesson ->
        if(index == editedId){
            CLessonEdit{
                this.lesson = lesson
                this.editLesson = {
                    props.editLesson(it)
                    editedId = -1
                }
            }

        }
        ReactHTML.div {
            +lesson.name

            ReactHTML.span {
                +"  ✎  "
                onClick = {
                    editedId = index
                }
            }

            ReactHTML.span {
                +" ✂ "
                onClick = {
                    props.deleteLesson(lesson.id)
                }
            }
        }
    }
}


val containerLessonList = FC("containerLesson") { _: Props ->
    val queryClient = useQueryClient()
    val profileQueryKey = arrayOf("Lessons").unsafeCast<QueryKey>()
    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = profileQueryKey,
        queryFn = {
            fetchText(Config.lessonsPath)
        }
    )

    val deleteLessonMutation = useMutation<HTTPResult, Any, ObjectId, Any>(
        mutationFn = { id ->
            fetch(
                "${Config.lessonsPath}$id",
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

    val addLessonMutation = useMutation<HTTPResult, Any, Lesson, Any>(
        mutationFn = { lesson ->
            fetch(
                Config.lessonsPath,
                jso {
                    method = "POST"
                    headers = json(
                        "Content-Type" to "application/json")
                    body = Json.encodeToString(lesson)
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(profileQueryKey)
            }
        }
    )

    val editLessonMutation = useMutation<HTTPResult, Any, Lesson, Any>(
        mutationFn = { group ->
            fetch(
                Config.lessonsPath,
                jso {
                    method = "PUT"
                    headers = json(
                        "Content-Type" to "application/json")
                    body = Json.encodeToString(group)
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
        val items: List<Lesson> =
            try {
                Json.decodeFromString(query.data ?: "")
            } catch (e: Throwable) {
                emptyList()
            }

        CLessonAdd{
            addLesson = {addLessonMutation.mutateAsync(it, null)}
        }

        CLessonList {
            lessons = items
            deleteLesson = {deleteLessonMutation.mutateAsync(it, null)}
            editLesson = {editLessonMutation.mutateAsync(it, null)}

        }
    }
}



