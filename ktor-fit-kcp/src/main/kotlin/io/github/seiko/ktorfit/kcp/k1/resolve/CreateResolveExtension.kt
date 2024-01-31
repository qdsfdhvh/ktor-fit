package io.github.seiko.ktorfit.kcp.k1.resolve

import io.github.seiko.ktorfit.kcp.KtorfitBaseContext
import io.github.seiko.ktorfit.kcp.KtorfitNames
import org.jetbrains.kotlin.descriptors.CallableMemberDescriptor
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.descriptors.DescriptorVisibilities
import org.jetbrains.kotlin.descriptors.Modality
import org.jetbrains.kotlin.descriptors.SimpleFunctionDescriptor
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.descriptors.findClassAcrossModuleDependencies
import org.jetbrains.kotlin.descriptors.impl.SimpleFunctionDescriptorImpl
import org.jetbrains.kotlin.descriptors.impl.ValueParameterDescriptorImpl
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.descriptorUtil.module
import org.jetbrains.kotlin.resolve.descriptorUtil.parents
import org.jetbrains.kotlin.resolve.extensions.SyntheticResolveExtension
import org.jetbrains.kotlin.types.KotlinTypeFactory
import org.jetbrains.kotlin.types.TypeAttributes

open class CreateResolveExtension(
  private val baseContext: KtorfitBaseContext = KtorfitBaseContext.EMPTY,
) : SyntheticResolveExtension {

  private val logger get() = baseContext.logger

  override fun getSyntheticCompanionObjectNameIfNeeded(thisDescriptor: ClassDescriptor): Name? {
    return if (thisDescriptor.isGenerateApiInterface) {
      // @GenerateApi always return companion object name
      SpecialNames.DEFAULT_NAME_FOR_COMPANION_OBJECT
    } else {
      null
    }
  }

  override fun getSyntheticFunctionNames(thisDescriptor: ClassDescriptor): List<Name> {
    return if (thisDescriptor.isGenerateApiCompanion) {
      logger.i { "k1: add create function in ${thisDescriptor.containingDeclaration.name}.Companion" }
      listOf(KtorfitNames.CREATE_METHOD)
    } else {
      emptyList()
    }
  }

  override fun generateSyntheticMethods(
    thisDescriptor: ClassDescriptor,
    name: Name,
    bindingContext: BindingContext,
    fromSupertypes: List<SimpleFunctionDescriptor>,
    result: MutableCollection<SimpleFunctionDescriptor>,
  ) {
    if (name != KtorfitNames.CREATE_METHOD) return

    val generateApiInterfaceDescriptor =
      getGenerateApiInterfaceDescriptorByCompanion(thisDescriptor)
    if (generateApiInterfaceDescriptor == null) {
      super.generateSyntheticMethods(thisDescriptor, name, bindingContext, fromSupertypes, result)
      return
    }

    logger.i { "k1: generate create body in ${thisDescriptor.name}.Companion" }
    result.add(createGenerateApiGetterDescriptor(thisDescriptor, generateApiInterfaceDescriptor))
  }

  private fun createGenerateApiGetterDescriptor(
    companionClass: ClassDescriptor,
    generateApiInterface: ClassDescriptor,
  ): SimpleFunctionDescriptor {
    return SimpleFunctionDescriptorImpl.create(
      companionClass,
      Annotations.EMPTY,
      KtorfitNames.CREATE_METHOD,
      CallableMemberDescriptor.Kind.SYNTHESIZED,
      companionClass.source,
    ).apply {
      val returnType = KotlinTypeFactory.simpleNotNullType(
        TypeAttributes.Empty,
        generateApiInterface,
        emptyList(),
      )

      val httpClientClass = generateApiInterface.module.findClassAcrossModuleDependencies(
        ClassId.topLevel(KtorfitNames.HTTP_CLIENT_NAME),
      ) ?: error("can't find ${KtorfitNames.HTTP_CLIENT_NAME}, is ktor by dependency?")

      @Suppress("DEPRECATION")
      initialize(
        null,
        companionClass.thisAsReceiverParameter,
        emptyList(),
        listOf(
          ValueParameterDescriptorImpl(
            containingDeclaration = this,
            original = null,
            index = 0,
            annotations = Annotations.EMPTY,
            name = KtorfitNames.CLIENT_NAME,
            outType = httpClientClass.defaultType,
            declaresDefaultValue = false,
            isCrossinline = false,
            isNoinline = false,
            varargElementType = null,
            source = this.source,
          ),
        ),
        returnType,
        Modality.FINAL,
        DescriptorVisibilities.PUBLIC,
      )
    }
  }
}

private fun getGenerateApiInterfaceDescriptorByCompanion(companion: ClassDescriptor): ClassDescriptor? {
  if (!companion.isCompanionObject) return null
  return companion.parents.filterIsInstance<ClassDescriptor>()
    .firstOrNull { it.isGenerateApiInterface }
}

private val ClassDescriptor.isGenerateApiCompanion: Boolean
  get() = isCompanionObject && (containingDeclaration as ClassDescriptor).isGenerateApiInterface

private val ClassDescriptor.isGenerateApiInterface: Boolean
  get() = isInterface && annotations.hasGenerateApi

private val ClassDescriptor.isInterface: Boolean
  get() = kind == ClassKind.INTERFACE

private val Annotations.hasGenerateApi: Boolean
  get() = hasAnnotation(KtorfitNames.GENERATE_API_NAME)
