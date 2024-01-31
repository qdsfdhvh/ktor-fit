package io.github.seiko.ktorfit.idea

import io.github.seiko.ktorfit.kcp.KtorfitBaseContext
import io.github.seiko.ktorfit.kcp.k1.resolve.CreateResolveExtension
import io.github.seiko.ktorfit.kcp.utils.KtorfitLogger

/**
 * .
 *
 * @author 985892345
 * 2024/1/31 12:38
 */
class IdeCreateResolveExtension : CreateResolveExtension(KtorfitBaseContext(KtorfitLogger(null))) {
}
