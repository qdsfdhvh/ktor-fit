package io.github.seiko.ktorfit.kcp.ir

import io.github.seiko.ktorfit.kcp.KtorfitNames
import io.github.seiko.ktorfit.kcp.KtorfitNames.HTTP_CLIENT_NAME
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.lower.DeclarationIrBuilder
import org.jetbrains.kotlin.backend.common.lower.irThrow
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.builders.irBlockBody
import org.jetbrains.kotlin.ir.builders.irCallConstructor
import org.jetbrains.kotlin.ir.builders.irGet
import org.jetbrains.kotlin.ir.builders.irReturn
import org.jetbrains.kotlin.ir.builders.irString
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrFunction
import org.jetbrains.kotlin.ir.symbols.IrClassSymbol
import org.jetbrains.kotlin.ir.types.classifierOrNull
import org.jetbrains.kotlin.ir.types.isClassType
import org.jetbrains.kotlin.ir.types.isNullableString
import org.jetbrains.kotlin.ir.util.classId
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

  private val waitToDoMap = mutableMapOf<ClassId, WaitToDo>()

  override fun visitClassNew(declaration: IrClass): IrStatement {
    val transformed = super.visitClassNew(declaration)

    val classId = declaration.classId
    if (classId != null && waitToDoMap.containsKey(classId)) {
      logger.i { "visit impl class generated for ${classId.asFqNameString()}, generate real body." }
      val waitToDo = waitToDoMap.remove(classId)!!
      createApiFunctionBody(
        irClass = waitToDo.irClass,
        createApiIrFunction = waitToDo.createApiIrFunction,
        implIrClassId = classId,
        implIrClassIdSymbol = declaration.symbol,
      )
      return declaration
    }

    if (!declaration.isInterface) return transformed
    if (!declaration.hasAnnotation(KtorfitNames.GENERATE_API_NAME)) return transformed

    val companionObject = findCompanionObject(declaration)
    if (companionObject == null) {
      logger.w { "no find companion object in ${declaration.packageFqName?.asString()}.${declaration.name.asString()}" }
      return transformed
    }

    val createApiIrFunction = findCreateApiFunction(companionObject, declaration.symbol)
    if (createApiIrFunction == null) {
      logger.w { "no find create api function in ${declaration.packageFqName?.asString()}.${declaration.name.asString()}" }
      return transformed
    }

    tryCreateApiFunctionBody(
      irClass = declaration,
      createApiIrFunction = createApiIrFunction,
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

  private fun tryCreateApiFunctionBody(
    irClass: IrClass,
    createApiIrFunction: IrFunction,
  ) {
    val implIrClassId = ClassId(
      requireNotNull(irClass.packageFqName),
      Name.identifier("_${irClass.name.asString()}Impl"),
    )
    createApiFunctionBody(
      irClass = irClass,
      createApiIrFunction = createApiIrFunction,
      implIrClassId = implIrClassId,
      implIrClassIdSymbol = pluginContext.referenceClass(implIrClassId),
    )
  }

  private fun createApiFunctionBody(
    irClass: IrClass,
    createApiIrFunction: IrFunction,
    implIrClassId: ClassId,
    implIrClassIdSymbol: IrClassSymbol?,
  ) {
    // find client parameter in function
    val clientParameter = createApiIrFunction.valueParameters.firstOrNull {
      it.type.isClassType(HTTP_CLIENT_NAME.toUnsafe(), false)
    } ?: error(
      "can't find ${HTTP_CLIENT_NAME.asString()} in " +
        "${irClass.packageFqName?.asString()}.${irClass.name.asString()}" +
        "#${createApiIrFunction.name.asString()} function",
    )

    // val implIrClassIdSymbol = pluginContext.referenceClass(implIrClassId)

    createApiIrFunction.body = DeclarationIrBuilder(pluginContext, createApiIrFunction.symbol).irBlockBody {
      if (implIrClassIdSymbol != null) {
        +irReturn(
          irCallConstructor(
            implIrClassIdSymbol.constructors.single(),
            typeArguments = listOf(),
          ).also { expression ->
            expression.putValueArgument(0, irGet(clientParameter))
          },
        )
      } else {
        val constructor = context.irBuiltIns.throwableClass.owner.constructors
          .firstOrNull {
            it.valueParameters.size == 1 && it.valueParameters.first().type.isNullableString()
          } ?: error("can't find throwable constructor")

        +irThrow(
          irCallConstructor(
            constructor.symbol,
            typeArguments = listOf(),
          ).apply {
            putValueArgument(
              0,
              irString(
                "Network request impl class generated for ${implIrClassId.asFqNameString()} not found. " +
                  "Please check if KSP is correctly imported.",
              ),
            )
          },
        )

        // in k2, we might can't get impl class, put it in wait to do map until visit impl class
        logger.i { "not find impl class generated for ${implIrClassId.asFqNameString()}, put in todo map." }
        waitToDoMap[implIrClassId] = WaitToDo(
          irClass = irClass,
          createApiIrFunction = createApiIrFunction,
        )
      }
    }
  }

  private data class WaitToDo(
    val irClass: IrClass,
    val createApiIrFunction: IrFunction,
  )
}
