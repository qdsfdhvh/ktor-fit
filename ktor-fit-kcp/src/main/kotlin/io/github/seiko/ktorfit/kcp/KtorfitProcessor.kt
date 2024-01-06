package io.github.seiko.ktorfit.kcp

import org.jetbrains.kotlin.compiler.plugin.AbstractCliOption
import org.jetbrains.kotlin.compiler.plugin.CommandLineProcessor
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi

/**
 * .
 *
 * @author 985892345
 * 2023/12/30 23:23
 */
@OptIn(ExperimentalCompilerApi::class)
class KtorfitProcessor : CommandLineProcessor {

  override val pluginId: String = "ktor-fit-kcp"

  override val pluginOptions: Collection<AbstractCliOption> = listOf()
}