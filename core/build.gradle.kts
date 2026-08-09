plugins {
    alias(libs.plugins.daytodo.kotlin.library)
}

dependencies {
    api(libs.kotlinx.coroutines.core)

    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.core)

    testImplementation(libs.junit)
}
