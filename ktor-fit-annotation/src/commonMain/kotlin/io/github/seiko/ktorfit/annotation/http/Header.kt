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
 * Add a header to a request
 *
 * @ GET("comments")
 *
 * suspend fun request( @ Header("Content-Type") name: String): List< Comment>
 *
 * A request with request("Hello World") will have the header "Content-Type:Hello World"
 * @see Headers
 * @see HeaderMap
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Header(val value: String)
