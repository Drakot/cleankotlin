package es.demokt.kotlindemoproject.data.api

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import es.demokt.kotlindemoproject.data.api.retrofit.OAuthRetrofit
import es.demokt.kotlindemoproject.data.api.retrofit.UnauthenticatedApi
import es.demokt.kotlindemoproject.data.local.RealmManager
import es.demokt.kotlindemoproject.data.network.common.ErrorApiResponse
import es.demokt.kotlindemoproject.data.network.home.StatsAvgResponse
import es.demokt.kotlindemoproject.data.network.home.StatsTodayResponse
import es.demokt.kotlindemoproject.data.network.home.StatusRequest
import es.demokt.kotlindemoproject.data.network.home.StatusResponse
import es.demokt.kotlindemoproject.data.network.login.LoginRequest
import es.demokt.kotlindemoproject.data.network.login.LoginResponse
import es.demokt.kotlindemoproject.data.network.login.RefreshTokenRequest
import es.demokt.kotlindemoproject.data.network.product.AddProductRequestBody
import es.demokt.kotlindemoproject.data.network.product.SearchProductByEanRequest
import es.demokt.kotlindemoproject.data.network.product.SearchProductResponse
import es.demokt.kotlindemoproject.data.network.product.UpdateProductRequest
import es.demokt.kotlindemoproject.data.network.product.UpdateProductRequestBody
import es.demokt.kotlindemoproject.data.network.signup.SignupRequest
import es.demokt.kotlindemoproject.data.network.store.SearchStoreByNameRequest
import es.demokt.kotlindemoproject.data.network.store.SearchStoreResponse
import es.demokt.kotlindemoproject.data.network.supplier.SearchSupplierRequest
import es.demokt.kotlindemoproject.data.network.supplier.SearchSupplierResponse
import es.demokt.kotlindemoproject.data.network.supplier.SupplierByIdRequest
import es.demokt.kotlindemoproject.data.network.supplier.SupplierByIdResponse
import es.demokt.kotlindemoproject.data.network.supplier.SupplierRequest
import es.demokt.kotlindemoproject.data.network.supplier.SupplierResponse
import es.demokt.kotlindemoproject.modules.MainApplication
import retrofit2.Call
import retrofit2.Response
import java.io.IOException

/**
 * ApiManager
 * Created by aluengo on 11/2/18.
 */
object ApiManager {

  var apiUnauthenticated = UnauthenticatedApi.get()
      .create<ApiService>(ApiService::class.java)
  var apiAuthenticated = OAuthRetrofit.get()
      .create<ApiService>(ApiService::class.java)

  private val realmManager = RealmManager

  private val isConnectedToInternet: Boolean
    get() {
      val conManager = MainApplication.applicationContext().getSystemService(
          Context.CONNECTIVITY_SERVICE
      ) as ConnectivityManager
      val ni = conManager.activeNetworkInfo
      return ni != null && ni.isConnected
    }

  fun isNotError(error: Int?): Boolean {
    if (error == 1) {
      return false
    }
    return true
  }

  private fun <T : ErrorApiResponse> doRequest(
    call: Call<T>,
    callback: MyCallback<T>,
    isLogin: Boolean = false
  ) {
    if (isConnectedToInternet) {
      try {
        val response = call.execute()

        if (response.isSuccessful) {
          val data = response.body()
          if (data != null && response.body()?.error != 1) {
//            Log.i("ApImanager", response.body().toString())
            if (response.body().toString().contains(
                    "The refresh token is invalid"
                )
            ) {
              callback.onError(ErrorReponse.Unauthorized)
            } else {
              callback.onSuccess(data)
            }
          } else {
            callback.onError(ErrorManager.getError(response.body()?.responseError!![0].code))
          }
        } else {

          if (response.code() == 401) {
            if (!isLogin) {
              doRequest(call.clone(), callback)
            } else if (checkErrorResponse(response)) {
              callback.onError(ErrorManager.getError(response.body()?.responseError!![0].code))
            } else {
              callback.onError(ErrorReponse.ServerError)
            }
          } else if (response.code() == 500) {
            callback.onError(ErrorReponse.ServerError)
          } else {
            if (checkErrorResponse(response)) {
              callback.onError(ErrorManager.getError(response.body()?.responseError!![0].code))
            } else {
              callback.onError(ErrorReponse.ServerError)
            }
          }
        }
      } catch (e: IOException) {
        callback.onError(ErrorReponse.Error)
      }
    } else {
      callback.onError(ErrorReponse.NoConnection)
    }
  }

