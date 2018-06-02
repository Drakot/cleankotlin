package es.demokt.kotlindemoproject.modules.product.result

import es.demokt.kotlindemoproject.modules.base.main.MainMVP

interface ProductResultView : MainMVP.View {
  fun setList(result: List<Product>)
  fun goToAddProduct()
  fun removeExtras()
}
