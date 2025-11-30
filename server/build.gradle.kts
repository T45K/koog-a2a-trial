plugins {
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.koog.a2a.core)
    implementation(libs.koog.a2a.server)
    implementation(libs.koog.a2a.transport.server)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
}
