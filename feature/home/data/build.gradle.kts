plugins {
    alias(libs.plugins.movie.android.library)
    alias(libs.plugins.movie.jvm.ktor)
}

android {
    namespace = "com.edu.feature.home.data"


}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.core.network)
    implementation(projects.core.database)

    implementation(projects.feature.home.domain)
}