package es.demokt.kotlindemoproject.modules.product.result

import android.util.Log
import es.demokt.kotlindemoproject.data.api.ErrorReponse
import es.demokt.kotlindemoproject.data.api.ErrorReponse.NoData
import es.demokt.kotlindemoproject.data.network.product.SearchProductByEanRequest
import es.demokt.kotlindemoproject.modules.base.main.MainMVP

class ProductResultPresenter(private val view: ProductResultView) : MainMVP.Presenter {

  private val productResultInteractor: ProductResultInteractor by lazy { ProductResultInteractor() }
  private val logTag: String? = javaClass.simpleName

  fun searchProducts(data: SearchProductByEanRequest) {

    view.showProgress(true)
    productResultInteractor.doRequest(
        data, object : MainMVP.Interactor.CallbackResult<List<Product>> {

      override fun onSuccess(result: List<Product>) {
        if (result.isNotEmpty()) {
          view.setList(result)
        } else {
          view.removeExtras()
          view.goToAddProduct()
        }
      }

      override fun onError(error: ErrorReponse) {
        Log.d(logTag, error.toString())

        if (error == NoData) {
          view.removeExtras()
          view.goToAddProduct()
        } else
          view.checkError(error)
      }

      override fun onFinish() {
        super.onFinish()
        view.showProgress(false)
      }

    })

  }

}