plugins {
    `kotlin-dsl`
    id("dependencies")
}

group = "deploy"
version = "SNAPSHOT"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(Deps.Plugins.Dependencies.Classpath)
    implementation(Deps.Plugins.Ssh.Classpath)
}

gradlePlugin {
    plugins.register("deploy") {
        id = "deploy"
        implementationClass = "me.y9san9.deploy.Deploy"
    }
}
