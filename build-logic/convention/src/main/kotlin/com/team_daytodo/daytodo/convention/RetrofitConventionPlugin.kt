package com.team_daytodo.daytodo.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class RetrofitConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

        dependencies {
            val okhttpBom = platform(libs.findLibrary("okhttp.bom").get())

            add("implementation", libs.findLibrary("retrofit.core").get())
            add("implementation", libs.findLibrary("retrofit.kotlinx.serialization").get())
            add("implementation", okhttpBom)
            add("implementation", libs.findLibrary("okhttp.core").get())
            add("implementation", libs.findLibrary("okhttp.logging").get())
            add("implementation", libs.findLibrary("kotlinx.serialization.json").get())
        }
    }
}
