package io.github.seiko.ktorfit.kcp

import io.github.seiko.ktorfit.kcp.ir.KtorfitIrGenerationExtension
import io.github.seiko.ktorfit.kcp.k1.registerK1Extensions
import io.github.seiko.ktorfit.kcp.k2.registerK2Extensions
import io.github.seiko.ktorfit.kcp.utils.KtorfitLogger
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.compiler.plugin.CompilerPluginRegistrar
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import org.jetbrains.kotlin.config.CompilerConfiguration

/**
 * .
 *
 * @author 985892345
 * 2023/12/30 23:36
 */
@OptIn(ExperimentalCompilerApi::class)
class KtorfitPluginRegistrar : CompilerPluginRegistrar() {
  override val supportsK2: Boolean
    get() = true

  override fun ExtensionStorage.registerExtensions(configuration: CompilerConfiguration) {
    val context = KtorfitBaseContext(
      logger = KtorfitLogger(configuration),
    )
    registerK1Extensions(context)
    registerK2Extensions(context)
    IrGenerationExtension.registerExtension(KtorfitIrGenerationExtension(context))
  }
}
