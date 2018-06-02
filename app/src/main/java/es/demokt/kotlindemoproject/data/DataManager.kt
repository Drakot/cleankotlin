package es.demokt.kotlindemoproject.data

import es.demokt.kotlindemoproject.data.api.ApiManager
import es.demokt.kotlindemoproject.data.api.ErrorReponse
import es.demokt.kotlindemoproject.data.api.ErrorReponse.NoData
import es.demokt.kotlindemoproject.data.api.MyCallback
import es.demokt.kotlindemoproject.data.local.RealmManager
import es.demokt.kotlindemoproject.data.network.common.ErrorApiResponse
import es.demokt.kotlindemoproject.data.network.home.ResponseItem
import es.demokt.kotlindemoproject.data.network.home.StatsAvgResponse
import es.demokt.kotlindemoproject.data.network.home.StatsTodayResponse
import es.demokt.kotlindemoproject.data.network.home.StatusResponse
import es.demokt.kotlindemoproject.data.network.login.LoginRequest
import es.demokt.kotlindemoproject.data.network.login.LoginResponse
import es.demokt.kotlindemoproject.data.network.login.RefreshTokenRequest
import es.demokt.kotlindemoproject.data.network.product.SearchProductByEanRequest
import es.demokt.kotlindemoproject.data.network.product.SearchProductResponse
import es.demokt.kotlindemoproject.data.network.store.SearchStoreByNameRequest
import es.demokt.kotlindemoproject.data.network.store.SearchStoreResponse
import es.demokt.kotlindemoproject.data.network.supplier.SearchSupplierRequest
import es.demokt.kotlindemoproject.data.network.supplier.SearchSupplierResponse
import es.demokt.kotlindemoproject.data.network.supplier.SupplierByIdResponse
import es.demokt.kotlindemoproject.data.network.supplier.SupplierResponse
import es.demokt.kotlindemoproject.modules.MainApplication
import es.demokt.kotlindemoproject.modules.home.Status
import es.demokt.kotlindemoproject.modules.home.UserStore
import es.demokt.kotlindemoproject.modules.login.Login
import es.demokt.kotlindemoproject.modules.login.Register
import es.demokt.kotlindemoproject.modules.product.result.Product
import es.demokt.kotlindemoproject.modules.store.SearchStoreModel
import es.demokt.kotlindemoproject.modules.store.SupplierModel
import es.demokt.kotlindemoproject.modules.supplier.SearchSupplierModel

/**
 * DataManager
 * Created by aluengo on 11/2/18.
 */
object DataManager {

  private val logTag = "DataManager"

  private val api = ApiManager
  private val realmManager = RealmManager

  fun login(
    login: LoginRequest,
    callback: MyCallback<Boolean>
  ) {
    api.loginRequest(login, object : MyCallback<LoginResponse> {

      override fun onSuccess(resultLogin: LoginResponse?) {
        val auth = Mapper.map(resultLogin!!)

        realmManager.saveAuth(auth)
        callback.onSuccess(true)

      }

      override fun onError(error: ErrorReponse) {
        callback.onError(error)
      }

    })
  }

  fun signup(
    request: Register,
    callback: MyCallback<Boolean>
  ) {

    val registerRequest = Mapper.map(request)
    api.signupRequest(registerRequest, object : MyCallback<LoginResponse> {
      override fun onSuccess(result: LoginResponse?) {
        val auth = Mapper.map(result!!)

        realmManager.saveAuth(auth)
        callback.onSuccess(true)
      }

      override fun onError(error: ErrorReponse) {
        callback.onError(error)
      }

    })
  }

  //fun setItemSelected(onItemSelected: (item: UserStore) -> Unit) {
  fun getCurrentStore(callback: (item: UserStore?) -> Unit) {
    callback(realmManager.getCurrentStore())
  }

  fun getAuth(): Login? {
    return realmManager.getAuth()
  }

  fun refreshToken() {
    val refreshToken = getAuth()
    val tokenRequest = RefreshTokenRequest()
    if (refreshToken != null)
      tokenRequest.refreshToken = refreshToken.refreshToken

    val refreshTokenResponse = api.refreshTokenRequest(tokenRequest)
    val data = Mapper.map(refreshTokenResponse)
    if (data != null) {
      realmManager.saveAuth(data)
    } else {
      MainApplication.instance.logOut()
    }

  }

  fun logout() {
    realmManager.deleteAll()
  }

  fun getStores(
    filter: SearchStoreByNameRequest,
    callback: MyCallback<SearchStoreModel>
  ) {

    api.searchStoreByName(filter, object : MyCallback<SearchStoreResponse> {

      override fun onSuccess(result: SearchStoreResponse?) {
        val data = Mapper.map(result)

        callback.onSuccess(data)

      }

      override fun onError(error: ErrorReponse) {
        callback.onError(error)
      }

    })

  }

