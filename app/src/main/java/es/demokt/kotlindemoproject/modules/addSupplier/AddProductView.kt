package es.demokt.kotlindemoproject.modules.main

import android.content.Context
import es.demokt.kotlindemoproject.data.network.supplier.SupplierByIdResponse
import es.demokt.kotlindemoproject.modules.base.main.MainMVP
import es.demokt.kotlindemoproject.modules.store.SupplierModel

/**
 * Created by aluengo on 12/4/18.
 */
interface AddSupplierView : MainMVP.View {
  fun retrieveData(): SupplierModel
  fun context(): Context
  fun setData(product: SupplierModel?)
  fun goToHome()
  fun goBackToList(result: SupplierByIdResponse)
}