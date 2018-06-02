package es.demokt.kotlindemoproject.modules.store

import android.os.Bundle
import android.util.Log
import es.demokt.kotlindemoproject.data.api.ErrorReponse
import es.demokt.kotlindemoproject.data.network.supplier.SearchSupplierRequest
import es.demokt.kotlindemoproject.modules.base.main.MainMVP
import es.demokt.kotlindemoproject.modules.supplier.SearchSupplierModel
import es.demokt.kotlindemoproject.utils.Extras

class SupplierPresenter(private val view: SupplierView) : MainMVP.Presenter {

  private var arguments: Bundle? = null

  fun init(arguments: Bundle?) {
    this.arguments = arguments
    val data = arguments?.getString(Extras.SEARCH_SUPPLIER)
    if(data != null && !data.isNullOrEmpty()){
      search(SearchSupplierRequest(data, "1"))
      arguments.remove(Extras.SEARCH_SUPPLIER)
    } else {
      search(SearchSupplierRequest("A", "1"))
    }
  }

  private val interactor: SupplierInteractor by lazy { SupplierInteractor() }
  private val logTag: String? = javaClass.simpleName

  fun search(
    reqData: SearchSupplierRequest,
    paginated: Boolean = false
  ) {
    view.showProgress(true)
    interactor.doRequest(reqData, object : MainMVP.Interactor.CallbackResult<SearchSupplierModel> {

      override fun onSuccess(result: SearchSupplierModel) {
        result.currentSupplier = arguments?.getSerializable(Extras.SUPPLIER) as SupplierModel?
        view.showList(result.data!!.isNotEmpty(), result.lastPage)
        if (!paginated) {
          view.setList(result)
        } else {
          view.addToList(result)
        }
        view.showProgress(false)
      }

      override fun onError(error: ErrorReponse) {
        Log.d(logTag, error.toString())
        view.checkError(error)
      }

      override fun onFinish() {
        super.onFinish()
        view.showProgress(false)
      }

    })

  }

}