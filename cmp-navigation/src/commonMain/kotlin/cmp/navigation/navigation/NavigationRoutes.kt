package cmp.navigation.navigation

sealed class NavigationRoutes {
    companion object {
        const val LOGIN_CONFIRMED = "login?confirmed"
    }
    sealed class NavWrapper(val route: String) {
        object Splash : NavWrapper("splash")
    }

    sealed class Unauthenticated(val route: String) : NavigationRoutes() {
        object NavigationRoute : Unauthenticated(route = "unauthenticated")
        object Auth : Unauthenticated(route = "auth")
        object SignIn : Unauthenticated(route = "login") {
            fun createRoute(confirmed: Boolean = false): String {
                return "$LOGIN_CONFIRMED=$confirmed"
            }
        }

        object Registration : Unauthenticated(route = "registration")
    }

    sealed class Feed(val route: String) : NavigationRoutes() {
        object NavigationRoute : Feed(route = "feed")
        object List : Feed(route = "list")
        object Detail : Feed("detail/{postId}/{artistId}")
    }

    sealed class Onboarding(val route: String) : NavigationRoutes() {
        object NavigationRoute : Onboarding(route = "onboarding")
        object Slider : Onboarding(route = "slider")
    }

    sealed class Profile(val route: String) : NavigationRoutes() {
        object NavigationRoute : Profile(route = "profile")
    }

    sealed class Fanzone(val route: String) : NavigationRoutes() {
        object NavigationRoute : Fanzone(route = "fanzone")
        object Detail : Feed("forum/{postId}/{artistId}")
    }

    sealed class Events(val route: String) : NavigationRoutes() {
        object NavigationRoute : Events(route = "events")
    }

    sealed class Merch(val route: String) : NavigationRoutes() {
        object NavigationRoute : Profile(route = "merch")
    }

    sealed class Library(val route: String) : NavigationRoutes() {
        object NavigationRoute : Profile(route = "library")
    }
}