  private fun <T : ErrorApiResponse> checkErrorResponse(response: Response<T>?): Boolean {
    return response?.body()?.responseError != null && response.body()?.responseError!!.isNotEmpty()
  }

  fun refreshTokenRequest(request: RefreshTokenRequest): LoginResponse? {
    val response = apiUnauthenticated.refreshToken(request)
        .execute()

    if (response.isSuccessful) {
      val data = response.body()
      if (data != null) {
        return data
      }
    }

    return null
  }

  fun loginRequest(
    request: LoginRequest,
    callback: MyCallback<LoginResponse>
  ) {
    return doRequest(apiUnauthenticated.login(request), callback, true)
  }

  fun signupRequest(
    request: SignupRequest,
    callback: MyCallback<LoginResponse>
  ) {
    return doRequest(apiUnauthenticated.register(request), callback)
  }

  fun searchStoreByName(
    filter: SearchStoreByNameRequest,
    callback: MyCallback<SearchStoreResponse>
  ) {

    return doRequest(apiAuthenticated.searchStoreByName(filter), callback)
  }

  fun searchProductByEan(
    request: SearchProductByEanRequest,
    callback: MyCallback<SearchProductResponse>
  ) {
    return doRequest(apiAuthenticated.searchProductByEan(request), callback)
  }

  fun updateProduct(
    request: UpdateProductRequestBody,
    callback: MyCallback<ErrorApiResponse>
  ) {
    return doRequest(apiAuthenticated.updateProduct(request.supplierId!!, request.productObservation!!, request.price!!, request.productId!!, request.productName!!, request.supplierRef!!, request.imagesList), callback)
  }

  fun addProduct(
    request: AddProductRequestBody,
    callback: MyCallback<ErrorApiResponse>
  ) {
    return doRequest(
        apiAuthenticated.addProduct(
            request.supplierId!!, request.productObservation!!,
            request.price!!, request.storeId!!, request.eanValue!!, request.productName!!, request.supplierRef!!, request.imagesList
        ), callback
    )
  }

  fun addSupplier(
    request: SupplierRequest,
    callback: MyCallback<SupplierByIdResponse>
  ) {
    return doRequest(
        apiAuthenticated.addSupplier(
            request.supplierName!!,
            request.supplierBusinessName!!,
            request.supplierDescription!!,
            request.supplierImage
        ), callback
    )
  }

  fun updateSupplier(
    request: SupplierRequest,
    callback: MyCallback<SupplierResponse>
  ) {
    return doRequest(apiAuthenticated.updateSupplier(request), callback)
  }

  fun searchSupplierByName(
    request: SearchSupplierRequest,
    callback: MyCallback<SearchSupplierResponse>
  ) {
    return doRequest(apiAuthenticated.searchSupplier(request), callback)
  }

  fun getSupplier(request: SupplierByIdRequest, callback: MyCallback<SupplierByIdResponse>) {
    return doRequest(apiAuthenticated.getSupplier(request), callback)
  }

  fun getStatusToday(callback: MyCallback<StatsTodayResponse>) {
    return doRequest(apiAuthenticated.getStatusToday(), callback)
  }

  fun getStatusAverage(request: StatusRequest, callback: MyCallback<StatsAvgResponse>) {
    return doRequest(apiAuthenticated.getStatusAverage(request), callback)
  }

  fun getScanStatus(request: StatusRequest, callback: MyCallback<StatusResponse>) {
    return doRequest(apiAuthenticated.getScanStatus(request), callback)
  }
}