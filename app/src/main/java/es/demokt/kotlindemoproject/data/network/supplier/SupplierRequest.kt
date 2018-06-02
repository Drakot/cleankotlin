package es.demokt.kotlindemoproject.data.network.supplier

import com.google.gson.annotations.SerializedName
import okhttp3.RequestBody

data class SupplierRequest(
  @SerializedName("supplier_description")
  var supplierDescription: RequestBody? = null,
  @SerializedName("supplier_business_name")
  var supplierBusinessName: RequestBody? = null,
  @SerializedName("supplier_image")
  var supplierImage: RequestBody? = null,
  @SerializedName("supplier_name")
  var supplierName: RequestBody? = null,
  @SerializedName("supplier_id")
  var supplierId: RequestBody? = null
)