pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

rootProject.name = "y9catbot"

includeBuild("buildUtils/dependencies")
includeBuild("buildUtils/configuration")
includeBuild("buildUtils/deploy")

include("db-migrations")
include("catgifs:cataas")
include("catgifs:from-cache")
include("bot")
