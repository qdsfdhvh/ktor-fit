package io.github.seiko.ktorfit.kcp.utils

import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.config.CompilerConfiguration

internal class KtorfitLogger(configuration: CompilerConfiguration) {

  private val logger = configuration.get(
    CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY,
    MessageCollector.NONE,
  )

  fun i(message: () -> String) {
    logger.report(CompilerMessageSeverity.INFO, message())
  }

  fun w(message: () -> String) {
    logger.report(CompilerMessageSeverity.WARNING, message())
  }
}
