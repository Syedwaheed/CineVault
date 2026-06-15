plugins {
    alias(libs.plugins.movie.android.ui.feature)
}

android {
    namespace = "com.edu.feature.detail.presentation"


}

dependencies {
    implementation(projects.feature.detail.domain)
    implementation(projects.core.domain)
    implementation(libs.youtube.player)
}