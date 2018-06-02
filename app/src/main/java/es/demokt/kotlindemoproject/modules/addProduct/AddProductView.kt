package es.demokt.kotlindemoproject.modules.main

import android.content.Context
import es.demokt.kotlindemoproject.modules.base.main.MainMVP
import es.demokt.kotlindemoproject.modules.product.result.Product

/**
 * Created by aluengo on 12/4/18.
 */
interface AddProductView : MainMVP.View {
  fun retrieveData(): Product
  fun context(): Context
  fun setData(product: Product?)
  fun goToHome()
  fun setSupplier(name: String)
}