package es.demokt.kotlindemoproject.data.network.supplier

import com.google.gson.annotations.SerializedName
import es.demokt.kotlindemoproject.data.network.common.ErrorApiResponse

class SupplierResponse : ErrorApiResponse() {
  @SerializedName("supplier_description")
  var supplierDescription: String = ""
  @SerializedName("supplier_business_name")
  var supplierBusinessName: String = ""
  @SerializedName("supplier_image")
  var supplierImage: String? = ""
  @SerializedName("supplier_name")
  var supplierName: String = ""
  @SerializedName("supplier_id")
  var supplierId: Int = 0
}