package io.github.seiko.ktorfit.sample

import io.ktor.client.HttpClient

fun test(client: HttpClient, api: TestInterfaceService) {
  // TestApi
  TestInterfaceService.create(client)
}
