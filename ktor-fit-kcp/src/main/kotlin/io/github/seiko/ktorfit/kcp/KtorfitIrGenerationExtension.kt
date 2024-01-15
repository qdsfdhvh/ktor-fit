package io.github.seiko.ktorfit.kcp

import io.github.seiko.ktorfit.kcp.ir.CreateClientIrElementTransformer
import io.github.seiko.ktorfit.kcp.util.Logger
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid

/**
 * .
 *
 * @author 985892345
 * 2023/12/30 23:37
 */
class KtorfitIrGenerationExtension(
  private val configuration: CompilerConfiguration,
) : IrGenerationExtension {

  override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
    moduleFragment.transformChildrenVoid(
      CreateClientIrElementTransformer(
        context = KtorfitIrContext(
          pluginContext = pluginContext,
          logger = Logger(configuration),
        ),
      ),
    )
  }
}
