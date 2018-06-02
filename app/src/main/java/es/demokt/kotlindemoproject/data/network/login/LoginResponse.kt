package es.demokt.kotlindemoproject.data.network.login

import com.google.gson.annotations.SerializedName
import es.demokt.kotlindemoproject.data.network.common.ErrorApiResponse

data class LoginResponse(
  @SerializedName("response")
  val response: Response
): ErrorApiResponse()

data class Response(
  @SerializedName("access_token")
  val accessToken: String = "",
  @SerializedName("refresh_token")
  val refreshToken: String = "",
  @SerializedName("token_type")
  val tokenType: String = "",
  @SerializedName("expires_in")
  val expiresIn: Int = 0
)