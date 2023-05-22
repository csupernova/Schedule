package component.shedule

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import query.QueryError
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.label
import react.dom.html.ReactHTML.option
import react.useRef
import react.useState
import ru.altmanea.webapp.config.Config
import ru.altmanea.webapp.data.Lesson
import ru.altmanea.webapp.data.LessonType
import ru.altmanea.webapp.data.ObjectId
import ru.altmanea.webapp.data.TypeGroup
import tanstack.query.core.QueryKey
import tanstack.react.query.useQuery
import tools.fetchText
import web.html.HTMLSelectElement

external interface GroupSelectProps : Props {
    var onPick: (ObjectId) -> Unit
    var onPickSubGroup: (String) -> Unit
    var lessonType: String
}

val CGroupSelect = FC<GroupSelectProps>("GroupInput") { props ->
    val selectQueryKey = arrayOf("GroupInput").unsafeCast<QueryKey>()

    val inputSubGroupRef = useRef<HTMLSelectElement>()
    val inputGroupRef = useRef<HTMLSelectElement>()

    val query = useQuery<String, QueryError, String, QueryKey>(
        queryKey = selectQueryKey,
        queryFn = {
            fetchText(Config.groupsPath)
        }
    )

    var groups: List<TypeGroup> =
        try {
            Json.decodeFromString(query.data ?: "")
        } catch (e: Throwable) {
            emptyList()
        }
    val selectedType = LessonType.values().findLast{it.type == props.lessonType}

    if(selectedType == LessonType.LECTURE){
        groups.filterIsInstance<TypeGroup.Flow>().also { groups = it }
    }
    else{
        groups.filterIsInstance<TypeGroup.Group>().also { groups = it }
    }
    ReactHTML.select {
        ref = inputGroupRef
        option{
            if(selectedType == LessonType.LECTURE){
            +"Выберите поток"
            }
            else{
                +"Выберите группу"
            }
            hidden = true
            selected = true
            disabled = true
        }
        groups.map {
            ReactHTML.option {
                +it.name
                value = it.id
            }
        }
        onClick = {
            props.onPick(inputGroupRef.current!!.value)
        }
    }
    if(selectedType == LessonType.LAB)
    ReactHTML.select {
        ref = inputSubGroupRef
        option{
            +"Выберите подгруппу"
            hidden = true
            selected = true
            disabled = true
        }
        listOf(1,2).map {
            ReactHTML.option {
                +it.toString()
                value = it
            }
        }
        onClick = {
            props.onPickSubGroup(inputSubGroupRef.current!!.value)
        }
    }


}