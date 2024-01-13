package io.github.seiko.ktorfit.kcp

import io.github.seiko.ktorfit.kcp.ir.CreateClientIrElementTransformer
import io.github.seiko.ktorfit.kcp.util.Logger
import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.isClassType
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.ir.visitors.transformChildrenVoid
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName

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
        )
      )
    )
  }
}
