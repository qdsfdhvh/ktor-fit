package io.github.seiko.ktorfit.kcp.ir

import io.github.seiko.ktorfit.kcp.KtorfitBaseContext
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.declarations.IrModuleFragment
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid

/**
 * .
 *
 * @author 985892345
 * 2023/12/30 23:37
 */
internal class KtorfitIrGenerationExtension(
  private val baseContext: KtorfitBaseContext,
) : IrGenerationExtension {

  override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
    moduleFragment.transformChildrenVoid(
      CreateClientIrElementTransformer(
        context = KtorfitIrContext(
          pluginContext = pluginContext,
          baseContext = baseContext,
        ),
      ),
    )
  }
}
