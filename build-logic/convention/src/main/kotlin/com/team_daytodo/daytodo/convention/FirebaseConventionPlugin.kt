package com.team_daytodo.daytodo.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class FirebaseConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {

        pluginManager.apply("com.google.gms.google-services")

        dependencies {
            val firebaseBom =
                platform(libs.findLibrary("firebase.bom").get())

            add("implementation", firebaseBom)
            add(
                "implementation",
                libs.findLibrary("firebase.messaging").get()
            )
        }
    }
}