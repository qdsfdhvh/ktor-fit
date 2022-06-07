package io.github.seiko.ktorfit.annotation.http

/**
 *  Needs to be used in combination with [FormUrlEncoded]
 * @param encoded true means that this value is already URL encoded and will not be encoded again
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class FieldMap(val encoded: Boolean = false)
