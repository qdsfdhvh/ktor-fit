package io.github.seiko.ktorfit.kcp.k2

import io.github.seiko.ktorfit.kcp.KtorfitBaseContext
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.extensions.FirDeclarationGenerationExtension
import org.jetbrains.kotlin.fir.extensions.MemberGenerationContext
import org.jetbrains.kotlin.fir.symbols.impl.FirNamedFunctionSymbol
import org.jetbrains.kotlin.name.CallableId

internal class CreateFirResolveExtension(
  session: FirSession,
  private val baseContext: KtorfitBaseContext,
) : FirDeclarationGenerationExtension(session) {
  // override fun generateFunctions(
  //   callableId: CallableId,
  //   context: MemberGenerationContext?,
  // ): List<FirNamedFunctionSymbol> {
  //   val owner = context?.owner ?: return emptyList()
  //   return super.generateFunctions(callableId, context)
  // }
}
