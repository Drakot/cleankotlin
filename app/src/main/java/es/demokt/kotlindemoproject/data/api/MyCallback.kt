package es.demokt.kotlindemoproject.data.api

interface MyCallback<T> {
    fun onSuccess(result: T?)
    fun onError(error: ErrorReponse)
}
