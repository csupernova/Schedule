package component.group

import csstype.*
import emotion.css.css
import emotion.react.css
import js.core.jso
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.label
import react.useState
import ru.altmanea.webapp.data.ObjectId
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.TypeGroup
import tanstack.query.core.QueryKey
import tanstack.react.query.useMutation
import tanstack.react.query.useQuery
import tanstack.react.query.useQueryClient
import tools.HTTPResult
import tools.fetch
import tools.fetchText
import kotlin.js.json

external interface GroupListProps : Props {
    var groups: List<TypeGroup>
    var deleteGroup: (ObjectId) -> Unit
    var editGroup: (TypeGroup) -> Unit
}

val CGroupList= FC<GroupListProps>("GroupList") { props ->
    var editedIdGroup by useState(-1)
    var editedIdFlow by useState(-1)
        div {
            label {
                +"Группы"
            }
            props.groups.filterIsInstance<TypeGroup.Group>().forEachIndexed { index, group ->
                if (index == editedIdGroup) {
                    CGroupEdit {
                        this.group = group
                        this.editGroup = {
                            props.editGroup(it)
                            editedIdGroup = -1
                        }
                    }
                }
                ReactHTML.div {
                    +group.name
                    ReactHTML.span {
                        +"  ✎  "
                        onClick = {
                            editedIdGroup = index
                        }
                    }
                    ReactHTML.span {
                        +" ✂ "
                        onClick = {
                            props.deleteGroup(group.id)
                        }
                    }
                }
            }
        }
        div {
            label {
                +"Потоки"
            }
            props.groups.filterIsInstance<TypeGroup.Flow>().forEachIndexed { index, group ->
                if (index == editedIdFlow) {
                    CGroupEdit {
                        this.group = group
                        this.editGroup = {
                            props.editGroup(it)
                            editedIdFlow = -1
                        }
                    }
                }
                ReactHTML.div {
                    +group.name
                    ReactHTML.span {
                        +"  ✎  "
                        onClick = {
                            editedIdFlow = index
                        }
                    }
                    ReactHTML.span {
                        +" ✂ "
                        onClick = {
                            props.deleteGroup(group.id)
                        }
                    }
                }
            }
        }
}


val containerGroupList = FC("containerGroups") { _: Props ->
    val queryClient = useQueryClient()
    val profileQueryKey = arrayOf("Groups").unsafeCast<QueryKey>()
    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = profileQueryKey,
        queryFn = {
            fetchText(Config.groupsPath)
        }
    )

    val deleteGroupMutation = useMutation<HTTPResult, Any, ObjectId, Any>(
        mutationFn = { id ->
            fetch(
                "${Config.groupsPath}/$id",
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

    val addGroupMutation = useMutation<HTTPResult, Any, TypeGroup, Any>(
        mutationFn = { group ->
            fetch(
                Config.groupsPath,
                jso {
                    method = "POST"
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

    val editGroupMutation = useMutation<HTTPResult, Any, TypeGroup, Any>(
        mutationFn = { group ->
            fetch(
                Config.groupsPath,
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
        val items: List<TypeGroup> =
            try {
                Json.decodeFromString(query.data ?: "")
            } catch (e: Throwable) {
                emptyList()
            }

        CGroupAdd{
            addGroup = {addGroupMutation.mutateAsync(it, null)}
        }

        CGroupList {
            groups = items
            deleteGroup = {deleteGroupMutation.mutateAsync(it, null)}
            editGroup = {editGroupMutation.mutateAsync(it, null)}

        }
    }
}
