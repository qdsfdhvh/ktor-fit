package io.github.seiko.ktorfit

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import io.github.seiko.ktorfit.annotation.generator.GenerateApi

class KtorfitProcessor(environment: SymbolProcessorEnvironment) : SymbolProcessor {

  private val codeGenerator: CodeGenerator = environment.codeGenerator

  override fun process(resolver: Resolver): List<KSAnnotated> {
    val classVisitor = KtorfitClassVisitor(codeGenerator)
    resolver
      .getSymbolsWithAnnotation(GENERATE_API_ANNOTATION_NAME)
      .filterIsInstance<KSClassDeclaration>()
      .forEach { it.accept(classVisitor, Unit) }
    return emptyList()
  }

  companion object {
    private val GENERATE_API_ANNOTATION_NAME = requireNotNull(GenerateApi::class.qualifiedName) {
      "Can not get qualifiedName for GenerateApi"
    }
  }
}
