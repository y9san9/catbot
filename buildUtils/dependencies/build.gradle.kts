plugins {
    `kotlin-dsl`
}

group = "dependencies"
version = "SNAPSHOT"

gradlePlugin {
    plugins.register("dependencies") {
        id = "dependencies"
        implementationClass = "unused.GradlePlugin"
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}

repositories {
    mavenCentral()
}
