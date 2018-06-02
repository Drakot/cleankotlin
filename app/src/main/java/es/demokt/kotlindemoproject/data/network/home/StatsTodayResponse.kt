package es.demokt.kotlindemoproject.data.network.home

import com.google.gson.annotations.SerializedName
import es.demokt.kotlindemoproject.data.network.common.ErrorApiResponse

data class Response(@SerializedName("date")
val date: String = "",
  @SerializedName("number_scanned")
  val numberScanned: Int = 0,
  @SerializedName("number_added")
  val numberAdded: Int = 0,
  @SerializedName("number_updated")
  val numberUpdated: Int = 0)

data class StatsTodayResponse(@SerializedName("response")
val response: Response) : ErrorApiResponse()


