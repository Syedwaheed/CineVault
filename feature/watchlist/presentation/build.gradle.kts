plugins {
    alias(libs.plugins.movie.android.ui.feature)
}

android {
    namespace = "com.edu.feature.watchlist.presentation"

}

dependencies {
    implementation(projects.feature.watchlist.domain)
    implementation(projects.core.domain)
}