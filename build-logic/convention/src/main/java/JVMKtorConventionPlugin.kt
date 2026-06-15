import com.edu.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class JVMKtorConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")
            dependencies{
                "implementation"(libs.findBundle("ktor").get())
            }
        }
    }
}