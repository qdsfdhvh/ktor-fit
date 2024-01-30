package io.github.seiko.ktorfit.kcp.k2.check

import io.github.seiko.ktorfit.kcp.KtorfitNames
import org.jetbrains.kotlin.descriptors.isClass
import org.jetbrains.kotlin.descriptors.isInterface
import org.jetbrains.kotlin.diagnostics.DiagnosticReporter
import org.jetbrains.kotlin.diagnostics.reportOn
import org.jetbrains.kotlin.fir.FirSession
import org.jetbrains.kotlin.fir.analysis.checkers.context.CheckerContext
import org.jetbrains.kotlin.fir.analysis.checkers.declaration.FirClassChecker
import org.jetbrains.kotlin.fir.declarations.FirClass
import org.jetbrains.kotlin.fir.declarations.utils.isExpect
import org.jetbrains.kotlin.fir.extensions.predicate.LookupPredicate
import org.jetbrains.kotlin.fir.extensions.predicateBasedProvider

internal class KtorfitFirClassChecker(
  private val session: FirSession,
) : FirClassChecker() {

  companion object {
    private val PREDICATE = LookupPredicate.create {
      annotated(KtorfitNames.GENERATE_API_NAME)
    }
  }

  override fun check(
    declaration: FirClass,
    context: CheckerContext,
    reporter: DiagnosticReporter,
  ) {
    if (!session.predicateBasedProvider.matches(PREDICATE, declaration.symbol)) return
    checkGenerateApiIsInterface(declaration, context, reporter)
  }

  private fun checkGenerateApiIsInterface(
    declaration: FirClass,
    context: CheckerContext,
    reporter: DiagnosticReporter,
  ) {
    if (declaration.classKind.isInterface) return
    if (declaration.classKind.isClass && declaration.isExpect) return
    reporter.reportOn(
      declaration.source,
      KtorfitK2Errors.GENERATE_API_MUST_BE_INTERFACE_OR_EXPECT_CLASS,
      context,
    )
  }
}
