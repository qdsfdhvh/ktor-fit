package io.github.seiko.ktorfit.kcp

import io.github.seiko.ktorfit.kcp.util.Logger
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext

internal data class KtorfitIrContext(
  val pluginContext: IrPluginContext,
  val logger: Logger,
)
