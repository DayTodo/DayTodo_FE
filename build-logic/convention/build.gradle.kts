plugins {
    `kotlin-dsl`
}

group = "com.team_daytodo.daytodo.buildlogic"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.detekt.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "daytodo.android.application"
            implementationClass = "com.team_daytodo.daytodo.convention.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "daytodo.android.library"
            implementationClass = "com.team_daytodo.daytodo.convention.AndroidLibraryConventionPlugin"
        }
        register("kotlinLibrary") {
            id = "daytodo.kotlin.library"
            implementationClass = "com.team_daytodo.daytodo.convention.KotlinLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = "daytodo.android.compose"
            implementationClass = "com.team_daytodo.daytodo.convention.ComposeConventionPlugin"
        }
        register("hilt") {
            id = "daytodo.hilt"
            implementationClass = "com.team_daytodo.daytodo.convention.HiltConventionPlugin"
        }
        register("retrofit") {
            id = "daytodo.retrofit"
            implementationClass = "com.team_daytodo.daytodo.convention.RetrofitConventionPlugin"
        }
        register("androidFeature") {
            id = "daytodo.android.feature"
            implementationClass = "com.team_daytodo.daytodo.convention.FeatureConventionPlugin"
        }
    }
}
