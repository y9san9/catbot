plugins {
    id(Deps.Plugins.Configuration.Kotlin.Jvm)
}

dependencies {
    implementation(Deps.Libs.Ktor.Client.Core)
    implementation(Deps.Libs.Ktor.Client.Cio)
    implementation(Deps.Libs.Ktor.Client.Serialization)
}
