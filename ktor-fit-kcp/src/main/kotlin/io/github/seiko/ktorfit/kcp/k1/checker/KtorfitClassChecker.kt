package io.github.seiko.ktorfit.kcp.k1.checker

import io.github.seiko.ktorfit.kcp.KtorfitNames
import org.jetbrains.kotlin.descriptors.ClassDescriptor
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.descriptors.annotations.Annotations
import org.jetbrains.kotlin.descriptors.isClass
import org.jetbrains.kotlin.descriptors.isInterface
import org.jetbrains.kotlin.psi.KtDeclaration
import org.jetbrains.kotlin.resolve.BindingTrace
import org.jetbrains.kotlin.resolve.checkers.DeclarationChecker
import org.jetbrains.kotlin.resolve.checkers.DeclarationCheckerContext

internal class KtorfitClassChecker : DeclarationChecker {
  override fun check(
    declaration: KtDeclaration,
    descriptor: DeclarationDescriptor,
    context: DeclarationCheckerContext,
  ) {
    if (descriptor !is ClassDescriptor) return
    if (!descriptor.annotations.hasGenerateApi) return
    checkGenerateApiIsInterface(declaration, descriptor, context.trace)
  }

  private fun checkGenerateApiIsInterface(
    declaration: KtDeclaration,
    descriptor: ClassDescriptor,
    trace: BindingTrace,
  ) {
    if (descriptor.kind.isInterface) return
    if (descriptor.kind.isClass) return
    trace.report(KtorfitK1Errors.GENERATE_API_MUST_BE_INTERFACE_OR_EXPECT_CLASS.on(declaration))
  }
}

private val Annotations.hasGenerateApi: Boolean
  get() = hasAnnotation(KtorfitNames.GENERATE_API_NAME)
