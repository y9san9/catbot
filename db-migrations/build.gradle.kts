plugins {
    id(Deps.Plugins.Configuration.Kotlin.Jvm)
}

dependencies {
    implementation(Deps.Libs.Exposed.Core)
    implementation(Deps.Libs.Exposed.Jdbc)
    implementation(Deps.Libs.Postgres.Jdbc)
}
