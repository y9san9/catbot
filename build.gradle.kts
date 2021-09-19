import me.y9san9.deploy.ssh
import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    id(Deps.Plugins.Configuration.Kotlin.Jvm)
}

group = AppInfo.PACKAGE
version = AppInfo.VERSION

allprojects {
    repositories {
        mavenCentral()
    }
}

dependencies {
    implementation(project(Deps.Projects.Bot))
    implementation(project(Deps.Projects.Catgifs))
    implementation(Deps.Libs.Exposed.Core)
    implementation(Deps.Libs.Exposed.Jdbc)
    implementation(Deps.Libs.InMo.TgBotApi)
}


val deployPropertiesFile = file("deploy.properties")

if(deployPropertiesFile.exists()) {
    val properties = loadProperties(deployPropertiesFile.absolutePath)

    project.apply<me.y9san9.deploy.Deploy>()
    project.configure<me.y9san9.deploy.DeployConfiguration> {
        serviceName = "catbot"
        implementationTitle = "catbot"
        mainClassName = "me.y9san9.catbot.MainKt"
        host = properties.getProperty("host")
        user = properties.getProperty("user")
        password = properties.getProperty("password")
        deployPath = properties.getProperty("deployPath")
        // On linux should be something like /home/user/.ssh/known_hosts
        // Or Default Allow Any Hosts if this value is not specified,
        // But then MITM may be performed
        knownHostsFile = properties.getProperty("knownHosts")
    }

    task("stop") {
        group = "deploy"

        doLast {
            project.ssh {
                execute("systemctl stop ${project.the<me.y9san9.deploy.DeployConfiguration>().serviceName}")
            }
        }
    }
}
