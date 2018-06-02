package es.demokt.kotlindemoproject.data.network.home

import com.google.gson.annotations.SerializedName

class StatusRequest {
  @SerializedName("custom_period")
  var custom_period: String? = null
  @SerializedName("start_date")
  var start_date: String? = null
  @SerializedName("end_date")
  var end_date: String? = null
}