plugins {
    alias(libs.plugins.movie.android.library)
    alias(libs.plugins.movie.jvm.ktor)
}

android {
    namespace = "com.edu.data"
}

dependencies {
    implementation(libs.timber)
    implementation(libs.bundles.koin)

    implementation(projects.core.domain)
    implementation(projects.core.database)
    implementation(projects.core.network)
    implementation(libs.androidx.google.tink)
    implementation(libs.androidx.datastore.preference)
}
