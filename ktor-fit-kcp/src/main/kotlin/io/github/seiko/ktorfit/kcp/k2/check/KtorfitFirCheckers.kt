package io.github.seiko.ktorfit.kcp.k2.check

import io.github.seiko.ktorfit.kcp.KtorfitBaseContext
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.DeclarationCheckers
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirRegularClassChecker
import org.jetbrains.kotlin.fir.analysis.extensions.FirAdditionalCheckersExtension

internal class KtorfitFirCheckers(
  session: FirSession,
  private val baseContext: KtorfitBaseContext,
) : FirAdditionalCheckersExtension(session) {
  override val declarationCheckers: DeclarationCheckers = object : DeclarationCheckers() {
    override val regularClassCheckers: Set<FirRegularClassChecker> = setOf(
      KtorfitFirClassChecker(session),
    )
  }
}
