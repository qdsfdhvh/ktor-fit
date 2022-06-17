@file:OptIn(KspExperimental::class)

package io.github.seiko.ktorfit

import com.google.devtools.ksp.KspExperimental
import com.google.devtools.ksp.getAnnotationsByType
import com.google.devtools.ksp.getDeclaredFunctions
import com.google.devtools.ksp.isAnnotationPresent
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSTypeReference
import com.google.devtools.ksp.symbol.KSValueParameter
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import com.squareup.kotlinpoet.ksp.toKModifier
import com.squareup.kotlinpoet.ksp.toTypeName
import com.squareup.kotlinpoet.ksp.writeTo
import io.github.seiko.ktorfit.annotation.generator.GenerateApi
import io.github.seiko.ktorfit.annotation.http.Body
import io.github.seiko.ktorfit.annotation.http.DELETE
import io.github.seiko.ktorfit.annotation.http.Field
import io.github.seiko.ktorfit.annotation.http.FieldMap
import io.github.seiko.ktorfit.annotation.http.FormUrlEncoded
import io.github.seiko.ktorfit.annotation.http.GET
import io.github.seiko.ktorfit.annotation.http.HEAD
import io.github.seiko.ktorfit.annotation.http.HTTP
import io.github.seiko.ktorfit.annotation.http.Header
import io.github.seiko.ktorfit.annotation.http.HeaderMap
import io.github.seiko.ktorfit.annotation.http.Headers
import io.github.seiko.ktorfit.annotation.http.Multipart
import io.github.seiko.ktorfit.annotation.http.OPTIONS
import io.github.seiko.ktorfit.annotation.http.PATCH
import io.github.seiko.ktorfit.annotation.http.POST
import io.github.seiko.ktorfit.annotation.http.PUT
import io.github.seiko.ktorfit.annotation.http.Part
import io.github.seiko.ktorfit.annotation.http.Path
import io.github.seiko.ktorfit.annotation.http.Query
import io.github.seiko.ktorfit.annotation.http.QueryMap
import io.github.seiko.ktorfit.annotation.http.QueryName
import io.github.seiko.ktorfit.annotation.http.ReqBuilder
import io.github.seiko.ktorfit.annotation.http.Streaming
import io.github.seiko.ktorfit.annotation.http.Url

class KtorfitProcessor(environment: SymbolProcessorEnvironment) : SymbolProcessor {

    private val codeGenerator: CodeGenerator = environment.codeGenerator

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver
            .getSymbolsWithAnnotation(
                GenerateApi::class.qualifiedName
                    ?: throw CloneNotSupportedException("Can not get qualifiedName for GenerateApi")
            )
            .filterIsInstance<KSClassDeclaration>()

