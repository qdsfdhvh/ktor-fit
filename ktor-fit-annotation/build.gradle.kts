import java.util.Properties

plugins {
    kotlin("multiplatform")
    id("maven-publish")
    id("signing")
}

kotlin {
    jvm()
    linuxX64()
    macosX64()
    watchosArm64()
    watchosX64()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    ios()
    js {
        browser()
        nodejs()
    }
    sourceSets {
        val commonMain by getting
        val jvmMain by getting
        val linuxX64Main by getting
        val macosX64Main by getting
        val watchosArm64Main by getting
        val watchosX64Main by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by getting
        val jsMain by getting
    }
}

group = "io.github.qdsfdhvh"
version = "1.0.0"

ext {
    val publishPropFile = rootProject.file("publish.properties")
    if (publishPropFile.exists()) {
        Properties().apply {
            load(publishPropFile.inputStream())
        }.forEach { name, value ->
            set(name.toString(), value)
        }
    } else {
        set("signing.keyId", System.getenv("SIGNING_KEY_ID"))
        set("signing.password", System.getenv("SIGNING_PASSWORD"))
        set("signing.secretKeyRingFile", System.getenv("SIGNING_SECRET_KEY_RING_FILE"))
        set("ossrhUsername", System.getenv("OSSRH_USERNAME"))
        set("ossrhPassword", System.getenv("OSSRH_PASSWORD"))
    }
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

publishing {
    signing {
        sign(publishing.publications)
    }
    repositories {
        maven {
            val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            url = if (version.toString().endsWith("SNAPSHOT")) {
                uri(snapshotsRepoUrl)
            } else {
                uri(releasesRepoUrl)
            }
            credentials {
                username = project.ext.get("ossrhUsername").toString()
                password = project.ext.get("ossrhPassword").toString()
            }
        }
    }
    publications.withType<MavenPublication> {
        artifact(javadocJar.get())
        pom {
            name.set("ktor-fit")
            description.set("use ktor like retrofit.")
            url.set("https://github.com/qdsfdhvh/ktor-fit")

            licenses {
                license {
                    name.set("MIT")
                    url.set("https://opensource.org/licenses/MIT")
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
