package io.github.seiko.ktorfit.kcp.ir

import org.jetbrains.kotlin.backend.common.extensions.IrGenerationExtension
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.builders.*
import org.jetbrains.kotlin.ir.declarations.*
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.isClassType
import org.jetbrains.kotlin.ir.util.*
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName

/**
 * .
 *
 * @author 985892345
 * 2023/12/30 23:37
 */
class KtorfitIrGenerationExtension(
  val logger: MessageCollector
) : IrGenerationExtension {

  private val generateApiFqName = FqName("io.github.seiko.ktorfit.annotation.generator.GenerateApi")
  private val clientFqName = FqName("io.ktor.client.HttpClient")

  override fun generate(moduleFragment: IrModuleFragment, pluginContext: IrPluginContext) {
    moduleFragment.files.asSequence()
      .map { it.declarations.filterIsInstance<IrClass>() }
      .flatten()
      .filter { it.hasAnnotation(generateApiFqName) }
      .forEach {
        transformClass(pluginContext, it)
      }
  }

  private fun transformClass(pluginContext: IrPluginContext, irClass: IrClass) {
    val implIrClassId =
      ClassId(FqName(irClass.packageFqName!!.asString()), FqName("_${irClass.name.asString()}Impl"), false)
    val implIrClassSymbol = pluginContext.referenceClass(implIrClassId)
      ?: error("Network request proxy class generated for ${irClass.name.asString()} not found. " +
          "Please check if KSP is correctly imported.")
    // test
    irClass.declarations.asSequence()
      .filterIsInstance<IrSimpleFunction>()
      .first { it.name.asString() == "dumpTest" }
      .logDump()

    irClass.declarations.asSequence()
      .filterIsInstance<IrSimpleFunction>()
      .filter { function ->
        function.name.asString()
          .let {
            it != "toString" && it != "hashCode" && it != "equals"
          }
      }
      .mapNotNull { function ->
        val implFunc = implIrClassSymbol.getSimpleFunction(function.name.asString())?.owner ?: return@mapNotNull null
        if (implFunc.returnType == function.returnType
          && implFunc.valueParameters.firstOrNull()?.type?.isClassType(clientFqName.toUnsafe(), false) == true
          && compareValueParameters(implFunc.valueParameters.drop(1), function.valueParameters)) {
          function to implFunc
        } else null
      }
      .forEach { pair ->
        pair.first.body = DeclarationIrBuilder(pluginContext, pair.first.symbol).irBlockBody {
          +irReturn(irCall(pair.second).also { expression ->
            expression.dispatchReceiver = irGetObject(implIrClassSymbol)
            expression.putValueArgument(
              0,
              irCall(getClientFunction(irClass)).apply { dispatchReceiver = irGet(pair.first.dispatchReceiverParameter!!) }
            )
          })
        }.logDump()
      }
  }

  private fun getClientFunction(irClass: IrClass): IrSimpleFunctionSymbol {
    val clientField = irClass.primaryConstructor!!.valueParameters.first {
      it.type.isClassType(clientFqName.toUnsafe(), false)
    }
    return irClass.getPropertyGetter(clientField.name.asString())!!
  }

  private fun compareValueParameters(parameter1: List<IrValueParameter>, parameter2: List<IrValueParameter>): Boolean {
    if (parameter1.size != parameter2.size) return false
    for (i in parameter1.indices) {
      val p1 = parameter1[i]
      val p2 = parameter2[i]
      if (p1.name != p2.name || p1.type != p2.type) return false
    }
    return true
  }

  private fun Any?.log() {
    logger.report(CompilerMessageSeverity.WARNING, toString())
  }

  private fun <T : IrElement> T.logDump(): T {
    dump().log()
    return this
  }
}