        val ret = symbols.filter { !it.validate() }.toList()
        symbols
            .filter { it.validate() }
            .forEach { generateFile(it) }
        return ret
    }

    private fun generateFile(classDeclaration: KSClassDeclaration) {
        val packageName = classDeclaration.packageName.asString()
        val className = classDeclaration.qualifiedName?.getShortName() ?: "<ERROR>"

        val fileBuilder = FileSpec.builder(packageName, className)
        generateClass(fileBuilder, classDeclaration, className)

        fileBuilder
            .addImport("io.ktor.http", "encodeURLPath")
            .build()
            .writeTo(codeGenerator, Dependencies(true))
    }

    private fun generateClass(
        fileBuilder: FileSpec.Builder,
        classDeclaration: KSClassDeclaration,
        className: String,
    ) {
        val clientName = classDeclaration.primaryConstructor?.parameters
            ?.first { it.type.toTypeName() == HttpClient }?.name?.getShortName()
        requireNotNull(clientName) {
            "Class constructor must include HttpClient"
        }

        val baseUrlName = classDeclaration.primaryConstructor?.parameters
            ?.first { it.type.isString }?.name?.getShortName()
        requireNotNull(baseUrlName) {
            "Class constructor must include BaseUrl"
        }

        val classBuilder = TypeSpec.classBuilder(className)
        generateClassConstructor(classBuilder, classDeclaration)

        classDeclaration.getDeclaredFunctions().forEach { function ->
            generateFunction(
                classBuilder = classBuilder,
                function = function,
                clientName = clientName,
                baseUrlName = baseUrlName,
            )
        }

        if (classDeclaration.modifiers.isNotEmpty()) {
            classBuilder.addModifiers(
                classDeclaration.modifiers
                    .filter { it.name != KModifier.EXPECT.name }
                    .mapNotNull { it.toKModifier() }
            )
        }

        fileBuilder.addType(
            classBuilder
                .addModifiers(KModifier.ACTUAL)
                .build()
        )
    }

    private fun generateClassConstructor(
        classBuilder: TypeSpec.Builder,
        classDeclaration: KSClassDeclaration,
    ) {
        val constructorBuilder = FunSpec.constructorBuilder()

        classDeclaration.primaryConstructor!!.parameters.forEach { parameter ->
            val name = parameter.name?.getShortName().orEmpty()
            val type = parameter.type.toTypeName()
            constructorBuilder.addParameter(
                ParameterSpec.builder(name, type).build()
            )
            // private val in constructor
            classBuilder.addProperty(
                PropertySpec.builder(name, type)
                    .initializer(name)
                    .addModifiers(KModifier.PRIVATE)
                    .build()
            )
        }

        classBuilder.primaryConstructor(
            constructorBuilder
                .addModifiers(KModifier.ACTUAL)
                .build()
        )
    }

    private fun generateFunction(
        classBuilder: TypeSpec.Builder,
        function: KSFunctionDeclaration,
        clientName: String,
        baseUrlName: String,
    ) {
        val functionName = function.qualifiedName?.getShortName() ?: return
        val functionBuilder = FunSpec.builder(functionName)

        if (function.modifiers.isNotEmpty()) {
            functionBuilder.addModifiers(
                function.modifiers.mapNotNull {
                    it.toKModifier()
                }
            )
        }
        if (function.parameters.isNotEmpty()) {
            functionBuilder.addParameters(
                function.parameters.map {
                    ParameterSpec.builder(
                        it.name?.getShortName().orEmpty(),
                        it.type.toTypeName()
                    ).build()
                }
            )
        }
        function.returnType?.let {
            functionBuilder.returns(it.toTypeName())
        }

        val isStreaming = function.getAnnotationsByType(Streaming::class).firstOrNull() != null
        val request = if (isStreaming) prepareRequest else request

        functionBuilder.withControlFlow("val result = %L.%T", clientName, request) {
            val (method, url) = getHttpMethodAndUrl(function)
            functionBuilder.addStatement("method = %T.parse(%S)", HttpMethod, method)
            generateUrlAndParams(functionBuilder, function, baseUrlName, url)
            generateHeaders(functionBuilder, function)
            generateBody(functionBuilder, function)
        }

        if (isStreaming) {
            functionBuilder.addStatement("return result")
        } else {
            functionBuilder.addStatement("return result.%T()", body)
        }

        classBuilder.addFunction(
            functionBuilder
                .addModifiers(KModifier.ACTUAL)
                .build()
        )
    }

    private fun generateUrlAndParams(
        functionBuilder: FunSpec.Builder,
        function: KSFunctionDeclaration,
        baseUrlName: String,
        url: String,
    ) {
        // Path
        var finalUrl = url
        function.parameters.forEach { parameter ->
            parameter.getAnnotationsByType(Path::class).firstOrNull()?.let {
                finalUrl = finalUrl.replace(
                    "{${it.value}}",
                    "\${${encode(parameter.shoreName, parameter.type.isString && it.encoded)}}"
                )
            }
        }

        // Url
        val urlParameter =
            function.parameters.firstOrNull { it.isAnnotationPresent(Url::class) }
        if (urlParameter != null) {
            functionBuilder.addStatement("%T(%S)", urlClass, urlParameter.shoreName)
        } else {
            functionBuilder.addStatement("%T(%L + \"%L\")", urlClass, baseUrlName, finalUrl)
        }

        // Query
        function.parameters.forEach { parameter ->
            parameter.getAnnotationsByType(Query::class).firstOrNull()?.let {
                functionBuilder.addStatement(
                    "%T(%S, %L)",
                    parameterClass,
                    it.value,
                    encode(parameter.shoreName, parameter.type.isString && it.encoded)
                )
                return@forEach
            }
            parameter.getAnnotationsByType(QueryMap::class).firstOrNull()?.let {
                functionBuilder.addStatement(
                    "%L.forEach { %T(it.key, %L) }",
                    parameter.shoreName,
                    parameterClass,
                    encode("it.value", it.encoded)
                )
                return@forEach
            }
            parameter.getAnnotationsByType(QueryName::class).firstOrNull()?.let {
                // only support a=b&b=c, different with retrofit
                functionBuilder.addStatement(
                    "url.parameters.appendAll(%T(%L))",
                    parseQueryString,
                    encode(parameter.shoreName, parameter.type.isString && it.encoded)
                )
            }
        }
    }

    private fun generateHeaders(
        functionBuilder: FunSpec.Builder,
        function: KSFunctionDeclaration,
    ) {
        function.getAnnotationsByType(Headers::class).forEach {
            it.value.forEach { header ->
                val (k, v) = header.split(":")
                functionBuilder.addStatement("%T(%S,%L)", headerClass, k, v)
            }
        }
        function.parameters.forEach { parameter ->
            parameter.getAnnotationsByType(Header::class).firstOrNull()?.let {
                functionBuilder.addStatement(
                    "%T(%S, %L)", headerClass, it.value, parameter.shoreName
                )
                return@forEach
            }
            parameter.getAnnotationsByType(HeaderMap::class).firstOrNull()?.let {
                functionBuilder.addStatement(
                    "%L.forEach { %T(it.key, it.value) }",
                    parameter.shoreName,
                    headerClass,
                )
            }
        }
    }

    private fun generateBody(
        functionBuilder: FunSpec.Builder,
        function: KSFunctionDeclaration,
    ) {
        function.parameters.forEach { parameter ->
            parameter.getAnnotationsByType(Body::class).firstOrNull()?.let {

                functionBuilder.addStatement("%T(%L)", setBody, parameter.shoreName)
                return
            }
        }
        function.getAnnotationsByType(FormUrlEncoded::class).firstOrNull()?.let {
            functionBuilder.withControlFlow("val parameters = %T.build", Parameters) {
                function.parameters.forEach { parameter ->
                    parameter.getAnnotationsByType(Field::class).firstOrNull()?.let {
                        when {
                            parameter.type.isString -> {
                                functionBuilder.addStatement(
                                    "append(%S, %L)",
                                    it.value,
                                    encode(parameter.shoreName, it.encoded)
                                )
                            }
                            parameter.type.isStringNullable -> {
                                functionBuilder.addStatement(
                                    "if (%L != null) append(%S, %L)",
                                    parameter.shoreName,
                                    it.value,
                                    encode(parameter.shoreName, it.encoded)
                                )
                            }
                            parameter.type.isArray || parameter.type.isList -> {
                                functionBuilder.addStatement(
                                    "append(%S, %S.joinToString { %L })",
                                    it.value,
                                    parameter.shoreName,
                                    encode("it", it.encoded)
                                )
                            }
                            parameter.type.isArrayNullable || parameter.type.isListNullable -> {
                                functionBuilder.addStatement(
                                    "append(%S, %S.joinToString { if (it != null) %L ?: \"\" })",
                                    it.value,
                                    parameter.shoreName,
                                    encode("it", it.encoded)
                                )
                            }
                            else -> Unit
                        }
                    }

                    parameter.getAnnotationsByType(FieldMap::class).firstOrNull()?.let {
                        parameter.type
                        functionBuilder.addStatement(
                            "%L.forEach { append(it.key, %L) }",
                            parameter.shoreName,
                            encode("it.value", it.encoded)
                        )
                    }
                }
            }
            functionBuilder.addStatement("%T(%T(parameters))", setBody, FormDataContent)
            return
        }
        function.getAnnotationsByType(Multipart::class).firstOrNull()?.let {
            functionBuilder.withControlFlow("val formData = %T", formData) {
                function.parameters.forEach { parameter ->
                    parameter.getAnnotationsByType(Part::class).firstOrNull()?.let {
                        functionBuilder.addStatement(
                            "append(%S, %S)",
                            it.value,
                            parameter.shoreName
                        )
                    }
                }
            }
            functionBuilder.addStatement("%T(%T(formData))", setBody, MultiPartFormDataContent)
            return
        }
        function.parameters.forEach { parameter ->
            parameter.getAnnotationsByType(ReqBuilder::class).firstOrNull()?.let {
                functionBuilder.addStatement("%T(%L)", takeFrom, parameter.shoreName)
            }
        }
    }
}

