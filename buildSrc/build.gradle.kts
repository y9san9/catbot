plugins {
    id("dependencies")
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies  {
    implementation(Deps.Plugins.Dependencies.Classpath)
    implementation(Deps.Plugins.Deploy.Classpath)
}
