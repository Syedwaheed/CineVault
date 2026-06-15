package com.edu.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureBuildType(
    commonExtension: CommonExtension<*, *, *, *, *,*>,
    extensionType: ExtensionType
){
    commonExtension.run {

        buildFeatures{
            buildConfig = true
        }
        val apiReadAccessToken = gradleLocalProperties(rootDir,providers).getProperty("API_READ_ACCESS_TOKEN")
        when(extensionType){
            ExtensionType.APPLICATION ->{
                extensions.configure<ApplicationExtension>{

                    buildTypes{
                        debug {
                            configureDebugBuildType(apiReadAccessToken)
                        }
                        release {
                            configureReleaseBuildType(commonExtension,apiReadAccessToken)
                        }
                    }
                }
            }
            ExtensionType.LIBRARY -> {
                extensions.configure<LibraryExtension>{

                    buildTypes{
                        debug {
                            configureDebugBuildType(apiReadAccessToken)
                        }
                        release {
                            configureReleaseBuildType(commonExtension,apiReadAccessToken)
                        }
                    }
                }
            }
        }

    }
}

private fun BuildType.configureDebugBuildType(apiReadAccessToken:String?){
    buildConfigField("String","API_READ_ACCESS_TOKEN","\"$apiReadAccessToken\"")
    buildConfigField("String","BASE_URL","\"https://api.themoviedb.org/3/\"")
    buildConfigField("String","IMAGE_BASE_URL","\"https://image.tmdb.org/t/p/\"")
}
private fun BuildType.configureReleaseBuildType(
    commonExtension: CommonExtension<*,*,*,*,*,*>,
    apiReadAccessToken:String?
){
    buildConfigField("String","API_READ_ACCESS_TOKEN","\"$apiReadAccessToken\"")
    buildConfigField("String","BASE_URL","\"https://api.themoviedb.org/3/\"")
    buildConfigField("String","IMAGE_BASE_URL","\"https://image.tmdb.org/t/p/\"")
    isMinifyEnabled = true
    proguardFiles(
        commonExtension.getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
    )
}