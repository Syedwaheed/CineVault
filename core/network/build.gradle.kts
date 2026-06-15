plugins {
    alias(libs.plugins.movie.jvm.library)
    alias(libs.plugins.movie.jvm.ktor)
}

dependencies {
    implementation(libs.koin.core)
    implementation(projects.core.domain)
}
