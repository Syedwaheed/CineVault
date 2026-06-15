plugins {
    alias(libs.plugins.movie.android.library)
    alias(libs.plugins.movie.android.room)
}

android {
    namespace = "com.edu.core.database"

}

dependencies {
    implementation(libs.bundles.koin)
    implementation(projects.core.domain)
}