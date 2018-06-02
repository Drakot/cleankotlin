package es.demokt.kotlindemoproject.data.network.product


import com.google.gson.annotations.SerializedName
import es.demokt.kotlindemoproject.data.network.common.ErrorApiResponse

data class ProductResponse(@SerializedName("ean_id")
                    val eanId: Int = 0,
                    @SerializedName("ean_value")
                    val eanValue: String = "",
                    @SerializedName("products")
                    val products: ArrayList<ProductsItemResponse>??)


data class Store(@SerializedName("store_id")
                 val storeId: Int? = 0,
                 @SerializedName("price")
                 val price: Double? = 0.0,
                 @SerializedName("store_name")
                 val storeName: String? = "",
                 @SerializedName("store_location")
                 val storeLocation: String? = "")


data class SearchProductResponse(@SerializedName("response")
                                 val response: ProductResponse?): ErrorApiResponse()


data class ProductsItemResponse(@SerializedName("images")
                        val images: ArrayList<ProductImageResponse>? = null,
                        @SerializedName("product_id")
                        val productId: Int? = 0,
                        @SerializedName("product_observation")
                        val productObservation: String? = "",
                        @SerializedName("store")
                        val store: Store?,
                        @SerializedName("product_name")
                        val productName: String? = "",
                        @SerializedName("supplier_id")
                        val supplierId: Int? = 0,
                        @SerializedName("supplier_ref")
                        val supplierRef: String? = null)
{
  constructor() : this(null,0,"", null, "", 0, "")
}

data class ProductImageResponse(@SerializedName("image_url")
val image_url: String? = "")
