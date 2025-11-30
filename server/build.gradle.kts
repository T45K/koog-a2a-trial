plugins {
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.koog.a2a.core)
    implementation(libs.koog.a2a.server)
    implementation(libs.koog.a2a.transport.server)

    // https://github.com/T45K/koog-google-search-tool/releases/tag/0.0.1
    implementation(files("../deps/koog-google-search-tool-0.0.1.jar"))

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
}
