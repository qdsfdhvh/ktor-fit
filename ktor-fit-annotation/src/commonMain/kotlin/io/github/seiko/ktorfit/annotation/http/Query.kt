/*
 *  Mask-Android
 *
 *  Copyright (C) 2022 DimensionDev and Contributors
 *
 *  This file is part of Mask X.
 *
 *  Mask X is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Mask-Android is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with Mask-Android.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.seiko.ktorfit.annotation.http

/**
 * Used for query parameters
 *
 * @ GET("comments")
 * suspend fun getCommentsById(@Query("postId") postId: String): List<Comment>
 *
 * A request with getCommentsById(3) will result in the relative URL “comments?postId=3”
 *
 * @ GET("comments")
 * suspend fun getCommentsById(@Query("postId") postId: List<String?>): List<Comment>
 *
 * A request with getCommentsById(listOf("3",null,"4")) will result in the relative URL “comments?postId=3&postId=4”
 *
 * =====
 * [value] is the key of the query parameter
 * @param encoded true means that this value is already URL encoded and will not be encoded again
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Query(val value: String, val encoded: Boolean = false)
