package es.demokt.kotlindemoproject.data.api.retrofit

abstract class Provider<T>(private val initializer: () -> T) {

    var override: T? = null
    var original: T? = null

    fun get(): T = override ?: original ?: initializer().apply { original = this }
    fun lazyGet(): Lazy<T> = lazy { get() }
}
