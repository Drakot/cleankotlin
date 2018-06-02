package es.demokt.kotlindemoproject.data.network.supplier

import com.google.gson.annotations.SerializedName

data class SupplierByIdRequest(

  @SerializedName("supplier_id")
  var supplierId: Int = 0
)