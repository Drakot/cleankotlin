package es.demokt.kotlindemoproject.modules.product.result

import es.demokt.kotlindemoproject.data.DataManager
import es.demokt.kotlindemoproject.data.api.ErrorReponse
import es.demokt.kotlindemoproject.data.api.ErrorReponse.Error
import es.demokt.kotlindemoproject.data.api.MyCallback
import es.demokt.kotlindemoproject.data.network.product.SearchProductByEanRequest
import es.demokt.kotlindemoproject.data.network.product.SearchProductResponse
import es.demokt.kotlindemoproject.modules.base.main.BaseInteractor

class ProductResultInteractor : BaseInteractor<SearchProductByEanRequest, List<Product>>() {

  override fun execute() {
    DataManager.getCurrentStore { store ->
      if (store != null) {

        requestData!!.store_id = store.id

        DataManager.searchProductByEan(requestData!!, object : MyCallback<List<Product>> {

          override fun onSuccess(result: List<Product>?) {
            callSuccess(result!!)
          }

          override fun onError(error: ErrorReponse) {
            callError(error)
          }

        })

      } else {
        // Error not store_id
        callError(Error)
      }
    }
  }


}