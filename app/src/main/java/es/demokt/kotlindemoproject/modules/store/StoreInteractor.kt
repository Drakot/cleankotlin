package es.demokt.kotlindemoproject.modules.store

import es.demokt.kotlindemoproject.data.DataManager
import es.demokt.kotlindemoproject.data.api.ErrorReponse
import es.demokt.kotlindemoproject.data.api.MyCallback
import es.demokt.kotlindemoproject.data.network.store.SearchStoreByNameRequest
import es.demokt.kotlindemoproject.modules.base.main.BaseInteractor

/**
 * Created by aluengo on 18/4/18.
 */
class StoreInteractor : BaseInteractor<SearchStoreByNameRequest, SearchStoreModel>() {

  override fun execute() {
    DataManager.getStores(requestData!!, object : MyCallback<SearchStoreModel> {

      override fun onSuccess(result: SearchStoreModel?) {
        val searchStoreModel = SearchStoreModel()

        DataManager.getCurrentStore {
          searchStoreModel.stores = result?.stores
          searchStoreModel.currentStore = it
          searchStoreModel.lastPage = result?.lastPage!!
          callSuccess(searchStoreModel)
        }

      }

      override fun onError(error: ErrorReponse) {
        callError(error)
      }

    })
  }
}