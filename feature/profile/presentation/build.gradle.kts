plugins {
    alias(libs.plugins.movie.android.ui.feature)
}

android {
    namespace = "com.edu.feature.profile.presentation"

}

dependencies {
    implementation(projects.feature.profile.domain)
    implementation(projects.core.domain)
}