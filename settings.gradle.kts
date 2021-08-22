pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

rootProject.name = "y9catbot"

includeBuild("buildUtils/dependencies")
includeBuild("buildUtils/configuration")

include("catgifs")
