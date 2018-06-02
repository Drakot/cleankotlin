package es.demokt.kotlindemoproject.data.network.common

import com.google.gson.annotations.SerializedName

open class ErrorApiResponse(@SerializedName("response_error")
val responseError: List<ErrorResponseItem>?,
  @SerializedName("error")
  val error: Int? = 0) {
  constructor() : this(null, 0)
}

data class ErrorResponseItem(@SerializedName("code")
val code: String = "",
  @SerializedName("parameter")
  val parameter: String = "",
  @SerializedName("text")
  val text: String = "") {
}


