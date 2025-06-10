package com.dailyfilm.buildlogic.primitive

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies

class SerializationPlugin: Plugin<Project> {
    override fun apply(target: Project) = with (target) {
        apply(plugin = "org.jetbrains.kotlin.plugin.serialization")

        dependencies {
            implementation(libs.library("kotlinx.serialization.json"))
        }
    }
}