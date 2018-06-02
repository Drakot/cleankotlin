package es.demokt.kotlindemoproject.modules.product.result

import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part
import java.io.Serializable

class Product : Serializable {

  var imagesList: MutableList<MultipartBody.Part> = arrayListOf()
  var images: List<String>? = null
  var productObservation: String? = ""
  var productId: Int? = 0
  var name: String? = ""
  var storeId: Int? = 0
  var supplierId: Int? = 0
  var supplierRef: String? = null
  var eanValue: String? = null
  var price: Double? = 0.0
}
