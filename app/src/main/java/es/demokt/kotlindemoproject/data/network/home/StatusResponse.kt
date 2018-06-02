package es.demokt.kotlindemoproject.data.network.home

import com.google.gson.annotations.SerializedName
import es.demokt.kotlindemoproject.data.network.common.ErrorApiResponse

data class ResponseItem(@SerializedName("date")
val date: String = "",
  @SerializedName("daily_number")
  val dailyNumber: Int = 0) {
  override fun hashCode(): Int {
    return date.hashCode()
  }

  override fun equals(other: Any?): Boolean {
    return this.date == (other as String)
  }
}

data class StatusResponse(@SerializedName("response")
val response: List<ResponseItem>?) : ErrorApiResponse() {

}