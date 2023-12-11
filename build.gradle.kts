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

//    implementation(files("polygon-1.0.2.jar"))
//    implementation("org.apache.commons:commons-geometry-parent:1.0")

}

tasks {
    wrapper {
        gradleVersion = "8.5"
    }
}
