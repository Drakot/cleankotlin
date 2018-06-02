package es.demokt.kotlindemoproject.modules.store

import es.demokt.kotlindemoproject.data.DataManager
import es.demokt.kotlindemoproject.data.api.ErrorReponse
import es.demokt.kotlindemoproject.data.api.MyCallback
import es.demokt.kotlindemoproject.modules.base.main.BaseInteractor

/**
 * Created by aluengo on 18/4/18.
 */
class GetSupplierInteractor : BaseInteractor<Int, SupplierModel>() {

  override fun execute() {
    DataManager.getSupplier(requestData!!, object : MyCallback<SupplierModel> {

      override fun onSuccess(result: SupplierModel?) {
        callSuccess(result!!)
      }

      override fun onError(error: ErrorReponse) {
        callError(error)
      }

    })
  }
}