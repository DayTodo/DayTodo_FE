plugins {
    alias(libs.plugins.daytodo.kotlin.library)
}

dependencies {
    api(project(":core"))

    api(libs.kotlinx.coroutines.core)
    api(libs.javax.inject)
    testImplementation(libs.junit)
    testImplementation(libs.kotlinx.coroutines.test)
}
