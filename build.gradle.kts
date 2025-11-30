plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

subprojects {
    apply(plugin = "kotlin")

    repositories {
        mavenCentral()
    }

    dependencies {
        "implementation"(rootProject.libs.koog.agents)

        // Suppress No SLF4J error
        "implementation"(rootProject.libs.logback.classic)
    }

    the<org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension>().apply {
        jvmToolchain(25)
    }
}