  fun getSuppliers(
    filter: SearchSupplierRequest,
    callback: MyCallback<SearchSupplierModel>
  ) {

    api.searchSupplierByName(filter, object : MyCallback<SearchSupplierResponse> {

      override fun onSuccess(result: SearchSupplierResponse?) {
        val data = Mapper.map(result)

        callback.onSuccess(data)

      }

      override fun onError(error: ErrorReponse) {
        callback.onError(error)
      }

    })

  }

  fun saveStore(
    data: UserStore,
    callback: MyCallback<UserStore>
  ) {
    realmManager.saveStore(data, {
      callback.onSuccess(it)
    })

  }

  fun searchProductByEan(
    searchData: SearchProductByEanRequest,
    callback: MyCallback<List<Product>>
  ) {
    api.searchProductByEan(searchData, object : MyCallback<SearchProductResponse> {

      override fun onSuccess(result: SearchProductResponse?) {
        val mappedData = Mapper.map(result)
        if (mappedData != null) {
          callback.onSuccess(mappedData)
        } else {
          callback.onError(NoData)
        }

      }

      override fun onError(error: ErrorReponse) {
        callback.onError(error)
      }

    })
  }

  fun saveProduct(
    requestData: Product,
    callback: MyCallback<Boolean>
  ) {
    if (requestData.productId == 0) {
      addProduct(requestData, callback)
    } else {
      updateProduct(requestData, callback)
    }
  }

  private fun updateProduct(
    requestData: Product,
    callback: MyCallback<Boolean>
  ) {

    api.updateProduct(Mapper.mapUpdateProduct(requestData), object : MyCallback<ErrorApiResponse> {

      override fun onSuccess(result: ErrorApiResponse?) {
        callback.onSuccess(true)
      }

      override fun onError(error: ErrorReponse) {
        callback.onError(error)
      }

    })
  }

  private fun addProduct(
    requestData: Product,
    callback: MyCallback<Boolean>
  ) {
    api.addProduct(Mapper.mapRequest(requestData), object : MyCallback<ErrorApiResponse> {

      override fun onSuccess(result: ErrorApiResponse?) {
        callback.onSuccess(true)
      }

      override fun onError(error: ErrorReponse) {
        callback.onError(error)
      }

    })
  }

  fun saveSupplier(
    requestData: SupplierModel,
    callback: MyCallback<SupplierByIdResponse>
  ) {
    if (requestData.id == 0) {
      addSupplier(requestData, callback)
    } else {
      addSupplier(requestData, callback)
    }
  }

  private fun updateSupplier(
    requestData: SupplierModel,
    callback: MyCallback<SupplierModel>
  ) {

    api.updateSupplier(Mapper.map(requestData), object : MyCallback<SupplierResponse> {

      override fun onSuccess(result: SupplierResponse?) {
        callback.onSuccess(Mapper.map(result!!))
      }

      override fun onError(error: ErrorReponse) {
        callback.onError(error)
      }

    })
  }

  private fun addSupplier(
    requestData: SupplierModel,
    callback: MyCallback<SupplierByIdResponse>
  ) {
    api.addSupplier(Mapper.map(requestData), object : MyCallback<SupplierByIdResponse> {

      override fun onSuccess(result: SupplierByIdResponse?) {
        //callback.onSuccess(Mapper.map(result!!))
        callback.onSuccess(result)
      }

      override fun onError(error: ErrorReponse) {
        callback.onError(error)
      }

    })
  }

  fun getSupplier(requestData: Int, callback: MyCallback<SupplierModel>) {
    api.getSupplier(Mapper.map(requestData), object : MyCallback<SupplierByIdResponse> {

      override fun onSuccess(result: SupplierByIdResponse?) {
        callback.onSuccess(Mapper.map(result!!))
      }

      override fun onError(error: ErrorReponse) {
        callback.onError(error)
      }

    })
  }

  fun getScanStatus(startDate: String?, endDate: String?, callback: MyCallback<List<ResponseItem>>) {
    api.getScanStatus(Mapper.map(startDate, endDate), object : MyCallback<StatusResponse> {

      override fun onSuccess(result: StatusResponse?) {
        callback.onSuccess(result!!.response)
      }

      override fun onError(error: ErrorReponse) {
        callback.onError(error)
      }

    })
  }

  fun getStatsToday(callback: MyCallback<Status>) {
    api.getStatusToday(object : MyCallback<StatsTodayResponse> {

      override fun onSuccess(result: StatsTodayResponse?) {
        callback.onSuccess(Mapper.map(result!!))
      }

      override fun onError(error: ErrorReponse) {
        callback.onError(error)
      }

    })
  }

  fun getStatsAverage(startDate: String?, endDate: String?, callback: MyCallback<Status>) {
    api.getStatusAverage(Mapper.map(startDate, endDate), object : MyCallback<StatsAvgResponse> {

      override fun onSuccess(result: StatsAvgResponse?) {
        callback.onSuccess(Mapper.map(result!!))
      }

      override fun onError(error: ErrorReponse) {
        callback.onError(error)
      }

    })
  }

}