package io.github.seiko.ktorfit.kcp.ir

import io.github.seiko.ktorfit.kcp.KtorfitIrContext
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.ir.IrElement
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irCall
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irGetObject
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrSimpleFunction
import org.jetbrains.kotlin.ir.declarations.IrValueParameter
import org.jetbrains.kotlin.ir.symbols.IrSimpleFunctionSymbol
import org.jetbrains.kotlin.ir.types.isClassType
import org.jetbrains.kotlin.ir.util.dump
import org.jetbrains.kotlin.ir.util.getPropertyGetter
import org.jetbrains.kotlin.ir.util.getSimpleFunction
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.packageFqName
import org.jetbrains.kotlin.ir.util.primaryConstructor
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.FqNameUnsafe

internal class CreateClientIrElementTransformer(
  private val context: KtorfitIrContext,
) : IrElementTransformerVoidWithContext() {

  private val pluginContext get() = context.pluginContext
  private val logger get() = context.logger

  override fun visitClassNew(declaration: IrClass): IrStatement {
    val transformed = super.visitClassNew(declaration)

    if (!declaration.hasAnnotation(FqName(GENERATE_API_NAME))) {
      return transformed
    }

    val implIrClassId = ClassId(
      FqName(declaration.packageFqName!!.asString()),
      FqName("_${declaration.name.asString()}Impl"),
      false,
    )
    val implIrClassSymbol = pluginContext.referenceClass(implIrClassId)
      ?: error("Network request proxy class generated for ${declaration.name.asString()} not found. " +
        "Please check if KSP is correctly imported.")

    declaration.declarations.asSequence()
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
          && implFunc.valueParameters.firstOrNull()?.type?.isClassType(FqNameUnsafe(HTTP_CLIENT_NAME), false) == true
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
              irCall(getClientFunction(declaration)).apply { dispatchReceiver = irGet(pair.first.dispatchReceiverParameter!!) }
            )
          })
        }.logDump()
      }

    return declaration
  }

  private fun getClientFunction(irClass: IrClass): IrSimpleFunctionSymbol {
    val clientField = irClass.primaryConstructor!!.valueParameters.first {
      it.type.isClassType(FqNameUnsafe(HTTP_CLIENT_NAME), false)
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

  private fun <T : IrElement> T.logDump(): T {
    logger.w { dump() }
    return this
  }

  companion object {
    private const val GENERATE_API_NAME = "io.github.seiko.ktorfit.annotation.generator.GenerateApi"
    private const val HTTP_CLIENT_NAME = "io.ktor.client.HttpClient"
  }
}
