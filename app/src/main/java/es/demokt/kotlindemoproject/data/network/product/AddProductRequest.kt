package es.demokt.kotlindemoproject.data.network.product

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

class AddProductRequest {
  @Transient lateinit var images: List<MultipartBody.Part>
  @SerializedName("product_observation")
  var productObservation: String? = ""
  @SerializedName("store_id")
  var storeId: Int? = 0
  @SerializedName("product_name")
  var productName: String? = ""
  @SerializedName("supplier_id")
  var supplierId: Int? = 0
  @SerializedName("supplier_ref")
  var supplierRef: String? = null
  @SerializedName("ean_value")
  var eanValue: String? = null
  @SerializedName("price")
  var price: Double? = 0.0
}