import com.google.firebase.appdistribution.gradle.firebaseAppDistribution

plugins {
    alias(libs.plugins.movie.android.application.compose)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.movie.jvm.ktor)
    alias(libs.plugins.firebase.distribution)
}

android {
    namespace = "com.edu.movieapplication"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

firebaseAppDistributionDefault {
    releaseNotes = System.getenv("FIREBASE_RELEASE_NOTES") ?: "Local build"
    groups = System.getenv("FIREBASE_TESTER_GROUPS") ?: "internal-tester"
}

dependencies {
    //Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    //Coil
    implementation(libs.coil.compose)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)

    //Navigation 3
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    //KotlinxSerialization
    implementation(libs.kotlinx.serialization.json)

    // Koin DI with Compose support
    implementation(libs.bundles.koin.compose)

   //tink
    implementation(libs.androidx.google.tink)
    //DataStore
    implementation(libs.androidx.datastore.preference)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    //Splash Screen
    implementation(libs.androidx.core.splashscreen)

    //Timber
    implementation(libs.timber)

    //Firebase
    implementation(platform(libs.firebase.bom))


    implementation(projects.core.data)
    implementation(projects.core.database)
    implementation(projects.core.domain)
    implementation(projects.core.network)
    implementation(projects.core.presentation.ui)
    implementation(projects.core.presentation.designsystem)

    implementation(projects.feature.auth.data)
    implementation(projects.feature.auth.domain)
    implementation(projects.feature.auth.presentation)

    implementation(projects.feature.detail.data)
    implementation(projects.feature.detail.domain)
    implementation(projects.feature.detail.presentation)

    implementation(projects.feature.home.data)
    implementation(projects.feature.home.domain)
    implementation(projects.feature.home.presentation)

    implementation(projects.feature.profile.data)
    implementation(projects.feature.profile.domain)
    implementation(projects.feature.profile.presentation)

    implementation(projects.feature.search.data)
    implementation(projects.feature.search.domain)
    implementation(projects.feature.search.presentation)

    implementation(projects.feature.watchlist.data)
    implementation(projects.feature.watchlist.domain)
    implementation(projects.feature.watchlist.presentation)
}
//Testing the CI/CD