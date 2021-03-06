import org.gradle.api.Plugin
import org.gradle.api.Project


class KotlinAndroidLibraryConfiguration : Plugin<Project> {
    override fun apply(target: Project) {
        target.plugins.apply(Deps.Plugins.Android.Library)
        target.plugins.apply(Deps.Plugins.Kotlin.Android)
        target.plugins.apply(Deps.Plugins.Dependencies.Id)
    }
}
