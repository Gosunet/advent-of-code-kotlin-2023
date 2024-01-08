plugins {
    kotlin("jvm") version "1.9.20"
}

sourceSets {
    main {
        kotlin.srcDir("src")
    }
}
dependencies {
    // coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC")
    implementation("org.choco-solver:choco-solver:4.10.10")
    implementation("org.jgrapht:jgrapht:1.5.2")
}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}
