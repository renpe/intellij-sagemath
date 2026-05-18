import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    // Must match (or be newer than) the Kotlin version that the targeted
    // IntelliJ Platform was compiled with — otherwise the K2 compiler hits
    // FirIncompatibleClassExpressionChecker on platform class files.
    kotlin("jvm") version "2.3.21"
    id("org.jetbrains.intellij.platform") version "2.16.0"
}

group = providers.gradleProperty("pluginGroup").get()
version = providers.gradleProperty("pluginVersion").get()

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        // Since 2025.3 there is one unified IDEA distribution — no separate
        // IC/IU artifacts. Use intellijIdea(version) instead of create().
        intellijIdea(providers.gradleProperty("platformVersion").get())
        testFramework(TestFrameworkType.Platform)
    }
}

kotlin {
    jvmToolchain(providers.gradleProperty("javaVersion").get().toInt())
}

intellijPlatform {
    pluginConfiguration {
        // Display name comes from plugin.xml's <name>SageMath</name>.
        // We intentionally don't patch it here so the IDE shows "SageMath",
        // not the technical project name "intellij-sagemath".
        version = providers.gradleProperty("pluginVersion")

        ideaVersion {
            sinceBuild = providers.gradleProperty("pluginSinceBuild")
            untilBuild = providers.gradleProperty("pluginUntilBuild")
        }
    }

    publishing {
        token = providers.environmentVariable("PUBLISH_TOKEN")
    }

    pluginVerification {
        // Default would verify against ~8 IDE versions in parallel, which
        // routinely hits the IntelliJ Plugin Verifier's known JAR-cache
        // race condition (ClosedByInterruptException across worker
        // threads). recommended() picks a small curated subset — usually
        // the lowest supported build, latest stable, and one intermediate.
        ides {
            recommended()
        }
    }
}

tasks {
    wrapper {
        gradleVersion = "9.5.1"
    }
}
