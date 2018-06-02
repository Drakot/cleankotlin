package es.demokt.kotlindemoproject.data.network.product

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddProductRequestBody {
  lateinit var images: RequestBody
  lateinit var imagesList: List<MultipartBody.Part>
  @SerializedName("product_observation")
  var productObservation: RequestBody? = null
  @SerializedName("store_id")
  var storeId: RequestBody? = null
  @SerializedName("product_name")
  var productName: RequestBody? = null
  @SerializedName("supplier_id")
  var supplierId: RequestBody? = null
  @SerializedName("supplier_ref")
  var supplierRef: RequestBody? = null
  @SerializedName("ean_value")
  var eanValue: RequestBody? = null
  @SerializedName("price")
  var price: RequestBody? = null
}