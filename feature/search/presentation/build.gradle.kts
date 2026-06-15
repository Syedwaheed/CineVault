plugins {
    alias(libs.plugins.movie.android.ui.feature)
}

android {
    namespace = "com.edu.feature.search.presentation"

}

dependencies {
    implementation(projects.feature.search.domain)
    implementation(projects.core.domain)
}