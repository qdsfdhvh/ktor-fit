package io.github.seiko.ktorfit.kcp.ir

import io.github.seiko.ktorfit.kcp.KtorfitNames
import io.github.seiko.ktorfit.kcp.KtorfitNames.HTTP_CLIENT_NAME
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irCallConstructor
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.types.classifierOrNull
import org.jetbrains.kotlin.ir.types.isClassType
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.isInterface
import org.jetbrains.kotlin.ir.util.packageFqName
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name

internal class CreateClientIrElementTransformer(
  private val context: KtorfitIrContext,
) : IrElementTransformerVoidWithContext() {

  private val pluginContext get() = context.pluginContext
  private val logger get() = context.baseContext.logger

  override fun visitClassNew(declaration: IrClass): IrStatement {
    val transformed = super.visitClassNew(declaration)
    if (!declaration.isInterface) return transformed
    if (!declaration.hasAnnotation(KtorfitNames.GENERATE_API_NAME)) return transformed

    val companionObject = findCompanionObject(declaration)
    if (companionObject == null) {
      logger.w { "no find companion object in ${declaration.packageFqName?.asString()}.${declaration.name.asString()}" }
      return transformed
    }

    val createApiFunction = findCreateApiFunction(companionObject, declaration.symbol)
    if (createApiFunction == null) {
      logger.w { "no find create api function in ${declaration.packageFqName?.asString()}.${declaration.name.asString()}" }
      return transformed
    }

    createApiFunctionBody(
      irClassDeclaration = declaration,
      createApiFunction = createApiFunction,
    )
    return declaration
  }

  private fun findCompanionObject(interfaceClass: IrClass): IrClass? {
    for (declaration in interfaceClass.declarations) {
      if (declaration is IrClass && declaration.isCompanion) {
        return declaration
      }
    }
    return null
  }

  private fun findCreateApiFunction(
    companionObject: IrClass,
    returnSymbol: IrClassSymbol,
  ): IrFunction? {
    return companionObject.declarations.asSequence()
      .filterIsInstance<IrFunction>()
      .filter { function ->
        function.name.asString()
          .let {
            it != "toString" && it != "hashCode" && it != "equals"
          }
      }
      .filter { function ->
        function.returnType.classifierOrNull == returnSymbol
      }.firstOrNull()
  }

  private fun createApiFunctionBody(
    irClassDeclaration: IrClass,
    createApiFunction: IrFunction,
  ) {
    val implIrClassIdSymbol = requireImplIrClassSymbol(irClassDeclaration)

    // find client parameter in function
    val clientParameter = createApiFunction.valueParameters.firstOrNull {
      it.type.isClassType(HTTP_CLIENT_NAME.toUnsafe(), false)
    } ?: error(
      "can't find ${HTTP_CLIENT_NAME.asString()} in " +
        "${irClassDeclaration.packageFqName?.asString()}.${irClassDeclaration.name.asString()}" +
        "#${createApiFunction.name.asString()} function",
    )

    createApiFunction.body = DeclarationIrBuilder(pluginContext, createApiFunction.symbol).irBlockBody {
      +irReturn(
        irCallConstructor(
          implIrClassIdSymbol.constructors.single(),
          typeArguments = listOf(),
        ).also { expression ->
          expression.putValueArgument(0, irGet(clientParameter))
        },
      )
    }
  }

  private fun requireImplIrClassSymbol(declaration: IrClass): IrClassSymbol {
    val implIrClassId = ClassId(
      requireNotNull(declaration.packageFqName),
      Name.identifier("_${declaration.name.asString()}Impl"),
    )
    // TODO can't find class in k2
    return pluginContext.referenceClass(implIrClassId)
      ?: error(
        "Network request proxy class generated for ${implIrClassId.asString()} not found. " +
          "Please check if KSP is correctly imported.",
      )
  }
}
