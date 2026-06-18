import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.edu.convention.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension
){
    when(commonExtension) {
        is ApplicationExtension -> {
            commonExtension.buildFeatures {
                compose = true
            }
        }
        is LibraryExtension -> {
            commonExtension.buildFeatures {
                compose = true
            }
        }
    }
    
    dependencies {
        val bom = libs.findLibrary("androidx.compose.bom").get()
        "implementation"(platform(bom))
        "androidTestImplementation"(platform(bom))
        "debugImplementation"(libs.findLibrary("androidx.compose.ui.tooling.preview").get())
    }
}