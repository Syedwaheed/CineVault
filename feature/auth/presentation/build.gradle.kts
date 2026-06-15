plugins {
    alias(libs.plugins.movie.android.ui.feature)
}

android {
    namespace = "com.edu.auth.presentation"
}

dependencies {
    implementation(projects.feature.auth.domain)
    implementation(projects.core.domain)
}