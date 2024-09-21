import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(projectDirPath)
                    }
                }

                cssSupport {
                    enabled = true
                }
            }
        }
        binaries.executable()
    }

    sourceSets {

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
        }
    }
}

tasks.register("cleanWebApp") {
    doLast {
        delete(rootProject.layout.projectDirectory.dir("webApp").asFile)
    }
}

tasks.register<Copy>("copyWebApp") {
    mustRunAfter("wasmJsBrowserDistributionWrapper")

    from("build/dist/wasmJs/productionExecutable") {
        include("**/*") // Копируем все файлы и папки
    }
    into(rootProject.layout.projectDirectory.dir("webApp"))
}

tasks.register("wasmJsBrowserDistributionWrapper") {
    mustRunAfter("cleanWebApp")
    dependsOn("wasmJsBrowserDistribution")
}

tasks.register("buildAndDeployWebApp") {
    dependsOn("wasmJsBrowserDistributionWrapper", "cleanWebApp", "copyWebApp")
}
