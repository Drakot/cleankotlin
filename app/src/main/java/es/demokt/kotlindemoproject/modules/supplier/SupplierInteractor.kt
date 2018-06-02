package es.demokt.kotlindemoproject.modules.store

import es.demokt.kotlindemoproject.data.DataManager
import es.demokt.kotlindemoproject.data.api.ErrorReponse
import es.demokt.kotlindemoproject.data.api.MyCallback
import es.demokt.kotlindemoproject.data.network.supplier.SearchSupplierRequest
import es.demokt.kotlindemoproject.modules.base.main.BaseInteractor
import es.demokt.kotlindemoproject.modules.supplier.SearchSupplierModel

/**
 * Created by aluengo on 18/4/18.
 */
class SupplierInteractor : BaseInteractor<SearchSupplierRequest, SearchSupplierModel>() {

  override fun execute() {
    DataManager.getSuppliers(requestData!!, object : MyCallback<SearchSupplierModel> {

      override fun onSuccess(result: SearchSupplierModel?) {
        val searchModel = SearchSupplierModel()

        searchModel.data = result?.data
        //searchModel.currentSupplier = it
        searchModel.lastPage = result?.lastPage!!
        callSuccess(searchModel)

        DataManager.getCurrentStore {

        }

      }

      override fun onError(error: ErrorReponse) {
        callError(error)
      }

    })
  }
}