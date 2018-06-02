package es.demokt.kotlindemoproject.data.network.supplier

import com.google.gson.annotations.SerializedName
import es.demokt.kotlindemoproject.data.network.common.ErrorApiResponse

data class Meta(
  @SerializedName("path")
  val path: String = "",
  @SerializedName("per_page")
  val perPage: Int = 0,
  @SerializedName("total")
  val total: Int = 0,
  @SerializedName("last_page")
  val lastPage: Int = 0,
  @SerializedName("from")
  val from: Int = 0,
  @SerializedName("to")
  val to: Int = 0,
  @SerializedName("current_page")
  val currentPage: Int = 0
)

data class SearchSupplierResponse(
  @SerializedName("data")
  val data: Data,
  @SerializedName("meta")
  val meta: Meta?,
  @SerializedName("links")
  val links: Links?
) : ErrorApiResponse()

data class Links(
  @SerializedName("next")
  val next: String? = null,
  @SerializedName("last")
  val last: String = "",
  @SerializedName("prev")
  val prev: String? = null,
  @SerializedName("first")
  val first: String = ""
)

data class Data(
  @SerializedName("response")
  val response: List<ResponseItem>?,
  @SerializedName("error")
  val error: Int = 0
)

data class ResponseItem(
  @SerializedName("url_imagen")
  val urlImagen: String? = null,
  @SerializedName("razon_social")
  val razonSocial: String? = "",
  @SerializedName("id")
  val id: Int = 0,
  @SerializedName("nombre")
  val nombre: String = "",
  @SerializedName("nota")
  val nota: String? = ""
)


