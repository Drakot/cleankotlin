package es.demokt.kotlindemoproject.data.api.retrofit

import android.util.Log
import es.demokt.kotlindemoproject.BuildConfig
import es.demokt.kotlindemoproject.data.DataManager
import okhttp3.Authenticator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Retrofit
 * Created by aluengo on 12/2/18.
 */

private const val BASE_URL = BuildConfig.BASE_URL

object UnauthenticatedApi : Provider<Retrofit>({
  makeRetrofit()
})

object OAuthRetrofit : Provider<Retrofit>({
  makeRetrofit(accessTokenProvidingInterceptor())
})

fun makeRetrofit(vararg interceptors: Interceptor): Retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .client(makeHttpClient(interceptors))
    .addConverters()
    .build()

private fun makeHttpClient(interceptors: Array<out Interceptor>) = OkHttpClient.Builder()
    .connectTimeout(60, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .addInterceptor(headersInterceptor())
    .retryOnConnectionFailure(true)
    .authenticator(TokenAuthenticator())
    .apply { interceptors().addAll(interceptors) }
    .addInterceptor(loggingInterceptor())
    .build()

fun accessTokenProvidingInterceptor() = Interceptor { chain ->
  val accessToken = DataManager.getAuth()
      ?.token
  chain.proceed(
      chain.request().newBuilder()
          .addHeader("Authorization", "Bearer $accessToken")
          .build()
  )
}

fun loggingInterceptor() = HttpLoggingInterceptor().apply {
  level =
      if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
}

fun headersInterceptor() = Interceptor { chain ->
  chain.proceed(
      chain.request().newBuilder()
          .addHeader("Accept", "application/json")
          .addHeader("Content-Type", "application/json")
          .build()
  )
}

/*var authenticator: Authenticator = Authenticator { route, response ->

    if (!isLoginRequest(response.request())) {
        Log.d("DALE", "QUE CULloNS \nCODE => " + response.code() + "\nDATAS => " + route.address().url())
        DataManager.refreshToken()

    }

    Log.d("DALE", "TOMA CULloNS")


    // TODO Check if is the same token and return null

    response.request().newBuilder()
            .addHeader("Authorization", "Bearer " + DataManager.getAuth()?.accessToken)
            .build()
}*/


fun isLoginRequest(request: Request?): Boolean {
  if (request?.url().toString() == BASE_URL + "refreshtoken") {
    return true
  }
  return false
}

class TokenAuthenticator : Authenticator {
  private val MAX_RETRIES = 3

  override fun authenticate(
    route: Route?,
    response: Response?
  ): Request? {
    if (responseCount(response!!) >= MAX_RETRIES) {
      return null
    }

    if (!isLoginRequest(response.request())) {
      DataManager.refreshToken()
    }

    // TODO Salir cuando el refreshtoken ha expriado

    return response.request()
        .newBuilder()
        .addHeader("Authorization", "Bearer " + DataManager.getAuth()?.token)
        .build()
  }

  fun responseCount(response: Response): Int {
    var result = 1
    while (response.priorResponse() != null) result++
    return result
  }

}