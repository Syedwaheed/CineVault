plugins {
    alias(libs.plugins.movie.android.ui.feature)
}

android {
    namespace = "com.edu.feature.home.presentation"
}

composeCompiler {
    stabilityConfigurationFile = rootProject.layout.projectDirectory.file("compose_stability.conf")
}

dependencies {
    implementation(projects.feature.home.domain)
    implementation(projects.core.domain)
    implementation(libs.kotlinx.collections.immutable)
}