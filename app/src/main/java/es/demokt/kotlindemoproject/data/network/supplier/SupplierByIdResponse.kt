package es.demokt.kotlindemoproject.data.network.supplier

import com.google.gson.annotations.SerializedName
import es.demokt.kotlindemoproject.data.network.common.ErrorApiResponse

 class SupplierByIdResponse : ErrorApiResponse() {
  @SerializedName("response")
  val response: SupplierResponse? = null
 }

