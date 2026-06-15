plugins {
    alias(libs.plugins.movie.android.ui.feature)
}

android {
    namespace = "com.edu.feature.home.presentation"


}

dependencies {
    implementation(projects.feature.home.domain)
    implementation(projects.core.domain)
}