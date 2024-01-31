package io.github.seiko.ktorfit.kcp.k1.checker

import io.github.seiko.ktorfit.kcp.KtorfitBaseContext
import org.jetbrains.kotlin.container.StorageComponentContainer
import org.jetbrains.kotlin.container.useInstance
import org.jetbrains.kotlin.descriptors.ModuleDescriptor
import org.jetbrains.kotlin.extensions.StorageComponentContainerContributor
import org.jetbrains.kotlin.platform.TargetPlatform

internal class KtorfitComponentContainerContributor(
  private val baseContext: KtorfitBaseContext,
) : StorageComponentContainerContributor {
  override fun registerModuleComponents(
    container: StorageComponentContainer,
    platform: TargetPlatform,
    moduleDescriptor: ModuleDescriptor,
  ) {
    container.useInstance(KtorfitClassChecker())
  }
}