private fun getHttpMethodAndUrl(function: KSFunctionDeclaration): Pair<String, String> {
    function.getAnnotationsByType(GET::class).firstOrNull()?.let {
        return "GET" to it.value
    }
    function.getAnnotationsByType(POST::class).firstOrNull()?.let {
        return "POST" to it.value
    }
    function.getAnnotationsByType(PUT::class).firstOrNull()?.let {
        return "PUT" to it.value
    }
    function.getAnnotationsByType(PATCH::class).firstOrNull()?.let {
        return "PATCH" to it.value
    }
    function.getAnnotationsByType(DELETE::class).firstOrNull()?.let {
        return "DELETE" to it.value
    }
    function.getAnnotationsByType(HEAD::class).firstOrNull()?.let {
        return "HEAD" to it.value
    }
    function.getAnnotationsByType(OPTIONS::class).firstOrNull()?.let {
        return "OPTIONS" to it.value
    }
    function.getAnnotationsByType(HTTP::class).firstOrNull()?.let {
        return it.method to it.path
    }
    error {
        "unknown httpMethod in function " +
            "with: ${function.packageName.asString()} ${function.qualifiedName?.getShortName()}"
    }
}

private fun FunSpec.Builder.withControlFlow(
    controlFlow: String,
    vararg args: Any,
    block: () -> Unit
) {
    beginControlFlow(controlFlow, *args)
    block()
    endControlFlow()
}

private val KSTypeReference.isString get() = toTypeName() == StringType
private val KSTypeReference.isStringNullable get() = toTypeName() == StringNullableType
private val KSTypeReference.isList get() = toTypeName() == ListType
private val KSTypeReference.isListNullable get() = toTypeName() == ListNullableType
private val KSTypeReference.isArray get() = toTypeName() == ArrayType
private val KSTypeReference.isArrayNullable get() = toTypeName() == ArrayNullableType

private val StringType = String::class.asTypeName()
private val StringNullableType = StringType.copy(nullable = true)
private val ListType = List::class.asTypeName()
private val ListNullableType = ListType.copy(nullable = true)
private val ArrayType = Array::class.asTypeName()
private val ArrayNullableType = ArrayType.copy(nullable = true)

private val KSValueParameter.shoreName: String
    get() = name?.getShortName().orEmpty()

private fun encode(value: String, enabled: Boolean = true) =
    if (enabled) "$value.encodeURLPath()" else value
