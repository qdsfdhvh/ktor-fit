package io.github.seiko.ktorfit.kcp.k1

import io.github.seiko.ktorfit.kcp.KtorfitBaseContext
import io.github.seiko.ktorfit.kcp.k1.checker.KtorfitComponentContainerContributor
import io.github.seiko.ktorfit.kcp.k1.resolve.CreateResolveExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.extensions.StorageComponentContainerContributor
import org.jetbrains.kotlin.resolve.extensions.SyntheticResolveExtension

@OptIn(ExperimentalCompilerApi::class)
internal fun CompilerPluginRegistrar.ExtensionStorage.registerK1Extensions(
  context: KtorfitBaseContext,
) {
  StorageComponentContainerContributor.registerExtension(KtorfitComponentContainerContributor(context))
  SyntheticResolveExtension.registerExtension(CreateResolveExtension(context))
}
