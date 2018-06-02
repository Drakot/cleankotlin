package es.demokt.kotlindemoproject.data.network.home

import com.google.gson.annotations.SerializedName
import es.demokt.kotlindemoproject.data.network.common.ErrorApiResponse

data class StatResponse(@SerializedName("avg_scanned")
val avgScanned: Double = 0.0,
  @SerializedName("avg_updated")
  val avgUpdated: Double = 0.0,
  @SerializedName("avg_added")
  val avgAdded: Double = 0.0)

data class StatsAvgResponse(@SerializedName("response")
val response: StatResponse) : ErrorApiResponse()


