package es.demokt.kotlindemoproject.modules.store

import es.demokt.kotlindemoproject.data.DataManager
import es.demokt.kotlindemoproject.data.api.ErrorReponse
import es.demokt.kotlindemoproject.data.api.MyCallback
import es.demokt.kotlindemoproject.data.network.supplier.SupplierByIdResponse
import es.demokt.kotlindemoproject.modules.base.main.BaseInteractor
import es.demokt.kotlindemoproject.modules.home.UserStore

/**
 * Created by aluengo on 18/4/18.
 */
class SaveSupplierInteractor : BaseInteractor<SupplierModel, SupplierByIdResponse>() {

  override fun execute() {
    DataManager.saveSupplier(requestData!!, object : MyCallback<SupplierByIdResponse> {

      override fun onSuccess(result: SupplierByIdResponse?) {
        callSuccess(result!!)
      }

      override fun onError(error: ErrorReponse) {
        callError(error)
      }

    })
  }
}