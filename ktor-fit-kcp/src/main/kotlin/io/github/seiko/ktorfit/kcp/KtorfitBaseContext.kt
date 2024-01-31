package io.github.seiko.ktorfit.kcp

import io.github.seiko.ktorfit.kcp.utils.KtorfitLogger

data class KtorfitBaseContext internal constructor(
  val logger: KtorfitLogger,
) {
  companion object {
    val EMPTY = KtorfitBaseContext(
      logger = KtorfitLogger(null),
    )
  }
}
