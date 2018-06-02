package es.demokt.kotlindemoproject.modules.addProduct

import es.demokt.kotlindemoproject.modules.product.result.Product
import java.util.ArrayList

object CurrentProduct {
  var filePaths: ArrayList<String> = arrayListOf()
  var product: Product? = Product()
}