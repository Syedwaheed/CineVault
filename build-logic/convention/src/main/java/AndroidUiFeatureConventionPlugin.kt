import com.edu.convention.addUiLayerDependency
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidUiFeatureConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager){
                apply("movie.android.library.compose")
            }
            dependencies{
                addUiLayerDependency(target)
            }
        }
    }
}