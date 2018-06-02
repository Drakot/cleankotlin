package es.demokt.kotlindemoproject.data.network

import com.google.gson.annotations.SerializedName
import es.demokt.kotlindemoproject.data.network.common.ErrorApiResponse

data class ResponseItem(
  @SerializedName("store_id")
  val storeId: Int = 0,
  @SerializedName("store_name")
  val storeName: String = "",
  @SerializedName("store_location")
  val storeLocation: String = ""
)

data class StoreResponse(
  val response: List<ResponseItem>?
) : ErrorApiResponse()


