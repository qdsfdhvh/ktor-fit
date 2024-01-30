package io.github.seiko.ktorfit.kcp.k2.resolve

import io.github.seiko.ktorfit.kcp.KtorfitBaseContext
import io.github.seiko.ktorfit.kcp.KtorfitNames
import org.jetbrains.kotlin.GeneratedDeclarationKey
import org.jetbrains.kotlin.descriptors.ClassKind
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.getContainingClassSymbol
import org.jetbrains.kotlin.fir.analysis.checkers.getContainingDeclarationSymbol
import org.jetbrains.kotlin.fir.declarations.FirDeclarationOrigin
import org.jetbrains.kotlin.fir.declarations.utils.isInterface
import org.jetbrains.kotlin.fir.extensions.FirDeclarationGenerationExtension
import org.jetbrains.kotlin.fir.extensions.FirDeclarationPredicateRegistrar
import org.jetbrains.kotlin.fir.extensions.MemberGenerationContext
import org.jetbrains.kotlin.fir.extensions.NestedClassGenerationContext
import org.jetbrains.kotlin.fir.extensions.predicate.LookupPredicate
import org.jetbrains.kotlin.fir.extensions.predicateBasedProvider
import org.jetbrains.kotlin.fir.plugin.createCompanionObject
import org.jetbrains.kotlin.fir.plugin.createConeType
import org.jetbrains.kotlin.fir.plugin.createDefaultPrivateConstructor
import org.jetbrains.kotlin.fir.plugin.createMemberFunction
import org.jetbrains.kotlin.fir.symbols.impl.FirClassLikeSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirClassSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirConstructorSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirNamedFunctionSymbol
import org.jetbrains.kotlin.fir.symbols.impl.FirRegularClassSymbol
import org.jetbrains.kotlin.fir.types.constructStarProjectedType
import org.jetbrains.kotlin.name.CallableId
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames

// https://github.com/JetBrains/kotlin/blob/master/plugins/fir-plugin-prototype/src/org/jetbrains/kotlin/fir/plugin/generators/CompanionGenerator.kt
internal class CreateFirResolveExtension(
  session: FirSession,
  private val baseContext: KtorfitBaseContext,
) : FirDeclarationGenerationExtension(session) {

  private val logger get() = baseContext.logger

  companion object {
    private val PREDICATE = LookupPredicate.create {
      annotated(KtorfitNames.GENERATE_API_NAME)
    }
  }

  override fun FirDeclarationPredicateRegistrar.registerPredicates() {
    register(PREDICATE)
  }

  override fun getNestedClassifiersNames(
    classSymbol: FirClassSymbol<*>,
    context: NestedClassGenerationContext,
  ): Set<Name> {
    return if (classSymbol.isInterface && session.predicateBasedProvider.matches(PREDICATE, classSymbol)) {
      // @GenerateApi always return companion object name
      setOf(SpecialNames.DEFAULT_NAME_FOR_COMPANION_OBJECT)
    } else {
      emptySet()
    }
  }

  override fun generateNestedClassLikeDeclaration(
    owner: FirClassSymbol<*>,
    name: Name,
    context: NestedClassGenerationContext,
  ): FirClassLikeSymbol<*>? {
    if (name != SpecialNames.DEFAULT_NAME_FOR_COMPANION_OBJECT) return null
    if (owner !is FirRegularClassSymbol) return null
    // Only generate new companion if class does not have one already
    if (owner.companionObjectSymbol != null) return null
    logger.i { "k2: create companion object in ${owner.name}" }
    return createCompanionObject(owner, Key).symbol
  }

  override fun getCallableNamesForClass(
    classSymbol: FirClassSymbol<*>,
    context: MemberGenerationContext,
  ): Set<Name> {
    if (classSymbol.classKind != ClassKind.OBJECT) return emptySet()
    if (classSymbol !is FirRegularClassSymbol) return emptySet()
    val classId = classSymbol.classId
    if (!classId.isNestedClass ||
      classId.shortClassName != SpecialNames.DEFAULT_NAME_FOR_COMPANION_OBJECT
    ) {
      return emptySet()
    }
    val origin = classSymbol.origin as? FirDeclarationOrigin.Plugin
    logger.i { "k2: add create function in ${classSymbol.getContainingDeclarationSymbol(session)?.name}.Companion" }
    return if (origin?.key == Key) {
      setOf(KtorfitNames.CREATE_METHOD, SpecialNames.INIT)
    } else {
      setOf(KtorfitNames.CREATE_METHOD)
    }
  }

  override fun generateConstructors(context: MemberGenerationContext): List<FirConstructorSymbol> {
    val constructor = createDefaultPrivateConstructor(context.owner, Key)
    return listOf(constructor.symbol)
  }

  override fun generateFunctions(
    callableId: CallableId,
    context: MemberGenerationContext?,
  ): List<FirNamedFunctionSymbol> {
    val owner = context?.owner ?: return emptyList()

    val ownerKey = (owner.origin as? FirDeclarationOrigin.Plugin)?.key ?: return emptyList()
    if (ownerKey != Key) return emptyList()

    if (callableId.callableName != KtorfitNames.CREATE_METHOD) return emptyList()

    val interfaceOwner = owner.getContainingClassSymbol(session) as? FirClassSymbol<*> ?: return emptyList()

    logger.i { "k2: generate create body in ${interfaceOwner.name}.Companion" }
    val function = createMemberFunction(
      owner,
      Key,
      callableId.callableName,
      interfaceOwner.constructStarProjectedType(),
    ) {
      valueParameter(
        KtorfitNames.CLIENT_NAME,
        ClassId.topLevel(KtorfitNames.HTTP_CLIENT_NAME).createConeType(session),
      )
    }
    return listOf(function.symbol)
  }

  object Key : GeneratedDeclarationKey() {
    override fun toString(): String {
      return "KtorfitGeneratorKey"
    }
  }
}
