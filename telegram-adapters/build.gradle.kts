plugins {
    id(Deps.Plugins.Configuration.Kotlin.Jvm)
}

dependencies {
    api(project(Deps.Projects.Bot))
    implementation(Deps.Libs.InMo.TgBotApi)
    implementation(Deps.Libs.Kotlinx.Coroutines)
}
