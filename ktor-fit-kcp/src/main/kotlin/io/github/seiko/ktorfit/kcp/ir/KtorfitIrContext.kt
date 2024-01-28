package io.github.seiko.ktorfit.kcp.ir

import io.github.seiko.ktorfit.kcp.KtorfitBaseContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext

internal data class KtorfitIrContext(
  val baseContext: KtorfitBaseContext,
  val pluginContext: IrPluginContext,
)
