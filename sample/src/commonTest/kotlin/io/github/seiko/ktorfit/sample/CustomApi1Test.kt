package io.github.seiko.ktorfit.sample

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CustomApi1Test {

  @Test
  fun test() = runTest {
    val mockEngine = MockEngine { _ ->
      respond(
        content = ByteReadChannel("success"),
        status = HttpStatusCode.OK,
      )
    }
    val client = HttpClient(mockEngine)
    val service: CustomInterfaceService = CustomInterfaceService.create(client)
    assertEquals(service.test11("", ""), "success")
  }
}
