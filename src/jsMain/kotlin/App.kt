
import component.Main.containerScheduleList
import component.group.containerGroupList
import component.lesson.containerAddSchedule
import component.lesson.containerLessonList
import component.room.containerRoomList
import component.teacher.containerTeacherList
import react.FC
import react.Props
import react.create
import react.createContext
import react.dom.client.createRoot
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ul
import react.router.Route
import react.router.Routes
import react.router.dom.HashRouter
import react.router.dom.Link
import ru.altmanea.webapp.config.Config
import tanstack.query.core.QueryClient
import tanstack.query.core.QueryKey
import tanstack.react.query.QueryClientProvider
import tanstack.react.query.devtools.ReactQueryDevtools
import web.dom.document

val invalidateRepoKey = createContext<QueryKey>()

fun main() {
    val container = document.getElementById("root")!!
    createRoot(container).render(app.create())
}

val app = FC<Props>("App") {
    HashRouter {
        QueryClientProvider {
            client = QueryClient()
            ul {
                listOf(Pair("Rooms", "Аудитории"),Pair("Groups", "Группы"),Pair("Teachers", "Преподаватели"),
                    Pair("Lessons", "Предметы"), Pair("Schedule", "Добавить расписание"))
                    .map { tag ->
                    li {
                        Link {
                            +tag.second
                            to = tag.first.lowercase()
                        }
                    }
                }
            }

            Routes {
                Route{
                    path = "*"
                    element = containerScheduleList.create()
                }
                Route{
                    path = Config.groupsPath
                    element = containerGroupList.create()
                }
                Route{
                    path = Config.teachersPath
                    element = containerTeacherList.create()
                }
                Route{
                    path = Config.lessonsPath
                    element = containerLessonList.create()
                }
                Route{
                    path = Config.schedulePath
                    element =containerAddSchedule.create()
                }
                Route {
                    path = Config.roomsPath
                    element = containerRoomList.create()
                }

            }
            ReactQueryDevtools { }
        }
    }
}