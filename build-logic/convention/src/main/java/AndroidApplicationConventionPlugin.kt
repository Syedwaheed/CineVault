import com.android.build.api.dsl.ApplicationExtension
import com.edu.convention.ExtensionType
import com.edu.convention.configureBuildType
import com.edu.convention.configureKotlinAndroid
import com.edu.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            with(pluginManager) {
                apply("com.android.application")
                apply(("org.jetbrains.kotlin.android"))
                apply("com.google.gms.google-services")
            }

            extensions.configure<ApplicationExtension> {
                defaultConfig {
                    applicationId = libs.findVersion("projectApplicationId").get().toString()
                    targetSdk = libs.findVersion("projectTargetSdk").get().toString().toInt()
                    versionCode = libs.findVersion("projectVersionCode").get().toString().toInt()
                    versionName = libs.findVersion("projectVersionName").get().toString()
                }
                configureKotlinAndroid(this)
                configureBuildType(
                    commonExtension = this,
                    extensionType = ExtensionType.APPLICATION
                )
            }


        }
    }
}
