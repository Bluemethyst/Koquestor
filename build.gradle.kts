import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.libsDirectory

val ktorVersion: String by project

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "dev.bluemethyst.apps.koquestor"
version = "0.0.0-INDEV"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-cio:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:1.5.6")
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
}

compose.desktop {
    application {
        mainClass = "dev.bluemethyst.apps.koquestor.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Exe, TargetFormat.Deb)
            packageName = "Koquestor"
            packageVersion = "1.0.0"
            /*macOS {
                iconFile.set(project.file("koquestor.icns"))
            }
            windows {
                iconFile.set(project.file("koquestor.ico"))
            }
            linux {
                iconFile.set(project.file("koquestor.png"))
            }*/
        }
    }
}
