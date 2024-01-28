package io.github.seiko.ktorfit.kcp

import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.name.Name
import org.jetbrains.kotlin.name.SpecialNames

object KtorfitNames {
  // FqName
  val GENERATE_API_NAME = FqName("io.github.seiko.ktorfit.annotation.generator.GenerateApi")
  val HTTP_CLIENT_NAME = FqName("io.ktor.client.HttpClient")

  val DEFAULT_COMPANION get() = SpecialNames.DEFAULT_NAME_FOR_COMPANION_OBJECT
  val CREATE_METHOD = Name.identifier("create")
  val CLIENT_NAME = Name.identifier("client")
}
