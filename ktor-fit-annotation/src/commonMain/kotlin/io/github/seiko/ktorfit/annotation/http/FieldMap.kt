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
 *  Needs to be used in combination with [FormUrlEncoded]
 * @param encoded true means that this value is already URL encoded and will not be encoded again
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class FieldMap(val encoded: Boolean = false)
