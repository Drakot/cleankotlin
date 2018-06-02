package es.demokt.kotlindemoproject.modules.store

import es.demokt.kotlindemoproject.modules.base.main.MainMVP

interface StoreView : MainMVP.View {
  fun addToList(result: SearchStoreModel)
  fun setList(result: SearchStoreModel)
  fun showList(show: Boolean, totalPages: Int)
  fun goBack()

}