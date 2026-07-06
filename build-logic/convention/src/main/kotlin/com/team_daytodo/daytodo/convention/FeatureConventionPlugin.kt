package com.team_daytodo.daytodo.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class FeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("daytodo.android.library")
        pluginManager.apply("daytodo.android.compose")

        dependencies {
            add("implementation", project(":core"))
            add("implementation", project(":domain"))
            add("implementation", project(":uikit"))
            add("implementation", libs.findLibrary("kotlinx.coroutines.android").get())
        }
    }
}
