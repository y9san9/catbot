import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.the
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


class KotlinJvmConfiguration : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply(Deps.Plugins.Kotlin.Jvm)
        target.plugins.apply(Deps.Plugins.Dependencies.Id)
        target.tasks.withType<KotlinCompile>().all {
            kotlinOptions {
                freeCompilerArgs += "-Xcontext-receivers"
            }
        }
    }
}