package io.github.seiko.ktorfit.kcp

import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name

object KtorfitNames {
  // FqName
  val GENERATE_API_NAME = FqName("io.github.seiko.ktorfit.annotation.generator.GenerateApi")
  val HTTP_CLIENT_NAME = FqName("io.ktor.client.HttpClient")

  // Name
  val CREATE_METHOD = Name.identifier("create")
  val CLIENT_NAME = Name.identifier("client")
}
