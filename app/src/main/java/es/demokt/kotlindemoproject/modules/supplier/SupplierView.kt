package es.demokt.kotlindemoproject.modules.store

import es.demokt.kotlindemoproject.modules.base.main.MainMVP
import es.demokt.kotlindemoproject.modules.supplier.SearchSupplierModel

interface SupplierView : MainMVP.View {
  fun addToList(result: SearchSupplierModel)
  fun setList(result: SearchSupplierModel)
  fun showList(show: Boolean, totalPages: Int)
  fun goBack()

}