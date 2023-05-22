package component.teacher

import js.core.jso
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.useState
import ru.altmanea.webapp.data.ObjectId
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.Teacher
import tanstack.query.core.QueryKey
import tanstack.react.query.useMutation
import tanstack.react.query.useQuery
import tanstack.react.query.useQueryClient
import tools.HTTPResult
import tools.fetch
import tools.fetchText
import kotlin.js.json


external interface TeacherListProps : Props {
    var teachers: List<Teacher>
    var deleteTeacher: (ObjectId) -> Unit
    var editTeacher: (Teacher) -> Unit
}

val CTeacherList= FC<TeacherListProps>("TeacherList") { props ->
    var editedId by useState(-1)

    props.teachers.forEachIndexed { index, teacher ->
        if(index == editedId){
            CTeacherEdit{
                this.teacher = teacher
                this.editTeacher = {
                    props.editTeacher(it)
                    editedId = -1
                }
            }

        }
        ReactHTML.div {
            +teacher.fullname()

            ReactHTML.span {
                +"  ✎  "
                onClick = {
                    editedId = index
                }
            }

            ReactHTML.span {
                +" ✂ "
                onClick = {
                    props.deleteTeacher(teacher.id)
                }
            }
        }
    }
}


val containerTeacherList = FC("containerTeacher") { _: Props ->
    val queryClient = useQueryClient()
    val profileQueryKey = arrayOf("Teachers").unsafeCast<QueryKey>()
    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = profileQueryKey,
        queryFn = {
            fetchText(Config.teachersPath)
        }
    )

    val deleteTeacherMutation = useMutation<HTTPResult, Any, ObjectId, Any>(
        mutationFn = { id ->
            fetch(
                "${Config.teachersPath}$id",
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

    val addTeacherMutation = useMutation<HTTPResult, Any, Teacher, Any>(
        mutationFn = { teacher ->
            fetch(
                Config.teachersPath,
                jso {
                    method = "POST"
                    headers = json(
                        "Content-Type" to "application/json")
                    body = Json.encodeToString(teacher)
                }
            )
        },
        options = jso {
            onSuccess = { _: Any, _: Any, _: Any? ->
                queryClient.invalidateQueries<Any>(profileQueryKey)
            }
        }
    )

    val editTeacherMutation = useMutation<HTTPResult, Any, Teacher, Any>(
        mutationFn = { teacher ->
            fetch(
                Config.teachersPath,
                jso {
                    method = "PUT"
                    headers = json(
                        "Content-Type" to "application/json")
                    body = Json.encodeToString(teacher)
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
        val items: List<Teacher> =
            try {
                Json.decodeFromString(query.data ?: "")
            } catch (e: Throwable) {
                emptyList()
            }

        CTeacherAdd{
            addTeacher = {addTeacherMutation.mutateAsync(it, null)}
        }

        CTeacherList {
            teachers = items
            deleteTeacher = {deleteTeacherMutation.mutateAsync(it, null)}
            editTeacher = {editTeacherMutation.mutateAsync(it, null)}

        }
    }
}