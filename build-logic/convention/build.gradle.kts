import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.room.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        val prefix = "local"
        register("androidComposeApplication") {
            id = "$prefix.android.compose.application"
            implementationClass = "AndroidComposeApplicationConventionPlugin"
        }
        register("androidComposeLibrary") {
            id = "$prefix.android.compose.library"
            implementationClass = "AndroidComposeLibraryConventionPlugin"
        }
        register("androidRoom") {
            id = "$prefix.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
    }
}