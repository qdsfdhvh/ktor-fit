package io.github.seiko.ktorfit.plugin

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

  override fun applyToCompilation(kotlinCompilation: KotlinCompilation<*>): Provider<List<SubpluginOption>> {
    return kotlinCompilation.target.project.provider { emptyList() }
  }

  override fun getCompilerPluginId(): String {
    return "io.github.seiko.ktorfit.kcp"
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
