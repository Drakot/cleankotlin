package es.demokt.kotlindemoproject.data.api

import es.demokt.kotlindemoproject.data.network.StoreResponse
import es.demokt.kotlindemoproject.data.network.common.ErrorApiResponse
import es.demokt.kotlindemoproject.data.network.home.StatsAvgResponse
import es.demokt.kotlindemoproject.data.network.home.StatsTodayResponse
import es.demokt.kotlindemoproject.data.network.home.StatusRequest
import es.demokt.kotlindemoproject.data.network.home.StatusResponse
import es.demokt.kotlindemoproject.data.network.login.LoginRequest
import es.demokt.kotlindemoproject.data.network.login.LoginResponse
import es.demokt.kotlindemoproject.data.network.login.RefreshTokenRequest
import es.demokt.kotlindemoproject.data.network.product.AddProductRequest
import es.demokt.kotlindemoproject.data.network.product.SearchProductByEanRequest
import es.demokt.kotlindemoproject.data.network.product.SearchProductResponse
import es.demokt.kotlindemoproject.data.network.product.UpdateProductRequest
import es.demokt.kotlindemoproject.data.network.signup.SignupRequest
import es.demokt.kotlindemoproject.data.network.store.SearchStoreByNameRequest
import es.demokt.kotlindemoproject.data.network.store.SearchStoreResponse
import es.demokt.kotlindemoproject.data.network.supplier.SearchSupplierRequest
import es.demokt.kotlindemoproject.data.network.supplier.SearchSupplierResponse
import es.demokt.kotlindemoproject.data.network.supplier.SupplierByIdRequest
import es.demokt.kotlindemoproject.data.network.supplier.SupplierByIdResponse
import es.demokt.kotlindemoproject.data.network.supplier.SupplierRequest
import es.demokt.kotlindemoproject.data.network.supplier.SupplierResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * API Services
 * Created by aluengo on 11/2/18.
 */

interface ApiService {

  @POST("user/login")
  fun login(@Body request: LoginRequest): Call<LoginResponse>

  @POST("user/register")
  fun register(@Body request: SignupRequest): Call<LoginResponse>

  @POST("refreshToken")
  fun refreshToken(@Body request: RefreshTokenRequest): Call<LoginResponse>

  @GET("user/getStores")
  fun getStores(): Call<StoreResponse>

  @POST("store/searchByName")
  fun searchStoreByName(@Body request: SearchStoreByNameRequest): Call<SearchStoreResponse>

  @POST("product/getByEan")
  fun searchProductByEan(@Body request: SearchProductByEanRequest): Call<SearchProductResponse>

  @Multipart
  @POST("product/add")
  fun addProduct(@Part request: AddProductRequest, @Part("images") images: List<MultipartBody.Part>): Call<ErrorApiResponse>

  @Multipart
  @POST("product/add")
  fun addProduct(@Part("product_name") name: RequestBody, @Part("images") images: RequestBody): Call<ErrorApiResponse>

  @POST("product/update")
  fun updateProduct(@Body request: UpdateProductRequest): Call<ErrorApiResponse>

  @POST("supplier/edit")
  fun updateSupplier(@Body request: SupplierRequest): Call<SupplierResponse>

  @Multipart
  @POST("supplier/add")
  fun addSupplier(@Part("supplier_name") name: RequestBody,
    @Part("supplier_business_name") businessName: RequestBody,
    @Part("supplier_description") description: RequestBody,
    @Part("supplier_image\"; filename=\"image.png ") image: RequestBody?): Call<SupplierByIdResponse>

  @POST("supplier/searchByName")
  fun searchSupplier(@Body request: SearchSupplierRequest): Call<SearchSupplierResponse>

  @Multipart
  @POST("product/add")
  fun addProduct(@Part("supplier_id") supplierId: RequestBody, @Part("product_observation") productObservation: RequestBody
      , @Part("price") price: RequestBody, @Part("store_id") storeId: RequestBody, @Part("ean_value") eanValue: RequestBody,
    @Part("product_name") productName: RequestBody, @Part("supplier_ref") supplierRef: RequestBody, @Part() images: List<MultipartBody.Part>): Call<ErrorApiResponse>

  @Multipart
  @POST("product/update")
  fun updateProduct(@Part("supplier_id") supplierId: RequestBody, @Part("product_observation") productObservation: RequestBody
      , @Part("price") price: RequestBody, @Part("product_id") product_id: RequestBody,
    @Part("new_name") productName: RequestBody, @Part("supplier_ref") supplierRef: RequestBody, @Part() images: List<MultipartBody.Part>): Call<ErrorApiResponse>


  @POST("supplier/id") fun getSupplier(
    @Body id: SupplierByIdRequest): Call<SupplierByIdResponse>

  @POST("stats/stats_today")
  fun getStatusToday(): Call<StatsTodayResponse>

  @POST("stats/average")
  fun getStatusAverage(@Body request: StatusRequest): Call<StatsAvgResponse>

  @POST("stats/ean_scans")
  fun getScanStatus(@Body request: StatusRequest): Call<StatusResponse>

}

