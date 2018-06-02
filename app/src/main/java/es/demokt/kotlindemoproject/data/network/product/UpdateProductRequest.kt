package es.demokt.kotlindemoproject.data.network.product

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UpdateProductRequest {

  @SerializedName("images")
  var images: ArrayList<String>? = null
  @SerializedName("product_observation")
  var productObservation: String? = ""
  @SerializedName("product_id")
  var productId: Int? = 0
  @SerializedName("new_name")
  var name: String? = ""
  @SerializedName("supplier_id")
  var supplierId: Int? = 0
  @SerializedName("supplier_ref")
  var supplierRef: String? = null
  @SerializedName("price")
  var price: Double? = null
}


class UpdateProductRequestBody {
  lateinit var images: RequestBody
  lateinit var imagesList: List<MultipartBody.Part>
  @SerializedName("product_observation")
  var productObservation: RequestBody? = null
  @SerializedName("product_id")
  var productId: RequestBody? = null
  @SerializedName("new_name")
  var productName: RequestBody? = null
  @SerializedName("supplier_id")
  var supplierId: RequestBody? = null
  @SerializedName("supplier_ref")
  var supplierRef: RequestBody? = null
  @SerializedName("price")
  var price: RequestBody? = null
}