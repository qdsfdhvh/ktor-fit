import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("com.android.application") version "7.2.0" apply false
    id("com.android.library") version "7.2.0" apply false
    kotlin("multiplatform") version "1.6.21" apply false
    id("org.jetbrains.dokka") version "1.6.21" apply false
    id("com.vanniktech.maven.publish") version "0.20.0" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    group = "io.github.qdsfdhvh"
    version = "1.0.3"

    tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
        dokkaSourceSets.configureEach {
            reportUndocumented.set(false)
            skipDeprecated.set(true)
            jdkVersion.set(8)
            perPackageOption {
                matchingRegex.set("app\\.cash\\.zipline\\.internal\\.*")
                suppress.set(true)
            }
        }
        if (name == "dokkaGfm") {
            outputDirectory.set(project.file("${project.rootDir}/docs/0.x"))
        }
    }

    plugins.withId("com.vanniktech.maven.publish.base") {
        @Suppress("UnstableApiUsage")
        configure<MavenPublishBaseExtension> {
            publishToMavenCentral(SonatypeHost.S01)
            signAllPublications()
            pom {
                description.set("use ktor like retrofit.")
                name.set(project.name)
                url.set("https://github.com/qdsfdhvh/ktor-fit")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("Seiko")
                        name.set("SeikoDes")
                        email.set("seiko_des@outlook.com")
                    }
                }
                scm {
                    url.set("https://github.com/qdsfdhvh/ktor-fit")
                    connection.set("scm:git:git://github.com/qdsfdhvh/ktor-fit.git")
                    developerConnection.set("scm:git:git://github.com/qdsfdhvh/ktor-fit.git")
                }
            }
        }
    }
}
