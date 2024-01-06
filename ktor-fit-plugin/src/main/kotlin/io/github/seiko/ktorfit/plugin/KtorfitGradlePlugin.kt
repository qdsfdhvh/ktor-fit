package io.github.seiko.ktorfit.plugin

import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilation
import org.jetbrains.kotlin.gradle.plugin.KotlinCompilerPluginSupportPlugin
import org.jetbrains.kotlin.gradle.plugin.SubpluginArtifact
import org.jetbrains.kotlin.gradle.plugin.SubpluginOption

/**
 * .
 *
 * @author 985892345
 * 2023/12/31 01:14
 */
class KtorfitGradlePlugin : KotlinCompilerPluginSupportPlugin {

  override fun apply(target: Project) {
    super.apply(target)
  }

  override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
    val project = kotlinCompilation.target.project
    return project.provider {
      mutableListOf()
    }
  }

  override fun getCompilerPluginId(): String {
    return "ktor-fit"
  }

  override fun getPluginArtifact(): SubpluginArtifact {
    return SubpluginArtifact(
      groupId = BuildConfig.GROUP,
      artifactId = "ktor-fit-kcp",
      version = BuildConfig.VERSION,
    )
  }

  override fun isApplicable(kotlinCompilation: KotlinCompilation<*>): Boolean {
    return true
  }
}